package org.zhr.Service;

import lombok.extern.java.Log;
import org.zhr.utils.ConnectionFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Log
public class SqlExecute<T> {
    private final Connection connection;
    private ConditionBuilder<T> conditionBuilder;


    public SqlExecute() throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.connection = ConnectionFactory.getSqlConnection();
    }

    private String SelectSqlMakeFactory(Class<?> className) {
        StringBuilder stringBuilder = new StringBuilder();
        String classNameSimpleName = className.getSimpleName();
        stringBuilder.append("select * from ").append(classNameSimpleName).append(" ");
        Map<String,String> map = conditionBuilder.getEqualCondition();
        stringBuilder.append("where ");
        boolean mark = false;
        if (conditionBuilder.getEqualCondition().size() != 0) {
            for (Map.Entry<String ,String> i : map.entrySet()) {
                stringBuilder.append(i.getKey()).append(" = ").append(i.getValue()).append(" and ");
            }
            mark = true;
        }
        if (conditionBuilder.getBtCondition().size() != 0) {
            map = conditionBuilder.getBtCondition();
            for (Map.Entry<String, String> i : map.entrySet()) {
                stringBuilder.append(i.getKey()).append(" > ").append(i.getValue()).append(" and ");
            }
            mark = true;
        }
        if (conditionBuilder.getLtCondition().size() != 0) {
            map = conditionBuilder.getLtCondition();
            for (Map.Entry<String ,String> i : map.entrySet()) {
                stringBuilder.append(i.getKey()).append(" < ").append(i.getValue()).append(" and ");
            }
            mark = true;
        }
        if (mark)
            stringBuilder.delete(stringBuilder.length() - 4,stringBuilder.length());
        stringBuilder.append(";");
        log.info(stringBuilder.toString());
        return stringBuilder.toString();
    }

    private String InsertSqlFactory(List<String> key,List<String> val,String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append(" ");
        sql.append("( ");
        key.forEach(i -> sql.append(i).append(" ,"));
        sql.delete(sql.length() - 1,sql.length());
        sql.append(" ) ");
        sql.append("values ( ");
        val.forEach(i -> sql.append(i).append(" ,"));
        sql.delete(sql.length() - 1,sql.length());
        sql.append(" ) ").append(";");
        log.info(sql.toString());
        return sql.toString();
    }

    public T selectOne(ConditionBuilder<T> conditions) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        this.conditionBuilder = conditions;
        Class<?> aClass = conditions.aClass;
        String s = SelectSqlMakeFactory(aClass);
        System.out.println(s);
        PreparedStatement preparedStatement = this.connection.prepareStatement(s);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int column = metaData.getColumnCount();
        T o = (T) aClass.getDeclaredConstructor().newInstance();
        Field[] declaredFields = aClass.getDeclaredFields();
        if (resultSet.next()) {
            for (int i = 0;i < column;i ++) {
                Object val = resultSet.getObject(i + 1);
                String columnName = metaData.getColumnName(i + 1);
                Field declaredField = aClass.getDeclaredField(columnName);
                declaredField.setAccessible(true);
                declaredField.set(o,val);
            }
        }
        return o;
    }
    public List<T> selectList(ConditionBuilder<T> conditions) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.conditionBuilder = conditions;
        Class<?> aClass = conditions.aClass;
        String s = SelectSqlMakeFactory(aClass);
        PreparedStatement preparedStatement = this.connection.prepareStatement(s);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int column = metaData.getColumnCount();
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            T o = (T) aClass.getDeclaredConstructor().newInstance();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field i : declaredFields) {
                i.setAccessible(true);
                Object object = resultSet.getObject(i.getName());
                i.set(o,object);
            }
            result.add(o);
        }
        return result;
    }
    public int insert(T t) throws SQLException {
        Class<?> aClass = t.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        List<String> name = new ArrayList<>();
        List<String> val = new ArrayList<>();
        Arrays.stream(declaredFields).forEach((i) -> name.add(i.getName()));
        Arrays.stream(declaredFields).forEach(i -> {
            try {
                i.setAccessible(true);
                if (!i.getType().getTypeName().equals("java.lang.String"))
                    val.add(i.get(t).toString());
                else
                    val.add("'" + i.get(t).toString() + "'");
            } catch (IllegalAccessException ignored) {
                val.add("null");
            }
        });
        String s = InsertSqlFactory(name, val,aClass.getSimpleName());
        PreparedStatement preparedStatement = this.connection.prepareStatement(s);
        return preparedStatement.executeUpdate();
    }
}

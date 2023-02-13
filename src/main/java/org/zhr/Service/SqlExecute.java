package org.zhr.Service;

import lombok.extern.java.Log;
import org.zhr.entity.Result;
import org.zhr.utils.ConnectionFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log
public class SqlExecute<T> {
    private final Connection connection;
    private ConditionBuilderImpl<T> conditionBuilder;
    private SqlMakeFactoryImpl<T> sqlMakeFactory;

    public SqlExecute() throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.connection = ConnectionFactory.getSqlConnection();
        this.sqlMakeFactory = new SqlMakeFactoryImpl<>();
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
    public Result select(ConditionBuilderImpl<T> conditions) throws SQLException {
        Class<?> aClass = conditions.aClass;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select * from ").append(aClass.getSimpleName()).append(" ");
        String sql = sqlMakeFactory.sqlMake(conditions);
        stringBuilder.append(sql);
        String s = stringBuilder.toString();
        log.info("sql :   " + s);
        PreparedStatement preparedStatement = this.connection.prepareStatement(s);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int column = metaData.getColumnCount();
        return new Result(column,resultSet,metaData);

    }
    public T selectOne(ConditionBuilderImpl<T> conditions) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        this.conditionBuilder = conditions;
        Class<?> aClass = conditions.aClass;
        Result select = select(conditions);
        int column = select.getColum();
        ResultSet resultSet = select.getResultSet();
        ResultSetMetaData metaData = select.getResultSetMetaData();
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
    public List<T> selectList(ConditionBuilderImpl<T> conditions) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.conditionBuilder = conditions;
        Class<?> aClass = conditions.aClass;
        Result select = select(conditions);
        ResultSet resultSet = select.getResultSet();
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
                if (i.get(t) == null) {
                    val.add("null");
                } else {
                    if (!i.getType().getTypeName().equals("java.lang.String")) {
                        val.add(i.get(t).toString());
                    } else {
                        val.add("'" + i.get(t).toString() + "'");
                    }
                }
            } catch (IllegalAccessException ignored) {
                val.add("null");
            }
        });
        String s = InsertSqlFactory(name, val,aClass.getSimpleName());
        PreparedStatement preparedStatement = this.connection.prepareStatement(s);
        return preparedStatement.executeUpdate();
    }
}

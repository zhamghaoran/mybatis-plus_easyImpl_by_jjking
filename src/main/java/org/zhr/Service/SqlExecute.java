package org.zhr.Service;

import lombok.extern.java.Log;
import org.zhr.entity.Result;
import org.zhr.utils.ConnectionFactory;
import org.zhr.utils.StringUtils;

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
    private final SqlMakeFactoryImpl<T> sqlMakeFactory;
    private final NameCheckImpl nameCheck;
    private final Class<T> aClass;
    private StringUtils stringUtils;

    public SqlExecute(Class<T> aclass) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        this.connection = ConnectionFactory.getSqlConnection();
        this.sqlMakeFactory = new SqlMakeFactoryImpl<>();
        this.nameCheck = new NameCheckImpl();
        this.stringUtils = new StringUtils();
        this.aClass = aclass;
    }

    private String InsertSqlFactory(List<String> key, List<String> val, String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tableName).append(" ");
        sql.append("( ");
        key.forEach(i -> sql.append(i).append(" ,"));
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) ");
        sql.append("values ( ");
        val.forEach(i -> sql.append(i).append(" ,"));
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" ) ").append(";");
        log.info(sql.toString());
        return sql.toString();
    }

    public Result select(ConditionBuilderImpl<T> conditions) throws SQLException, IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String name = this.aClass.getSimpleName();
        name = nameCheck.CheckTableName(name);
        stringBuilder.append("select * from ").append(name).append(" ");

        String sql = sqlMakeFactory.sqlMake(conditions);

        stringBuilder.append(sql);
        String s = stringBuilder.toString();
        log.info("sql :   " + s);
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(s);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int column = metaData.getColumnCount();
        return new Result(column, resultSet, metaData);

    }

    public T selectOne(ConditionBuilderImpl<T> conditions) throws SQLException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException, IOException {
        Result select = select(conditions);
        int column = select.getColum();
        ResultSet resultSet = select.getResultSet();
        ResultSetMetaData metaData = select.getResultSetMetaData();
        T o = this.aClass.getDeclaredConstructor().newInstance();
        Field[] declaredFields = aClass.getDeclaredFields();
        if (resultSet.next()) {
            for (int i = 0; i < column; i++) {
                Object val = resultSet.getObject(i + 1);
                String columnName = metaData.getColumnName(i + 1);
                Field declaredField = aClass.getDeclaredField(columnName);
                declaredField.setAccessible(true);
                declaredField.set(o, val);
            }
        }
        return o;
    }

    public List<T> selectList(ConditionBuilderImpl<T> conditions) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException {
        Result select = select(conditions);
        ResultSet resultSet = select.getResultSet();
        List<T> result = new ArrayList<>();
        while (resultSet.next()) {
            T o = this.aClass.getDeclaredConstructor().newInstance();
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field i : declaredFields) {
                i.setAccessible(true);
                Object object = resultSet.getObject(stringUtils.smallHumpToUnderline(i.getName()));
                i.set(o, object);
            }
            result.add(o);
        }
        return result;
    }

    public int insert(T t) throws SQLException {
        Field[] declaredFields = this.aClass.getDeclaredFields();
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
        String s = InsertSqlFactory(name, val, aClass.getSimpleName());
        PreparedStatement preparedStatement = this.connection.prepareStatement(s);
        return preparedStatement.executeUpdate();
    }
}

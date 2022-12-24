package org.zhr.Service;

import org.zhr.utils.ConnectionFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SqlExecute<T> {
    private Connection connection;
    public SqlExecute() throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.connection = ConnectionFactory.getSqlConnection();
    }
    private String sqlMakeFactory(Map<String,String> map,Class<?> className) {
        StringBuilder stringBuilder = new StringBuilder();
        String classNameSimpleName = className.getSimpleName();
        stringBuilder.append("select * from ").append(classNameSimpleName).append(" ");
        if (map != null) {
            stringBuilder.append("where ");
            for (Map.Entry<String ,String> i : map.entrySet()) {
                stringBuilder.append(i.getKey()).append(" = ").append(i.getValue()).append(" and ");

            }
            stringBuilder.delete(stringBuilder.length() - 4,stringBuilder.length());
        }
        stringBuilder.append(";");
        return stringBuilder.toString();
    }
    public T selectOne(ConditionBuilder<T> conditions) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException, NoSuchFieldException {
        Class<?> aClass = conditions.aClass;
        String s = sqlMakeFactory(conditions.getConditions(), aClass);
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
        Class<?> aClass = conditions.aClass;
        String s = sqlMakeFactory(conditions.getConditions(), aClass);
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
}

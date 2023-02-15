package org.zhr.Service;

import org.zhr.Service.Interface.Cache;
import org.zhr.Service.Interface.NameCheck;
import org.zhr.annotation.Table;
import org.zhr.utils.ConnectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class NameCheckImpl implements NameCheck {
    private Connection connection;

    public NameCheckImpl() throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.connection = ConnectionFactory.getSqlConnection();
    }

    private String getDatabaseName() throws IOException {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("jdbc.properties");
        Properties pros = new Properties();
        pros.load(resourceAsStream);
        String url = pros.getProperty("url");
        String[] split = url.split("/");
        return split[split.length - 1];
    }

    private String smallHumpToUnderline(String name) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0;i < name.length();i ++) {
            if (name.charAt(i) >= 'A' && name.charAt(i) <= 'Z') {
                stringBuilder.append('_');
                stringBuilder.append(String.valueOf(name.charAt(i)).toLowerCase());
            } else {
                stringBuilder.append(name.charAt(i));
            }
        }
        return stringBuilder.toString();
    }
    private String getName(List<String> tableName, String name) {
        name = smallHumpToUnderline(name);
        for (String i : tableName) {
            if (i.equals(name))
                return name;
        }
        return null;
    }

    @Override
    public String CheckTableName(String name,Class<?> aclass) throws SQLException, IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String s = IsCustomTableName(aclass);
        if (s != null) {
            return s;
        }
        String sql = "show tables";
        String databaseName = getDatabaseName();
        String sqlGet = "Tables_in_" + databaseName;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> tableName = new ArrayList<>();
        while (resultSet.next()) {
            String tablesInStudentDb = (String) resultSet.getObject(sqlGet);
            tableName.add(tablesInStudentDb);
        }
        String name1 = getName(tableName, name);
        if (name1 != null) {
            return name1;
        }
        else throw new SQLException("表名查询失败");
    }
    private String IsCustomTableName(Class<?> aClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (aClass.isAnnotationPresent(Table.class)) {
            Table annotation = aClass.getAnnotation(Table.class);
            return annotation.tableName();
        }
        return null;
    }
}

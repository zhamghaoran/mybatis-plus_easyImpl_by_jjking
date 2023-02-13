package org.zhr.Service;

import org.zhr.utils.ConnectionFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

public class Select {
    public <T> T SelectOne(Class<T> tClass, String sql, Object... args) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException { // 查询函数，可以查询声明类型的表
        Connection conn = ConnectionFactory.getSqlConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                ps.setObject(i + 1, args[i]);
            }
        }
        ResultSet qaq = ps.executeQuery();
        ResultSetMetaData rsmd = qaq.getMetaData();
        int column = rsmd.getColumnCount();
        T t = null;
        if (qaq.next()) {
            t = tClass.newInstance();
            for (int i = 0; i < column; i++) {
                Object val = qaq.getObject(i + 1);
                String nomaxvalue = rsmd.getColumnName(i + 1);
                Field declaredField = tClass.getDeclaredField(nomaxvalue);
                declaredField.setAccessible(true);
                declaredField.set(t, val);
            }
        }
        ps.close();
        return t;
    }
}

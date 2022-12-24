package org.zhr.Service;

import org.zhr.utils.ConnectionFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;

public class Select {
    public <T> T SelectOne(Class<T> tClass,String sql,Object ...args) throws SQLException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException { // 查询函数，可以查询声明类型的表
        Connection conn = ConnectionFactory.getSqlConnection();
        PreparedStatement ps = conn.prepareStatement(sql); // 预处理sql语句
        if (args != null) {
            for(int i = 0;i < args.length;i ++) {
                ps.setObject(i + 1,args[i]);  // 填充占位符
            }
        }
        ResultSet qaq = ps.executeQuery();  // 执行语句
        ResultSetMetaData rsmd = qaq.getMetaData();  // 获取元数据
        int column = rsmd.getColumnCount();  // 获取一行数据有多少列
        T t = null;
        if (qaq.next()) { // 判断下一条是否有数据
            t = tClass.newInstance();  // 创建一个实例
            for (int i = 0; i < column; i++) {
                Object val = qaq.getObject(i + 1);   // 获取这一行的数据
                String nomaxvalue = rsmd.getColumnName(i + 1);  // 获取这一行的类型名字
                Field declaredField = tClass.getDeclaredField(nomaxvalue); // 反射
                declaredField.setAccessible(true);
                declaredField.set(t, val); // 赋值
            }
        }
        ps.close();
        return t;
    }
}

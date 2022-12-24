package org.zhr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory {
    public static Connection getSqlConnection() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException, IOException {
        //从jdbc.properties这个配置文件中读入数据
        InputStream is = ConnectionFactory.class.getClassLoader().getResourceAsStream("jdbc.properties");
        //创建properties对象
        Properties pros = new Properties();
        //将is加载到pros中
        pros.load(is);
        //获取相关信息
        String user = pros.getProperty("user");
        String password = pros.getProperty("password");
        String url = pros.getProperty("url");
        String driverName = pros.getProperty("driverClass");
        //加载驱动
        Class.forName(driverName);
        // 进行连接
        Connection conn = DriverManager.getConnection(url,user,password);
        return conn;
    }
}

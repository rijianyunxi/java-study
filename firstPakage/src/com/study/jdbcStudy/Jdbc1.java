package com.study.jdbcStudy;


import com.mysql.jdbc.Driver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Jdbc1 {
    public static void main(String[] args) throws SQLException {
        Driver driver = new Driver(); //注册驱动
//        String url = "jdbc:mysql://192.168.1.88:3306/study";
        String url = "jdbc:mysql://localhost:3306/study?useSSL=false";
        Properties properties = new Properties();
        properties.setProperty("user","root");
        properties.setProperty("password","123456");
        Connection connect =  driver.connect(url,properties);
        Statement statement = connect.createStatement();
        String sql = "INSERT INTO actor (name, sex, score, birthday) VALUES ('刘德华', '男', 9.9, '1960-05-15')";
        int rows = statement.executeUpdate(sql);
        System.out.println(rows>0?"成功":"失败");
        statement.close();
        connect.close();

    }
}


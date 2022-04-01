package com.xiezirui.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

//操作数据库的公共类
public class baseDao {
    private static String driver;
    private static String url;
    private static String username;
    private static String password;
    static {
        Properties properties = new Properties();

        InputStream inputstream = baseDao.class.getClassLoader().getResourceAsStream("db.properties");

        try {
            properties.load(inputstream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        driver = properties.getProperty("driver");
        url = properties.getProperty("url");
        username = properties.getProperty("username");
        password = properties.getProperty("password");
    }
    //获取数据库连接
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    //查询公共方法
    public static ResultSet select(Connection connection,PreparedStatement preparedStatement,ResultSet resultSet,String sql,Object[] datas) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < datas.length; i++) {
            preparedStatement.setObject(i + 1,datas[i]);
        }

        resultSet = preparedStatement.executeQuery();

        return resultSet;
    }
    //增删改公共方法
    public static int execute(Connection connection,PreparedStatement preparedStatement,String sql,Object[] datas) throws SQLException {
        preparedStatement = connection.prepareStatement(sql);

        for (int i = 0; i < datas.length; i++) {
            preparedStatement.setObject(i + 1,datas[i]);
        }

        int updata = preparedStatement.executeUpdate();

        return updata;
    }
    //资源释放
    public static boolean closeResources(Connection connection,PreparedStatement PreparedStatement,ResultSet resultSet){
        boolean flag = true;
        if (resultSet != null){
            try {
                resultSet.close();
                resultSet = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (PreparedStatement != null){
            try {
                PreparedStatement.close();
                PreparedStatement = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        if (connection != null){
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
                flag = false;
            }
        }
        return flag;
    }
}

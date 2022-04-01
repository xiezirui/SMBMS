package com.xiezirui.dao.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.xiezirui.pojo.Role;
import com.xiezirui.pojo.User;
import sun.awt.windows.WingDings;

public interface userDao {
    //得到登录用户
    public User getLoginUser(Connection connection, String userCode, String userPassword);

    //修改登录密码
    public int updataPassword(Connection connection,Long id,String password);

    //获得用户总数
    public int getUserCount(Connection connection,String userName,int userRole);

    //通过用户输入的条件查询用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws  Exception;

    //添加用户
    public int add(Connection connection,User user);

    //修改用户信息
    public int modify(Connection connection, User user);

    //通过userId查看当前用户信息
    public User getUserById(Connection connection, String id) throws SQLException;

    //通过用户id删除用户信息
    public int deleteUserById(Connection connection, Integer delId)throws Exception;
}

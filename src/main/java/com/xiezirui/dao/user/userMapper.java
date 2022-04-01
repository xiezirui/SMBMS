package com.xiezirui.dao.user;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.xiezirui.pojo.Role;
import com.xiezirui.pojo.User;
import sun.awt.windows.WingDings;

public interface userMapper {
    //得到登录用户
    public User getLoginUser(Map map);

    //修改登录密码
    public int updataPassword(Map map);

    //获得用户总数
    public int getUserCount(Map map);

    //通过用户输入的条件查询用户列表
    public List<User> getUserList(Map map);

    //添加用户
    public int add(User user);

    //修改用户信息
    public int modify(User user);

    //通过userId查看当前用户信息
    public User getUserById(String id) throws SQLException;

    //通过用户id删除用户信息
    public int deleteUserById(Integer delId)throws Exception;
}

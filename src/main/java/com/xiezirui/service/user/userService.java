package com.xiezirui.service.user;

import com.xiezirui.pojo.User;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface userService {
    public User login(String userCode,String userPassword);

    public Boolean updataPassword(Long id, String password);

    public int userCount(String userName,int userRole);

    //根据条件查询用户列表
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize);

    public boolean add(User user);

    //修改用户信息
    public Boolean modify(User user) throws Exception;

    //根据用户id删除用户
    public boolean deleteUserById(Integer delId);

    //根据用户id得到当前用户
    public User getUserById(String id);

}

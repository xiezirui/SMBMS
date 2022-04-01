package com.xiezirui.service.user;

import com.xiezirui.dao.*;
import com.xiezirui.dao.user.userDao;
import com.xiezirui.dao.user.userDaoImpl;
import com.xiezirui.pojo.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static com.xiezirui.dao.baseDao.getConnection;

public class userServiceImpl implements userService{

    private userDao userDao;

    public userServiceImpl() {
        userDao = new userDaoImpl();
    }

    @Override
    public User login(String userCode, String userPassword) {
        Connection connection = null;
        User user = null;

        connection = getConnection();
        user = userDao.getLoginUser(connection, userCode, userPassword);

        baseDao.closeResources(connection,null,null);

        return user;
    }

    @Override
    public Boolean updataPassword(Long id, String password) {
        Connection connection = null;
        connection = getConnection();
        Boolean flag = false;
        if (userDao.updataPassword(connection,id,password) > 0){
            flag = true;

        }
        return flag;
    }

    @Override
    public int userCount(String userName, int userRole) {
        Connection connection = null;
        connection = getConnection();
        int count = 0;
        count = userDao.getUserCount(connection, userName, userRole);
        baseDao.closeResources(connection,null,null);
        return count;
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        Connection connection = null;
        List<User> userList = null;

        connection = baseDao.getConnection();
        try {
            userList = userDao.getUserList(connection,userName,userRole,currentPageNo,pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        baseDao.closeResources(connection,null,null);

        return userList;
    }

    @Override
    public Boolean modify(User user) throws Exception {
        Boolean flag=false;
        Connection connection=null;
        try {
            connection=baseDao.getConnection();
            connection.setAutoCommit(false);//开启JDBC事务
            int updateNum = userDao.modify(connection, user);//执行修改sql
            connection.commit();//提交事务
            if(updateNum>0){
                flag=true;
                System.out.println("修改用户成功");
            }else{
                System.out.println("修改用户失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            //若抛出异常，则说明修改失败需要回滚
            System.out.println("修改失败，回滚事务");
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }finally {
            baseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    @Override
    public boolean add(User user) {
        boolean flag = false;
        Connection connection = getConnection();

        try {

            userDaoImpl userDao = new userDaoImpl();
            int isAdd = userDao.add(connection, user);
            connection.setAutoCommit(false);//开启JDBC事务管理

            if (isAdd > 0){
                flag = true;
                System.out.println("success");
            }else {
                System.out.println("false");
            }

        } catch (Exception e) {
            e.printStackTrace();

            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } finally {
            baseDao.closeResources(connection, null, null);
        }
        return flag;
    }

    @Override
    public boolean deleteUserById(Integer delId) {
        Boolean flag=false;
        Connection connection=null;
        connection=baseDao.getConnection();
        try {
            int deleteNum=userDao.deleteUserById(connection,delId);
            if(deleteNum>0)flag=true;
        } catch (Exception e) {
        }finally {
            baseDao.closeResources(connection,null,null);
        }
        return flag;
    }

    @Override
    public User getUserById(String id) {
        User user = new User();
        Connection connection=null;
        try {
            connection=baseDao.getConnection();
            user = userDao.getUserById(connection,id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            baseDao.closeResources(connection,null,null);
        }
        return  user;
    }

    //----text----

    @Test
    public void test(){
        userServiceImpl userService = new userServiceImpl();
        System.out.println(userService.getUserList("admin", 1, 1, 5).size());

    }
    @Test
    public void test02(){
        System.out.println(new userServiceImpl().login("wen", null).getUserPassword());
    }
    @Test
    public void textUpdata(){
        updataPassword(1L,"1234567");
    }
}

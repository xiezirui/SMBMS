package com.xiezirui.service.user;

import com.xiezirui.dao.*;
import com.xiezirui.dao.user.userMapper;
import com.xiezirui.pojo.User;
import com.xiezirui.util.mybatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import static com.xiezirui.dao.baseDao.getConnection;

public class userServiceImpl implements userService{
    @Override
    public User login(String userCode, String userPassword) {
        SqlSession sqlSession = mybatisUtil.getSqlSession();
        userMapper mapper = sqlSession.getMapper(userMapper.class);

        HashMap<Object, Object> map = new HashMap<>();
        map.put("userCode",userCode);
        map.put("userPassword",userPassword);

        User user = mapper.getLoginUser(map);

        sqlSession.close();

        return user;
    }

    @Override
    public Boolean updataPassword(Long id, String password) {
        boolean flag = false;
        SqlSession sqlSession = null;
        try {
            sqlSession = mybatisUtil.getSqlSession();
            userMapper mapper = sqlSession.getMapper(userMapper.class);

            HashMap<Object, Object> map = new HashMap<>();
            map.put("id",id);
            map.put("userPassword",password);
            int i = mapper.updataPassword(map);
            if (i > 0){
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        } finally {
            sqlSession.close();
        }

        return flag;
    }

    @Override
    public int userCount(String userName, int userRole) {
        int count = 0;

        SqlSession sqlSession = mybatisUtil.getSqlSession();
        userMapper mapper = sqlSession.getMapper(userMapper.class);

        HashMap<Object, Object> map = new HashMap<>();
        map.put("userName",userName);
        map.put("userRole",userRole);

        count = mapper.getUserCount(map);

        sqlSession.close();

        return count;
    }

    @Override
    public List<User> getUserList(String userName, int userRole, int currentPageNo, int pageSize) {
        List<User> userList = null;
        SqlSession sqlSession = null;

        try {
            sqlSession = mybatisUtil.getSqlSession();
            userMapper mapper = sqlSession.getMapper(userMapper.class);

            HashMap<Object, Object> map = new HashMap<>();
            map.put("userName",userName);
            map.put("userRole",userRole);
            map.put("currentPageNo",(currentPageNo - 1) * 5);
            map.put("pageSize",pageSize);

            userList = mapper.getUserList(map);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
        }

        return userList;

    }

    @Override
    public Boolean modify(User user) {
        Boolean flag = false;
        SqlSession sqlSession = mybatisUtil.getSqlSession();
        userMapper mapper = sqlSession.getMapper(userMapper.class);

        int isSuccess = 0;
        try {
            isSuccess = mapper.modify(user);
            if (isSuccess > 0){
                flag = true;
                sqlSession.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();
        }finally {
            sqlSession.close();
        }

        return flag;
    }

    @Override
    public boolean add(User user) {
        boolean flag = false;
        int isAdd = 0;
        SqlSession sqlSession = null;

        try {

            sqlSession = mybatisUtil.getSqlSession();
            userMapper mapper = sqlSession.getMapper(userMapper.class);
            isAdd = mapper.add(user);

            if (isAdd > 0){
                flag = true;
                sqlSession.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.rollback();

        } finally {
            sqlSession.close();
        }

        return flag;
    }

    @Override
    public boolean deleteUserById(Integer delId) {
        Boolean flag=false;
        int isDelete = 0;
        SqlSession sqlSession = null;

        try {
            sqlSession = mybatisUtil.getSqlSession();
            userMapper mapper = sqlSession.getMapper(userMapper.class);
            isDelete = mapper.deleteUserById(delId);

            if (isDelete > 0){
                flag = true;
                sqlSession.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            sqlSession.commit();
        }finally {
            sqlSession.close();
        }

        return flag;
    }

    @Override
    public User getUserById(String id) {
        User user = new User();
        SqlSession sqlSession = null;

        try {
            sqlSession = mybatisUtil.getSqlSession();
            userMapper mapper = sqlSession.getMapper(userMapper.class);
            user = mapper.getUserById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            sqlSession.close();
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

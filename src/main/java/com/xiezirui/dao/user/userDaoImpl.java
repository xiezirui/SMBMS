package com.xiezirui.dao.user;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.mysql.jdbc.StringUtils;
import com.xiezirui.dao.baseDao;
import com.xiezirui.pojo.Role;
import com.xiezirui.pojo.User;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class userDaoImpl implements userDao{
    @Override//得到登录用户
    public User getLoginUser(Connection connection, String userCode, String userPassword) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        if (connection != null){
            String sql = "select * from `smbms_user` where userCode=? and userPassword=?";
            Object[] datas = {userCode,userPassword};

            try {
                resultSet = baseDao.select(connection, preparedStatement, resultSet,sql, datas);
                if (resultSet.next()){
                    user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setUserCode(resultSet.getString("userCode"));
                    user.setUserName(resultSet.getString("userName"));
                    user.setUserPassword(resultSet.getString("userPassword"));
                    user.setGender(resultSet.getInt("gender"));
                    user.setBirthday(resultSet.getDate("birthday"));
                    user.setPhone(resultSet.getString("phone"));
                    user.setAddress(resultSet.getString("address"));
                    user.setUserRole(resultSet.getInt("userRole"));
                    user.setCreatedBy(resultSet.getLong("createdBy"));
                    user.setCreationDate(resultSet.getDate("creationDate"));
                    user.setModifyBy(resultSet.getLong("modifyBy"));
                    user.setModifyDate(resultSet.getDate("modifyDate"));
                }
                baseDao.closeResources(connection,preparedStatement,resultSet);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    @Override//修改密码
    public int updataPassword(Connection connection, Long id, String password) {

        PreparedStatement preparedStatement = null;
        int execute = 0;
        if (connection != null){
            String sql = "UPDATE `smbms_user` SET userPassword = ? WHERE `id` =?";

            Object[] data = {password,id};

            try {
                execute = baseDao.execute(connection, preparedStatement, sql, data);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            baseDao.closeResources(connection,preparedStatement,null);
        }
        return execute;
    }

    @Override//获得用户总数
    public int getUserCount(Connection connection, String userName, int userRole) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int count = 0;

        ArrayList<Object> list = new ArrayList<>();

        if (connection != null){
            StringBuffer sql = new StringBuffer();
            sql.append("select count(1) as count from smbms.smbms_user u,smbms.smbms_role r where u.userRole = r.id");
            if (!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%" + userName +"%");
            }
            if (userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            //list --> object[]
            Object[] datas = list.toArray();
            //baseDao查询
            try {
                resultSet = baseDao.select(connection, preparedStatement, resultSet, sql.toString(), datas);
                if (resultSet.next()){
                    count = resultSet.getInt("count");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            baseDao.closeResources(connection,preparedStatement,resultSet);
        }
        return count;
    }

    @Override//获得用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        List<User> userList = new ArrayList<>();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        if (connection != null){
            ArrayList<Object> list = new ArrayList<>();
            StringBuffer sql = new StringBuffer();
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            if (!StringUtils.isNullOrEmpty(userName)){
                sql.append(" and u.userName like ?");
                list.add("%" + userName + "%");
            }
            if (userRole > 0){
                sql.append(" and u.userRole = ?");
                list.add(userRole);
            }
            sql.append(" order by creationDate DESC limit ?,?");
            list.add((currentPageNo - 1) * 5);
            list.add(pageSize);
            Object[] datas = list.toArray();
            resultSet = baseDao.select(connection, preparedStatement, resultSet, sql.toString(), datas);
            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setUserCode(resultSet.getString("userCode"));
                user.setUserName(resultSet.getString("userName"));
                user.setGender(resultSet.getInt("gender"));
                user.setBirthday(resultSet.getDate("birthday"));
                user.setPhone(resultSet.getString("phone"));
                user.setUserRole(resultSet.getInt("userRole"));
                user.setUserRoleName(resultSet.getString("userRoleName"));
                userList.add(user);
            }
            baseDao.closeResources(null,preparedStatement,resultSet);
        }
        System.out.println(userList);
        return userList;
    }

    @Override
    public User getUserById(Connection connection, String id) throws SQLException {
        PreparedStatement pstm=null;
        ResultSet rs=null;
        User user = new User();
        if(connection!=null){
            String sql="select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.id=? and u.userRole = r.id";
            Object[] datas={id};
            try {
                rs = baseDao.select(connection, pstm,rs,sql,datas);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            while(rs.next()){
                user.setId(rs.getLong("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getLong("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getLong("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
                user.setUserRoleName(rs.getString("userRoleName"));
            }
            baseDao.closeResources(null,pstm,rs);
        }
        return user;
    }

    @Override//修改用户信息
    public int modify(Connection connection, User user){
        int updateNum = 0;
        PreparedStatement pstm = null;
        if(null != connection){
            String sql = "update smbms_user set userName=?,"+
                    "gender=?,birthday=?,phone=?,address=?,userRole=?,modifyBy=?,modifyDate=? where id = ? ";
            Object[] datas = {user.getUserName(),user.getGender(),user.getBirthday(),
                    user.getPhone(),user.getAddress(),user.getUserRole(),user.getModifyBy(),
                    user.getModifyDate(),user.getId()};
            try {
                updateNum = baseDao.execute(connection,pstm,sql,datas);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            baseDao.closeResources(null, pstm, null);
        }
        return updateNum;
    }

    @Override//添加用户
    public int add(Connection connection, User user) {
        PreparedStatement preparedStatement=null;
        int updateNum=0;

        if (connection != null) {
            String sql = "insert into smbms_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] datas = {user.getUserCode(), user.getUserName(), user.getUserPassword(),
                    user.getUserRole(), user.getGender(), user.getBirthday(),
                    user.getPhone(), user.getAddress(), user.getCreationDate(), user.getCreatedBy()};

            try {
                updateNum = baseDao.execute(connection, preparedStatement, sql, datas);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            baseDao.closeResources(null, preparedStatement, null);
        }
        return updateNum;
    }

    @Override
    public int deleteUserById(Connection connection, Integer delId) throws Exception {
        PreparedStatement pstm=null;
        int deleteNum=0;
        if(connection!=null){
            String sql="DELETE FROM `smbms_user` WHERE id=?";
            Object[] datas={delId};
            deleteNum=baseDao.execute(connection,pstm,sql,datas);
            baseDao.closeResources(null,pstm,null);
        }
        return deleteNum;
    }

    @Test
    public void test(){
        Connection connection = baseDao.getConnection();
        String sql = "select * from `smbms_user` where userCode=?";
        try {
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            Object[] datas = {"wen"};
            ResultSet select = baseDao.select(connection, preparedStatement, resultSet, sql, datas);
            if (select.next()){
                User user = new User();
                user.setUserPassword(select.getString("userPassword"));
                System.out.println(user.getUserPassword());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

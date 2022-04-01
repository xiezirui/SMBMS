package com.xiezirui.dao.role;

import com.xiezirui.dao.baseDao;
import com.xiezirui.pojo.Role;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class roleDaoImpl implements roleDao{
    @Override
    public List<Role> getUserRole(Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Role> roleList = new ArrayList<>();

        if (connection != null){
            Object[] datas = {};
            String sql  = "select * from smbms_role";
            try {
                resultSet = baseDao.select(connection, preparedStatement, resultSet, sql, datas);

                while (resultSet.next()){
                    Role role = new Role();
                    role.setId(resultSet.getLong("id"));
                    role.setRoleCode(resultSet.getString("roleCode"));
                    role.setRoleName(resultSet.getString("roleName"));
                    roleList.add(role);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            baseDao.closeResources(null,preparedStatement,resultSet);
        }
        return roleList;
    }

    @Test
    public void textSelect(){
        List<Role> roleList = getUserRole(baseDao.getConnection());
        for (Role role : roleList) {
            System.out.println(role.getRoleName());
        }

    }
}

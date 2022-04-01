package com.xiezirui.service.role;

import com.xiezirui.dao.baseDao;
import com.xiezirui.dao.role.roleDao;
import com.xiezirui.dao.role.roleDaoImpl;
import com.xiezirui.pojo.Role;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

public class roleServiceImpl implements roleService{
    private roleDao roleDao;

    public roleServiceImpl() {
        roleDao = new roleDaoImpl();
    }

    public List<Role> getRoleList() {

        Connection connection = baseDao.getConnection();
        List<Role> roleListle = roleDao.getUserRole(connection);
        baseDao.closeResources(connection,null,null);

        return roleListle;
    }

    @Test
    public void text(){
        List<Role> roleList = getRoleList();
        for (Role role : roleList) {
            System.out.println(role.getRoleName());
        }
    }
}

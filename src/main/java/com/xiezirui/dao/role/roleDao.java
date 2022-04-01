package com.xiezirui.dao.role;

import com.xiezirui.pojo.Role;

import java.sql.Connection;
import java.util.List;

public interface roleDao {
    //获取角色列表
    public List<Role> getUserRole(Connection connection);
}

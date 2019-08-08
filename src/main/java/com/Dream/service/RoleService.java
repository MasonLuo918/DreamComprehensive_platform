package com.Dream.service;

import com.Dream.entity.Role;

import java.util.List;

public interface RoleService {
    public Role getRole(Long id);

    public int deleteRole(Long id);

    public int insertRole(Role role);

    public int updateRole(Role role);

    public List<Role> findRoles(String roleName, String note);
}

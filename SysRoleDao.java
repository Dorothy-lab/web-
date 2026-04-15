package dao;

import java.util.List;

import bean.SysRole;

public interface SysRoleDao {
    List<SysRole> findAll();

    int save(SysRole role);
}
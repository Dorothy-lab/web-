package dao;

import java.util.List;

import bean.SysUser;

public interface SysUserDao {
    SysUser login(String username, String password);

    int countAllManage();

    List<SysUser> findByPageManage(int start, int pageSize);

    SysUser findById(Integer id);

    int save(SysUser user);

    int update(SysUser user);

    int updateStatus(Integer id, Integer status);
}
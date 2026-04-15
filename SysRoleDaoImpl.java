package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.SysRole;
import dao.SysRoleDao;
import util.DBUtil;

public class SysRoleDaoImpl implements SysRoleDao {

    @Override
    public List<SysRole> findAll() {
        List<SysRole> list = new ArrayList<SysRole>();
        String sql = "select id, role_name, role_desc, create_time from sys_role order by id asc";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                SysRole role = new SysRole();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));
                role.setRoleDesc(rs.getString("role_desc"));
                role.setCreateTime(rs.getTimestamp("create_time"));
                list.add(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    @Override
    public int save(SysRole role) {
        String sql = "insert into sys_role(role_name, role_desc, create_time) values(?, ?, now())";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, role.getRoleName());
            ps.setString(2, role.getRoleDesc());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, conn);
        }
        return 0;
    }
}
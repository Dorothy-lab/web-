package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.SysConfig;
import dao.SysConfigDao;
import util.DBUtil;

public class SysConfigDaoImpl implements SysConfigDao {

    @Override
    public List<SysConfig> findAll() {
        List<SysConfig> list = new ArrayList<SysConfig>();
        String sql = "select id, config_key, config_value, remark, update_time from sys_config order by id asc";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                SysConfig c = new SysConfig();
                c.setId(rs.getInt("id"));
                c.setConfigKey(rs.getString("config_key"));
                c.setConfigValue(rs.getString("config_value"));
                c.setRemark(rs.getString("remark"));
                c.setUpdateTime(rs.getTimestamp("update_time"));
                list.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    @Override
    public int update(SysConfig config) {
        String sql = "update sys_config set config_value = ?, remark = ?, update_time = now() where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, config.getConfigValue());
            ps.setString(2, config.getRemark());
            ps.setInt(3, config.getId());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, conn);
        }
        return 0;
    }
}
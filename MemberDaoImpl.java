package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Member;
import dao.MemberDao;
import util.DBUtil;

public class MemberDaoImpl implements MemberDao {

    @Override
    public Member login(String username, String password) {
        String sql = "select id, username, password, nickname, phone, email, address, status, register_time, audit_time "
                   + "from member where username = ? and password = ? and status = 1";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                Member m = new Member();
                m.setId(rs.getInt("id"));
                m.setUsername(rs.getString("username"));
                m.setPassword(rs.getString("password"));
                m.setNickname(rs.getString("nickname"));
                m.setPhone(rs.getString("phone"));
                m.setEmail(rs.getString("email"));
                m.setAddress(rs.getString("address"));
                m.setStatus(rs.getInt("status"));
                m.setRegisterTime(rs.getTimestamp("register_time"));
                m.setAuditTime(rs.getTimestamp("audit_time"));
                return m;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    @Override
    public boolean existsByUsername(String username) {
        String sql = "select count(*) from member where username = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return false;
    }

    @Override
    public int save(Member member) {
        String sql = "insert into member(username, password, nickname, phone, email, address, status, register_time, audit_time) "
                   + "values(?, ?, ?, ?, ?, ?, 0, now(), null)";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, member.getUsername());
            ps.setString(2, member.getPassword());
            ps.setString(3, member.getNickname());
            ps.setString(4, member.getPhone());
            ps.setString(5, member.getEmail());
            ps.setString(6, member.getAddress());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, conn);
        }
        return 0;
    }

    @Override
    public int countAllManage() {
        String sql = "select count(*) from member";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return 0;
    }

    @Override
    public List<Member> findByPageManage(int start, int pageSize) {
        List<Member> list = new ArrayList<Member>();
        String sql = "select id, username, password, nickname, phone, email, address, status, register_time, audit_time "
                   + "from member order by id desc limit ?, ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, start);
            ps.setInt(2, pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                Member m = new Member();
                m.setId(rs.getInt("id"));
                m.setUsername(rs.getString("username"));
                m.setPassword(rs.getString("password"));
                m.setNickname(rs.getString("nickname"));
                m.setPhone(rs.getString("phone"));
                m.setEmail(rs.getString("email"));
                m.setAddress(rs.getString("address"));
                m.setStatus(rs.getInt("status"));
                m.setRegisterTime(rs.getTimestamp("register_time"));
                m.setAuditTime(rs.getTimestamp("audit_time"));
                list.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    @Override
    public void updateStatus(Integer id, Integer status) {
        String sql;
        if (status != null && status == 1) {
            sql = "update member set status = ?, audit_time = now() where id = ?";
        } else {
            sql = "update member set status = ? where id = ?";
        }

        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }
}
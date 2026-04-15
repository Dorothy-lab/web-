package dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Cart;
import dao.CartDao;
import util.DBUtil;

public class CartDaoImpl implements CartDao {

    @Override
    public void addOrIncrease(Integer memberId, Integer productId, Integer quantity) {
        String checkSql = "select id, quantity from cart where member_id = ? and product_id = ?";
        String updateSql = "update cart set quantity = ? where id = ?";
        String insertSql = "insert into cart(member_id, product_id, quantity, create_time) values(?, ?, ?, now())";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();

            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, memberId);
            ps.setInt(2, productId);
            rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int oldQty = rs.getInt("quantity");
                DBUtil.close(rs, ps, null);

                ps = conn.prepareStatement(updateSql);
                ps.setInt(1, oldQty + quantity);
                ps.setInt(2, id);
                ps.executeUpdate();
            } else {
                DBUtil.close(rs, ps, null);

                ps = conn.prepareStatement(insertSql);
                ps.setInt(1, memberId);
                ps.setInt(2, productId);
                ps.setInt(3, quantity);
                ps.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    @Override
    public List<Cart> findByMemberId(Integer memberId) {
        List<Cart> list = new ArrayList<Cart>();
        String sql = "select c.id, c.member_id, c.product_id, c.quantity, c.create_time, "
                   + "p.product_name, p.price, p.image "
                   + "from cart c left join product p on c.product_id = p.id "
                   + "where c.member_id = ? order by c.id desc";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Cart c = new Cart();
                c.setId(rs.getInt("id"));
                c.setMemberId(rs.getInt("member_id"));
                c.setProductId(rs.getInt("product_id"));
                c.setQuantity(rs.getInt("quantity"));
                c.setCreateTime(rs.getTimestamp("create_time"));
                c.setProductName(rs.getString("product_name"));
                c.setPrice(rs.getBigDecimal("price"));
                c.setImage(rs.getString("image"));

                BigDecimal subtotal = BigDecimal.ZERO;
                if (c.getPrice() != null && c.getQuantity() != null) {
                    subtotal = c.getPrice().multiply(new BigDecimal(c.getQuantity()));
                }
                c.setSubtotal(subtotal);

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
    public void updateQuantity(Integer id, Integer memberId, Integer quantity) {
        String sql = "update cart set quantity = ? where id = ? and member_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, quantity);
            ps.setInt(2, id);
            ps.setInt(3, memberId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    @Override
    public void delete(Integer id, Integer memberId) {
        String sql = "delete from cart where id = ? and member_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, memberId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    @Override
    public void clearByMemberId(Integer memberId) {
        String sql = "delete from cart where member_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }
}
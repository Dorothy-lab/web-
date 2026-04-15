package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.Cart;
import bean.OrderItem;
import bean.Orders;
import dao.OrderDao;
import util.DBUtil;

public class OrderDaoImpl implements OrderDao {

    @Override
    public int countAll() {
        String sql = "select count(*) from orders";
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
    public List<Orders> findByPage(int start, int pageSize) {
        List<Orders> list = new ArrayList<Orders>();
        String sql = "select id, order_no, member_id, total_amount, status, receiver_name, receiver_phone, receiver_address, create_time, update_time "
                   + "from orders order by id desc limit ?, ?";
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
                Orders o = buildOrder(rs);
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    @Override
    public Orders findById(Integer id) {
        String sql = "select id, order_no, member_id, total_amount, status, receiver_name, receiver_phone, receiver_address, create_time, update_time "
                   + "from orders where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return buildOrder(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    @Override
    public List<OrderItem> findItemsByOrderId(Integer orderId) {
        List<OrderItem> list = new ArrayList<OrderItem>();
        String sql = "select id, order_id, product_id, product_name, price, quantity, subtotal "
                   + "from order_item where order_id = ? order by id asc";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setProductName(rs.getString("product_name"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setQuantity(rs.getInt("quantity"));
                item.setSubtotal(rs.getBigDecimal("subtotal"));
                list.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    @Override
    public int createOrder(Orders order, List<Cart> cartList) {
        String orderSql = "insert into orders(order_no, member_id, total_amount, status, receiver_name, receiver_phone, receiver_address, create_time, update_time) "
                        + "values(?, ?, ?, ?, ?, ?, ?, now(), now())";
        String itemSql = "insert into order_item(order_id, product_id, product_name, price, quantity, subtotal) "
                       + "values(?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement orderPs = null;
        PreparedStatement itemPs = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            orderPs = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            orderPs.setString(1, order.getOrderNo());
            orderPs.setInt(2, order.getMemberId());
            orderPs.setBigDecimal(3, order.getTotalAmount());
            orderPs.setInt(4, order.getStatus());
            orderPs.setString(5, order.getReceiverName());
            orderPs.setString(6, order.getReceiverPhone());
            orderPs.setString(7, order.getReceiverAddress());
            orderPs.executeUpdate();

            rs = orderPs.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            itemPs = conn.prepareStatement(itemSql);
            for (Cart c : cartList) {
                itemPs.setInt(1, orderId);
                itemPs.setInt(2, c.getProductId());
                itemPs.setString(3, c.getProductName());
                itemPs.setBigDecimal(4, c.getPrice());
                itemPs.setInt(5, c.getQuantity());
                itemPs.setBigDecimal(6, c.getSubtotal());
                itemPs.addBatch();
            }
            itemPs.executeBatch();

            conn.commit();
            return orderId;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } finally {
            DBUtil.close(rs, orderPs, null);
            DBUtil.close(null, itemPs, conn);
        }
        return 0;
    }

    @Override
    public int countByMemberId(Integer memberId) {
        String sql = "select count(*) from orders where member_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
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
    public List<Orders> findByMemberIdByPage(Integer memberId, int start, int pageSize) {
        List<Orders> list = new ArrayList<Orders>();
        String sql = "select id, order_no, member_id, total_amount, status, receiver_name, receiver_phone, receiver_address, create_time, update_time "
                   + "from orders where member_id = ? order by id desc limit ?, ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, memberId);
            ps.setInt(2, start);
            ps.setInt(3, pageSize);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(buildOrder(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    @Override
    public Orders findByIdAndMemberId(Integer id, Integer memberId) {
        String sql = "select id, order_no, member_id, total_amount, status, receiver_name, receiver_phone, receiver_address, create_time, update_time "
                   + "from orders where id = ? and member_id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.setInt(2, memberId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return buildOrder(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    private Orders buildOrder(ResultSet rs) throws Exception {
        Orders o = new Orders();
        o.setId(rs.getInt("id"));
        o.setOrderNo(rs.getString("order_no"));
        o.setMemberId(rs.getInt("member_id"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getInt("status"));
        o.setReceiverName(rs.getString("receiver_name"));
        o.setReceiverPhone(rs.getString("receiver_phone"));
        o.setReceiverAddress(rs.getString("receiver_address"));
        o.setCreateTime(rs.getTimestamp("create_time"));
        o.setUpdateTime(rs.getTimestamp("update_time"));
        return o;
    }
}
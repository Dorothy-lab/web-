package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bean.Product;
import dao.ProductDao;
import util.DBUtil;

public class ProductDaoImpl implements ProductDao {

    @Override
    public int countAll() {
        String sql = "select count(*) from product";
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
    public List<Product> findByPage(int start, int pageSize) {
        List<Product> list = new ArrayList<Product>();
        String sql = "select p.id, p.product_name, p.category_id, c.category_name, p.price, p.stock, p.image, p.description, p.status, p.create_time, p.update_time, p.operator_id "
                   + "from product p left join category c on p.category_id = c.id "
                   + "order by p.id desc limit ?, ?";
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
                list.add(buildProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    @Override
    public Product findById(Integer id) {
        String sql = "select p.id, p.product_name, p.category_id, c.category_name, p.price, p.stock, p.image, p.description, p.status, p.create_time, p.update_time, p.operator_id "
                   + "from product p left join category c on p.category_id = c.id where p.id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return buildProduct(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return null;
    }

    @Override
    public void updateStatus(Integer id, Integer status, Integer operatorId) {
        String sql = "update product set status = ?, operator_id = ?, update_time = now() where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            ps.setInt(1, status);
            ps.setInt(2, operatorId);
            ps.setInt(3, id);
            ps.executeUpdate();

            insertLog(conn, id, status == 1 ? "上架" : "下架", operatorId, "后台商品状态变更");

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tryRollback(conn);
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    @Override
    public void save(Product product) {
        String sql = "insert into product(product_name, category_id, price, stock, image, description, status, operator_id, create_time, update_time) "
                   + "values(?, ?, ?, ?, ?, ?, ?, ?, now(), now())";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getProductName());
            ps.setInt(2, product.getCategoryId());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getImage());
            ps.setString(6, product.getDescription());
            ps.setInt(7, product.getStatus());
            ps.setInt(8, product.getOperatorId());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            Integer productId = null;
            if (rs.next()) {
                productId = rs.getInt(1);
            }

            if (productId != null) {
                insertLog(conn, productId, "新增", product.getOperatorId(), "后台新增商品");
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tryRollback(conn);
        } finally {
            DBUtil.close(rs, ps, conn);
        }
    }

    @Override
    public void update(Product product) {
        String sql = "update product set product_name = ?, category_id = ?, price = ?, stock = ?, image = ?, description = ?, status = ?, operator_id = ?, update_time = now() where id = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            ps.setString(1, product.getProductName());
            ps.setInt(2, product.getCategoryId());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStock());
            ps.setString(5, product.getImage());
            ps.setString(6, product.getDescription());
            ps.setInt(7, product.getStatus());
            ps.setInt(8, product.getOperatorId());
            ps.setInt(9, product.getId());
            ps.executeUpdate();

            insertLog(conn, product.getId(), "修改", product.getOperatorId(), "后台修改商品");

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tryRollback(conn);
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }

    @Override
    public int countFrontAll() {
        String sql = "select count(*) from product where status = 1";
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
    public List<Product> findFrontByPage(int start, int pageSize) {
        List<Product> list = new ArrayList<Product>();
        String sql = "select p.id, p.product_name, p.category_id, c.category_name, p.price, p.stock, p.image, p.description, p.status, p.create_time, p.update_time, p.operator_id "
                   + "from product p left join category c on p.category_id = c.id "
                   + "where p.status = 1 order by p.id desc limit ?, ?";
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
                list.add(buildProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }

    private Product buildProduct(ResultSet rs) throws Exception {
        Product p = new Product();
        p.setId(rs.getInt("id"));
        p.setProductName(rs.getString("product_name"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStock(rs.getInt("stock"));
        p.setImage(rs.getString("image"));
        p.setDescription(rs.getString("description"));
        p.setStatus(rs.getInt("status"));
        p.setCreateTime(rs.getTimestamp("create_time"));
        p.setUpdateTime(rs.getTimestamp("update_time"));
        p.setOperatorId(rs.getInt("operator_id"));
        return p;
    }

    private void insertLog(Connection conn, Integer productId, String operationType, Integer operatorId, String remark) throws Exception {
        String sql = "insert into product_log(product_id, operation_type, operator_id, operation_time, remark) values(?, ?, ?, now(), ?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);
            ps.setString(2, operationType);
            ps.setInt(3, operatorId);
            ps.setString(4, remark);
            ps.executeUpdate();
        } finally {
            DBUtil.close(null, ps, null);
        }
    }

    private void tryRollback(Connection conn) {
        try {
            if (conn != null) {
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
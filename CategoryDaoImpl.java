package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.Category;
import dao.CategoryDao;
import util.DBUtil;

public class CategoryDaoImpl implements CategoryDao {

    @Override
    public List<Category> findAll() {
        String sql = "SELECT * FROM category ORDER BY id ASC";
        List<Category> list = new ArrayList<Category>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setId(rs.getInt("id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setCreateTime(rs.getTimestamp("create_time"));
                list.add(category);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }
        return list;
    }
}

package dao;

import java.util.List;

import bean.Product;

public interface ProductDao {

    // 后台
    int countAll();
    List<Product> findByPage(int start, int pageSize);
    Product findById(Integer id);
    void updateStatus(Integer id, Integer status, Integer operatorId);
    void save(Product product);
    void update(Product product);

    // 前台
    int countFrontAll();
    List<Product> findFrontByPage(int start, int pageSize);
}
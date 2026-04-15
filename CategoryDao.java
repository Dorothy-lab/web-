package dao;

import java.util.List;

import bean.Category;

public interface CategoryDao {
    List<Category> findAll();
}

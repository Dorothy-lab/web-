package dao;

import java.util.List;

import bean.Cart;

public interface CartDao {
    void addOrIncrease(Integer memberId, Integer productId, Integer quantity);
    List<Cart> findByMemberId(Integer memberId);
    void updateQuantity(Integer id, Integer memberId, Integer quantity);
    void delete(Integer id, Integer memberId);
    void clearByMemberId(Integer memberId);
}
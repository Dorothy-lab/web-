package dao;

import java.util.List;

import bean.Cart;
import bean.OrderItem;
import bean.Orders;

public interface OrderDao {

    // 后台
    int countAll();
    List<Orders> findByPage(int start, int pageSize);
    Orders findById(Integer id);
    List<OrderItem> findItemsByOrderId(Integer orderId);

    // 前台
    int createOrder(Orders order, List<Cart> cartList);
    int countByMemberId(Integer memberId);
    List<Orders> findByMemberIdByPage(Integer memberId, int start, int pageSize);
    Orders findByIdAndMemberId(Integer id, Integer memberId);
}
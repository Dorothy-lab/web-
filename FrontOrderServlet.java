package servlet.front;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import bean.Cart;
import bean.Member;
import bean.OrderItem;
import bean.Orders;
import dao.CartDao;
import dao.OrderDao;
import dao.impl.CartDaoImpl;
import dao.impl.OrderDaoImpl;
import util.PageBean;

public class FrontOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderDao orderDao = new OrderDaoImpl();
    private CartDao cartDao = new CartDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handle(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        handle(request, response);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getPathInfo();

        if (path == null || "/".equals(path) || "/list".equals(path)) {
            list(request, response);
        } else if ("/submit".equals(path)) {
            submit(request, response);
        } else if ("/detail".equals(path)) {
            detail(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/front/order/list");
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Member memberUser = (Member) request.getSession().getAttribute("memberUser");

        int currentPage = 1;
        int pageSize = 8;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !"".equals(pageStr.trim())) {
            try {
                currentPage = Integer.parseInt(pageStr);
            } catch (Exception e) {
                currentPage = 1;
            }
        }

        if (currentPage < 1) {
            currentPage = 1;
        }

        int totalCount = orderDao.countByMemberId(memberUser.getId());
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        int start = (currentPage - 1) * pageSize;
        List<Orders> orderList = orderDao.findByMemberIdByPage(memberUser.getId(), start, pageSize);
        PageBean<Orders> pageBean = new PageBean<Orders>(currentPage, pageSize, totalCount, orderList);

        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/WEB-INF/front/order/list.jsp").forward(request, response);
    }

    private void submit(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Member memberUser = (Member) request.getSession().getAttribute("memberUser");

        List<Cart> cartList = cartDao.findByMemberId(memberUser.getId());
        if (cartList == null || cartList.size() == 0) {
            response.sendRedirect(request.getContextPath() + "/front/cart/list");
            return;
        }

        String receiverName = request.getParameter("receiverName");
        String receiverPhone = request.getParameter("receiverPhone");
        String receiverAddress = request.getParameter("receiverAddress");

        if (isEmpty(receiverName) || isEmpty(receiverPhone) || isEmpty(receiverAddress)) {
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (Cart c : cartList) {
                if (c.getSubtotal() != null) {
                    totalAmount = totalAmount.add(c.getSubtotal());
                }
            }
            request.setAttribute("cartList", cartList);
            request.setAttribute("totalAmount", totalAmount);
            request.setAttribute("errorMsg", "收货人、联系电话、收货地址不能为空");
            request.getRequestDispatcher("/WEB-INF/front/cart/list.jsp").forward(request, response);
            return;
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart c : cartList) {
            if (c.getSubtotal() != null) {
                totalAmount = totalAmount.add(c.getSubtotal());
            }
        }

        Orders order = new Orders();
        order.setOrderNo("ORD" + System.currentTimeMillis());
        order.setMemberId(memberUser.getId());
        order.setTotalAmount(totalAmount);
        order.setStatus(1);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);

        int orderId = orderDao.createOrder(order, cartList);
        if (orderId > 0) {
            cartDao.clearByMemberId(memberUser.getId());
        }

        response.sendRedirect(request.getContextPath() + "/front/order/list");
    }

    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Member memberUser = (Member) request.getSession().getAttribute("memberUser");
        Integer id = parseInteger(request.getParameter("id"));

        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/front/order/list");
            return;
        }

        Orders order = orderDao.findByIdAndMemberId(id, memberUser.getId());
        if (order == null) {
            response.sendRedirect(request.getContextPath() + "/front/order/list");
            return;
        }

        List<OrderItem> itemList = orderDao.findItemsByOrderId(id);
        request.setAttribute("order", order);
        request.setAttribute("itemList", itemList);
        request.getRequestDispatcher("/WEB-INF/front/order/detail.jsp").forward(request, response);
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }
}
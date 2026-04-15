package servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.OrderItem;
import bean.Orders;
import dao.OrderDao;
import dao.impl.OrderDaoImpl;
import util.PageBean;

public class AdminOrderServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private OrderDao orderDao = new OrderDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handle(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handle(request, response);
    }

    private void handle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String path = request.getPathInfo();
        if (path == null || "/".equals(path) || "/list".equals(path)) {
            list(request, response);
        } else if ("/detail".equals(path)) {
            detail(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/order/list");
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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

        int totalCount = orderDao.countAll();
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        int start = (currentPage - 1) * pageSize;
        List<Orders> orderList = orderDao.findByPage(start, pageSize);
        PageBean<Orders> pageBean = new PageBean<Orders>(currentPage, pageSize, totalCount, orderList);

        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/WEB-INF/admin/order/list.jsp").forward(request, response);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = parseInteger(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/order/list");
            return;
        }

        Orders order = orderDao.findById(id);
        List<OrderItem> itemList = orderDao.findItemsByOrderId(id);

        request.setAttribute("order", order);
        request.setAttribute("itemList", itemList);
        request.getRequestDispatcher("/WEB-INF/admin/order/detail.jsp").forward(request, response);
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
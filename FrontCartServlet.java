package servlet.front;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import bean.Cart;
import bean.Member;
import dao.CartDao;
import dao.ProductDao;
import dao.impl.CartDaoImpl;
import dao.impl.ProductDaoImpl;

public class FrontCartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private CartDao cartDao = new CartDaoImpl();
    private ProductDao productDao = new ProductDaoImpl();

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
        } else if ("/add".equals(path)) {
            add(request, response);
        } else if ("/update".equals(path)) {
            update(request, response);
        } else if ("/delete".equals(path)) {
            delete(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/front/cart/list");
        }
    }

    private void list(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Member memberUser = (Member) request.getSession().getAttribute("memberUser");
        List<Cart> cartList = cartDao.findByMemberId(memberUser.getId());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Cart c : cartList) {
            if (c.getSubtotal() != null) {
                totalAmount = totalAmount.add(c.getSubtotal());
            }
        }

        request.setAttribute("cartList", cartList);
        request.setAttribute("totalAmount", totalAmount);
        request.getRequestDispatcher("/WEB-INF/front/cart/list.jsp").forward(request, response);
    }

    private void add(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Member memberUser = (Member) request.getSession().getAttribute("memberUser");

        Integer productId = parseInteger(request.getParameter("productId"));
        Integer quantity = parseInteger(request.getParameter("quantity"));

        if (productId != null) {
            if (quantity == null || quantity < 1) {
                quantity = 1;
            }
            if (productDao.findById(productId) != null) {
                cartDao.addOrIncrease(memberUser.getId(), productId, quantity);
            }
        }

        response.sendRedirect(request.getContextPath() + "/front/cart/list");
    }

    private void update(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Member memberUser = (Member) request.getSession().getAttribute("memberUser");

        Integer id = parseInteger(request.getParameter("id"));
        Integer quantity = parseInteger(request.getParameter("quantity"));

        if (id != null && quantity != null) {
            if (quantity < 1) {
                quantity = 1;
            }
            cartDao.updateQuantity(id, memberUser.getId(), quantity);
        }

        response.sendRedirect(request.getContextPath() + "/front/cart/list");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Member memberUser = (Member) request.getSession().getAttribute("memberUser");

        Integer id = parseInteger(request.getParameter("id"));
        if (id != null) {
            cartDao.delete(id, memberUser.getId());
        }

        response.sendRedirect(request.getContextPath() + "/front/cart/list");
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
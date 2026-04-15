package servlet.front;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import bean.Product;
import dao.ProductDao;
import dao.impl.ProductDaoImpl;
import util.PageBean;

public class FrontProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductDao productDao = new ProductDaoImpl();

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
        String path = request.getPathInfo();

        if (path == null || "/".equals(path) || "/list".equals(path)) {
            list(request, response);
        } else if ("/detail".equals(path)) {
            detail(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/front/product/list");
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

        int totalCount = productDao.countFrontAll();
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        int start = (currentPage - 1) * pageSize;
        List<Product> productList = productDao.findFrontByPage(start, pageSize);

        PageBean<Product> pageBean = new PageBean<Product>(currentPage, pageSize, totalCount, productList);
        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/WEB-INF/front/product/list.jsp").forward(request, response);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = parseInteger(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/front/product/list");
            return;
        }

        Product product = productDao.findById(id);
        if (product == null || product.getStatus() == null || product.getStatus() != 1) {
            response.sendRedirect(request.getContextPath() + "/front/product/list");
            return;
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/front/product/detail.jsp").forward(request, response);
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
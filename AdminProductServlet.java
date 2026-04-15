package servlet.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Category;
import bean.Product;
import bean.SysUser;
import dao.CategoryDao;
import dao.ProductDao;
import dao.impl.CategoryDaoImpl;
import dao.impl.ProductDaoImpl;
import util.PageBean;

public class AdminProductServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private ProductDao productDao = new ProductDaoImpl();
    private CategoryDao categoryDao = new CategoryDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        handleRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        handleRequest(request, response);
    }

    private void handleRequest(HttpServletRequest request, HttpServletResponse response)
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
        } else if ("/toggleStatus".equals(path)) {
            toggleStatus(request, response);
        } else if ("/add".equals(path)) {
            addPage(request, response);
        } else if ("/save".equals(path)) {
            save(request, response);
        } else if ("/edit".equals(path)) {
            editPage(request, response);
        } else if ("/update".equals(path)) {
            update(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
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
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        if (currentPage < 1) {
            currentPage = 1;
        }

        int totalCount = productDao.countAll();
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);

        if (totalPage == 0) {
            totalPage = 1;
        }
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        int start = (currentPage - 1) * pageSize;
        List<Product> productList = productDao.findByPage(start, pageSize);

        PageBean<Product> pageBean = new PageBean<Product>(currentPage, pageSize, totalCount, productList);

        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/WEB-INF/admin/product/list.jsp").forward(request, response);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = parseInteger(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
            return;
        }

        Product product = productDao.findById(id);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
            return;
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("/WEB-INF/admin/product/detail.jsp").forward(request, response);
    }

    private void toggleStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer id = parseInteger(request.getParameter("id"));
        Integer status = parseInteger(request.getParameter("status"));

        HttpSession session = request.getSession(false);
        SysUser adminUser = (SysUser) session.getAttribute("adminUser");

        if (id != null && status != null) {
            productDao.updateStatus(id, status, adminUser.getId());
        }

        response.sendRedirect(request.getContextPath() + "/admin/product/list");
    }

    private void addPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categoryList = categoryDao.findAll();
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("/WEB-INF/admin/product/add.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        SysUser adminUser = (SysUser) session.getAttribute("adminUser");

        Product product = buildProduct(request);
        product.setOperatorId(adminUser.getId());

        productDao.save(product);

        response.sendRedirect(request.getContextPath() + "/admin/product/list");
    }

    private void editPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = parseInteger(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
            return;
        }

        Product product = productDao.findById(id);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
            return;
        }

        List<Category> categoryList = categoryDao.findAll();
        request.setAttribute("product", product);
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("/WEB-INF/admin/product/edit.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        SysUser adminUser = (SysUser) session.getAttribute("adminUser");

        Integer id = parseInteger(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/product/list");
            return;
        }

        Product product = buildProduct(request);
        product.setId(id);
        product.setOperatorId(adminUser.getId());

        productDao.update(product);

        response.sendRedirect(request.getContextPath() + "/admin/product/list");
    }

    private Product buildProduct(HttpServletRequest request) {
        Product product = new Product();
        product.setProductName(request.getParameter("productName"));
        product.setCategoryId(parseInteger(request.getParameter("categoryId")));
        product.setPrice(parseBigDecimal(request.getParameter("price")));
        product.setStock(parseInteger(request.getParameter("stock")));
        product.setImage(request.getParameter("image"));
        product.setDescription(request.getParameter("description"));
        Integer status = parseInteger(request.getParameter("status"));
        product.setStatus(status == null ? 1 : status);
        return product;
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
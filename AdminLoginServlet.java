package servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.SysUser;
import dao.SysUserDao;
import dao.impl.SysUserDaoImpl;

public class AdminLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SysUserDao sysUserDao = new SysUserDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("AdminLoginServlet doGet, URI=" + request.getRequestURI());
        request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("AdminLoginServlet doPost, username=" + username);

        if (username == null || "".equals(username.trim())
                || password == null || "".equals(password.trim())) {
            request.setAttribute("errorMsg", "用户名和密码不能为空");
            request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
            return;
        }

        SysUser user = sysUserDao.login(username.trim(), password.trim());

        System.out.println("login result user=" + user);

        if (user == null) {
            request.setAttribute("errorMsg", "用户名或密码错误，或账号已被禁用");
            request.getRequestDispatcher("/admin/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("adminUser", user);

        System.out.println("session id=" + session.getId() + ", adminUser set success");

        response.sendRedirect(request.getContextPath() + "/admin/product/list");
    }
}
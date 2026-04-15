package servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import bean.SysRole;
import dao.SysRoleDao;
import dao.impl.SysRoleDaoImpl;

public class AdminRoleServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SysRoleDao sysRoleDao = new SysRoleDaoImpl();

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
            request.setAttribute("roleList", sysRoleDao.findAll());
            request.getRequestDispatcher("/WEB-INF/admin/role/list.jsp").forward(request, response);
        } else if ("/save".equals(path)) {
            SysRole role = new SysRole();
            role.setRoleName(request.getParameter("roleName"));
            role.setRoleDesc(request.getParameter("roleDesc"));
            sysRoleDao.save(role);
            response.sendRedirect(request.getContextPath() + "/admin/role/list");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/role/list");
        }
    }
}
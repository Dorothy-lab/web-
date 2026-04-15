package servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import bean.SysRole;
import bean.SysUser;
import dao.SysRoleDao;
import dao.SysUserDao;
import dao.impl.SysRoleDaoImpl;
import dao.impl.SysUserDaoImpl;
import util.PageBean;

public class AdminUserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SysUserDao sysUserDao = new SysUserDaoImpl();
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
            list(request, response);
        } else if ("/add".equals(path)) {
            addPage(request, response);
        } else if ("/save".equals(path)) {
            save(request, response);
        } else if ("/edit".equals(path)) {
            editPage(request, response);
        } else if ("/update".equals(path)) {
            update(request, response);
        } else if ("/updateStatus".equals(path)) {
            updateStatus(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/user/list");
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

        int totalCount = sysUserDao.countAllManage();
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (currentPage < 1) {
            currentPage = 1;
        }
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        int start = (currentPage - 1) * pageSize;
        List<SysUser> userList = sysUserDao.findByPageManage(start, pageSize);
        PageBean<SysUser> pageBean = new PageBean<SysUser>(currentPage, pageSize, totalCount, userList);

        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/WEB-INF/admin/user/list.jsp").forward(request, response);
    }

    private void addPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("roleList", sysRoleDao.findAll());
        request.getRequestDispatcher("/WEB-INF/admin/user/add.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        SysUser user = new SysUser();
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setRealName(request.getParameter("realName"));
        user.setRoleId(parseInteger(request.getParameter("roleId")));
        user.setStatus(parseInteger(request.getParameter("status")) == null ? 1 : parseInteger(request.getParameter("status")));
        sysUserDao.save(user);

        response.sendRedirect(request.getContextPath() + "/admin/user/list");
    }

    private void editPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Integer id = parseInteger(request.getParameter("id"));
        if (id == null) {
            response.sendRedirect(request.getContextPath() + "/admin/user/list");
            return;
        }

        request.setAttribute("userObj", sysUserDao.findById(id));
        request.setAttribute("roleList", sysRoleDao.findAll());
        request.getRequestDispatcher("/WEB-INF/admin/user/edit.jsp").forward(request, response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        SysUser user = new SysUser();
        user.setId(parseInteger(request.getParameter("id")));
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setRealName(request.getParameter("realName"));
        user.setRoleId(parseInteger(request.getParameter("roleId")));
        user.setStatus(parseInteger(request.getParameter("status")) == null ? 1 : parseInteger(request.getParameter("status")));
        sysUserDao.update(user);

        response.sendRedirect(request.getContextPath() + "/admin/user/list");
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer id = parseInteger(request.getParameter("id"));
        Integer status = parseInteger(request.getParameter("status"));
        if (id != null && status != null) {
            sysUserDao.updateStatus(id, status);
        }
        response.sendRedirect(request.getContextPath() + "/admin/user/list");
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
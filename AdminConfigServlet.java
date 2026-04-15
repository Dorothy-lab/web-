package servlet.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import bean.SysConfig;
import dao.SysConfigDao;
import dao.impl.SysConfigDaoImpl;

public class AdminConfigServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private SysConfigDao sysConfigDao = new SysConfigDaoImpl();

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
            request.setAttribute("configList", sysConfigDao.findAll());
            request.getRequestDispatcher("/WEB-INF/admin/config/list.jsp").forward(request, response);
        } else if ("/update".equals(path)) {
            SysConfig config = new SysConfig();
            config.setId(parseInteger(request.getParameter("id")));
            config.setConfigValue(request.getParameter("configValue"));
            config.setRemark(request.getParameter("remark"));
            sysConfigDao.update(config);
            response.sendRedirect(request.getContextPath() + "/admin/config/list");
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/config/list");
        }
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
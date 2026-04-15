package filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import bean.SysUser;

public class AdminAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();

        if (uri.equals(contextPath + "/admin/login")
                || uri.equals(contextPath + "/admin/login.jsp")
                || uri.contains("/css/")
                || uri.contains("/js/")
                || uri.contains("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            resp.sendRedirect(contextPath + "/admin/login");
            return;
        }

        SysUser adminUser = (SysUser) session.getAttribute("adminUser");

        // 销售人员限制：不能访问用户管理、角色管理、系统设置
        if (adminUser.getRoleId() != null && adminUser.getRoleId() == 2) {
            if (uri.startsWith(contextPath + "/admin/user")
                    || uri.startsWith(contextPath + "/admin/role")
                    || uri.startsWith(contextPath + "/admin/config")) {
                resp.sendRedirect(contextPath + "/admin/product/list");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
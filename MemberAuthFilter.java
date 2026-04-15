package filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

public class MemberAuthFilter implements Filter {

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

        if (uri.equals(contextPath + "/front/login")
                || uri.equals(contextPath + "/front/register")
                || uri.equals(contextPath + "/front/logout")
                || uri.equals(contextPath + "/front/login.jsp")
                || uri.equals(contextPath + "/front/register.jsp")
                || uri.contains("/css/")
                || uri.contains("/js/")
                || uri.contains("/images/")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("memberUser") != null) {
            chain.doFilter(request, response);
            return;
        }

        resp.sendRedirect(contextPath + "/front/login");
    }

    @Override
    public void destroy() {
    }
}
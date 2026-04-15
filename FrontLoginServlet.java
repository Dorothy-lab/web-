package servlet.front;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import bean.Member;
import dao.MemberDao;
import dao.impl.MemberDaoImpl;

public class FrontLoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberDao memberDao = new MemberDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/front/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || "".equals(username.trim())
                || password == null || "".equals(password.trim())) {
            request.setAttribute("errorMsg", "用户名和密码不能为空");
            request.getRequestDispatcher("/front/login.jsp").forward(request, response);
            return;
        }

        Member member = memberDao.login(username.trim(), password.trim());
        if (member == null) {
            request.setAttribute("errorMsg", "登录失败：账号不存在、密码错误，或会员尚未审核通过");
            request.getRequestDispatcher("/front/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("memberUser", member);

        response.sendRedirect(request.getContextPath() + "/front/product/list");
    }
}
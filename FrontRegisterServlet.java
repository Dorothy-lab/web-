package servlet.front;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import bean.Member;
import dao.MemberDao;
import dao.impl.MemberDaoImpl;

public class FrontRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberDao memberDao = new MemberDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/front/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String nickname = request.getParameter("nickname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");

        if (isEmpty(username) || isEmpty(password) || isEmpty(confirmPassword)) {
            request.setAttribute("errorMsg", "用户名、密码、确认密码不能为空");
            request.getRequestDispatcher("/front/register.jsp").forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMsg", "两次输入的密码不一致");
            request.getRequestDispatcher("/front/register.jsp").forward(request, response);
            return;
        }

        if (memberDao.existsByUsername(username.trim())) {
            request.setAttribute("errorMsg", "该用户名已存在");
            request.getRequestDispatcher("/front/register.jsp").forward(request, response);
            return;
        }

        Member member = new Member();
        member.setUsername(username.trim());
        member.setPassword(password.trim());
        member.setNickname(nickname);
        member.setPhone(phone);
        member.setEmail(email);
        member.setAddress(address);

        int result = memberDao.save(member);

        if (result > 0) {
            request.setAttribute("successMsg", "注册成功，当前状态为待审核。请到后台会员管理中审核通过后再登录。");
        } else {
            request.setAttribute("errorMsg", "注册失败，请重试");
        }

        request.getRequestDispatcher("/front/register.jsp").forward(request, response);
    }

    private boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }
}
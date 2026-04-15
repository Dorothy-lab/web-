package servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Member;
import dao.MemberDao;
import dao.impl.MemberDaoImpl;
import util.PageBean;

public class AdminMemberServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private MemberDao memberDao = new MemberDaoImpl();

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

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("adminUser") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String path = request.getPathInfo();
        if (path == null || "/".equals(path) || "/list".equals(path)) {
            list(request, response);
        } else if ("/updateStatus".equals(path)) {
            updateStatus(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/member/list");
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

        int totalCount = memberDao.countAllManage();
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        if (totalPage == 0) {
            totalPage = 1;
        }
        if (currentPage > totalPage) {
            currentPage = totalPage;
        }

        int start = (currentPage - 1) * pageSize;
        List<Member> memberList = memberDao.findByPageManage(start, pageSize);
        PageBean<Member> pageBean = new PageBean<Member>(currentPage, pageSize, totalCount, memberList);

        request.setAttribute("pageBean", pageBean);
        request.getRequestDispatcher("/WEB-INF/admin/member/list.jsp").forward(request, response);
    }

    private void updateStatus(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Integer id = parseInteger(request.getParameter("id"));
        Integer status = parseInteger(request.getParameter("status"));
        if (id != null && status != null) {
            memberDao.updateStatus(id, status);
        }
        response.sendRedirect(request.getContextPath() + "/admin/member/list");
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.valueOf(value);
        } catch (Exception e) {
            return null;
        }
    }
}
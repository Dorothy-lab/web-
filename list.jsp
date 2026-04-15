<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="util.PageBean" %>
<%@ page import="bean.Product" %>
<%@ page import="bean.Member" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品列表</title>
<style>
    body { margin:0; background:#f4f4f4; font-family:Arial,"Microsoft YaHei",sans-serif; color:#333; }
    .topbar { background:#e1251b; color:#fff; padding:14px 24px; display:flex; justify-content:space-between; align-items:center; }
    .topbar a { color:#fff; text-decoration:none; margin-left:16px; }
    .container { width:1200px; margin:24px auto; }
    .title { font-size:24px; margin-bottom:18px; }
    .grid { display:grid; grid-template-columns:repeat(4, 1fr); gap:18px; }
    .card {
        background:#fff; border-radius:8px; box-shadow:0 2px 12px rgba(0,0,0,0.08);
        padding:16px; min-height:220px;
    }
    .img-box {
        height:110px; display:flex; align-items:center; justify-content:center;
        background:#fafafa; border-radius:6px; margin-bottom:12px; color:#999;
    }
    .name { font-size:16px; font-weight:700; min-height:44px; line-height:1.4; }
    .price { color:#e1251b; font-size:20px; margin:10px 0; font-weight:700; }
    .meta { color:#666; font-size:13px; margin-bottom:8px; }
    .detail-link { color:#409eff; text-decoration:none; }
    .pager { margin-top:24px; text-align:center; }
    .pager a, .pager span {
        display:inline-block; margin:0 4px; padding:8px 12px; border:1px solid #dcdfe6;
        border-radius:4px; background:#fff; color:#333; text-decoration:none;
    }
    .pager .current { background:#e1251b; color:#fff; border-color:#e1251b; }
</style>
</head>
<body>
<%
    Member memberUser = (Member) session.getAttribute("memberUser");
    PageBean<Product> pageBean = (PageBean<Product>) request.getAttribute("pageBean");
    List<Product> list = pageBean == null ? null : pageBean.getData();
%>

<div class="topbar">
    <div>京东风格前台商城</div>
    <div>
        当前会员：<%= memberUser == null ? "" : memberUser.getUsername() %>
        <a href="<%= request.getContextPath() %>/front/product/list">商品浏览</a>
        <a href="<%= request.getContextPath() %>/front/logout">退出登录</a>
    </div>
</div>

<div class="container">
    <div class="title">商品列表</div>

    <div class="grid">
        <%
            if (list != null) {
                for (Product p : list) {
        %>
        <div class="card">
            <div class="img-box">
                <%= p.getImage() == null ? "商品图片" : p.getImage() %>
            </div>
            <div class="name"><%= p.getProductName() %></div>
            <div class="price">¥<%= p.getPrice() %></div>
            <div class="meta">分类：<%= p.getCategoryName() == null ? "" : p.getCategoryName() %></div>
            <div class="meta">库存：<%= p.getStock() %></div>
            <a class="detail-link" href="<%= request.getContextPath() %>/front/product/detail?id=<%= p.getId() %>">查看详情</a>
        </div>
        <%
                }
            }
        %>
    </div>

    <div class="pager">
        <%
            if (pageBean != null) {
                int currentPage = pageBean.getCurrentPage();
                int totalPage = pageBean.getTotalPage();

                if (currentPage > 1) {
        %>
            <a href="<%= request.getContextPath() %>/front/product/list?page=1">首页</a>
            <a href="<%= request.getContextPath() %>/front/product/list?page=<%= currentPage - 1 %>">上一页</a>
        <%
                }

                for (int i = 1; i <= totalPage; i++) {
                    if (i == currentPage) {
        %>
            <span class="current"><%= i %></span>
        <%
                    } else {
        %>
            <a href="<%= request.getContextPath() %>/front/product/list?page=<%= i %>"><%= i %></a>
        <%
                    }
                }

                if (currentPage < totalPage) {
        %>
            <a href="<%= request.getContextPath() %>/front/product/list?page=<%= currentPage + 1 %>">下一页</a>
            <a href="<%= request.getContextPath() %>/front/product/list?page=<%= totalPage %>">末页</a>
        <%
                }
            }
        %>
    </div>
</div>
</body>
</html>
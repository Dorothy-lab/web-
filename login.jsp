<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会员登录</title>
<style>
    body { margin:0; font-family:Arial,"Microsoft YaHei",sans-serif; background:linear-gradient(135deg,#fff5f5,#ffeaea); }
    .wrap { width:420px; margin:120px auto; background:#fff; border-radius:10px; box-shadow:0 8px 30px rgba(0,0,0,0.12); padding:36px; }
    .title { margin:0 0 20px; text-align:center; color:#e1251b; font-size:28px; font-weight:700; }
    .sub { margin:0 0 24px; text-align:center; color:#999; font-size:13px; }
    .form-item { margin-bottom:16px; }
    .form-item label { display:block; margin-bottom:8px; color:#555; }
    .form-item input { width:100%; box-sizing:border-box; padding:12px 14px; border:1px solid #dcdfe6; border-radius:6px; }
    .btn { width:100%; padding:12px 0; border:none; border-radius:6px; background:#e1251b; color:#fff; font-size:16px; cursor:pointer; }
    .btn:hover { background:#c81623; }
    .error { margin-bottom:16px; background:#fdecea; border:1px solid #f5c6cb; color:#c0392b; padding:10px 12px; border-radius:6px; font-size:13px; }
    .link-box { margin-top:16px; text-align:center; font-size:14px; }
    .link-box a { color:#409eff; text-decoration:none; }
</style>
</head>
<body>
<div class="wrap">
    <h1 class="title">前台会员登录</h1>
    <p class="sub">登录后才能浏览商品和后续下单</p>

    <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        if (errorMsg != null && !"".equals(errorMsg)) {
    %>
        <div class="error"><%= errorMsg %></div>
    <%
        }
    %>

    <form action="<%= request.getContextPath() %>/front/login" method="post">
        <div class="form-item">
            <label>用户名</label>
            <input type="text" name="username" placeholder="请输入会员用户名" />
        </div>
        <div class="form-item">
            <label>密码</label>
            <input type="password" name="password" placeholder="请输入密码" />
        </div>
        <button type="submit" class="btn">登 录</button>
    </form>

    <div class="link-box">
        还没有账号？
        <a href="<%= request.getContextPath() %>/front/register">立即注册</a>
    </div>

    <div class="link-box">
        可直接测试已审核账号：zhangsan / 123456
    </div>
</div>
</body>
</html>
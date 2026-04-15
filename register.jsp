<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>会员注册</title>
<style>
    body { margin:0; font-family:Arial,"Microsoft YaHei",sans-serif; background:linear-gradient(135deg,#f6f9fc,#eef3f8); }
    .wrap { width:520px; margin:60px auto; background:#fff; border-radius:10px; box-shadow:0 8px 30px rgba(0,0,0,0.12); padding:36px; }
    .title { margin:0 0 20px; text-align:center; color:#333; font-size:28px; font-weight:700; }
    .form-item { margin-bottom:14px; }
    .form-item label { display:block; margin-bottom:8px; color:#555; }
    .form-item input { width:100%; box-sizing:border-box; padding:12px 14px; border:1px solid #dcdfe6; border-radius:6px; }
    .btn { width:100%; padding:12px 0; border:none; border-radius:6px; background:#e1251b; color:#fff; font-size:16px; cursor:pointer; }
    .btn:hover { background:#c81623; }
    .error { margin-bottom:16px; background:#fdecea; border:1px solid #f5c6cb; color:#c0392b; padding:10px 12px; border-radius:6px; font-size:13px; }
    .success { margin-bottom:16px; background:#edf7ed; border:1px solid #c8e6c9; color:#2e7d32; padding:10px 12px; border-radius:6px; font-size:13px; }
    .link-box { margin-top:16px; text-align:center; font-size:14px; }
    .link-box a { color:#409eff; text-decoration:none; }
</style>
</head>
<body>
<div class="wrap">
    <h1 class="title">会员注册</h1>

    <%
        String errorMsg = (String) request.getAttribute("errorMsg");
        String successMsg = (String) request.getAttribute("successMsg");
        if (errorMsg != null && !"".equals(errorMsg)) {
    %>
        <div class="error"><%= errorMsg %></div>
    <%
        }
        if (successMsg != null && !"".equals(successMsg)) {
    %>
        <div class="success"><%= successMsg %></div>
    <%
        }
    %>

    <form action="<%= request.getContextPath() %>/front/register" method="post">
        <div class="form-item">
            <label>用户名</label>
            <input type="text" name="username" />
        </div>
        <div class="form-item">
            <label>密码</label>
            <input type="password" name="password" />
        </div>
        <div class="form-item">
            <label>确认密码</label>
            <input type="password" name="confirmPassword" />
        </div>
        <div class="form-item">
            <label>昵称</label>
            <input type="text" name="nickname" />
        </div>
        <div class="form-item">
            <label>手机号</label>
            <input type="text" name="phone" />
        </div>
        <div class="form-item">
            <label>邮箱</label>
            <input type="text" name="email" />
        </div>
        <div class="form-item">
            <label>地址</label>
            <input type="text" name="address" />
        </div>
        <button type="submit" class="btn">注 册</button>
    </form>

    <div class="link-box">
        已有账号？
        <a href="<%= request.getContextPath() %>/front/login">返回登录</a>
    </div>
</div>
</body>
</html>
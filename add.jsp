<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="bean.Category" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新增商品</title>
<style>
    body { margin:0; background:#f5f5f5; font-family:Arial,"Microsoft YaHei",sans-serif; }
    .container { width:900px; margin:30px auto; }
    .card { background:#fff; border-radius:8px; box-shadow:0 2px 12px rgba(0,0,0,0.08); padding:24px 28px; }
    h2 { margin-top:0; margin-bottom:20px; }
    .form-item { margin-bottom:16px; }
    .form-item label { display:block; margin-bottom:8px; color:#555; }
    .form-item input, .form-item select, .form-item textarea {
        width:100%; box-sizing:border-box; padding:10px 12px; border:1px solid #dcdfe6; border-radius:6px;
    }
    .form-item textarea { height:120px; resize:vertical; }
    .btn { display:inline-block; padding:10px 18px; border:none; border-radius:6px; cursor:pointer; text-decoration:none; }
    .btn-save { background:#e1251b; color:#fff; }
    .btn-back { background:#909399; color:#fff; margin-left:10px; }
</style>
</head>
<body>
<%
    List<Category> categoryList = (List<Category>) request.getAttribute("categoryList");
%>
<div class="container">
    <div class="card">
        <h2>新增商品</h2>
        <form action="<%= request.getContextPath() %>/admin/product/save" method="post">
            <div class="form-item">
                <label>商品名称</label>
                <input type="text" name="productName" required />
            </div>

            <div class="form-item">
                <label>商品分类</label>
                <select name="categoryId" required>
                    <%
                        if (categoryList != null) {
                            for (Category c : categoryList) {
                    %>
                    <option value="<%= c.getId() %>"><%= c.getCategoryName() %></option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <div class="form-item">
                <label>价格</label>
                <input type="text" name="price" required />
            </div>

            <div class="form-item">
                <label>库存</label>
                <input type="number" name="stock" required />
            </div>

            <div class="form-item">
                <label>图片路径</label>
                <input type="text" name="image" value="images/default.jpg" />
            </div>

            <div class="form-item">
                <label>状态</label>
                <select name="status">
                    <option value="1">上架</option>
                    <option value="0">下架</option>
                </select>
            </div>

            <div class="form-item">
                <label>商品描述</label>
                <textarea name="description"></textarea>
            </div>

            <button type="submit" class="btn btn-save">保存</button>
            <a href="<%= request.getContextPath() %>/admin/product/list" class="btn btn-back">返回</a>
        </form>
    </div>
</div>
</body>
</html>
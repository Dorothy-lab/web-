<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="bean.Category" %>
<%@ page import="bean.Product" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改商品</title>
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
    Product product = (Product) request.getAttribute("product");
    List<Category> categoryList = (List<Category>) request.getAttribute("categoryList");
%>
<div class="container">
    <div class="card">
        <h2>修改商品</h2>
        <form action="<%= request.getContextPath() %>/admin/product/update" method="post">
            <input type="hidden" name="id" value="<%= product.getId() %>" />

            <div class="form-item">
                <label>商品名称</label>
                <input type="text" name="productName" value="<%= product.getProductName() %>" required />
            </div>

            <div class="form-item">
                <label>商品分类</label>
                <select name="categoryId" required>
                    <%
                        if (categoryList != null) {
                            for (Category c : categoryList) {
                    %>
                    <option value="<%= c.getId() %>" <%= c.getId().equals(product.getCategoryId()) ? "selected" : "" %>>
                        <%= c.getCategoryName() %>
                    </option>
                    <%
                            }
                        }
                    %>
                </select>
            </div>

            <div class="form-item">
                <label>价格</label>
                <input type="text" name="price" value="<%= product.getPrice() %>" required />
            </div>

            <div class="form-item">
                <label>库存</label>
                <input type="number" name="stock" value="<%= product.getStock() %>" required />
            </div>

            <div class="form-item">
                <label>图片路径</label>
                <input type="text" name="image" value="<%= product.getImage() == null ? "" : product.getImage() %>" />
            </div>

            <div class="form-item">
                <label>状态</label>
                <select name="status">
                    <option value="1" <%= product.getStatus() == 1 ? "selected" : "" %>>上架</option>
                    <option value="0" <%= product.getStatus() == 0 ? "selected" : "" %>>下架</option>
                </select>
            </div>

            <div class="form-item">
                <label>商品描述</label>
                <textarea name="description"><%= product.getDescription() == null ? "" : product.getDescription() %></textarea>
            </div>

            <button type="submit" class="btn btn-save">保存修改</button>
            <a href="<%= request.getContextPath() %>/admin/product/list" class="btn btn-back">返回</a>
        </form>
    </div>
</div>
</body>
</html>
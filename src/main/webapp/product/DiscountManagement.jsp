<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List, model.Category" %>

<jsp:useBean id="productService" scope="page" class="service.ProductService"/>
<%
    if(request.getAttribute("categories") == null){
        request.setAttribute("categories", productService.getAllCategories());
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>Quản lý khuyến mãi</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <style>
        /* General body styling */
        body {
            background-color: #f8f9fa;
            color: #333;
            font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
            margin: 0;
            padding: 0;
        }

        /* Container styling */
        .container {
            background-color: #fff;
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            margin-top: 30px;
            margin-bottom: 30px;
        }

        /* Header styling */
        h2.text-primary {
            color: red !important;
            font-weight: 700;
            margin-bottom: 1.5rem;
            position: relative;
            padding-bottom: 10px;
        }

        h2.text-primary::after {
            content: '';
            position: absolute;
            left: 50%;
            bottom: 0;
            transform: translateX(-50%);
            width: 80px;
            height: 3px;
            background-color: red !important;
        }

        /* Form styling */
        .form-card {
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
            padding: 25px;
            margin-bottom: 30px;
        }

        .form-label {
            font-weight: 600;
            color: red;
            margin-bottom: 0.5rem;
        }

        /* Buttons */
        .btn-primary {
            background-color: #3c9d74;
            border-color: #3c9d74;
            color: #fff;
            font-weight: 500;
            padding: 0.6rem 1.5rem;
            border-radius: 6px;
            transition: all 0.2s ease;
        }

        .btn-primary:hover, .btn-primary:focus {
            background-color: #2e8b57;
            border-color: #2e8b57;
            color: #fff;
            box-shadow: 0 4px 8px rgba(46, 139, 87, 0.3);
            transform: translateY(-2px);
        }

        .btn-danger {
            background-color: #dc3545;
            border-color: #dc3545;
            color: #fff;
            font-weight: 500;
            padding: 0.6rem 1.5rem;
            border-radius: 6px;
            transition: all 0.2s ease;
        }

        .btn-danger:hover, .btn-danger:focus {
            background-color: #c82333;
            border-color: #bd2130;
            color: #fff;
            box-shadow: 0 4px 8px rgba(220, 53, 69, 0.3);
            transform: translateY(-2px);
        }

        /* Form controls */
        .form-select, .form-control {
            border: 1px solid #e0e0e0;
            border-radius: 6px;
            color: #333;
            padding: 0.6rem 1rem;
            transition: all 0.2s ease;
        }

        .form-select:focus, .form-control:focus {
            border-color: #3c9d74;
            box-shadow: 0 0 0 0.25rem rgba(60, 157, 116, 0.15);
        }

        /* Alert messages */
        .alert {
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }

        .alert-success {
            background-color: #d4edda;
            border-color: #c3e6cb;
            color: #155724;
        }

        .alert-danger {
            background-color: #f8d7da;
            border-color: #f5c6cb;
            color: #721c24;
        }
    </style>
</head>
<body>

<div class="container mt-4">
    <h2 class="text-center text-primary mb-4">Quản lý Khuyến Mãi</h2>

    <!-- Display success/error messages for Apply Discount -->
    <c:if test="${not empty applyMessage}">
        <div class="alert alert-success">Áp dụng khuyến mãi thành công: ${applyMessage}</div>
    </c:if>
    <c:if test="${not empty applyError}">
        <div class="alert alert-danger">Lỗi khi áp dụng khuyến mãi: ${applyError}</div>
    </c:if>

    <!-- Display success/error messages for Remove Discount -->
    <c:if test="${not empty removeMessage}">
        <div class="alert alert-success">Xóa khuyến mãi thành công: ${removeMessage}</div>
    </c:if>
    <c:if test="${not empty removeError}">
        <div class="alert alert-danger">Lỗi khi xóa khuyến mãi: ${removeError}</div>
    </c:if>

    <!-- Apply Discount Form -->
    <div class="row">
        <div class="col-md-6">
            <div class="form-card">
                <h4 class="mb-3">Áp dụng Khuyến Mãi</h4>
                <form action="products" method="post">
                    <input type="hidden" name="action" value="applyDiscount">

                    <div class="mb-3">
                        <label for="categoryId" class="form-label">Danh mục sản phẩm</label>
                        <select name="categoryId" id="categoryId" class="form-select">
                            <option value="">Tất cả danh mục</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.id}">${category.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="searchName" class="form-label">Tên sản phẩm</label>
                        <input type="text" name="searchName" id="searchName" class="form-control" placeholder="Để trống nếu muốn áp dụng cho tất cả">
                    </div>

                    <div class="mb-3">
                        <label for="discountPercent" class="form-label">Phần trăm giảm giá (%)</label>
                        <input type="number" name="discountPercent" id="discountPercent" class="form-control" min="1" max="99" required>
                    </div>

                    <div class="mb-3">
                        <label for="expireDate" class="form-label">Ngày hết hạn</label>
                        <input type="date" name="expireDate" id="expireDate" class="form-control" required>
                        <script>
                            // Set min date to today
                            const today = new Date();
                            const yyyy = today.getFullYear();
                            const mm = String(today.getMonth() + 1).padStart(2, '0');
                            const dd = String(today.getDate()).padStart(2, '0');
                            document.getElementById('expireDate').min = `${yyyy}-${mm}-${dd}`;
                        </script>
                    </div>

                    <button type="submit" class="btn btn-primary w-100">Áp dụng Khuyến Mãi</button>
                </form>
            </div>
        </div>

        <!-- Remove Discount Form -->
        <div class="col-md-6">
            <div class="form-card">
                <h4 class="mb-3">Xóa Khuyến Mãi</h4>
                <form action="products" method="post">
                    <input type="hidden" name="action" value="removeDiscount">

                    <div class="mb-3">
                        <label for="removeCategory" class="form-label">Danh mục sản phẩm</label>
                        <select name="categoryId" id="removeCategory" class="form-select">
                            <option value="">Tất cả danh mục</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.id}">${category.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="removeSearchName" class="form-label">Tên sản phẩm</label>
                        <input type="text" name="searchName" id="removeSearchName" class="form-control" placeholder="Để trống nếu muốn xóa cho tất cả">
                    </div>

                    <div class="alert alert-warning">
                        <strong>Lưu ý:</strong> Hành động này sẽ xóa tất cả khuyến mãi của các sản phẩm phù hợp với điều kiện trên.
                    </div>

                    <button type="submit" class="btn btn-danger w-100">Xóa Khuyến Mãi</button>
                </form>
            </div>
        </div>
    </div>

    <!-- View Discounted Products Link -->
    <div class="text-center mt-3">
        <a href="${pageContext.request.contextPath}/products?action=showDiscountedProducts" class="btn btn-primary">
            Xem Danh Sách Sản Phẩm Khuyến Mãi
        </a>
    </div>
</div>

</body>
</html>
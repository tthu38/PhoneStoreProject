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
            background-color: #fafafa;
            color: #333;
            font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
            margin: 0;
            padding: 0;
        }

        /* Container styling */
        .container {
            background-color: #fff;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
            margin: 40px auto;
            max-width: 1200px;
        }

        /* Header styling */
        h2.text-primary {
            color: #d32f2f !important;
            font-weight: 700;
            font-size: 2.2rem;
            margin-bottom: 2.5rem;
            position: relative;
            text-align: center;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        h2.text-primary::after {
            content: '';
            position: absolute;
            left: 50%;
            bottom: 0;
            transform: translateX(-50%);
            width: 100px;
            height: 4px;
            background: linear-gradient(to right, #ef5350, #d32f2f);
            border-radius: 2px;
        }

        /* Form styling */
        .form-card {
            background-color: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.06);
            padding: 25px;
            margin-bottom: 30px;
            transition: all 0.3s ease;
        }

        .form-card:hover {
            box-shadow: 0 6px 20px rgba(211, 47, 47, 0.1);
        }

        .form-label {
            font-weight: 600;
            color: #d32f2f;
            margin-bottom: 0.5rem;
        }

        /* Buttons */
        .btn-primary {
            background-color: #ef5350;
            border-color: #ef5350;
            color: #fff;
            font-weight: 600;
            padding: 0.7rem 1.8rem;
            border-radius: 8px;
            transition: all 0.3s ease;
            box-shadow: 0 2px 5px rgba(239, 83, 80, 0.3);
        }

        .btn-primary:hover, .btn-primary:focus {
            background-color: #d32f2f;
            border-color: #d32f2f;
            color: #fff;
            box-shadow: 0 5px 10px rgba(211, 47, 47, 0.4);
            transform: translateY(-2px);
        }

        .btn-danger {
            background-color: #dc3545;
            border-color: #dc3545;
            color: #fff;
            font-weight: 600;
            padding: 0.7rem 1.8rem;
            border-radius: 8px;
            transition: all 0.3s ease;
            box-shadow: 0 2px 5px rgba(220, 53, 69, 0.3);
        }

        .btn-danger:hover, .btn-danger:focus {
            background-color: #c82333;
            border-color: #bd2130;
            color: #fff;
            box-shadow: 0 5px 10px rgba(220, 53, 69, 0.4);
            transform: translateY(-2px);
        }

        /* Form controls */
        .form-select, .form-control {
            border: 1px solid #e0e0e0;
            border-radius: 8px;
            color: #333;
            padding: 0.7rem 1rem;
            transition: all 0.2s ease;
        }

        .form-select:focus, .form-control:focus {
            border-color: #ef5350;
            box-shadow: 0 0 0 0.25rem rgba(239, 83, 80, 0.15);
        }

        /* Alert messages */
        .alert {
            border-radius: 10px;
            padding: 1.2rem;
            margin-bottom: 1.5rem;
            font-size: 1.1rem;
        }

        .alert-success {
            background-color: #f1e7e7;
            border-color: #e2c7c7;
            color: #721c24;
        }

        .alert-danger {
            background-color: #f8d7da;
            border-color: #f5c6cb;
            color: #721c24;
        }

        /* Responsive adjustments */
        @media (max-width: 768px) {
            .container {
                padding: 20px;
                margin: 20px auto;
            }

            .form-card {
                padding: 15px;
            }

            h2.text-primary {
                font-size: 1.8rem;
            }

            .btn-primary, .btn-danger {
                padding: 0.6rem 1.5rem;
                font-size: 0.9rem;
            }
        }
    </style>
</head>
<body>
<!-- Include the admin header JSP file -->
<jsp:include page="/templates/header.jsp"/>

<div class="container mt-4">
    <h2 class="text-center text-primary mb-4">Quản lý Khuyến Mãi</h2>

    <!-- Display success/error messages if any -->
    <c:if test="${not empty message}">
        <div class="alert alert-success">${message}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <!-- Apply Discount Form -->
    <div class="row">
        <div class="col-md-6">
            <div class="form-card">
                <h4 class="mb-3" style="color: #d32f2f;">Áp dụng Khuyến Mãi</h4>
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
                <h4 class="mb-3" style="color: #d32f2f;">Xóa Khuyến Mãi</h4>
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
<!--    <div class="text-center mt-4">
        <a href="${pageContext.request.contextPath}/products?action=showDiscountedProducts" class="btn btn-primary">
            Xem Danh Sách Sản Phẩm Khuyến Mãi
        </a>
    </div>-->
</div>

<!-- Include the footer JSP file -->
<jsp:include page="/templates/footer.jsp"/>
</body>
</html>
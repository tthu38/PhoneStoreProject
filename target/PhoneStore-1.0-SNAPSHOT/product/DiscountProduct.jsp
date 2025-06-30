<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Product, model.Category" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Sản phẩm Khuyến Mãi</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
            padding: 40px;
            border-radius: 16px;
            box-shadow: 0 6px 18px rgba(0, 0, 0, 0.08);
            margin-top: 40px;
            margin-bottom: 40px;
        }

        /* Header styling */
        h2.text-primary {
            color: #d32f2f !important;
            font-weight: 700;
            margin-bottom: 2rem;
            position: relative;
            padding-bottom: 12px;
            font-size: 2rem;
            text-align: center;
        }

        h2.text-primary::after {
            content: '';
            position: absolute;
            left: 50%;
            bottom: 0;
            transform: translateX(-50%);
            width: 80px;
            height: 4px;
            background-color: #d32f2f;
            border-radius: 2px;
        }

        /* Card styling */
        .card {
            border: none;
            border-radius: 12px;
            background-color: #fff;
            transition: all 0.3s ease;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.06);
            overflow: hidden;
            position: relative;
        }

        .card:hover {
            transform: translateY(-10px);
            box-shadow: 0 10px 25px rgba(211, 47, 47, 0.15);
        }

        .card-img-top {
            border-top-left-radius: 12px;
            border-top-right-radius: 12px;
            transition: transform 0.5s ease;
            height: 250px;
            object-fit: cover;
        }

        .card:hover .card-img-top {
            transform: scale(1.05);
        }

        .card-body {
            padding: 1.5rem;
        }

        .card-title {
            color: #d32f2f;
            font-weight: 600;
            font-size: 1.3rem;
            margin-bottom: 0.8rem;
            overflow: visible;
            white-space: normal;
            line-height: 1.4;
            min-height: 3em;
        }

        .card-text {
            color: #555;
            font-weight: 500;
            margin-bottom: 0.8rem;
        }

        .card-text .text-decoration-line-through {
            color: #999;
            font-size: 0.95rem;
        }

        .card-text .discount-price {
            color: #d32f2f;
            font-weight: 700;
            font-size: 1.2rem;
        }

        .card-footer {
            background-color: #fff;
            border-top: 1px solid rgba(211, 47, 47, 0.1);
            padding: 1.2rem;
            text-align: center;
        }

        /* Discount badge */
        .discount-badge {
            position: absolute;
            top: 12px;
            right: 12px;
            background-color: #d32f2f;
            color: white;
            border-radius: 50%;
            width: 60px;
            height: 60px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-weight: bold;
            font-size: 1.1rem;
            box-shadow: 0 3px 6px rgba(0, 0, 0, 0.2);
            z-index: 10;
        }

        /* Buttons */
        .btn-primary {
            background-color: #ef5350;
            border-color: #ef5350;
            color: #fff;
            font-weight: 500;
            padding: 0.7rem 1.8rem;
            border-radius: 8px;
            transition: all 0.2s ease;
        }

        .btn-primary:hover, .btn-primary:focus {
            background-color: #d32f2f;
            border-color: #d32f2f;
            color: #fff;
            box-shadow: 0 5px 10px rgba(211, 47, 47, 0.3);
            transform: translateY(-2px);
        }

        /* Pagination */
        .pagination {
            justify-content: center;
            margin-top: 2.5rem;
        }

        .page-item.active .page-link {
            background-color: #ef5350;
            border-color: #ef5350;
        }

        .page-link {
            color: #d32f2f;
        }

        .page-link:hover {
            color: #b71c1c;
        }

        /* Animation for page load */
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .card {
            animation: fadeIn 0.5s ease forwards;
            animation-delay: calc(var(--animation-order) * 0.1s);
            opacity: 0;
        }

        /* Adding animation delay for each card */
        .col-md-3:nth-child(1) .card { --animation-order: 1; }
        .col-md-3:nth-child(2) .card { --animation-order: 2; }
        .col-md-3:nth-child(3) .card { --animation-order: 3; }
        .col-md-3:nth-child(4) .card { --animation-order: 4; }
        .col-md-3:nth-child(5) .card { --animation-order: 5; }
        .col-md-3:nth-child(6) .card { --animation-order: 6; }
        .col-md-3:nth-child(7) .card { --animation-order: 7; }
        .col-md-3:nth-child(8) .card { --animation-order: 8; }

        /* Responsive adjustments */
        @media (max-width: 768px) {
            .container {
                padding: 20px;
            }

            .card-img-top {
                height: 200px;
            }

            .card-title {
                font-size: 1.1rem;
            }

            .discount-badge {
                width: 50px;
                height: 50px;
                font-size: 0.9rem;
            }
        }
    </style>
</head>
<body>
<!-- Include the header JSP file -->
<jsp:include page="/templates/header.jsp"/>

<div class="container mt-4">
    <h2 class="text-center text-primary mb-4">Sản Phẩm Khuyến Mãi</h2>

    <c:choose>
        <c:when test="${empty discountedProducts}">
            <div class="alert alert-warning text-center" style="background-color: #fff3cd; color: #856404;">
                Hiện không có sản phẩm khuyến mãi nào!
            </div>
        </c:when>
        <c:otherwise>
            <!-- Danh sách sản phẩm khuyến mãi -->
            <div class="row">
                <c:forEach var="product" items="${discountedProducts}">
                    <div class="col-md-3 mb-4">
                        <div class="card">
                            <!-- Discount badge -->
                            <div class="discount-badge">
                                -${product.discountPercent}%
                            </div>
                            <a href="products?action=productDetail&productId=${product.id}" class="text-decoration-none text-dark">
                                <img src="${product.imageURL}" class="card-img-top" alt="${product.name}">
                                <div class="card-body">
                                    <h5 class="card-title">${product.name}</h5>
                                    <p class="card-text">
                                        <span class="text-decoration-line-through">${product.originalPrice} VND</span><br>
                                        <span class="discount-price">${product.discountPrice} VND</span>
                                    </p>
                                </div>
                            </a>
                            <div class="card-footer text-center">
                                <form action="AddToCartServlet" method="post">
                                    <input type="hidden" name="productId" value="${product.id}">
                                    <button type="submit" class="btn btn-primary">Thêm vào giỏ hàng</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Pagination -->
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="products?action=showDiscountedProducts&page=${currentPage-1}" aria-label="Previous">
                                <span aria-hidden="true">«</span>
                            </a>
                        </li>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                        <li class="page-item ${pageNumber == currentPage ? 'active' : ''}">
                            <a class="page-link" href="products?action=showDiscountedProducts&page=${pageNumber}">${pageNumber}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="products?action=showDiscountedProducts&page=${currentPage+1}" aria-label="Next">
                                <span aria-hidden="true">»</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </c:otherwise>
    </c:choose>
</div>

<!-- Include the footer JSP file -->
<jsp:include page="/templates/footer.jsp"/>
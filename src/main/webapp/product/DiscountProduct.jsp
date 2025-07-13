<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Product, model.Category" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
    <title>Sản phẩm Khuyến Mãi</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;600;700&display=swap" rel="stylesheet">
    <style>
        
        body {
            background-color: #f5f7fa;
            color: #1a1a1a;
            font-family: 'Poppins', sans-serif;
            margin: 0;
            padding: 0;
            line-height: 1.5;
            overflow-x: hidden;
        }

       
        .container {
            background-color: #ffffff;
            padding: 3rem;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            margin: 4rem auto;
            max-width: 1400px;
        }

        
        h2.section-title {
            color: #ff2e63;
            font-family: 'Poppins', sans-serif;
            font-weight: 700;
            font-size: 2.8rem;
            text-align: center;
            text-transform: uppercase;
            letter-spacing: 1.2px;
            margin-bottom: 3rem;
            position: relative;
        }

        h2.section-title::after {
            content: '';
            position: absolute;
            left: 50%;
            bottom: -10px;
            transform: translateX(-50%);
            width: 120px;
            height: 6px;
            background: linear-gradient(90deg, #ff2e63, #ff6b6b);
            border-radius: 3px;
        }

        
        .col-custom-5 {
            flex: 0 0 20%; 
            max-width: 20%;
            padding-left: 10px;
            padding-right: 10px;
        }

        /* Filter buttons container */
        .filter-buttons {
            display: flex;
            flex-wrap: nowrap; 
            gap: 10px; 
            margin-bottom: 20px;
            justify-content: flex-start;
            overflow-x: auto; 
            padding-bottom: 10px; 
            -webkit-overflow-scrolling: touch; 
        }

        /* Individual filter button styling */
        .filter-buttons .btn {
            white-space: nowrap; 
            border-radius: 8px; 
            padding: 8px 15px; 
            font-size: 0.95rem; 
            display: flex; 
            align-items: center; 
        }

        
        .filter-buttons .btn:first-child {
            border: 1px solid #007bff; 
            color: #007bff; 
        }
        .filter-buttons .btn:first-child:hover {
            background-color: #007bff;
            color: white;
        }

        /* Logo inside filter buttons */
        .logo {
            height: 18px; 
            vertical-align: middle; 
            margin-right: 5px; 
        }

        /* Card styling */
        .card {
            border: none;
            border-radius: 15px;
            background-color: #ffffff;
            transition: all 0.3s ease;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
            overflow: hidden;
            position: relative;
            display: flex;
            flex-direction: column;
            height: 100%;
        }

        .card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 35px rgba(255, 46, 99, 0.25);
        }

        .card-img-top {
            border-radius: 15px 15px 0 0;
            width: 100%;
            height: 200px; 
            object-fit: contain;
            background-color: #f9f9f9;
            padding: 1rem;
            transition: transform 0.5s ease;
        }

        .card:hover .card-img-top {
            transform: scale(1.05);
        }

        .card-body {
            padding: 1.25rem;
            flex-grow: 1;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
        }

        .card-title {
            font-family: 'Poppins', sans-serif;
            color: #ff2e63;
            font-weight: 600;
            font-size: 1.2rem; 
            margin-bottom: 0.8rem;
            height: 2.8em;
            overflow: hidden;
            text-overflow: ellipsis;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
        }

        .card-text {
            color: #555;
            font-weight: 400;
            margin-bottom: 1rem;
            font-size: 0.95rem; 
        }

        .card-text .text-decoration-line-through {
            color: #aaa;
            font-size: 0.8rem;
            margin-bottom: 0.3rem;
            display: block;
            text-decoration: none; 
        }


        .card-text .discount-price {
            color: #ff2e63;
            font-weight: 700;
            font-size: 1.3rem; 
        }

        .card-footer {
            background-color: #f8f9fa;
            border-top: 1px solid rgba(255, 46, 99, 0.1);
            padding: 0.8rem;
            text-align: center;
        }

        /* Discount badge */
        .discount-badge {
            position: absolute;
            top: 15px;
            right: 15px;
            background: linear-gradient(45deg, #ff2e63, #ff6b6b);
            color: white;
            border-radius: 50%;
            width: 60px;
            height: 60px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-weight: 700;
            font-size: 1rem; 
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
            z-index: 10;
            animation: pulse 1.5s infinite;
        }

        @keyframes pulse {
            0% {
                transform: scale(1);
            }
            50% {
                transform: scale(1.1);
            }
            100% {
                transform: scale(1);
            }
        }

        /* Buttons */
        .btn-primary {
            background: linear-gradient(90deg, #ff2e63, #ff6b6b);
            border: none;
            color: #fff;
            font-weight: 600;
            padding: 0.7rem 1.5rem; 
            border-radius: 50px;
            transition: all 0.3s ease;
            box-shadow: 0 5px 15px rgba(255, 46, 99, 0.3);
            font-size: 0.9rem;
        }

        .btn-primary:hover, .btn-primary:focus {
            background: linear-gradient(90deg, #e71e53, #ff4d4d);
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(255, 46, 99, 0.4);
        }

        /* Pagination */
        .pagination {
            justify-content: center;
            margin-top: 3rem;
        }

        .page-item .page-link {
            border-radius: 10px;
            margin: 0 6px;
            min-width: 45px;
            text-align: center;
            padding: 0.7rem 1rem;
            font-weight: 600;
            color: #ff2e63;
            border: 1px solid #ffadad;
            transition: all 0.3s ease;
        }

        .page-item.active .page-link,
        .page-item .page-link:hover {
            background: linear-gradient(90deg, #ff2e63, #ff6b6b);
            border-color: #ff2e63;
            color: #fff;
            box-shadow: 0 5px 15px rgba(255, 46, 99, 0.2);
        }

        .page-link:focus {
            box-shadow: none;
        }

        /* No products alert */
        .alert-warning {
            background-color: #fff4e6;
            color: #e67e22;
            border-radius: 15px;
            padding: 1.5rem;
            font-size: 1.1rem;
            font-weight: 500;
            text-align: center;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
        }

        /* Card animation */
        .card {
            animation: slideUp 0.5s ease-out forwards;
            animation-delay: calc(var(--animation-order) * 0.1s);
            opacity: 0;
        }
        .description-ellipsis {
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .col-custom-5:nth-child(1) .card {
            --animation-order: 1;
        }
        .col-custom-5:nth-child(2) .card {
            --animation-order: 2;
        }
        .col-custom-5:nth-child(3) .card {
            --animation-order: 3;
        }
        .col-custom-5:nth-child(4) .card {
            --animation-order: 4;
        }
        .col-custom-5:nth-child(5) .card {
            --animation-order: 5;
        }
        .card a {
    text-decoration: none !important;
    color: inherit;
}


        /* Responsive adjustments */
        @media (max-width: 1200px) {
            .col-custom-5 {
                flex: 0 0 33.333%; /* 3 columns */
                max-width: 33.333%;
            }
            .card-img-top {
                height: 180px;
            }
            .card-title {
                font-size: 1.15rem;
            }
            .discount-badge {
                width: 55px;
                height: 55px;
                font-size: 0.9rem;
            }
            .btn-primary {
                padding: 0.6rem 1.2rem;
                font-size: 0.85rem;
            }
        }

        @media (max-width: 767px) {
            .container {
                padding: 1.5rem;
                margin: 2rem auto;
            }
            h2.section-title {
                font-size: 2rem;
                margin-bottom: 2rem;
            }
            h2.section-title::after {
                width: 100px;
            }
            .col-custom-5 {
                flex: 0 0 100%; /* 1 column */
                max-width: 100%;
            }
            .card-img-top {
                height: 250px;
            }
            .card-title {
                font-size: 1.25rem;
            }
            .discount-badge {
                width: 60px;
                height: 60px;
                font-size: 1rem;
            }
            .btn-primary {
                padding: 0.7rem 1.5rem;
                font-size: 0.95rem;
            }
            .page-item .page-link {
                padding: 0.6rem 0.9rem;
                min-width: 40px;
                font-size: 0.9rem;
            }
        }
    </style>
</head>
    <body>
        <jsp:include page="/templates/header.jsp"/>

        <div class="container">
            <h2 class="section-title">Sản Phẩm Khuyến Mãi</h2>
<!--            <div class="filter-buttons filter-container">
                <button class="btn btn-outline-primary">Lọc</button>
                <button class="btn btn-outline-primary">
                    <img src="${pageContext.request.contextPath}/images/samsung.png" alt="Samsung" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="${pageContext.request.contextPath}/images/iphone.png" alt="Iphone" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="${pageContext.request.contextPath}/images/oppo.png" alt="Oppo" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="${pageContext.request.contextPath}/images/xiaomi.png" alt="Xiaomi" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="${pageContext.request.contextPath}/images/realme.png" alt="Realme" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="${pageContext.request.contextPath}/images/vivo.png" alt="Vivo" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="${pageContext.request.contextPath}/images/nokia.png" alt="Nokia" class="logo">
                </button>
            </div>-->

            <c:choose>
                <c:when test="${empty discountedProducts}">
                    <div class="alert alert-warning text-center">
                        Hiện không có sản phẩm khuyến mãi nào!
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row">
                        <c:forEach var="product" items="${discountedProducts}">
                            <div class="col-custom-5 mb-4 d-flex">
                                <div class="card w-100">
                                    <div class="discount-badge">
                                        -${product.discountPercent}%
                                    </div>
                                    <a href="products?action=productDetail&productId=${product.id}">


                                        <img src="${product.thumbnailImage}" class="card-img-top" alt="${product.name}">
                                        <div class="card-body">
                                            <h5 class="card-title">${product.name}</h5>
                                            <p class="card-text description-ellipsis">${product.description}</p>
                                            <p class="card-text">
                                                <span class="discount-price">${product.discountPrice}₫</span><br>
                                                <span class="text-decoration-line-through">${product.originalPrice}₫</span>
                                            </p>
                                        </div>
                                    </a>
                                    
                                </div>
                            </div>
                        </c:forEach>
                    </div>

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

        <jsp:include page="/templates/footer.jsp"/>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
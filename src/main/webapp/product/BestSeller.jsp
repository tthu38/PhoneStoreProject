<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Sản phẩm bán chạy</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
                max-width: 1400px;
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

            /* Card styling */
            .card {
                border: none;
                border-radius: 12px;
                background-color: #fff;
                transition: all 0.4s ease;
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
                width: 100%;
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
                margin-bottom: 1rem;
                line-height: 1.5;
                min-height: 3em;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
                overflow: hidden;
            }

            .card-text {
                color: #666;
                font-weight: 500;
                margin-bottom: 1rem;
                font-size: 1.1rem;
            }

            .card-footer {
                background-color: #fff;
                border-top: 1px solid rgba(211, 47, 47, 0.1);
                padding: 1.2rem;
                text-align: center;
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

            /* Pagination */
            .pagination {
                justify-content: center;
                margin-top: 3rem;
            }

            .page-item.active .page-link {
                background-color: #ef5350;
                border-color: #ef5350;
                color: #fff;
            }

            .page-link {
                color: #d32f2f;
                font-weight: 500;
                border-radius: 8px;
            }

            .page-link:hover {
                color: #b71c1c;
                background-color: #ffebee;
            }

            .col-custom-5 {
                flex: 0 0 20%;
                max-width: 20%;
                padding-left: 10px;
                padding-right: 10px;
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
            .btn-primary {
               background: linear-gradient(90deg, #ff2e63, #ff6b6b) !important;
    border-color: #b71c1c !important;
            }

            .btn-primary:hover {
                background-color: darkred;
                border-color: darkred;
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

            /* Animation for page load */
            @keyframes fadeInUp {
                from {
                    opacity: 0;
                    transform: translateY(30px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }

            .card {
                animation: fadeInUp 0.6s ease-out forwards;
                animation-delay: calc(var(--animation-order) * 0.15s);
                opacity: 0;
            }

            /* Adding animation delay for each card */
            .col-md-3:nth-child(1) .card {
                --animation-order: 1;
            }
            .col-md-3:nth-child(2) .card {
                --animation-order: 2;
            }
            .col-md-3:nth-child(3) .card {
                --animation-order: 3;
            }
            .col-md-3:nth-child(4) .card {
                --animation-order: 4;
            }
            .col-md-3:nth-child(5) .card {
                --animation-order: 5;
            }
            .col-md-3:nth-child(6) .card {
                --animation-order: 6;
            }
            .col-md-3:nth-child(7) .card {
                --animation-order: 7;
            }
            .col-md-3:nth-child(8) .card {
                --animation-order: 8;
            }

            /* Responsive adjustments */
            @media (max-width: 768px) {
                .container {
                    padding: 20px;
                    margin: 20px auto;
                }

                h2.text-primary {
                    font-size: 1.8rem;
                }

                .card-img-top {
                    height: 200px;
                }

                .card-title {
                    font-size: 1.1rem;
                }

                .btn-primary {
                    padding: 0.6rem 1.5rem;
                    
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="/templates/header.jsp"/>

        <div class="container mt-4">
            <h2 class="text-center text-primary mb-4">Danh Sách Sản Phẩm Bán Chạy</h2>

            <c:choose>
                <c:when test="${empty products}">
                    <div class="alert alert-warning text-center" style="background-color: #fff3cd; color: #856404; padding: 1.5rem; border-radius: 10px; font-size: 1.1rem;">
                        Không có sản phẩm bán chạy nào!
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- Product List -->
                    <div class="row">
                        <c:set var="pageSize" value="12"/>
                        <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>
                        <c:set var="start" value="${(currentPage - 1) * pageSize}"/>
                        <c:set var="end" value="${start + pageSize}"/>
                        <c:set var="totalProducts" value="${products.size()}"/>
                        <c:set var="totalPages" value="${Math.ceil(totalProducts / pageSize)}"/>

                        <div class="row">
                            <c:forEach var="product" items="${products}">
                                <div class="col-custom-5 mb-4 d-flex">
                                    <div class="card w-100">
                                        <c:if test="${product['discountPercent'] > 0}">
                                            <div class="discount-badge">
                                                -${product['discountPercent']}%
                                            </div>
                                        </c:if>

                                        <a href="products?action=productDetail&productId=${product['id']}">
                                            <img src="${product['thumbnailImage']}" class="card-img-top" alt="${product['name']}">
                                        </a>

                                        <div class="card-body">
                                            <h5 class="card-title">${product['name']}</h5>
                                            <p class="card-text">
                                                <c:choose>
                                                    <c:when test="${product['discountPercent'] > 0}">
                                                        <span class="discount-price">
                                                            <fmt:formatNumber value="${product['discountPrice']}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                                        </span><br>
                                                        <span class="text-decoration-line-through text-muted">
                                                            <fmt:formatNumber value="${product['originalPrice']}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="discount-price">
                                                            <fmt:formatNumber value="${product['originalPrice']}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>

                                        <div class="card-footer">
                                            <form action="<%= request.getContextPath()%>/carts?action=add" method="get">
                                                <input type="hidden" name="variantId" value="${product['variantId']}">
                                                <button type="submit" class="text-cart btn btn-primary w-100">Thêm vào giỏ hàng</button>
                                            </form>
                                        </div>
                                    </div> <!-- end .card -->
                                </div> <!-- end .col-custom-5 -->
                            </c:forEach>

                        </div> <!-- end .row -->


                        <div>
                            <!-- Pagination -->
                            <nav aria-label="Page navigation">
                                <ul class="pagination">
                                    <c:if test="${currentPage > 1}">
                                        <li class="page-item">
                                            <a class="page-link" href="${pageContext.request.contextPath}/products?action=productBestSeller&page=${currentPage-1}" aria-label="Previous">
                                                <span aria-hidden="true">«</span>
                                            </a>
                                        </li>
                                    </c:if>

                                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                                        <li class="page-item ${pageNumber == currentPage ? 'active' : ''}">
                                            <a class="page-link" href="${pageContext.request.contextPath}/products?action=productBestSeller&page=${pageNumber}">${pageNumber}</a>
                                        </li>
                                    </c:forEach>

                                    <c:if test="${currentPage < totalPages}">
                                        <li class="page-item">
                                            <a class="page-link" href="${pageContext.request.contextPath}/products?action=productBestSeller&page=${currentPage+1}" aria-label="Next">
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

                </body>
                </html>
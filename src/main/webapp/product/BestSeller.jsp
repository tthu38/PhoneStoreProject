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

        /* Animation for page load */
        @keyframes fadeInUp {
            from { opacity: 0; transform: translateY(30px); }
            to { opacity: 1; transform: translateY(0); }
        }

        .card {
            animation: fadeInUp 0.6s ease-out forwards;
            animation-delay: calc(var(--animation-order) * 0.15s);
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

                <c:forEach var="product" items="${products}" varStatus="status">
                    <c:if test="${status.index >= start && status.index < end}">
                        <div class="col-md-3 mb-5">
                            <div class="card" style="--animation-order: ${status.index + 1}">
                                <a href="products?action=productDetail&productId=${product['productId']}" class="text-decoration-none text-dark">
                                    <img src="${product['imageURL']}" class="card-img-top" alt="${product['productName']}">
                                    <div class="card-body">
                                        <h5 class="card-title">${product['productName']}</h5>
                                        <p class="card-text">
                                            <fmt:formatNumber value="${product['productPrice']}" pattern="#,###"/> VND
                                        </p>
                                    </div>
                                </a>
                                <div class="card-footer text-center">
                                    <form action="AddToCartServlet" method="post">
                                        <input type="hidden" name="productId" value="${product['productId']}">
                                        <button type="submit" class="btn btn-primary">Thêm vào giỏ hàng</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>

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
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Product" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Danh sách sản phẩm</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Roboto, sans-serif;
            }

            .container {
                background-color: #fff;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
                margin-top: 30px;
                margin-bottom: 30px;
            }

            h2.text-primary {
                color: #2e8b57 !important;
                font-weight: 700;
                margin-bottom: 1.5rem;
                text-align: center;
                position: relative;
            }

            .card {
                border: none;
                border-radius: 12px;
                background-color: #fff;
                transition: 0.3s ease;
                box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
                overflow: hidden;
                height: 400px;
                display: flex;
                flex-direction: column;
            }

            .card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 20px rgba(46, 139, 87, 0.15);
            }

            .card-img-top {
                height: 240px;
                object-fit: contain;
                background-color: #f6f6f6;
                padding: 10px;
            }

            .card-body {
                flex: 1;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                padding: 1rem;
            }

            .card-title {
                color: #2e8b57;
                font-weight: 600;
                font-size: 1rem;
                margin-bottom: 0.5rem;
                text-align: center;
                min-height: 48px;
            }

            .card-text {
                color: #555;
                font-size: 0.9rem;
                text-align: center;
                max-height: 40px;
                overflow: hidden;
                text-overflow: ellipsis;
            }

            .card-footer {
                background-color: white;
                border-top: none;
                padding: 0.75rem 1rem;
            }

            .btn-primary {
                background-color: #3c9d74;
                border-color: #3c9d74;
                font-weight: 500;
                padding: 0.4rem 1rem;
                font-size: 0.9rem;
                border-radius: 6px;
            }

            .btn-primary:hover {
                background-color: #2e8b57;
                border-color: #2e8b57;
            }

            .pagination {
                justify-content: center;
                margin-top: 2rem;
            }

            .page-link {
                color: #3c9d74;
            }

            .page-item.active .page-link {
                background-color: #3c9d74;
                border-color: #3c9d74;
                color: white;
            }
            .action-links {
                display: flex;
                align-items: center; /* Đảm bảo nút dọc giữa hàng */
                gap: 8px;
                justify-content: flex-end;
                height: 100%;
            }



        </style>
    </head>
    <body>

        <jsp:include page="/templates/header.jsp"/>

        <div class="container">
            <c:set var="products" value="${requestScope.products}"/>
            <c:set var="pageSize" value="10"/>
            <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>
            <c:set var="start" value="${(currentPage - 1) * pageSize}"/>
            <c:set var="end" value="${start + pageSize}"/>
            <c:set var="totalProducts" value="${products.size()}"/>
            <c:set var="totalPages" value="${Math.ceil(totalProducts / pageSize)}"/>
            <h2 class="text-primary">Danh sách sản phẩm</h2>

            <div class="row">
                <c:forEach var="product" items="${products}">
                    <div class="col-md-3 mb-4">
                        <div class="card">
                            <img src="${pageContext.request.contextPath}/${product.thumbnailImage}" class="card-img-top" alt="${product.name}">
                            <div class="card-body">
                                <h5 class="card-title">${product.name}</h5>

                            </div>

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
                            <a class="page-link" href="products?action=find&page=${currentPage-1}&categoryId=${param.categoryId}&searchName=${param.searchName}&sort=${param.sort}" aria-label="Previous">
                                <span aria-hidden="true">&laquo;</span>
                            </a>
                        </li>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                        <li class="page-item ${pageNumber == currentPage ? 'active' : ''}">
                            <a class="page-link" href="products?action=find&page=${pageNumber}&categoryId=${param.categoryId}&searchName=${param.searchName}&sort=${param.sort}">${pageNumber}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="products?action=find&page=${currentPage+1}&categoryId=${param.categoryId}&searchName=${param.searchName}&sort=${param.sort}" aria-label="Next">
                                <span aria-hidden="true">&raquo;</span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/products?page=${currentPage - 1}"><i
                            class="fas fa-chevron-left"></i> Previous</a>
                    </c:if>

                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <strong>${i}</strong>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/products?page=${i}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/products?page=${currentPage + 1}">Next <i
                            class="fas fa-chevron-right"></i></a>
                    </c:if>
            </div>

        <jsp:include page="/templates/footer.jsp"/>

    </body>
</html>

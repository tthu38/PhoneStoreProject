<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Product" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
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
            color: red !important;
            font-weight: 700;
            margin-bottom: 1.5rem;
            text-align: center;
        }

        .card {
            border: none;
            border-radius: 12px;
            background-color: #fff;
            transition: 0.3s ease;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.08);
            height: 100%;
            display: flex;
            flex-direction: column;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 20px rgba(255, 0, 0, 0.2);
        }

        .card-img-top {
            height: 200px;
            object-fit: contain;
            background-color: #f6f6f6;
            padding: 10px;
        }

        .card-title {
            color: #2e8b57;
            font-weight: 600;
            font-size: 1rem;
            text-align: center;
            min-height: 48px;
        }

        .btn-primary {
            background-color: red;
            border-color: red;
        }

        .btn-primary:hover {
            background-color: darkred;
            border-color: darkred;
        }

        .pagination {
            justify-content: center;
        }

        .page-link {
            color: red;
        }

        .page-item.active .page-link {
            background-color: red;
            border-color: red;
            color: white;
        }

        .logo {
            height: 25px;
        }

        .filter-container {
            display: flex;
            flex-wrap: nowrap;
            gap: 10px;
            margin-bottom: 20px;
            justify-content: flex-start;
        }

        .sort-options {
            text-align: left;
            margin-top: 20px;
        }

        .brand-logo {
            width: 60px;
            height: 40px;
            object-fit: contain;
            padding: 5px;
            background-color: white;
            border-radius: 6px;
            transition: transform 0.2s ease;
            border: 1px solid #ccc;
        }

        .brand-logo:hover {
            transform: scale(1.1);
            border-color: red;
        }

        .brand-button {
            border: none;
            background: none;
            padding: 0;
            margin: 0;
        }
    </style>
</head>
<body>
    

    <div class="container">
        <c:set var="products" value="${requestScope.products}" />
        <c:set var="pageSize" value="10" />
        <c:set var="currentPage" value="${param.page != null ? param.page : 1}" />
        <c:set var="totalProducts" value="${products.size()}" />
        <c:set var="totalPages" value="${(totalProducts + pageSize - 1) / pageSize}" />

        <h2 class="text-primary">Danh sách sản phẩm</h2>

        <form method="GET" action="products" class="row g-3 mb-4">
            <input type="hidden" name="action" value="find">
            <div class="col-md-3">
                <select name="categoryId" class="form-select">
                    <option value="" ${empty param.categoryId ? 'selected' : ''}>Tất cả danh mục</option>
                    <c:forEach var="category" items="${categories}">
                        <option value="${category.id}" ${param.categoryId == category.id ? 'selected' : ''}>${category.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="col-md-3">
                <input type="text" name="searchName" value="${param.searchName}" class="form-control" placeholder="Tìm theo tên sản phẩm...">
            </div>
            <div class="col-md-3">
                <select name="sort" class="form-select">
                    <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá: Thấp → Cao</option>
                    <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá: Cao → Thấp</option>
                    <option value="name_asc" ${param.sort == 'name_asc' ? 'selected' : ''}>Tên: A → Z</option>
                    <option value="name_desc" ${param.sort == 'name_desc' ? 'selected' : ''}>Tên: Z → A</option>
                </select>
            </div>
            <div class="col-md-3">
                <button type="submit" class="btn btn-primary w-100"><i class="fas fa-search me-2"></i>Lọc & Tìm kiếm</button>
            </div>
        </form>

    
                <div class="content-frame">
        <div class="filter-buttons filter-container">
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
        </div>

        <div class="sort-options">
            <strong>Sắp xếp theo:</strong>
            <button class="btn btn-link text-primary">Nổi bật</button>
            <button class="btn btn-link text-primary">Bán chạy</button>
            <button class="btn btn-link text-primary">Giảm giá</button>
            <button class="btn btn-link text-primary">Giá</button>
        </div>

        <c:choose>
            <c:when test="${empty products}">
                <div class="alert alert-warning text-center">Không có sản phẩm nào phù hợp!</div>
            </c:when>
            <c:otherwise>
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-5 g-4">
                    <c:forEach var="product" items="${products}">
                        <div class="col">
                            <div class="card h-100">
                                <a href="products?action=productDetail&productId=${product.id}" class="text-decoration-none text-dark">
                                    <img src="${product.thumbnailImage}" class="card-img-top" alt="${product.name}">
                                    <div class="card-body">
                                        <h5 class="card-title">${product.name}</h5>
                                        <p class="card-text">
                                            <c:choose>
                                                <c:when test="${product.discountPercent > 0}">
                                                    <fmt:formatNumber value="${product.originalPrice}" type="number" /> VND

                                                    <fmt:formatNumber value="${product.discountPrice}" type="number" /> VND

                                                </c:when>
                                                <c:otherwise>
                                                    <span>${product.originalPrice} VND</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </p>
                                    </div>
                                </a>
                                <div class="card-footer bg-white border-0 text-center">
                                    <form action="AddToCartServlet" method="post">
                                        <input type="hidden" name="productId" value="${product.id}">
                                        <button type="submit" class="btn btn-primary btn-sm">Thêm vào giỏ hàng</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <nav class="mt-4" aria-label="Page navigation">
                    <ul class="pagination">
                        <c:if test="${currentPage > 1}">
                            <li class="page-item">
                                <a class="page-link" href="products?action=find&page=${currentPage-1}&categoryId=${param.categoryId}&searchName=${param.searchName}&sort=${param.sort}" aria-label="Previous">
                                    <span aria-hidden="true">«</span>
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
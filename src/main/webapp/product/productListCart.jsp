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

        h2.text-primary {
            color: red !important;
            font-weight: 700;
            margin-bottom: 1.5rem;
            text-align: center;
        }

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
            min-height: 40px;
        }

        .card-text {
            color: #555;
            font-weight: 400;
            margin-bottom: 1rem;
            font-size: 0.95rem;
            min-height: 40px;
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
            margin-top: auto;
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

        .col-custom-5 {
            flex: 0 0 20%;
            max-width: 20%;
            padding-left: 10px;
            padding-right: 10px;
        }

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
            0% { transform: scale(1); }
            50% { transform: scale(1.1); }
            100% { transform: scale(1); }
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

        .col-custom-5:nth-child(1) .card { --animation-order: 1; }
        .col-custom-5:nth-child(2) .card { --animation-order: 2; }
        .col-custom-5:nth-child(3) .card { --animation-order: 3; }
        .col-custom-5:nth-child(4) .card { --animation-order: 4; }
        .col-custom-5:nth-child(5) .card { --animation-order: 5; }

        .card a {
            text-decoration: none !important;
            color: inherit;
        }

        @media (max-width: 1200px) {
            .col-custom-5 {
                flex: 0 0 33.333%;
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
    </style>
</head>
<body>
<%--<jsp:include page="/templates/header.jsp"/>--%>
<div class="container">
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

    <!-- Lọc theo hãng (brandId) -->
    <div class="filter-container">
        <a href="<c:url value='/products?action=find&brandId=2'/>"><img src="${pageContext.request.contextPath}/images/samsung.png" alt="Samsung" class="brand-logo"></a>
        <a href="<c:url value='/products?action=find&brandId=1'/>"><img src="${pageContext.request.contextPath}/images/iphone.png" alt="iPhone" class="brand-logo"></a>
        <a href="<c:url value='/products?action=find&brandId=5'/>"><img src="${pageContext.request.contextPath}/images/oppo.png" alt="Oppo" class="brand-logo"></a>
        <a href="<c:url value='/products?action=find&brandId=3'/>"><img src="${pageContext.request.contextPath}/images/xiaomi.png" alt="Xiaomi" class="brand-logo"></a>
        <a href="<c:url value='/products?action=find&brandId=4'/>"><img src="${pageContext.request.contextPath}/images/realme.png" alt="Realme" class="brand-logo"></a>
        <a href="<c:url value='/products?action=find&brandId=6'/>"><img src="${pageContext.request.contextPath}/images/vivo.png" alt="Vivo" class="brand-logo"></a>
    </div>

    <c:choose>
        <c:when test="${empty products}">
            <div class="alert alert-warning text-center">
                Không tìm thấy sản phẩm nào phù hợp!
            </div>
        </c:when>
        <c:otherwise>
            <div class="row">
                <c:forEach var="product" items="${products}">
                    <div class="col-custom-5 mb-4 d-flex">
                        <div class="card w-100">
                            <c:if test="${product.discountPercent > 0}">
                                <div class="discount-badge">
                                    -${product.discountPercent}%
                                </div>
                            </c:if>
                            <a href="<c:url value='/products?action=productDetail&productId=${product.id}'/>">
                                <img src="${product.thumbnailImage}" class="card-img-top" alt="${product.name}">
                                <div class="card-body">
                                    <h5 class="card-title">${product.name}</h5>
                                    <p class="card-text">
                                        <c:choose>
                                            <c:when test="${product.discountPercent > 0}">
                                                <span class="discount-price">${product.discountPrice}₫</span><br>
                                                <span class="text-decoration-line-through text-muted">${product.originalPrice}₫</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="discount-price">${product.originalPrice}₫</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </p>
                                </div>
                            </a>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Phân trang -->
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="<c:url value='/products'>
                                <c:param name='action' value='find'/>
                                <c:param name='searchName' value='${param.searchName}'/>
                                <c:param name='categoryId' value='${param.categoryId}'/>
                                <c:param name='sort' value='${param.sort}'/>
                                <c:param name='page' value='${currentPage-1}'/>
                            </c:url>">«</a>
                        </li>
                    </c:if>

                    <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                        <li class="page-item ${pageNumber == currentPage ? 'active' : ''}">
                            <a class="page-link" href="<c:url value='/products'>
                                <c:param name='action' value='find'/>
                                <c:param name='searchName' value='${param.searchName}'/>
                                <c:param name='categoryId' value='${param.categoryId}'/>
                                <c:param name='sort' value='${param.sort}'/>
                                <c:param name='page' value='${pageNumber}'/>
                            </c:url>">${pageNumber}</a>
                        </li>
                    </c:forEach>

                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="<c:url value='/products'>
                                <c:param name='action' value='find'/>
                                <c:param name='searchName' value='${param.searchName}'/>
                                <c:param name='categoryId' value='${param.categoryId}'/>
                                <c:param name='sort' value='${param.sort}'/>
                                <c:param name='page' value='${currentPage+1}'/>
                            </c:url>">»</a>
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
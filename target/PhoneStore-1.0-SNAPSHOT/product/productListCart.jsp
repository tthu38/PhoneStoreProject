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
                height: 400px;
                display: flex;
                flex-direction: column;
            }

            .card:hover {
                transform: translateY(-5px);
                box-shadow: 0 8px 20px rgba(255, 0, 0, 0.2);
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

            .card-footer {
                background-color: white;
                border-top: none;
                padding: 0.75rem 1rem;
                text-align: center;
            }

            .btn-primary {
                background-color: red;
                border-color: red;
                font-weight: 500;
                padding: 0.4rem 1rem;
                font-size: 0.9rem;
                border-radius: 6px;
            }

            .btn-primary:hover {
                background-color: darkred;
                border-color: darkred;
            }

            .pagination {
                justify-content: center;
                margin-top: 2rem;
            }

            .page-link {
                color: red;
            }

            .page-item.active .page-link {
                background-color: red;
                border-color: red;
                color: white;
            }

            .filter-buttons .btn {
                margin-right: 10px;
                margin-bottom: 5px;
            }

            .filter-buttons img.logo {
                height: 24px;
                width: auto;
            }
        </style>
    </head>

    <body>

        <jsp:include page="/templates/header.jsp"/>

        <div class="container">
            <c:set var="products" value="${requestScope.products}" />
            <c:set var="pageSize" value="10" />
            <c:set var="currentPage" value="${param.page != null ? param.page : 1}" />
            <c:set var="start" value="${(currentPage - 1) * pageSize}" />
            <c:set var="end" value="${start + pageSize > products.size() ? products.size() : start + pageSize}" />
            <c:set var="totalProducts" value="${products.size()}" />
            <c:set var="totalPages" value="${(totalProducts + pageSize - 1) / pageSize}" />

            <h2 class="text-primary">Danh sách sản phẩm</h2>

            <form method="GET" action="products" class="filter-section row g-3 mb-4">
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
            <!-- Bộ lọc hãng -->
            <div class="content-frame">
                <div class="filter-buttons filter-container">
                    <button class="btn btn-outline-primary">Lọc</button>
                    <button class="btn btn-outline-primary">
                        <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/3f/68/3f68e22880dd800e9e34d55245048a0f.png" alt="Samsung" class="logo">
                    </button>
                    <button class="btn btn-outline-primary">
                        <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/57/03/5703d996359650c57421b72f3f7ff5cd.png" alt="Iphone" class="logo">
                    </button>
                    <button class="btn btn-outline-primary">
                        <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/2c/ea/2cea467041fb9effb3a6d3dcc88f38f8.png" alt="Oppo" class="logo">
                    </button>
                    <button class="btn btn-outline-primary">
                        <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/e9/df/e9df3ae9fb60a1460e9030975d0e024a.png" alt="Xiaomi" class="logo">
                    </button>
                    <button class="btn btn-outline-primary">
                        <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/54/2a/542a235b0e366a11fd855108dd9c499c.png" alt="Realme" class="logo">
                    </button>
                    <button class="btn btn-outline-primary">
                        <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/78/38/783870ef310908b123c50cb43b8f6f92.png" alt="Vivo" class="logo">
                    </button>
                    <button class="btn btn-outline-primary">
                        <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/b5/68/b5686886a3142a87df546889d8c14402.png" alt="Nokia" class="logo">
                    </button>
                </div>

                <!-- Bộ lọc sắp xếp -->
                <div class="sort-options filter-container2">
                    <strong>Sắp xếp theo:</strong>
                    <button class="btn btn-link text-primary">Nổi bật</button>
                    <button class="btn btn-link text-primary">Bán chạy</button>
                    <button class="btn btn-link text-primary">Giảm giá</button>
                    <button class="btn btn-link text-primary">Giá</button>
                </div>
            </div>

            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 row-cols-lg-5 g-4">
                <c:forEach var="i" begin="${start}" end="${end - 1}" varStatus="status">
                    <c:set var="product" value="${products[i]}" />
                    <div class="col">
                        <div class="card h-100">
                            <img src="${pageContext.request.contextPath}/${product.thumbnailImage}" class="card-img-top" alt="${product.name}">
                            <div class="card-body">
                                <h5 class="card-title">${product.name}</h5>
                            </div>
                            <div class="card-footer">
                                <form action="AddToCartServlet" method="post">
                                    <input type="hidden" name="productId" value="${product.id}">
                                    <button type="submit" class="btn btn-primary"><i class="fas fa-shopping-cart me-2"></i>Thêm vào giỏ hàng</button>
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

        <jsp:include page="/templates/footer.jsp"/>

    </body>
</html>

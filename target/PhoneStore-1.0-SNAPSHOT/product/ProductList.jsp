<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
    <head>
        <title>Product List</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">

        <style>
            :root {
                --primary-color: #d32f2f;
                --primary-dark: #b71c1c;
                --text-color: #333;
                --light-gray: #f5f5f5;
                --white: #fff;
                --border-color: #e0e0e0;
                --spacing-sm: 8px;
                --spacing-md: 16px;
                --spacing-lg: 24px;
            }

            body {
                font-family: 'Poppins', sans-serif;
                background: var(--light-gray);
                color: var(--text-color);
                margin: 0;
                padding: 0;
                line-height: 1.6;
            }

            .container {
                max-width: 1200px;
                margin: var(--spacing-lg) auto;
                background: var(--white);
                padding: var(--spacing-md);
                border-radius: 10px;
                box-shadow: 0 6px 12px rgba(0,0,0,0.1);
                transition: box-shadow 0.3s ease;
            }

            .container:hover {
                box-shadow: 0 8px 16px rgba(0,0,0,0.15);
            }

            /* Filter Section */
            .filters {
                display: flex;
                flex-direction: column;
                width: 100%;
                margin-bottom: var(--spacing-md);
                gap: var(--spacing-sm);
            }

            .top-filters,
            .bottom-filters {
                display: flex;
                flex-wrap: wrap;
                justify-content: space-between;
                width: 100%;
                gap: var(--spacing-sm);
                margin-bottom: var(--spacing-sm);
                align-items: center;
            }

            .filters select,
            .filters input,
            .filters button {
                padding: var(--spacing-sm) var(--spacing-md);
                border: 1px solid var(--border-color);
                border-radius: 6px;
                font-size: 14px;
                background: var(--white);
                transition: border-color 0.3s ease;
                height: 40px;
            }

            .filters input,
            .filters select {
                flex: 1 1 auto;
                min-width: 150px;
            }

            .filters button {
                background: var(--primary-color);
                color: white;
                border: none;
                font-weight: 500;
                display: inline-flex;
                align-items: center;
                gap: var(--spacing-sm);
                cursor: pointer;
                white-space: nowrap;
            }

            .filters button:hover {
                background: var(--primary-dark);
            }

            .filters select:focus,
            .filters input:focus {
                border-color: var(--primary-color);
                outline: none;
            }

            .layout-icons {
                display: flex;
                gap: 8px;
                font-size: 18px;
                color: var(--text-color);
            }

            .layout-icons i {
                cursor: pointer;
                padding: 6px;
                border-radius: 4px;
                transition: background-color 0.3s ease;
            }

            .layout-icons i:hover {
                background-color: var(--light-gray);
            }


            .add-product-btn {
                background: var(--primary-color);
                color: white;
                padding: var(--spacing-sm) var(--spacing-md);
                border-radius: 6px;
                text-decoration: none;
                font-weight: 500;
                display: inline-flex;
                align-items: center;
                gap: var(--spacing-sm);
                transition: background-color 0.3s ease;
                height: 40px; /* Match filter input height */
                white-space: nowrap;
            }

            .add-product-btn:hover {
                background-color: var(--primary-dark);
            }

            /* Table Styles */
            .table-responsive {
                overflow-x: auto;
                margin-bottom: var(--spacing-md);
            }

            table {
                width: 100%;
                border-collapse: separate;
                border-spacing: 0;
            }

            th, td {
                padding: var(--spacing-sm) var(--spacing-md);
                font-size: 14px;
                text-align: center;
                border-bottom: 1px solid var(--border-color);
            }

            th {
                background: var(--light-gray);
                text-transform: uppercase;
                font-size: 12px;
                font-weight: 600;
            }

            td img.product-thumb {
                width: 40px;
                height: 40px;
                object-fit: cover;
                border-radius: 6px;
                margin-right: var(--spacing-sm);
            }

            td .product-info {
                display: flex;
                align-items: center;
                gap: var(--spacing-sm);
            }

            .status-badge.active {
                display: inline-flex;
                align-items: center;
                gap: var(--spacing-sm);
                font-size: 13px;
                font-weight: 500;
                color: #4caf50;
            }

            .status-badge.active::before {
                content: '';
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background-color: #4caf50;
            }

            .status-badge.inactive {
                display: inline-flex;
                align-items: center;
                gap: var(--spacing-sm);
                font-size: 13px;
                font-weight: 500;
                color: #d32f2f;
            }

            .status-badge.inactive::before {
                content: '';
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background-color: #d32f2f;

            }

            .action-links {
                display: flex;
                gap: var(--spacing-sm);
                justify-content: flex-end;
                align-items: center;
            }

            .action-links a {
                padding: var(--spacing-sm) var(--spacing-md);
                border-radius: 6px;
                font-size: 14px;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: var(--spacing-sm);
                transition: background-color 0.3s ease;
            }

            .action-links .edit {
                background-color: var(--primary-color);
                color: white;
            }

            .action-links .edit:hover {
                background-color: var(--primary-dark);
            }

            .action-links .delete {
                background-color: #e0e0e0;
                color: #444;
            }

            .action-links .delete:hover {
                background-color: #d0d0d0;
            }
            .filters-row, .filters-bottom-row {
                display: flex;
                flex-wrap: wrap;
                gap: 12px;
                margin-bottom: 16px;
                align-items: center;
            }

            .filters-row input,
            .filters-row select,
            .filters-bottom-row select {
                height: 40px;
                padding: 0 12px;
                border: 1px solid var(--border-color);
                border-radius: 6px;
                font-size: 14px;
                background-color: var(--white);
                min-width: 160px;
                flex: 1;
            }

            .filters-row button {
                height: 40px;
                padding: 0 16px;
                background-color: var(--primary-color);
                border: none;
                color: white;
                border-radius: 6px;
                font-size: 14px;
                display: flex;
                align-items: center;
                gap: 6px;
            }

            .add-product-btn {
                height: 40px;
                padding: 0 16px;
                background-color: var(--primary-color);
                color: white;
                border-radius: 6px;
                text-decoration: none;
                display: flex;
                align-items: center;
                gap: 6px;
            }



            /* Pagination */
            .pagination {
                display: flex;
                justify-content: center;
                margin-top: var(--spacing-md);
                gap: var(--spacing-sm);
                align-items: center;
            }

            .pagination a, .pagination strong {
                width: 40px;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 6px;
                text-decoration: none;
                font-size: 14px;
                transition: background-color 0.3s ease, color 0.3s ease;
            }

            .pagination a {
                background: var(--white);
                color: var(--text-color);
                border: 1px solid var(--border-color);
            }

            .pagination a:hover {
                background: var(--light-gray);
            }

            .pagination strong {
                background: var(--primary-color);
                color: white;
                font-weight: 600;
            }

            /* Responsive Adjustments */
            @media (max-width: 768px) {
                .filters {
                    flex-wrap: wrap; /* Allow wrapping on small screens */
                }

                .filter-form {
                    flex-wrap: wrap;
                }

                .filters select,
                .filters input,
                .filters button,
                .add-product-btn {
                    width: 100%;
                    margin-bottom: var(--spacing-sm);
                }
            }
        </style>
    </head>
    <body>
        <div class="container">
            <c:set var="products" value="${requestScope.products}"/>
            <c:set var="pageSize" value="${pageSize}" />

            <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>
            <%-- Xóa hoặc comment --%>
            <%-- <c:set var="start" value="${(currentPage - 1) * pageSize}"/>
            <c:set var="end" value="${start + pageSize}"/> --%>

            <!-- <c:set var="totalProducts" value="${totalProducts}" /> -->


            <form action="products" method="get">
    <input type="hidden" name="action" value="findAdmin"/>
    <input type="hidden" name="pageSize" value="${pageSize}" />


                <!-- Hàng 1: Sắp xếp + Thương hiệu + Trạng thái + Kho -->
                <div class="filters-row">
                    <select name="sort">
                        <option value="">Sắp xếp</option>
                        <option value="price_asc" ${param.sort == 'price_asc' ? 'selected' : ''}>Giá: Thấp → Cao</option>
                        <option value="price_desc" ${param.sort == 'price_desc' ? 'selected' : ''}>Giá: Cao → Thấp</option>
                        <option value="name_asc" ${param.sort == 'name_asc' ? 'selected' : ''}>Tên: A → Z</option>
                        <option value="name_desc" ${param.sort == 'name_desc' ? 'selected' : ''}>Tên: Z → A</option>
                    </select>

                    <select name="brandId">
                        <option value="">Thương hiệu</option>
                        <c:forEach var="brand" items="${brands}">
                            <option value="${brand.id}" ${param.brandId == brand.id.toString() ? 'selected' : ''}>${brand.name}</option>
                        </c:forEach>

                    </select>


                    <select name="status">
                        <option value="">Trạng thái</option>
                        <option value="active" ${param.status == 'active' ? 'selected' : ''}>Active</option>
                        <option value="inactive" ${param.status == 'inactive' ? 'selected' : ''}>Inactive</option>
                    </select>



                    <button type="submit"><i class="fas fa-filter"></i> Lọc</button>

                </div>

                <!-- Hàng 2: Tìm kiếm -->
                <div class="filters-row">
                    <input type="text" name="searchName" placeholder="Tìm theo tên sản phẩm..." value="${param.searchName}" />
                    <a href="${pageContext.request.contextPath}/product/ProductCreate.jsp" class="add-product-btn">
                        <i class="fas fa-plus"></i> Thêm sản phẩm
                    </a>
                </div>


            </form>



            <!-- Table -->
            <div class="table-responsive">
                <table>
                    <thead>
                        <tr>
                            <th></th>
                            <th>ID</th>
                            <th>Tên sản phẩm</th>
                            <th>Nhãn hàng</th>
                            <th>Lượt xem</th>
                            <th>Ngày tạo</th>
                            <th>Tồn kho</th>
                            <th>Trạng thái</th>
                            <th>Hoạt động</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:if test="${products == null or empty products}">
                            <tr>
                                <td colspan="9" style="text-align: center; color: red; padding: var(--spacing-md);">Không tìm thấy sản phẩm.</td>
                            </tr>
                        </c:if>
                        <c:if test="${products != null and not empty products}">
                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <td><input type="checkbox"/></td>
                                    <td>${product.id}</td>
                                    <td>
                                        <div class="product-info">
                                            <img class="product-thumb" src="${product.thumbnailImage}"/>
                                            <span>${product.name}</span>
                                        </div>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${product.brand != null}">
                                                ${product.brand.name}
                                            </c:when>
                                            <c:otherwise>N/A</c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>${applicationScope.productViewCount[product.id] != null ? applicationScope.productViewCount[product.id] : 0}</td>
                                    <td>${product.createAt}</td>
                                    <td>${productStockQuantity[product.id] != null ? productStockQuantity[product.id] : 0}</td>
                                    <td>
                                        <span class="status-badge ${product.isActive ? 'active' : 'inactive'}">${product.isActive ? 'Active' : 'Inactive'}</span>
                                    </td>
                                    <td class="action-links">
                                        <a href="${pageContext.request.contextPath}/products?action=update&productId=${product.id}" class="edit">
                                            <i class="fas fa-edit"></i> Edit
                                        </a>
                                        <a href="${pageContext.request.contextPath}/products?action=delete&id=${product.id}" class="delete"
                                           onclick="return confirm('Are you sure you want to delete this product?');">Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>

                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <c:url var="prevUrl" value="/products">
                        <c:param name="action" value="findAdmin"/>
                        <c:param name="page" value="${currentPage - 1}"/>
                        <c:param name="searchName" value="${param.searchName}"/>
                        <c:param name="brandId" value="${param.brandId}"/>
                        <c:param name="status" value="${param.status}"/>
                        <c:param name="sort" value="${param.sort}"/>
                    </c:url>
                    <a href="${prevUrl}"><i class="fas fa-chevron-left"></i> Previous</a>
                </c:if>

                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:choose>
                        <c:when test="${currentPage == i}">
                            <strong>${i}</strong>
                        </c:when>
                        <c:otherwise>
                            <c:url var="paginationUrl" value="/products">
                                <c:param name="action" value="findAdmin"/>
                                <c:param name="page" value="${i}"/>
                                <c:param name="pageSize" value="${pageSize}"/>
                                <c:if test="${not empty param.searchName}">
                                    <c:param name="searchName" value="${param.searchName}"/>
                                </c:if>
                                <c:if test="${not empty param.brandId}">
                                    <c:param name="brandId" value="${param.brandId}"/>
                                </c:if>
                                <c:if test="${not empty param.status}">
                                    <c:param name="status" value="${param.status}"/>
                                </c:if>
                                <c:if test="${not empty param.sort}">
                                    <c:param name="sort" value="${param.sort}"/>
                                </c:if>
                            </c:url>


                            <a href="${paginationUrl}">${i}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <c:url var="nextUrl" value="/products">
                        <c:param name="action" value="findAdmin"/>
                        <c:param name="page" value="${currentPage + 1}"/>
                        <c:param name="searchName" value="${param.searchName}"/>
                        <c:param name="brandId" value="${param.brandId}"/>
                        <c:param name="status" value="${param.status}"/>
                        <c:param name="sort" value="${param.sort}"/>
                    </c:url>
                    <a href="${nextUrl}">Next <i class="fas fa-chevron-right"></i></a>
                    </c:if>
            </div>

        </div>
    </body>
</html>
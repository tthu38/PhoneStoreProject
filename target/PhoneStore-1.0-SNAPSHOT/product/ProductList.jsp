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
                --primary-color: #5b57d1;
                --primary-dark: #4a45ba;
                --text-color: #333;
                --light-gray: #f5f5f5;
                --white: #fff;
                --border-color: #e0e0e0;
            }

            body {
                font-family: 'Poppins', sans-serif;
                background: var(--light-gray);
                color: var(--text-color);
                margin: 0;
                padding: 0;
            }

            .container {
                max-width: 1200px;
                margin: 30px auto;
                background: var(--white);
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            }

            .filters {
                display: flex;
                justify-content: space-between;
                flex-wrap: wrap;
                margin-bottom: 20px;
                gap: 10px;
            }

            .filters select,
            .filters input {
                padding: 8px 12px;
                border: 1px solid var(--border-color);
                border-radius: 4px;
                font-size: 14px;
                background: var(--white);
            }

            .add-product-btn {
                background: var(--primary-color);
                color: white;
                padding: 8px 16px;
                border-radius: 4px;
                text-decoration: none;
                font-weight: 500;
                display: inline-flex;
                align-items: center;
                gap: 6px;
            }

            .table-responsive {
                overflow-x: auto;
            }

            table {
                width: 100%;
                border-collapse: collapse;
            }

            th, td {
                padding: 12px 15px;
                font-size: 14px;
                text-align: center;
                ;
                border-bottom: 1px solid var(--border-color);
            }

            th {
                background: var(--light-gray);
                text-transform: uppercase;
                font-size: 12px;
            }

            td img.product-thumb {
                width: 40px;
                height: 40px;
                object-fit: cover;
                border-radius: 6px;
                margin-right: 10px;
            }

            td .product-info {
                display: flex;
                align-items: center;
            }

            .status-badge {
                display: inline-flex;
                align-items: center;
                gap: 6px;
                font-size: 13px;
                font-weight: 500;
                color: #4caf50;
            }

            .status-badge::before {
                content: '';
                width: 8px;
                height: 8px;
                border-radius: 50%;
                background-color: #4caf50;
            }

            .action-links {
                display: flex;
                gap: 8px;
                justify-content: flex-end;
            }

            .action-links a {
                padding: 10px 12px;
                border-radius: 6px;
                font-size: 14px;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: 6px;
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

            .pagination {
                display: flex;
                justify-content: center;
                margin-top: 20px;
                gap: 8px;
            }

            .pagination a, .pagination strong {
                width: 32px;
                height: 32px;
                display: flex;
                align-items: center;
                justify-content: center;
                border-radius: 4px;
                text-decoration: none;
                font-size: 14px;
            }

            .pagination a {
                background: var(--white);
                color: var(--text-color);
                border: 1px solid var(--border-color);
            }

            .pagination strong {
                background: var(--primary-color);
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
        <div class="container">
            <c:set var="products" value="${requestScope.products}"/>
            <c:set var="pageSize" value="10"/>
            <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>
            <c:set var="start" value="${(currentPage - 1) * pageSize}"/>
            <c:set var="end" value="${start + pageSize}"/>
            <c:set var="totalProducts" value="${products.size()}"/>
            <c:set var="totalPages" value="${Math.ceil(totalProducts / pageSize)}"/>
            <!-- Filters -->
            <div class="filters">
                <div>
                    <select><option>Điện thoại</option></select>
                    <select>
                        <option>Tất cả trạng thái</option>
                        <option>ACTIVE</option>
                        <option>INACTIVE</option>
                    </select>
                    <select><option>$50 - $100</option></select>
                    <select><option>Tất cả các cửa hàng</option></select>
                </div>
                <div>
                    <input type="text" placeholder="Search products..." />
                    <a href="${pageContext.request.contextPath}/product/ProductCreate.jsp" class="add-product-btn">
                        <i class="fas fa-plus"></i> Thêm sản phẩm mới
                    </a>
                </div>
            </div>

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
                                <td colspan="9" style="text-align: center; color: red;">Không tìm thấy sản phẩm.</td>
                            </tr>
                        </c:if>

                        <c:if test="${products != null and not empty products}">
                            <c:forEach var="product" items="${products}" varStatus="status">
                                <c:if test="${status.index >= start && status.index < end}">

                                    <tr>
                                        <td><input type="checkbox" /></td>
                                        <td>${product.id}</td>
                                        <td>
                                            <div class="product-info">
                                                <img class="product-thumb" src="${product.thumbnailImage}" />
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
                                        <span class="status-badge">${product.isActive ? 'Active' : 'Inactive'}</span>
                                    </td>
                                    <td class="action-links">
                                        <a href="${pageContext.request.contextPath}/products?action=edit&id=${product.id}" class="edit">
                                            <i class="fas fa-edit"></i> Edit
                                        </a>
                                        <a href="${pageContext.request.contextPath}/products?action=delete&productId=${product.id}" class="delete">
                                            <i class="fas fa-trash"></i>
                                        </a>
                                    </td>
                                </tr>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- Pagination -->
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
    </body>
</html>

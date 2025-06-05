<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <!-- Thêm để định dạng ngày -->

<html>
<head>
    <title>Product List</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        :root {
            --primary-color: #d32f2f; /* Màu đỏ chủ đạo */
            --primary-light: #ef5350; /* Màu đỏ nhạt */
            --primary-dark: #b71c1c; /* Màu đỏ đậm */
            --accent-color: #ff9800; /* Màu cam cho nút Edit */
            --text-color: #333;
            --light-gray: #f5f5f5;
            --white: #fff;
            --border-color: #e0e0e0;
            --shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            --shadow-hover: 0 8px 15px rgba(0, 0, 0, 0.15);
            --radius: 8px;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: 'Poppins', Arial, sans-serif;
            background-color: var(--light-gray);
            color: var(--text-color);
            line-height: 1.6;
            padding: 0;
            margin: 0;
        }

        .container {
            max-width: 1100px;
            margin: 30px auto;
            background: var(--white);
            padding: 30px;
            border-radius: var(--radius);
            box-shadow: var(--shadow);
        }

        h2 {
            color: var(--primary-color);
            margin-bottom: 20px;
            font-weight: 600;
            text-align: center;
            position: relative;
            padding-bottom: 10px;
        }

        h2::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 80px;
            height: 3px;
            background-color: var(--primary-color);
            border-radius: 2px;
        }

        .welcome-text {
            margin-bottom: 15px;
            color: var(--primary-color);
            font-size: 1.1rem;
            display: flex;
            align-items: center;
        }

        .welcome-text i {
            margin-right: 8px;
            font-size: 1.2rem;
        }

        .links {
            display: flex;
            justify-content: center;
            flex-wrap: wrap;
            gap: 15px;
            margin: 25px 0;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            padding: 10px 16px;
            background: var(--primary-color);
            color: var(--white);
            border-radius: var(--radius);
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
            box-shadow: var(--shadow);
        }

        .btn i {
            margin-right: 8px;
        }

        .btn:hover {
            background: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: var(--shadow-hover);
        }

        table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            margin: 25px 0;
            border-radius: var(--radius);
            overflow: hidden;
            box-shadow: var(--shadow);
        }

        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid var(--border-color);
        }

        th {
            background: var(--primary-color);
            color: var(--white);
            font-weight: 500;
            letter-spacing: 0.5px;
        }

        tr:last-child td {
            border-bottom: none;
        }

        tr:hover {
            background-color: rgba(211, 47, 47, 0.05);
        }

        .action-links {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 8px;
        }

        .edit {
            background: var(--accent-color);
        }

        .edit:hover {
            background: #e68900;
        }

        .delete {
            background: #c62828;
        }

        .delete:hover {
            background: #b71c1c;
        }

        .restore {
            background: #388e3c;
        }

        .restore:hover {
            background: #2e7d32;
        }

        .pagination {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
            margin-top: 30px;
        }

        .pagination a {
            display: flex;
            align-items: center;
            justify-content: center;
            min-width: 40px;
            height: 40px;
            padding: 0 12px;
            background: var(--primary-light);
            color: var(--white);
            border-radius: var(--radius);
            text-decoration: none;
            font-weight: 500;
            transition: all 0.3s ease;
        }

        .pagination a:hover {
            background: var(--primary-dark);
            transform: translateY(-2px);
            box-shadow: var(--shadow-hover);
        }

        .pagination strong {
            display: flex;
            align-items: center;
            justify-content: center;
            min-width: 40px;
            height: 40px;
            padding: 0 12px;
            background: var(--primary-dark);
            color: var(--white);
            border-radius: var(--radius);
            font-weight: 500;
        }

        .table-responsive {
            overflow-x: auto;
        }

        @media (max-width: 768px) {
            .container {
                padding: 20px 15px;
                margin: 15px;
            }

            th, td {
                padding: 12px 10px;
            }

            .btn {
                padding: 8px 12px;
                font-size: 0.9rem;
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <c:if test="${not empty errorMessage}">
            <div class="error-message">${errorMessage}</div>
        </c:if>
        <p class="welcome-text"><i class="fas fa-user-circle"></i> Welcome, ${user.name}</p>
        <h2>Product List</h2>

        <div class="links">
            <a href="${pageContext.request.contextPath}/users" class="btn"><i class="fas fa-users"></i> Users Page</a>
            <a href="${pageContext.request.contextPath}/products?action=create" class="btn"><i class="fas fa-plus-circle"></i> Add New Product</a>
            <a href="#" class="btn"><i class="fas fa-clipboard-list"></i> Inventory Log</a>
        </div>

        <c:set var="products" value="${requestScope.products}"/>
        <c:set var="pageSize" value="10"/>
        <c:set var="currentPage" value="${param.page != null ? param.page : 1}"/>
        <c:set var="start" value="${(currentPage - 1) * pageSize}"/>
        <c:set var="end" value="${start + pageSize}"/>
        <c:set var="totalProducts" value="${products != null ? products.size() : 0}"/>
        <c:set var="totalPages" value="${totalProducts > 0 ? Math.ceil(totalProducts / pageSize) : 0}"/>

        <div class="table-responsive">
            <table>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th> <!-- Sửa từ Categories thành Category -->
                    <th>Brand</th>
                    <th>Description</th>
                    <th>Specifications</th>
                    <th>Created At</th>
                    <th>Active</th> <!-- Thêm cột IsActive -->
                    <th>Views</th>
                    <th>Actions</th>
                </tr>
                <c:if test="${products != null and not empty products}">
                    <c:forEach var="product" items="${products}" varStatus="status">
                        <c:if test="${status.index >= start and status.index < end}">
                            <tr>
                                <td>${product.id}</td>
                                <td>${product.name}</td>
                                <td>${product.category.name}</td> <!-- Sửa từ categories thành category -->
                                <td>${product.brand.name}</td>
                                <td>${product.description}</td>
                                <td>${product.specifications}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.createAt != null}">
                                            <fmt:formatDate value="${product.createAtAsDate}" pattern="yyyy-MM-dd HH:mm:ss" />
                                        </c:when>
                                        <c:otherwise>
                                            N/A
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${product.isActive ? 'Yes' : 'No'}</td> <!-- Hiển thị IsActive -->
                                <td>${applicationScope.productViewCount[product.id] != null ? applicationScope.productViewCount[product.id] : 0}</td>
                                <td class="action-links">
                                    <a href="${pageContext.request.contextPath}/products?action=edit&id=${product.id}" class="btn edit"><i class="fas fa-edit"></i> Edit</a>
                                    <c:choose>
                                        <c:when test="${!product.isDeleted}">
                                            <a href="${pageContext.request.contextPath}/products?action=delete&id=${product.id}" class="btn delete"><i class="fas fa-trash"></i> Delete</a>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="${pageContext.request.contextPath}/products?action=restore&id=${product.id}" class="btn restore"><i class="fas fa-trash-restore"></i> Restore</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                </c:if>
                <c:if test="${products == null or empty products}">
                    <tr>
                        <td colspan="10" style="text-align: center;">No products found.</td> <!-- Cập nhật colspan cho số cột mới -->
                    </tr>
                </c:if>
            </table>
        </div>

        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="?page=${currentPage - 1}"><i class="fas fa-chevron-left"></i> Previous</a>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:choose>
                    <c:when test="${currentPage == i}">
                        <strong>${i}</strong>
                    </c:when>
                    <c:otherwise>
                        <a href="?page=${i}">${i}</a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="?page=${currentPage + 1}">Next <i class="fas fa-chevron-right"></i></a>
            </c:if>
        </div>
    </div>

    <jsp:include page="/templates/footer.jsp" />
</body>
</html>
<%-- 
    Document   : header
    Created on : May 27, 2025, 7:26:04 PM
    Author     : ThienThu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
    </head>
    <body>
<%@ page import="model.User" %>
<%
    User currentUser = (User) session.getAttribute("user");
    request.setAttribute("currentUser", currentUser);
%>
        <c:if test="${not empty user}">
            <c:set var="currentUser" value="${user}" scope="page"/>
        </c:if>

        <header class="header-main">
            <div class="header-container">
                <a href="${pageContext.request.contextPath}/" class="brand-logo">
                    <img src="${pageContext.request.contextPath}/images/logo.jpg">
                    <span class="brand-name">Thế Giới Công Nghệ</span>
                </a>

                <form class="search-form" action="${pageContext.request.contextPath}/search" method="get" autocomplete="off">
                    <div class="search-input-group">
                        <input class="search-input" type="search" name="q" id="searchInput" 
                               placeholder="Tìm kiếm sản phẩm, hãng, phụ kiện..." aria-label="Search">
                        <button class="btn border-0 bg-white rounded-pill search-btn" type="submit">
                            <i class="fa fa-search"></i>
                        </button>
                    </div>
                </form>

                <div class="header-actions">
                    <c:if test="${empty currentUser}">
                        <a href="#" class="header-login-btn" data-bs-toggle="modal" data-bs-target="#authModal">
                            <i class="fa-solid fa-user"></i>
                            <span>Đăng nhập/Đăng ký</span>
                        </a>
                    </c:if>
                    <c:if test="${not empty currentUser}">
                        <div class="dropdown">
                            <a class="header-login-btn dropdown-toggle" href="#" role="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                                <i class="fa-solid fa-user"></i>
                                <span>${currentUser.fullName}</span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/profile.jsp">Trang cá nhân</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/logout">Đăng xuất</a></li>
                            </ul>
                        </div>
                    </c:if>
                </div>

                <!-- Cart -->
                <a href="${pageContext.request.contextPath}/carts" class="cart-link">
                    <i class="fa fa-shopping-cart"></i>
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger cart-badge" 
                          id="cartBadge">2</span>
                    <span class="cart-text">Giỏ hàng</span>
                </a>
            </div>

            <!-- Main Navigation -->
            <nav class="main-nav">
                <div class="nav-container">
                    <ul class="nav main-menu flex-nowrap justify-content-center">
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/">Trang chủ</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/category?type=phone">Điện thoại</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/category?type=accessory">Phụ kiện</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/promo">Khuyến mãi</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/faq">Hỏi đáp</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/contact">Liên hệ</a></li>
                    </ul>
                </div>
            </nav>
        </header>

        <!-- Modal đăng nhập/đăng ký -->
        <div class="modal fade" id="authModal" tabindex="-1" aria-labelledby="authModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="authModalLabel">Đăng nhập/Đăng ký</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <div class="register-left">
                            <div class="register-left-inner">
                                <div class="register-title">Đăng nhập vào Thế Giới Công Nghệ</div>
                                <ul class="benefit-list">
                                    <li><i class="fa-solid fa-star"></i> Trải nghiệm mua sắm tốt nhất</li>
                                    <li><i class="fa-solid fa-tag"></i> Ưu đãi và khuyến mãi độc quyền</li>
                                    <li><i class="fa-solid fa-truck"></i> Giao hàng nhanh chóng</li>
                                </ul>
                            </div>
                        </div>
                        <div class="register-right">
                            <div class="register-right-inner">
                                <div class="text-center">
                                    <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn btn-primary w-100 mb-3">Đăng nhập</a>
                                    <a href="${pageContext.request.contextPath}/user/register.jsp" class="btn btn-outline-secondary w-100">Đăng ký</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <script>
            const placeholders = [
                "Tìm kiếm sản phẩm, hãng, phụ kiện...",
                "iPhone 16 Pro Max, Samsung S25, Xiaomi 14T...",
                "Ốp lưng, sạc nhanh, tai nghe, MacBook...",
                "Khuyến mãi, trả góp, bảo hành..."
            ];

            let currentPlaceholderIndex = 0;
            const searchInput = document.getElementById('searchInput');

            function rotatePlaceholder() {
                searchInput.placeholder = placeholders[currentPlaceholderIndex];
                currentPlaceholderIndex = (currentPlaceholderIndex + 1) % placeholders.length;
            }

            setInterval(rotatePlaceholder, 2500);
        </script>
    </body>
</html>
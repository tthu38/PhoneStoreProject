<%-- 
    Document   : header
    Created on : May 27, 2025, 7:26:04 PM
    Author     : ThienThu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ocean SmartPhone</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/header.css">
</head>
<body>
    <% User currentUser = (User) session.getAttribute("userObject"); %>
    
    <header class="header-main">
        <div class="header-container">
            <!-- Logo -->
            <a href="#" class="brand-logo">
                <img src="/images/logo.png">
                <span class="brand-name">Ocean SmartPhone</span>
            </a>

            <!-- Search -->
            <form class="search-form" action="/search" method="get" autocomplete="off">
                <div class="search-input-group">
                    <input class="search-input" type="search" name="q" id="searchInput" 
                           placeholder="Tìm kiếm sản phẩm, hãng, phụ kiện..." aria-label="Search">
                    <button class="btn border-0 bg-white rounded-pill search-btn" type="submit">
                        <i class="fa fa-search"></i>
                    </button>
                </div>
            </form>

            <!-- Actions -->
            <div class="header-actions">
                <!-- User Dropdown -->
                <div class="user-dropdown">
                    <% if (currentUser != null) { %>
                        <a href="<%=request.getContextPath()%>/user/profile.jsp" class="header-icon-link">
                            <i class="fa-solid fa-user"></i>
                        </a>
                        <div class="dropdown-content">
                            <div class="user-info">
                                <% if (currentUser.getPicture() != null) { %>
                                    <img src="<%= currentUser.getPicture() %>" alt="Avatar" class="user-avatar">
                                <% } else { %>
                                    <i class="fa-solid fa-user-circle fa-3x"></i>
                                <% } %>
                                <div class="user-details">
                                    <div class="user-name"><%= currentUser.getFullname() %></div>
                                    <div class="user-email"><%= currentUser.getEmail() %></div>
                                </div>
                            </div>
                            <ul class="dropdown-menu">
                                <li><a href="<%=request.getContextPath()%>/user/profile.jsp">
                                    <i class="fa-solid fa-user-pen"></i> Thông tin tài khoản</a></li>
                                <li><a href="#"><i class="fa-solid fa-clock-rotate-left"></i> Lịch sử đơn hàng</a></li>
                                <% if (currentUser.isAdmin()) { %>
                                    <li><a href="<%=request.getContextPath()%>/admin/dashboard">
                                        <i class="fa-solid fa-gauge"></i> Quản trị</a></li>
                                <% } %>
                            </ul>
                            <form action="<%=request.getContextPath()%>/logout" method="post">
                                <button type="submit" class="logout-btn">
                                    <i class="fa-solid fa-sign-out-alt"></i> Đăng xuất
                                </button>
                            </form>
                        </div>
                    <% } else { %>
                        <a href="<%=request.getContextPath()%>/oauth2/google" class="header-icon-link">
                            <i class="fa-solid fa-user"></i>
                        </a>
                    <% } %>
                </div>

                <!-- Cart -->
                <a href="/cart" class="cart-link">
                    <i class="fa fa-shopping-cart"></i>
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger cart-badge" 
                          id="cartBadge">2</span>
                    <span class="cart-text">Giỏ hàng</span>
                </a>
            </div>
        </div>

        <!-- Main Navigation -->
        <nav class="main-nav">
            <div class="nav-container">
                <ul class="nav main-menu flex-nowrap justify-content-center">
                    <li class="nav-item"><a class="nav-link" href="/">Trang chủ</a></li>
                    <li class="nav-item"><a class="nav-link" href="/category?type=phone">Điện thoại</a></li>
                    <li class="nav-item"><a class="nav-link" href="/category?type=accessory">Phụ kiện</a></li>
                    <li class="nav-item"><a class="nav-link" href="/promo">Khuyến mãi</a></li>
                    <li class="nav-item"><a class="nav-link" href="/faq">Hỏi đáp</a></li>
                    <li class="nav-item"><a class="nav-link" href="/contact">Liên hệ</a></li>
                </ul>
            </div>
        </nav>
    </header>

    <script>
    // Placeholder động cho thanh tìm kiếm
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
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

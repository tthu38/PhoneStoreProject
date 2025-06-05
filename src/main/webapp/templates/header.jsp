<%-- 
    Document   : header
    Created on : May 27, 2025, 7:26:04 PM
    Author     : ThienThu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ocean SmartPhone</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/header.css">
</head>
<body>
    <% User currentUser = (User) session.getAttribute("userObject");%>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Thế Giới Công Nghệ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/header.css">
</head>
<body>


    <header class="header-main">
        <div class="header-container">
            <a href="${pageContext.request.contextPath}/" class="brand-logo">
                <img src="/images/logo.png">
                <span class="brand-name">Thế Giới Công Nghệ</span>
            </a>


            <header class="header-main">
                <div class="header-container">
                    <a href="${pageContext.request.contextPath}/" class="brand-logo">
                        <img src="/images/logo.png">
                        <span class="brand-name">Thế Giới Công NghệNghệ</span>
                    </a>

                    <form class="search-form" action="/search" method="get" autocomplete="off">
                        <div class="search-input-group">
                            <input class="search-input" type="search" name="q" id="searchInput" 
                                   placeholder="Tìm kiếm sản phẩm, hãng, phụ kiện..." aria-label="Search">
                            <button class="btn border-0 bg-white rounded-pill search-btn" type="submit">
                                <i class="fa fa-search"></i>
                            </button>
                        </div>
                    </form>

                    <div class="header-actions">
                        <div class="user-dropdown">
                            <% if (currentUser != null) {%>
                            <a href="#" id="profileAvatarBtn" class="header-login-btn">
                                <i class="fa-solid fa-user"></i>
                                <span>Trang cá nhân</span>
                            </a>
                            <div id="profileModal" class="profile-modal-backdrop" style="display:none;">
                                <div class="profile-modal-box">
                                    <span class="close-modal" onclick="closeProfileModal()">&times;</span>
                                    <a href="<%=request.getContextPath()%>/user/profile.jsp" class="profile-modal-option"><i class="fa-solid fa-user-pen"></i> Thông tin tài khoản</a>
                                    <form action="<%=request.getContextPath()%>/logout" method="post" style="margin:0;">
                                        <button type="submit" class="profile-modal-option"><i class="fa-solid fa-sign-out-alt"></i> Đăng xuất</button>
                                    </form>
                                </div>
                            </div>
                            <% } else { %>
                            <button type="button" id="openAuthModalBtn" class="header-login-btn" style="border:none;background:none;padding:0;">
                                <i class="fa-solid fa-user"></i>
                                <span class="cart-text">Đăng nhập</span>
                            </button>
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

            <% if (currentUser == null) { %>
            <!-- Modal đăng nhập/đăng ký -->
            <div id="authModal" class="profile-modal-backdrop" style="display:none;">
                <div class="profile-modal-box" style="align-items:center; text-align:center; max-width:340px;">
                    <button class="profile-modal-close" onclick="closeAuthModal()">&times;</button>
                    <img src="https://cdn.cellphones.com.vn/media/logo/smember.png" alt="Logo" style="height:54px; margin-bottom:0.7rem;"/>
                    <div style="font-size:1.5rem; color:#ea1d25; font-weight:700; margin-bottom:0.7rem;">Thế Giới Công Nghệ</div>
                    <div style="color:#333; font-size:1.08rem; margin-bottom:1.2rem;">Vui lòng đăng nhập tài khoản Thế Giới Công Nghệ để xem ưu đãi và thanh toán dễ dàng hơn.</div>
                    <div style="display:flex; gap:1rem; justify-content:center;">
                        <a href="${pageContext.request.contextPath}/user/register.jsp" class="profile-modal-option" style="border:1.5px solid #ea1d25; background:#fff; color:#ea1d25; text-align:center;">Đăng ký</a>
                        <a href="${pageContext.request.contextPath}/user/login.jsp" class="profile-modal-option" style="background:#ea1d25; color:#fff; text-align:center;">Đăng nhập</a>
                    </div>
                </div>
            </div>
            <% }%>

            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
            <script src="${pageContext.request.contextPath}/js/header.js"></script>


            </body>


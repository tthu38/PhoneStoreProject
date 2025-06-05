
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>The Gioi C?ng Ngh?</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/header.css">

    <div class="header-container">
        <a href="${pageContext.request.contextPath}/" class="brand-logo">
            <img src="/images/logo.png">
            <span class="brand-name">Th? Gi?i C?ng Ngh?</span>
        </a>

        <form class="search-form" action="/search" method="get" autocomplete="off">

            </a>
            <div id="profileModal" class="profile-modal-backdrop" style="display:none;">
                <div class="profile-modal-box">
                    <span class="close-modal" onclick="closeProfileModal()">&times;</span>
                    <a href="<%=request.getContextPath()%>/user/profile.jsp" class="profile-modal-option"><i class="fa-solid fa-user-pen"></i> Th?ng tin t?i kho?n</a>
                    <form action="<%=request.getContextPath()%>/logout" method="post" style="margin:0;">
                        <button type="submit" class="profile-modal-option"><i class="fa-solid fa-sign-out-alt"></i> ??ng xu?t</button>

                        <button type="button" id="openAuthModalBtn" class="header-login-btn" style="border:none;background:none;padding:0;">
                            <i class="fa-solid fa-user"></i>
                            <span class="cart-text">??ng nh?p</span>
                        </button>
                        <% }%>
                </div>

                <div class="profile-modal-box" style="align-items:center; text-align:center; max-width:340px;">
                    <button class="profile-modal-close" onclick="closeAuthModal()">&times;</button>
                    <img src="https://cdn.cellphones.com.vn/media/logo/smember.png" alt="Logo" style="height:54px; margin-bottom:0.7rem;"/>
                    <div style="font-size:1.5rem; color:#ea1d25; font-weight:700; margin-bottom:0.7rem;">Th? Gi?i C?ng Ngh?</div>
                    <div style="color:#333; font-size:1.08rem; margin-bottom:1.2rem;">Vui l?ng ??ng nh?p t?i kho?n Th? Gi?i C?ng Ngh? ?? xem ?u ??i v? thanh to?n d? d?ng h?n.</div>
                    <div style="display:flex; gap:1rem; justify-content:center;">
                        <a href="${pageContext.request.contextPath}/user/register.jsp" class="profile-modal-option" style="border:1.5px solid #ea1d25; background:#fff; color:#ea1d25; text-align:center;">??ng k?</a>
                        <a href="${pageContext.request.contextPath}/user/login.jsp" class="profile-modal-option" style="background:#ea1d25; color:#fff; text-align:center;">??ng nh?p</a>
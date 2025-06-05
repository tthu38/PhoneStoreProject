<%-- 
    Document   : login
    Created on : May 29, 2025, 11:25:27 AM
    Author     : dangt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Đăng nhập - Thế giới công nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <script src="${pageContext.request.contextPath}/js/login.js"></script>
    </head>
    <body>
        <div class="login-container">
            <div class="login-left">
                <div class="login-left-inner">
                    <div class="login-title">Chào mừng đến với Thế giới công nghệ</div>
                    <ul class="benefit-list">
                        <li><i class="fa-solid fa-gift"></i>Ưu đãi thành viên hấp dẫn</li>
                        <li><i class="fa-solid fa-truck-fast"></i>Miễn phí giao hàng toàn quốc</li>
                        <li><i class="fa-solid fa-headset"></i>Hỗ trợ khách hàng 24/7</li>
                        <div class="text-center mt-4">
                            <span style="color:#888;">* Đăng nhập để nhận nhiều ưu đãi hơn</span>
                        </div>
                        <img src="${pageContext.request.contextPath}/images/login.jpg" alt="Login Illustration" class="login-illustration"/>
                        <div style="text-align:center; font-size:2rem; color:#ea1d25; font-weight:bold; margin-top:1.2rem;">Đăng nhập</div>
                    </ul>
                </div>
            </div>
            <div class="login-right">
                <div class="login-right-inner">
                    <form class="login-form" action="${pageContext.request.contextPath}/login" method="post">
                        <div class="login-title" style="font-size:1.7rem;">Đăng nhập Thế giới công nghệ</div>
                        <div class="mb-3">
                            <label class="form-label">Số điện thoại hoặc Email</label>
                            <input type="text" class="form-control" name="email" placeholder="Nhập số điện thoại hoặc email" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mật khẩu</label>
                            <input type="password" class="form-control" name="password" placeholder="Nhập mật khẩu" required>
                        </div>
                        <div class="d-flex justify-content-between align-items-center mb-3">
                            <a href="#" class="text-decoration-none">Quên mật khẩu?</a>
                        </div>
                        <div class="text-center mb-2" style="color:#888;">Hoặc đăng nhập bằng</div>
                        <div class="d-flex justify-content-between mb-3">
                            <a href="${pageContext.request.contextPath}/login-google" class="btn social-btn google"><i class="fab fa-google"></i>Google</a>
                        </div>
                        <div class="text-center mt-3">
                            <span>Bạn chưa có tài khoản? <a href="${pageContext.request.contextPath}/user/register.jsp" class="text-danger fw-bold">Đăng ký ngay</a></span>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
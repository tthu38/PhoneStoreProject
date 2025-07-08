<%-- Document : login Created on : May 29, 2025, 11:25:27 AM Author : dangt --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
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
                            <img src="${pageContext.request.contextPath}/images/login.jpg" alt="Login Illustration"
                                class="login-illustration" />
                        </ul>
                    </div>
                </div>
                <div class="login-right">
                    <form class="login-form" action="${pageContext.request.contextPath}/login" method="post">
                        <input type="hidden" name="action" value="login">
                        <div class="login-title" style="font-size:1.7rem;">Đăng Nhập Thế Giới Công Nghệ</div>
                        <div class="mb-3">
                            <label class="form-label">Số điện thoại</label>
                            <input type="text" class="form-control" name="username" placeholder="Nhập số điện thoại"
                                required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Mật khẩu</label>
                            <div style="position: relative;">
                                <input type="password" class="form-control" id="password" name="password"
                                    placeholder="Nhập mật khẩu" required>
                                <button type="button" id="togglePassword" tabindex="-1"
                                    style="position:absolute; right:16px; top:50%; transform:translateY(-50%); background:transparent; border:none; outline:none;">
                                    <i class="fa-regular fa-eye" id="eyeIcon"></i>
                                </button>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary" style="width:100%; margin-bottom: 0.5rem;">Đăng
                            nhập</button>
                        <div class="d-flex justify-content-between align-items-center mb-3" style="margin-top: 0.5rem;">
                            <div>
                                <input type="checkbox" id="rememberMe" name="rememberMe" value="true" />
                                <label for="rememberMe" style="font-size: 1rem; margin-bottom:0;">Ghi Nhớ Đăng
                                    Nhập</label>
                            </div>
                            <a href="#" class="text-decoration-none">Quên mật khẩu?</a>
                        </div>
                        <div class="text-center mb-2" style="color:#888;">Hoặc đăng nhập bằng</div>
                        <div class="d-flex justify-content-center mb-3 gap-2">
                            <a href="#" id="google-login-btn" class="btn social-btn google">
                                <i class="fab fa-google"></i> Google
                            </a>
                            <a href="#" id="facebook-login-btn" class="btn social-btn facebook"
                                style="background:#1877f2; color:#fff;">
                                <i class="fab fa-facebook-f"></i> Facebook
                            </a>
                        </div>
                        <div class="text-center mt-3">
                            <span>Bạn chưa có tài khoản? <a href="${pageContext.request.contextPath}/user/register.jsp"
                                    class="text-danger fw-bold">Đăng ký ngay</a></span>
                        </div>
                    </form>

                </div>
            </div>
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
            <script>
                // Ẩn/hiện mật khẩu
                document.getElementById('togglePassword').addEventListener('click', function () {
                    const pwd = document.getElementById('password');
                    const icon = document.getElementById('eyeIcon');
                    if (pwd.type === 'password') {
                        pwd.type = 'text';
                        icon.classList.remove('fa-eye');
                        icon.classList.add('fa-eye-slash');
                    } else {
                        pwd.type = 'password';
                        icon.classList.remove('fa-eye-slash');
                        icon.classList.add('fa-eye');
                    }
                });

                // Đăng nhập Google
                document.getElementById('google-login-btn').addEventListener('click', function (e) {
                    e.preventDefault();
                    var remember = document.querySelector('input[name="rememberMe"]').checked;
                    var url = '${pageContext.request.contextPath}/login?action=login-google';
                    if (remember) {
                        url += '&rememberMe=true';
                    }
                    window.location.href = url;
                });

                // Đăng nhập Facebook (chưa có backend, chỉ demo)
                document.getElementById('facebook-login-btn').addEventListener('click', function (e) {
                    e.preventDefault();
                    alert('Tính năng đăng nhập bằng Facebook đang phát triển!');
                });
            </script>
        </body>

        </html>
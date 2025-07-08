<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@page import="java.util.ResourceBundle" %>
        <% ResourceBundle bundle=ResourceBundle.getBundle("application"); String
            fbAppId=bundle.getString("facebook.app.id"); String fbRedirectUri=bundle.getString("facebook.redirect.uri");
            String fbLoginUrl="https://www.facebook.com/v18.0/dialog/oauth?client_id=" + fbAppId + "&redirect_uri=" +
            java.net.URLEncoder.encode(fbRedirectUri, "UTF-8" ) + "&scope=email" ; %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Đăng ký thành viên - Thế Giới Công Nghệ</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
            </head>

            <body class="bg-light">
                <div class="container min-vh-100 d-flex align-items-center justify-content-center">
                    <div class="w-90">
                        <div class="bg-white rounded-4 shadow p-4">
                            <img src="https://cdn.cellphones.com.vn/media/logo/smember.png" alt="Logo"
                                class="d-block mx-auto mb-3" style="max-width:120px;">
                            <div class="text-center fw-bold fs-5 mb-4">Đăng ký trở thành thành viên của TGCN</div>
                            <form class="register-form" action="${pageContext.request.contextPath}/register"
                                method="post">
                                <div class="d-flex justify-content-between mb-3 gap-2">
                                    <a href="${pageContext.request.contextPath}/login-google"
                                        class="btn btn-outline-secondary flex-fill">
                                        <i class="fab fa-google"></i> Google
                                    </a>
                                    <a href="<%= fbLoginUrl %>" class="btn btn-primary flex-fill"
                                        style="background:#1877f2; border:none;">
                                        <i class="fab fa-facebook-f"></i> Facebook
                                    </a>
                                </div>
                                <div class="text-center mb-3 text-secondary">Hoặc điền thông tin sau</div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Họ và tên</label>
                                        <input type="text" class="form-control" name="fullname"
                                            placeholder="Nhập họ và tên" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Ngày sinh</label>
                                        <input type="date" class="form-control" name="dob">
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Số điện thoại</label>
                                        <input type="text" class="form-control" name="phone"
                                            placeholder="Nhập số điện thoại" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label class="form-label">Email</label>
                                        <input type="email" class="form-control" name="email" placeholder="Nhập email"
                                            required>
                                    </div>
                                </div>
                                <div class="mb-3 row address-row">
                                    <div class="col-md-4 mb-2">
                                        <label class="form-label">Tỉnh/Thành phố</label>
                                        <select class="form-select" id="province" name="province" required>
                                            <option value="">Chọn tỉnh/thành phố</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4 mb-2">
                                        <label class="form-label">Quận/Huyện</label>
                                        <select class="form-select" id="district" name="district" required disabled>
                                            <option value="">Chọn quận/huyện</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4 mb-2">
                                        <label class="form-label">Phường/Xã</label>
                                        <select class="form-select" id="ward" name="ward" required disabled>
                                            <option value="">Chọn phường/xã</option>
                                        </select>
                                    </div>
                                </div>
                                <input type="hidden" name="address" id="address">
                                <div class="mb-3">
                                    <label class="form-label">Mật khẩu</label>
                                    <input type="password" class="form-control" id="password" name="password" required>
                                    <div id="password-error" class="form-text text-danger" style="display:none;">
                                        Mật khẩu tối thiểu 6 ký tự, có ít nhất 1 chữ và 1 số
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Nhập lại mật khẩu</label>
                                    <input type="password" class="form-control" name="confirmPassword"
                                        placeholder="Nhập lại mật khẩu" required>
                                </div>
                                <div class="form-check mb-3">
                                    <input class="form-check-input" type="checkbox" name="subscribe" id="subscribe">
                                    <label class="form-check-label" for="subscribe">
                                        Đăng ký nhận tin khuyến mãi từ Thế Giới Công Nghệ
                                    </label>
                                </div>
                                <div class="form-text mb-3">
                                    Bằng việc Đăng ký, bạn đã đọc và đồng ý với <a href="#">Điều khoản sử dụng</a> và <a
                                        href="#">Chính sách bảo mật của Ocean SmartPhone</a>.
                                </div>
                                <div class="d-flex justify-content-between">
                                    <a href="${pageContext.request.contextPath}/user/login.jsp"
                                        class="btn btn-outline-secondary">&lt; Quay lại đăng nhập</a>
                                    <button type="submit" class="btn btn-primary">Hoàn tất đăng ký</button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <script src="${pageContext.request.contextPath}/js/register.js"></script>
            </body>

            </html>
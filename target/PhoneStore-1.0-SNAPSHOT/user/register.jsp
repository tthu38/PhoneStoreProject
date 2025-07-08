<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Đăng ký thành viên - Ocean SmartPhone</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <script src="${pageContext.request.contextPath}/js/register.js"></script>
    <style>
        .register-center-container {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            background: #f8f9fa;
        }
        .register-center-inner {
            background: #fff;
            box-shadow: 0 0 32px rgba(0,0,0,0.10);
            border-radius: 18px;
            max-width: 520px;
            width: 100%;
            padding: 3.5rem 2.5rem;
        }
        .register-title {
            color: #ea1d25;
            font-weight: bold;
            font-size: 2.5rem;
            margin-bottom: 2.2rem;
            text-align: center;
        }
        .register-logo {
            display: block;
            margin: 0 auto 1.2rem auto;
            max-height: 70px;
        }
    </style>
</head>
<body>
    <div class="register-center-container">
        <div class="register-center-inner">
            <img src="https://cdn.cellphones.com.vn/media/logo/smember.png" alt="Logo" class="register-logo"/>
            <div class="register-title">Đăng ký trở thành SMEMBER</div>
            <form class="register-form" action="${pageContext.request.contextPath}/register" method="post">
                <div class="d-flex justify-content-between mb-3">
                    <button type="button" class="btn social-btn google" disabled><i class="fab fa-google"></i>Google</button>
                    <button type="button" class="btn social-btn zalo" disabled><i class="fab fa-facebook-messenger"></i>Zalo</button>
                </div>
                <div class="text-center mb-3" style="color:#888;">Hoặc điền thông tin sau</div>
                <div class="mb-3">
                    <label class="form-label">Họ và tên</label>
                    <input type="text" class="form-control" name="fullname" placeholder="Nhập họ và tên" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Ngày sinh</label>
                    <input type="date" class="form-control" name="dob">
                </div>
                <div class="mb-3">
                    <label class="form-label">Số điện thoại</label>
                    <input type="text" class="form-control" name="phone" placeholder="Nhập số điện thoại" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Email (Không bắt buộc)</label>
                    <input type="email" class="form-control" name="email" placeholder="Nhập email">
                </div>
                <div class="mb-3">
                    <label class="form-label">Mật khẩu</label>
                    <input type="password" class="form-control" name="password" placeholder="Nhập mật khẩu của bạn" required>
                    <div class="form-text text-success">Mật khẩu tối thiểu 6 ký tự, có ít nhất 1 chữ và 1 số</div>
                </div>
                <div class="mb-3">
                    <label class="form-label">Nhập lại mật khẩu</label>
                    <input type="password" class="form-control" name="confirmPassword" placeholder="Nhập lại mật khẩu" required>
                </div>
                <div class="form-check mb-3">
                    <input class="form-check-input" type="checkbox" name="subscribe" id="subscribe">
                    <label class="form-check-label" for="subscribe">
                        Đăng ký nhận tin khuyến mãi từ Ocean SmartPhone
                    </label>
                </div>
                <div class="form-text mb-3">
                    Bằng việc Đăng ký, bạn đã đọc và đồng ý với <a href="#">Điều khoản sử dụng</a> và <a href="#">Chính sách bảo mật của Ocean SmartPhone</a>.
                </div>
                <div class="d-flex justify-content-between">
                    <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn btn-outline-secondary">&lt; Quay lại đăng nhập</a>
                    <button type="submit" class="btn btn-primary">Hoàn tất đăng ký</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("userObject");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/user/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Cập nhật thông tin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .update-form-container {
            max-width: 700px;
            margin: 40px auto;
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
            padding: 2.5rem 2.5rem 2rem 2.5rem;
        }
        .form-label { font-weight: 500; }
        .btn-primary { background: #ea1d25; border: none; }
        .btn-primary:hover { background: #b71c1c; }
        .form-check-label { font-size: 0.95rem; }
        .update-title { color: #ea1d25; font-weight: bold; text-align: center; margin-bottom: 2rem; font-size: 2rem; }
        .form-text { font-size: 0.95rem; }
        .btn-outline-secondary { border: 1px solid #ccc; }
        .form-control { font-size: 1.1rem; padding: 0.75rem 1rem; }
        @media (max-width: 768px) {
            .update-form-container { padding: 1rem; }
        }
    </style>
</head>
<body style="background: #f8f9fa;">
    <jsp:include page="/templates/header.jsp"/>
    <div class="update-form-container">
        <div class="update-title">Cập nhật thông tin</div>
        <form action="${pageContext.request.contextPath}/update-profile" method="post">
            <div class="row mb-3">
                <div class="col">
                    <label class="form-label">Họ và tên</label>
                    <input type="text" class="form-control" name="fullname" value="<%= user.getFullName() %>" required>
                </div>
                
            </div>
            <div class="row mb-3">
                <div class="col">
                    <label class="form-label">Số điện thoại</label>
                    <input type="text" class="form-control" name="phone" value="<%= user.getPhoneNumber() != null ? user.getPhoneNumber() : "" %>">
                </div>
                <div class="col">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control" style="min-width:260px;max-width:100%;" value="<%= user.getEmail() %>" readonly>
                </div>
            </div>
            <div class="form-check mb-3">
                <input class="form-check-input" type="checkbox" name="subscribe" id="subscribe">
                <label class="form-check-label" for="subscribe">
                    Đăng ký nhận tin khuyến mãi từ PhoneStore
                </label>
            </div>
            <div class="form-text mb-3">
                Bằng việc Đăng ký, bạn đã đọc và đồng ý với <a href="#">Điều khoản sử dụng</a> và <a href="#">Chính sách bảo mật của PhoneStore</a>.
            </div>
            <div class="d-flex justify-content-between">
                <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn btn-outline-secondary">&lt; Quay lại đăng nhập</a>
                <button type="submit" class="btn btn-primary">Hoàn tất đăng ký</button>
            </div>
        </form>
    </div>
    <jsp:include page="/templates/footer.jsp"/>
</body>
</html> 
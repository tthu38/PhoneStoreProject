<%-- 
    Document   : registerfb
    Created on : Jul 5, 2025, 1:50:45 PM
    Author     : dangt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.User"%>
<%
    User fbUser = (User) session.getAttribute("fbUser");
    if (fbUser == null) {
        response.sendRedirect("user/register.jsp?error=fb_session");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Bổ sung thông tin tài khoản Facebook</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/register.css">
</head>
<body class="bg-light">
    <div class="container min-vh-100 d-flex align-items-center justify-content-center">
        <div class="bg-white rounded-4 shadow p-4" style="max-width:500px; width:100%;">
            <div class="text-center mb-3">
                <img src="<%= fbUser.getPicture() %>" alt="Avatar" style="width:80px; border-radius:50%;">
                <h4 class="mt-2">Chào mừng, <%= fbUser.getFullName() %>!</h4>
                <div class="text-secondary">Bổ sung thông tin để hoàn tất đăng ký</div>
            </div>
            <form action="${pageContext.request.contextPath}/registerfb" method="post">
                <div class="mb-3">
                    <label class="form-label">Họ và tên</label>
                    <input type="text" class="form-control" name="fullname" value="<%= fbUser.getFullName() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Email</label>
                    <input type="email" class="form-control" name="email" value="<%= fbUser.getEmail() %>" readonly>
                </div>
                <div class="mb-3">
                    <label class="form-label">Số điện thoại <span class="text-danger">*</span></label>
                    <input type="text" class="form-control" name="phone" placeholder="Nhập số điện thoại" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Ngày sinh</label>
                    <input type="date" class="form-control" name="dob">
                </div>
                <div class="mb-3">
                    <label class="form-label">Địa chỉ</label>
                    <input type="text" class="form-control" name="address" placeholder="Nhập địa chỉ">
                </div>
                <input type="hidden" name="oauthId" value="<%= fbUser.getOauthId() %>">
                <input type="hidden" name="oauthProvider" value="facebook">
                <button type="submit" class="btn btn-primary w-100">Hoàn tất đăng ký</button>
            </form>
        </div>
    </div>
</body>
</html>

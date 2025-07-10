<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@page import="model.UserAddress"%>
<%@page import="java.util.List"%>
<%@ page import="service.UserAddressService" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/user/login.jsp");
        return;
    }
    UserAddressService addressService = new UserAddressService();
    List<UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Thông tin cá nhân - Thế Giới Công Nghệ</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css">
</head>
<body style="background: #f8f9fa;">
    <jsp:include page="/templates/header.jsp"/>
    <div class="profile-container">
        <div class="profile-header position-relative">
            <div class="profile-actions position-absolute end-0 top-0 d-flex gap-2">
                <a href="${pageContext.request.contextPath}/user/editprofile.jsp" class="btn btn-outline-primary btn-sm"><i class="fas fa-edit"></i> Chỉnh sửa</a>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger btn-sm"><i class="fas fa-sign-out-alt"></i> Đăng xuất</a>
            </div>
            <% if (user.getPicture() != null && !user.getPicture().isEmpty()) { %>
                <img src="<%= user.getPicture() %>" alt="Avatar" class="profile-avatar">
            <% } else { %>
                <i class="fa-solid fa-user-circle fa-5x profile-avatar" style="color: #EA6D22; background: #eee;"></i>
            <% } %>
            <div class="profile-title">
                <h1 class="profile-name"><%= user.getFullName() != null && !user.getFullName().isEmpty() ? user.getFullName() : "Chưa cập nhật" %></h1>
                <div class="profile-email"><%= user.getEmail() != null && !user.getEmail().isEmpty() ? user.getEmail() : "Chưa cập nhật" %></div>
            </div>
        </div>
        <div class="profile-section">
            <h3><i class="fas fa-info-circle"></i> Thông tin cơ bản</h3>
            <div class="info-grid">
                <div>
                    <div class="info-label">Họ và tên</div>
                    <div class="info-value"><%= user.getFullName() != null && !user.getFullName().isEmpty() ? user.getFullName() : "Chưa cập nhật" %></div>
                </div>
                <div>
                    <div class="info-label">Email</div>
                    <div class="info-value"><%= user.getEmail() != null && !user.getEmail().isEmpty() ? user.getEmail() : "Chưa cập nhật" %></div>
                </div>
                <div>
                    <div class="info-label">Số điện thoại</div>
                    <div class="info-value"><%= user.getPhoneNumber() != null && !user.getPhoneNumber().isEmpty() ? user.getPhoneNumber() : "Chưa cập nhật" %></div>
                </div>
                <div>
                    <div class="info-label">Loại tài khoản</div>
                    <div class="info-value">
                        <% if (user.getRoleID() == 1) { %>
                            <span class="badge bg-danger">Admin</span>
                        <% } else { %>
                            <span class="badge bg-primary">Khách hàng</span>
                        <% } %>
                    </div>
                </div>
                <div>
                    <div class="info-label">Ngày sinh</div>
                    <div class="info-value">
                        <%= user.getDob() != null ? user.getDob().toString() : "Chưa cập nhật" %>
                    </div>
                </div>
            </div>
        </div>
        <div class="profile-section">
            <h3><i class="fas fa-map-marker-alt"></i> Địa chỉ giao hàng</h3>
            <%-- Hiển thị địa chỉ hiện tại --%>
            <% if (addresses != null && !addresses.isEmpty()) { %>
                <% for (UserAddress address : addresses) { %>
                    <div class="info-value mb-2">
                        <div><%= address.getAddress() != null ? address.getAddress() : "" %></div>
                        <% if (address.getIsDefault() != null && address.getIsDefault()) { %>
                            <span class="badge bg-primary">Mặc định</span>
                        <% } %>
                    </div>
                <% } %>
            <% } else { %>
                <div class="info-value">Chưa có địa chỉ giao hàng</div>
            <% } %>
        </div>
    </div>
    <jsp:include page="/templates/footer.jsp"/>
</body>
</html>
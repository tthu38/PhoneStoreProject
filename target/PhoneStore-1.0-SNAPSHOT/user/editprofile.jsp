<%-- 
    Document   : editprofile
    Created on : Jul 5, 2025, 7:57:55 PM
    Author     : dangt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%@ page import="java.util.List" %>
<%@ page import="model.UserAddress" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/user/login.jsp");
        return;
    }
    UserAddress userAddress = (UserAddress) request.getAttribute("userAddress");
    String addressValue = "";
    if (userAddress != null && userAddress.getAddress() != null) {
        addressValue = userAddress.getAddress();
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cập nhật thông tin cá nhân</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/editprofile.css">
</head>
<body style="background: #f8f9fa;">
    <jsp:include page="/templates/header.jsp"/>
    <div class="container mt-5">
        <div class="card mx-auto" style="max-width: 500px;">
            <div class="card-header text-white text-center">
                <h4>Cập nhật thông tin cá nhân</h4>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/users" method="post">
                    <input type="hidden" name="action" value="update-profile">
                    <div class="mb-3">
                        <label class="form-label">Họ và tên</label>
                        <input type="text" class="form-control" name="fullname"
                               value="<%= user.getFullName() != null ? user.getFullName() : "" %>" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Email</label>
                        <input type="email" class="form-control" name="email"
                               value="<%= user.getEmail() != null ? user.getEmail() : "" %>" readonly>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Số điện thoại</label>
                        <input type="text" class="form-control" name="phone"
                               value="<%= user.getPhoneNumber() != null ? user.getPhoneNumber() : "" %>" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Ngày sinh</label>
                        <input type="date" class="form-control" name="dob"
                               value="<%= user.getDob() != null ? user.getDob().toString() : "" %>">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Địa chỉ</label>
                        <input type="text" class="form-control" name="address"
                               value="<%= addressValue %>" required placeholder="Nhập địa chỉ của bạn">
                    </div>
                    <div class="mb-3">
                        <button type="submit" class="btn btn-primary w-100">Cập nhật</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <jsp:include page="/templates/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

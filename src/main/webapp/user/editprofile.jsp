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
    String provinceName = "";
    String districtName = "";
    String wardName = "";
    if (userAddress != null && userAddress.getAddress() != null) {
        addressValue = userAddress.getAddress();
        String[] parts = addressValue.split(",\\s*");
        if (parts.length == 3) {
            wardName = parts[0];
            districtName = parts[1];
            provinceName = parts[2];
        }
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
                               value="<%= user.getPhoneNumber() != null ? user.getPhoneNumber() : "" %>">
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Ngày sinh</label>
                        <input type="date" class="form-control" name="dob"
                               value="<%= user.getDob() != null ? user.getDob().toString() : "" %>">
                    </div>
                    <div class="mb-3 row">
                        <div class="col-md-4 mb-2">
                            <label class="form-label">Tỉnh/Thành phố</label>
                            <select class="form-select" id="province" name="province" data-selected="<%= provinceName %>" required>
                                <option value="">Chọn tỉnh/thành phố</option>
                            </select>
                        </div>
                        <div class="col-md-4 mb-2">
                            <label class="form-label">Quận/Huyện</label>
                            <select class="form-select" id="district" name="district" data-selected="<%= districtName %>" required disabled>
                                <option value="">Chọn quận/huyện</option>
                            </select>
                        </div>
                        <div class="col-md-4 mb-2">
                            <label class="form-label">Phường/Xã</label>
                            <select class="form-select" id="ward" name="ward" data-selected="<%= wardName %>" required disabled>
                                <option value="">Chọn phường/xã</option>
                            </select>
                        </div>
                    </div>
                    <input type="hidden" name="address" id="address" value="<%= addressValue %>">
                    <div class="mb-3">
                        <button type="submit" class="btn btn-primary w-100">Cập nhật</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <jsp:include page="/templates/footer.jsp"/>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/address.js"></script>
</body>
</html>

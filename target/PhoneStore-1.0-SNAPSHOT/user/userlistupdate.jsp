<%-- 
    Document   : userupdate1
    Created on : Jun 19, 2025, 1:24:04 PM
    Author     : dangt
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    model.User user = (model.User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Chỉnh sửa thông tin cá nhân</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/profile.css">
</head>
<body>
<div class="profile-container">
    <h2>Chỉnh sửa thông tin cá nhân</h2>
    <form action="<%=request.getContextPath()%>/update-profile" method="post">
        <div class="profile-info">
            <label>Họ và tên</label>
            <input class="form-control" type="text" name="fullname" value="<%=user.getFullName()%>" required>

            <label>Email</label>
            <input class="form-control" type="email" value="<%=user.getEmail()%>" disabled>

            <label>Số điện thoại</label>
            <input class="form-control" type="text" name="phone" value="<%=user.getPhoneNumber() != null ? user.getPhoneNumber() : ""%>">

            <button type="submit" class="btn btn-success">Lưu thay đổi</button>
            <a href="<%=request.getContextPath()%>/user/profile.jsp" class="btn btn-secondary">Hủy</a>
        </div>
    </form>
</div>
</body>
</html>

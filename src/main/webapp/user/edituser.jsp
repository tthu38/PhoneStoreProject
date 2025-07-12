<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.UserAddress" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    User user = (User) request.getAttribute("user");
    UserAddress address = (UserAddress) request.getAttribute("address");
    String province = (String) request.getAttribute("province");
    String district = (String) request.getAttribute("district");
    String ward = (String) request.getAttribute("ward");
%>

<html>
    <head>
        <title>Chỉnh sửa người dùng</title>
        <script src="${pageContext.request.contextPath}/js/address.js"></script>
    </head>
    <body>
        <h2>Chỉnh sửa người dùng</h2>

        <c:if test="${not empty error}">
            <p style="color:red">${error}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/users?action=edit" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="edit">
            <input type="hidden" name="userID" value="<%= user.getUserID()%>">

            <label>Họ và tên:</label><br>
            <input type="text" name="fullName" value="<%= user.getFullName()%>" required><br><br>

            <label>Email:</label><br>
            <input type="email" name="email" value="<%= user.getEmail()%>" required><br><br>

            <label>Mật khẩu mới (nếu muốn đổi):</label><br>
            <input type="password" name="password" placeholder="Để trống nếu không thay đổi"><br><br>

            <label>Số điện thoại:</label><br>
            <input type="text" name="phoneNumber" pattern="0[0-9]{9,10}"
                   title="Số điện thoại phải bắt đầu bằng 0 và có 10 hoặc 11 chữ số"
                   value="<%= user.getPhoneNumber()%>" required><br><br>

            <label>Ngày sinh:</label><br>
            <input type="date" name="dob" max="<%= java.time.LocalDate.now()%>"
                   value="<%= user.getDob() != null ? user.getDob().toString() : ""%>"><br><br>

            <label>Vai trò:</label><br>
            <select name="roleID">
                <option value="1" <%= user.getRoleID() == 1 ? "selected" : ""%>>Admin</option>
                <option value="2" <%= user.getRoleID() == 2 ? "selected" : ""%>>Người dùng</option>
            </select><br><br>

            <label>Trạng thái:</label><br>
            <select name="isActive">
                <option value="1" <%= user.getIsActive() ? "selected" : ""%>>Hoạt động</option>
                <option value="0" <%= !user.getIsActive() ? "selected" : ""%>>Vô hiệu hóa</option>
            </select><br><br>

            <label>Tỉnh/Thành phố:</label><br>
            <select id="province" data-selected="${province}">
                <option value="">-- Chọn tỉnh --</option>
            </select><br><br>

            <label>Quận/Huyện:</label><br>
            <select id="district" data-selected="${district}">
                <option value="">-- Chọn quận/huyện --</option>
            </select><br><br>

            <label>Phường/Xã:</label><br>
            <select id="ward" data-selected="${ward}">
                <option value="">-- Chọn phường/xã --</option>
            </select><br><br>

            <input type="hidden" id="address" name="address"
                   value="<%= address != null ? address.getAddress() : ""%>">

            <label>Ảnh đại diện hiện tại:</label><br>
            <img src="<%= user.getPicture()%>" width="100"><br><br>

            <label>Thay ảnh đại diện mới:</label><br>
            <input type="file" name="picture"><br><br>

            <button type="submit">Cập nhật</button>
        </form>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                if (typeof initProvinceDistrictWard === "function") {
                    initProvinceDistrictWard("province", "district", "ward");
                }
            });
        </script>
    </body>
</html>

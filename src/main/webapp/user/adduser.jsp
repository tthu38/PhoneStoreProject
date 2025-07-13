<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Thêm người dùng</title>
        <script src="${pageContext.request.contextPath}/js/address.js"></script>
    </head>
    <body>
        <h2>Thêm người dùng</h2>

        <c:if test="${not empty error}">
            <p style="color:red">${error}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/users?action=add" method="post" enctype="multipart/form-data">
            <input type="hidden" name="action" value="add" />

            <label>Họ và tên:</label><br>
            <input type="text" name="fullName" value="${fullName}" required><br><br>

            <label>Email:</label><br>
            <input type="email" name="email" value="${email}" required><br><br>

            <label>Mật khẩu:</label><br>
            <input type="password" name="password" required><br><br>

            <label>Số điện thoại:</label><br>
            <input type="text" name="phoneNumber" value="${phoneNumber}"
                   pattern="0[0-9]{9}" title="Số điện thoại phải bắt đầu bằng 0 và đủ 10 chữ số" required><br><br>

            <label>Ngày sinh:</label><br>
            <input type="date" name="dob" max="<%= java.time.LocalDate.now()%>" value="${dob}"><br><br>

            <label>Vai trò:</label><br>
            <select name="roleID">
                <option value="1">Admin</option>
                <option value="2" selected>Người dùng</option>
            </select><br><br>

            <label>Trạng thái:</label><br>
            <select name="isActive">
                <option value="1" selected>Hoạt động</option>
                <option value="0">Vô hiệu hóa</option>
            </select><br><br>

            <label>Tỉnh/Thành phố:</label><br>
            <select id="province" name="province" required>
                <option value="">-- Chọn tỉnh --</option>
            </select><br><br>

            <label>Quận/Huyện:</label><br>
            <select id="district" name="district" required>
                <option value="">-- Chọn quận/huyện --</option>
            </select><br><br>

            <label>Phường/Xã:</label><br>
            <select id="ward" name="ward" required>
                <option value="">-- Chọn phường/xã --</option>
            </select><br><br>

            <input type="hidden" id="address" name="address" value="${address}">

            <label>Ảnh đại diện:</label><br>
            <input type="file" name="picture"><br><br>

            <button type="submit">Thêm người dùng</button>
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

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
    <head>
        <title>Thêm người dùng</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/adduser.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
        <script src="${pageContext.request.contextPath}/js/address.js"></script>            
    </head>

    <body>
        <main class="add-user-page">
            <div class="container">
                <a href="${pageContext.request.contextPath}/admin?menu=customers" class="back-link">
                    <i class="fas fa-arrow-left"></i> Trở lại danh sách người dùng
                </a>

                <h2 class="title"><i class="fas fa-user-plus"></i> Thêm người dùng</h2>

                <c:if test="${not empty error}">
                    <p class="error-text">${error}</p>
                </c:if>

                <form action="${pageContext.request.contextPath}/users?action=add" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="add"/>

                    <div class="form-row">
                        <div class="form-group">
                            <label><i class="fas fa-user"></i> Họ và tên:</label>
                            <input type="text" name="fullName" value="${fullName}" required>
                        </div>

                        <div class="form-group">
                            <label><i class="fas fa-envelope"></i> Email:</label>
                            <input type="email" name="email" value="${email}" required>
                        </div>

                        <div class="form-group">
                            <label><i class="fas fa-lock"></i> Mật khẩu:</label>
                            <input type="password" name="password" required>
                        </div>

                        <div class="form-group">
                            <label><i class="fas fa-phone"></i> Số điện thoại:</label>
                            <input type="text" name="phoneNumber" value="${phoneNumber}"
                                   pattern="0[0-9]{9}" title="Số điện thoại phải bắt đầu bằng 0 và đủ 10 chữ số" required>
                        </div>

                        <div class="form-group">
                            <label><i class="fas fa-calendar"></i> Ngày sinh:</label>
                            <input type="date" name="dob" max="<%= java.time.LocalDate.now()%>" value="${dob}">
                        </div>

                        <div class="form-group">
                            <label><i class="fas fa-user-tag"></i> Vai trò:</label>
                            <select name="roleID">
                                <option value="1">Admin</option>
                                <option value="2" selected>Người dùng</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label><i class="fas fa-toggle-on"></i> Trạng thái:</label>
                            <select name="isActive">
                                <option value="1" selected>Hoạt động</option>
                                <option value="0">Vô hiệu hóa</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label><i class="fas fa-map-marker-alt"></i> Tỉnh/Thành phố:</label>
                            <select id="province" name="province" required>
                                <option value="">-- Chọn tỉnh --</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label><i class="fas fa-map"></i> Quận/Huyện:</label>
                            <select id="district" name="district" required>
                                <option value="">-- Chọn quận/huyện --</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label><i class="fas fa-location-arrow"></i> Phường/Xã:</label>
                            <select id="ward" name="ward" required>
                                <option value="">-- Chọn phường/xã --</option>
                            </select>
                        </div>

                        <input type="hidden" id="address" name="address" value="${address}">

                        <div class="form-group">
                            <label><i class="fas fa-image"></i> Ảnh đại diện:</label>
                            <input type="file" name="picture">
                        </div>
                    </div>

                    <button type="submit" class="submit-btn">
                        <i class="fas fa-user-plus"></i> Thêm người dùng
                    </button>
                </form>
            </div>
        </main>

        <script>
            document.addEventListener("DOMContentLoaded", function () {
                if (typeof initProvinceDistrictWard === "function") {
                    initProvinceDistrictWard("province", "district", "ward");
                }
            });
        </script>
    </body>
</html>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.UserAddress" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    User user = (User) request.getAttribute("user");
    if (user == null) {
%>
    <div class="alert alert-danger">Không tìm thấy thông tin người dùng.</div>
<%
    } else {
        UserAddress address = (UserAddress) request.getAttribute("address");
        String province = (String) request.getAttribute("province");
        String district = (String) request.getAttribute("district");
        String ward = (String) request.getAttribute("ward");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Chỉnh sửa người dùng</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/edituser.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css" />
        <script src="${pageContext.request.contextPath}/js/address.js"></script>
    </head>
    <body>
        <main class="edit-user-page">
            <div class="container">
                <div class="card">
                    <a href="${pageContext.request.contextPath}/admin?menu=customers" class="back-link">
                        <i class="fas fa-arrow-left"></i> Trở lại danh sách người dùng
                    </a>

                    <h2 class="title"><i class="fas fa-user-edit"></i> Chỉnh sửa người dùng</h2>

                    <c:if test="${not empty error}">
                        <p class="error-text">${error}</p>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/users?action=edit" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="edit">
                        <input type="hidden" name="userID" value="<%= user.getUserID()%>">

                        <div class="form-row">
                            <div class="form-group">
                                <label><i class="fas fa-user text-danger"></i> Họ và tên:</label>
                                <input type="text" name="fullName" value="<%= user.getFullName()%>" required>
                            </div>

                            <div class="form-group">
                                <label><i class="fas fa-envelope text-danger"></i> Email:</label>
                                <input type="email" name="email" value="<%= user.getEmail()%>" required>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label><i class="fas fa-lock text-danger"></i> Mật khẩu mới:</label>
                                <input type="password" name="password" placeholder="Để trống nếu không thay đổi">
                            </div>

                            <div class="form-group">
                                <label><i class="fas fa-phone text-danger"></i> Số điện thoại:</label>
                                <input type="text" name="phoneNumber" value="<%= user.getPhoneNumber()%>"
                                       pattern="0[0-9]{9,10}" title="Bắt đầu bằng 0, có 10-11 số" required>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label><i class="fas fa-calendar-alt text-danger"></i> Ngày sinh:</label>
                                <input type="date" name="dob" value="<%= user.getDob() != null ? user.getDob().toString() : ""%>" max="<%= java.time.LocalDate.now()%>">
                            </div>

                            <div class="form-group">
                                <label><i class="fas fa-user-tag text-danger"></i> Vai trò:</label>
                                <select name="roleID">
                                    <option value="1" <%= user.getRoleID() == 1 ? "selected" : ""%>>Admin</option>
                                    <option value="2" <%= user.getRoleID() == 2 ? "selected" : ""%>>Người dùng</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label><i class="fas fa-toggle-on text-danger"></i> Trạng thái:</label>
                                <select name="isActive">
                                    <option value="1" <%= user.getIsActive() ? "selected" : ""%>>Hoạt động</option>
                                    <option value="0" <%= !user.getIsActive() ? "selected" : ""%>>Vô hiệu hóa</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label><i class="fas fa-map-marker-alt text-danger"></i> Tỉnh/Thành phố:</label>
                                <select id="province" name="province" data-selected="${province}" required>
                                    <option value="">-- Chọn tỉnh --</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-row">
                            <div class="form-group">
                                <label><i class="fas fa-map text-danger"></i> Quận/Huyện:</label>
                                <select id="district" name="district" data-selected="${district}" required>
                                    <option value="">-- Chọn quận/huyện --</option>
                                </select>
                            </div>

                            <div class="form-group">
                                <label><i class="fas fa-street-view text-danger"></i> Phường/Xã:</label>
                                <select id="ward" name="ward" data-selected="${ward}" required>
                                    <option value="">-- Chọn phường/xã --</option>
                                </select>
                            </div>
                        </div>

                        <input type="hidden" id="address" name="address" value="<%= address != null ? address.getAddress() : ""%>">

                        <div class="form-row">
                            <div class="form-group">
                                <label><i class="fas fa-image text-danger"></i> Ảnh đại diện hiện tại:</label><br>
                                <img class="user-avatar" src="<%= user.getPicture()%>" alt="Avatar" width="100">
                            </div>

                            <div class="form-group">
                                <label><i class="fas fa-upload text-danger"></i> Thay ảnh đại diện mới:</label>
                                <input type="file" name="picture">
                            </div>
                        </div>

                        <button type="submit" class="submit-btn">
                            <i class="fas fa-save"></i> Cập nhật người dùng
                        </button>
                    </form>
                </div>
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
<%
    }
%>

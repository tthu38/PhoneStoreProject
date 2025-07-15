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
                    <input type="text" class="form-control" name="phone"
                           pattern="0[0-9]{9,10}" title="Bắt đầu bằng 0, có 10-11 số" required>
                </div>
                <div class="mb-3">
                    <label class="form-label">Ngày sinh</label>
                    <input type="date" class="form-control" name="dob" max="<%= java.time.LocalDate.now() %>">
                </div>
                <!-- Địa chỉ API -->
                <div class="row">
                    <div class="col-md-4 mb-2">
                        <label class="form-label">Tỉnh/Thành phố</label>
                        <select class="form-select" id="province" name="province" required data-selected="">
                            <option value="">Chọn tỉnh/thành phố</option>
                        </select>
                    </div>
                    <div class="col-md-4 mb-2">
                        <label class="form-label">Quận/Huyện</label>
                        <select class="form-select" id="district" name="district" required data-selected="" disabled>
                            <option value="">Chọn quận/huyện</option>
                        </select>
                    </div>
                    <div class="col-md-4 mb-2">
                        <label class="form-label">Phường/Xã</label>
                        <select class="form-select" id="ward" name="ward" required data-selected="" disabled>
                            <option value="">Chọn phường/xã</option>
                        </select>
                    </div>
                </div>
                <input type="hidden" name="address" id="address">
                <input type="hidden" name="oauthId" value="<%= fbUser.getOauthId() %>">
                <input type="hidden" name="oauthProvider" value="facebook">
                <button type="submit" class="btn btn-primary w-100">Hoàn tất đăng ký</button>
            </form>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/address.js"></script>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            if (typeof initProvinceDistrictWard === "function") {
                initProvinceDistrictWard("province", "district", "ward");
            }
        });
    </script>
</body>
</html>

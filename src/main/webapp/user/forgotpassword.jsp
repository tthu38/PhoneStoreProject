<%-- 
    Document   : forgotpassword
    Created on : Jul 10, 2025, 4:25:57 PM
    Author     : dangt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String step = request.getParameter("step");
    String email = request.getParameter("email") != null ? request.getParameter("email") : "";
    String error = (String) request.getAttribute("error");
    String message = (String) request.getAttribute("message");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Quên mật khẩu</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/forgotpassword.css">
</head>
<body>
    <div class="fpw-container">
        <div class="fpw-title">Quên mật khẩu</div>
        <div class="fpw-desc">Nhập email đã đăng ký để nhận mã xác nhận và đặt lại mật khẩu mới.</div>
        <form action="<%=request.getContextPath()%>/login" method="post" class="fpw-form">
            <% if (step == null || "email".equals(step)) { %>
                <input type="hidden" name="action" value="forgot-password-send-otp">
                <input type="email" class="fpw-input" name="email" placeholder="Nhập email của bạn" required autocomplete="off" value="<%=email%>">
                <button type="submit" class="fpw-btn">Gửi mã xác nhận</button>
            <% } else { %>
                <input type="hidden" name="action" value="forgot-password-reset">
                <input type="email" class="fpw-input" name="email" value="<%=email%>" readonly>
                <input type="text" class="fpw-input" name="otp" placeholder="Nhập mã xác nhận" required autocomplete="off">
                <input type="password" class="fpw-input" name="newPassword" placeholder="Nhập mật khẩu mới" required>
                <button type="submit" class="fpw-btn">Đổi mật khẩu</button>
            <% } %>
        </form>
        <% if (error != null) { %>
            <div class="fpw-error"><%=error%></div>
        <% } %>
        <% if (message != null) { %>
            <div class="fpw-success"><%=message%></div>
        <% } %>
        <div class="fpw-back">
            <a href="<%=request.getContextPath()%>/user/login.jsp">Quay lại đăng nhập</a>
        </div>
    </div>
</body>
</html>

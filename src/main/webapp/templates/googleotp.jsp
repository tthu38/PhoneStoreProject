<%-- 
    Document   : googleotp
    Created on : Jul 10, 2025, 3:32:36 PM
    Author     : dangt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Xác nhận email Google</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/googleotp.css">
</head>
<body>
    <div class="otp-container">
        <div class="otp-title">Xác nhận email Google</div>
        <div class="otp-desc">Nhập mã xác nhận đã gửi về email Google của bạn để tiếp tục.</div>
        <form action="${pageContext.request.contextPath}/register" method="post" class="otp-form">
            <input type="hidden" name="action" value="verify-otp">
            <input type="text" class="otp-input" name="otp" placeholder="Nhập mã OTP" required autocomplete="off">
            <button type="submit" class="otp-btn">Xác nhận</button>
        </form>
        <c:if test="${not empty error}">
            <div class="otp-error">${error}</div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="otp-success">${message}</div>
        </c:if>
        <form action="${pageContext.request.contextPath}/mails" method="post" class="otp-resend-form">
            <input type="hidden" name="action" value="reset_otp">
            <input type="hidden" name="email" value="${sessionScope.user.email}">
            <button type="submit" class="otp-resend">Gửi lại mã xác nhận</button>
        </form>
    </div>
</body>
</html>

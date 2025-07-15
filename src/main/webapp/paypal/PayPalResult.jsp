<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Kết quả thanh toán PayPal</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <style>
            .payment-result-container {
                max-width: 800px;
                margin: 50px auto;
                padding: 20px;
                background: #f8f9fa;
                min-height: 100vh;
            }
            .result-card {
                background: white;
                border-radius: 20px;
                padding: 40px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                text-align: center;
                margin-bottom: 30px;
            }
            .result-icon {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 20px;
                font-size: 40px;
            }
            .result-icon.success {
                background: linear-gradient(135deg, #0070ba, #003087);
                color: white;
            }
            .result-icon.failure {
                background: linear-gradient(135deg, #dc3545, #fd7e14);
                color: white;
            }
            .result-title {
                font-size: 28px;
                font-weight: bold;
                margin-bottom: 10px;
            }
            .result-message {
                font-size: 16px;
                color: #666;
                margin-bottom: 30px;
            }
            .transaction-details {
                background: #f8f9fa;
                border-radius: 15px;
                padding: 25px;
                margin: 30px 0;
                text-align: left;
            }
            .detail-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 12px 0;
                border-bottom: 1px solid #e9ecef;
            }
            .detail-item:last-child {
                border-bottom: none;
            }
            .detail-label {
                font-weight: 500;
                color: #495057;
            }
            .detail-value {
                font-weight: 600;
                color: #212529;
            }
            .action-buttons {
                display: flex;
                gap: 15px;
                justify-content: center;
                margin-top: 30px;
            }
            .btn-primary {
                background: linear-gradient(135deg, #0070ba 0%, #003087 100%);
                border: none;
                padding: 12px 30px;
                border-radius: 10px;
                font-weight: 500;
                transition: all 0.3s ease;
            }
            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(0,112,186,0.4);
            }
            .btn-secondary {
                background: #6c757d;
                border: none;
                padding: 12px 30px;
                border-radius: 10px;
                font-weight: 500;
                transition: all 0.3s ease;
            }
            .btn-secondary:hover {
                background: #5a6268;
                transform: translateY(-2px);
            }
            .countdown {
                background: #e3f2fd;
                border-radius: 10px;
                padding: 15px;
                margin: 20px 0;
                text-align: center;
                color: #1976d2;
                font-weight: 500;
            }
            @media (max-width: 768px) {
                .payment-result-container { margin: 20px auto; padding: 10px; }
                .result-card { padding: 30px 20px; }
                .action-buttons { flex-direction: column; }
                .detail-item { flex-direction: column; align-items: flex-start; gap: 5px; }
            }
        </style>
    </head>
    <body>
        <div class="payment-result-container">
            <div class="result-card">
                <c:choose>
                    <c:when test="${transResult == true}">
                        <div class="result-icon success">
                            <i class="fab fa-paypal"></i>
                        </div>
                        <h1 class="result-title text-success">Thanh toán PayPal thành công!</h1>
                        <p class="result-message">
                            Đơn hàng của bạn đã được xác nhận và đang được xử lý.
                            Email xác nhận đã được gửi đến địa chỉ email của bạn.
                        </p>
                    </c:when>
                    <c:otherwise>
                        <div class="result-icon failure">
                            <i class="fas fa-times"></i>
                        </div>
                        <h1 class="result-title text-danger">Thanh toán PayPal thất bại!</h1>
                        <p class="result-message">
                            Có lỗi xảy ra trong quá trình thanh toán hoặc bạn đã hủy giao dịch.
                        </p>
                    </c:otherwise>
                </c:choose>
                <div class="transaction-details">
                    <h5 class="mb-3">
                        <i class="fas fa-info-circle"></i> Chi tiết giao dịch
                    </h5>
                    <c:if test="${not empty orderId}">
                        <div class="detail-item">
                            <span class="detail-label">Mã đơn hàng:</span>
                            <span class="detail-value">#${orderId}</span>
                        </div>
                    </c:if>
                    <div class="detail-item">
                        <span class="detail-label">Thời gian:</span>
                        <span class="detail-value">
                            <%= new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date()) %>
                        </span>
                    </div>
                    <div class="detail-item">
                        <span class="detail-label">Phương thức:</span>
                        <span class="detail-value">
                            <i class="fab fa-paypal"></i> PayPal
                        </span>
                    </div>
                    <c:if test="${not empty message}">
                        <div class="detail-item">
                            <span class="detail-label">Thông báo:</span>
                            <span class="detail-value">${message}</span>
                        </div>
                    </c:if>
                </div>
                <div class="action-buttons">
                    <c:choose>
                        <c:when test="${transResult == true}">
                            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                                <i class="fas fa-home"></i> Về trang chủ
                            </a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/cart/confirm.jsp" class="btn btn-primary">
                                <i class="fas fa-redo"></i> Thử lại
                            </a>
                            <a href="${pageContext.request.contextPath}/cart/cart.jsp" class="btn btn-secondary">
                                <i class="fas fa-shopping-cart"></i> Giỏ hàng
                            </a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="countdown" id="countdown">
                    <i class="fas fa-clock"></i>
                    Tự động chuyển hướng sau <span id="timer">10</span> giây
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            let timeLeft = 10;
            const timerElement = document.getElementById('timer');
            const countdown = setInterval(function() {
                timeLeft--;
                timerElement.textContent = timeLeft;
                if (timeLeft <= 0) {
                    clearInterval(countdown);
                    var isSuccess = document.querySelector('.result-icon.success') !== null;
                    if (isSuccess) {
                        window.location.href = '${pageContext.request.contextPath}/';
                    } else {
                        window.location.href = '${pageContext.request.contextPath}/cart/confirm.jsp';
                    }
                }
            }, 1000);
        </script>
    </body>
</html> 
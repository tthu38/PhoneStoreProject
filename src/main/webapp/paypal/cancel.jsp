<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thanh toán PayPal bị hủy - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <style>
            .payment-cancel-container {
                max-width: 800px;
                margin: 50px auto;
                padding: 20px;
                background: #f8f9fa;
                min-height: 100vh;
            }
            .cancel-card {
                background: white;
                border-radius: 20px;
                padding: 40px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                text-align: center;
                margin-bottom: 30px;
            }
            .cancel-icon {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 20px;
                font-size: 40px;
                background: linear-gradient(135deg, #dc3545, #fd7e14);
                color: white;
            }
            .cancel-title {
                font-size: 28px;
                font-weight: bold;
                margin-bottom: 10px;
                color: #dc3545;
            }
            .cancel-message {
                font-size: 16px;
                color: #666;
                margin-bottom: 30px;
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
                .payment-cancel-container { margin: 20px auto; padding: 10px; }
                .cancel-card { padding: 30px 20px; }
                .action-buttons { flex-direction: column; }
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp" />

        <div class="payment-cancel-container">
            <div class="cancel-card">
                <div class="cancel-icon">
                    <i class="fas fa-times"></i>
                </div>
                <h1 class="cancel-title">Thanh toán PayPal bị hủy</h1>
                <p class="cancel-message">
                    Bạn đã hủy giao dịch thanh toán PayPal. Đơn hàng của bạn chưa được xử lý.
                    Bạn có thể thử lại hoặc chọn phương thức thanh toán khác.
                </p>

                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/cart/confirm.jsp" class="btn btn-primary">
                        <i class="fas fa-redo"></i> Thử lại
                    </a>
                    <a href="${pageContext.request.contextPath}/cart/cart.jsp" class="btn btn-secondary">
                        <i class="fas fa-shopping-cart"></i> Giỏ hàng
                    </a>
                </div>

                <div class="countdown" id="countdown">
                    <i class="fas fa-clock"></i>
                    Tự động chuyển hướng sau <span id="timer">10</span> giây
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Auto redirect countdown
            let timeLeft = 10;
            const timerElement = document.getElementById('timer');
            
            const countdown = setInterval(function() {
                timeLeft--;
                timerElement.textContent = timeLeft;
                
                if (timeLeft <= 0) {
                    clearInterval(countdown);
                    window.location.href = '${pageContext.request.contextPath}/cart/confirm.jsp';
                }
            }, 1000);
            
            // Add cancel animation
            setTimeout(() => {
                const cancelIcon = document.querySelector('.cancel-icon');
                if (cancelIcon) {
                    cancelIcon.style.transform = 'scale(1.1)';
                    setTimeout(() => {
                        cancelIcon.style.transform = 'scale(1)';
                    }, 200);
                }
            }, 500);
        </script>
    </body>
</html> 
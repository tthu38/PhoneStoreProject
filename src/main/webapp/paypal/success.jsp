<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Thanh toán PayPal thành công - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
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

            .loading-spinner {
                display: inline-block;
                width: 20px;
                height: 20px;
                border: 3px solid #f3f3f3;
                border-top: 3px solid #0070ba;
                border-radius: 50%;
                animation: spin 1s linear infinite;
                margin-right: 10px;
            }

            @keyframes spin {
                0% { transform: rotate(0deg); }
                100% { transform: rotate(360deg); }
            }

            @media (max-width: 768px) {
                .payment-result-container {
                    margin: 20px auto;
                    padding: 10px;
                }

                .result-card {
                    padding: 30px 20px;
                }

                .action-buttons {
                    flex-direction: column;
                }

                .detail-item {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 5px;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp" />

        <div class="payment-result-container">
            <div class="result-card">
                <!-- Success Result -->
                <div class="result-icon success">
                    <i class="fab fa-paypal"></i>
                </div>
                <h1 class="result-title text-success">Thanh toán PayPal thành công!</h1>
                <p class="result-message">
                    Đơn hàng của bạn đã được xác nhận và đang được xử lý.
                    Email xác nhận đã được gửi đến địa chỉ email của bạn.
                </p>

                <!-- Transaction Details -->
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
                    
                    <c:if test="${not empty amountVND}">
                        <div class="detail-item">
                            <span class="detail-label">Tổng tiền (VND):</span>
                            <span class="detail-value">
                                <fmt:formatNumber value="${amountVND}" type="currency" currencySymbol="₫" />
                            </span>
                        </div>
                    </c:if>
                    
                    <c:if test="${not empty amountUSD}">
                        <div class="detail-item">
                            <span class="detail-label">Tổng tiền (USD):</span>
                            <span class="detail-value">
                                $${amountUSD}
                            </span>
                        </div>
                    </c:if>
                    
                    <div class="detail-item">
                        <span class="detail-label">Thời gian:</span>
                        <span class="detail-value">
                            <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss" />
                        </span>
                    </div>
                    
                    <div class="detail-item">
                        <span class="detail-label">Phương thức:</span>
                        <span class="detail-value">
                            <i class="fab fa-paypal"></i> PayPal
                        </span>
                    </div>
                    
                    <c:if test="${not empty itemCount}">
                        <div class="detail-item">
                            <span class="detail-label">Số sản phẩm:</span>
                            <span class="detail-value">${itemCount} sản phẩm</span>
                        </div>
                    </c:if>
                </div>

                <!-- Action Buttons -->
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">
                        <i class="fas fa-home"></i> Về trang chủ
                    </a>
                </div>

                <!-- Auto Redirect Countdown -->
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
                    window.location.href = '${pageContext.request.contextPath}/';
                }
            }, 1000);
            
            // Add loading effect to buttons
            document.querySelectorAll('.btn').forEach(button => {
                button.addEventListener('click', function() {
                    const icon = this.querySelector('i');
                    const originalIcon = icon.className;
                    
                    icon.className = 'fas fa-spinner fa-spin';
                    this.disabled = true;
                    
                    // Re-enable after a short delay (in case of navigation)
                    setTimeout(() => {
                        icon.className = originalIcon;
                        this.disabled = false;
                    }, 2000);
                });
            });
            
            // Add success animation
            setTimeout(() => {
                const resultIcon = document.querySelector('.result-icon.success');
                if (resultIcon) {
                    resultIcon.style.transform = 'scale(1.1)';
                    setTimeout(() => {
                        resultIcon.style.transform = 'scale(1)';
                    }, 200);
                }
            }, 500);
        </script>
    </body>
</html> 
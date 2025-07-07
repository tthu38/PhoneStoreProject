<%@page import="model.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // Get data from session/request attributes instead of generating
    String orderId = (String) request.getAttribute("orderId");
    String total = (String) request.getAttribute("total");
    String orderDate = (String) request.getAttribute("orderDate");
    Boolean success = (Boolean) request.getAttribute("success");
    User user = (User) session.getAttribute("user");

    // Set default values if not provided
    if (orderId == null) {
        orderId = "N/A";
    }
    if (total == null) {
        total = "0";
    }
    if (orderDate == null)
        orderDate = "N/A";
    if (success == null) {
        success = false;
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= success ? "Đặt hàng thành công" : "Đặt hàng thất bại" %> - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <style>
            .result-container {
                max-width: 800px;
                margin: 0 auto;
                padding: 40px 20px;
                text-align: center;
            }

            .result-card {
                background: white;
                border-radius: 20px;
                padding: 40px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                margin-bottom: 30px;
            }

            .result-icon {
                width: 100px;
                height: 100px;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 30px;
                color: white;
                font-size: 40px;
                animation: pulse 2s infinite;
            }

            .result-icon.success {
                background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
            }

            .result-icon.fail {
                background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
            }

            @keyframes pulse {
                0% {
                    transform: scale(1);
                }
                50% {
                    transform: scale(1.05);
                }
                100% {
                    transform: scale(1);
                }
            }

            .result-title {
                font-size: 28px;
                font-weight: 600;
                margin-bottom: 15px;
            }

            .result-title.success {
                color: #28a745;
            }

            .result-title.fail {
                color: #dc3545;
            }

            .result-message {
                color: #666;
                font-size: 16px;
                margin-bottom: 30px;
                line-height: 1.6;
            }

            .order-details {
                background: #f8f9fa;
                border-radius: 15px;
                padding: 25px;
                margin: 30px 0;
                text-align: left;
            }

            .order-details h5 {
                color: #333;
                margin-bottom: 20px;
                font-weight: 600;
            }

            .detail-item {
                display: flex;
                justify-content: space-between;
                margin-bottom: 10px;
                padding-bottom: 10px;
                border-bottom: 1px solid #eee;
            }

            .detail-item:last-child {
                border-bottom: none;
                margin-bottom: 0;
                padding-bottom: 0;
                font-weight: 600;
                font-size: 18px;
            }

            .detail-item:last-child.success {
                color: #28a745;
            }

            .detail-item:last-child.fail {
                color: #dc3545;
            }

            .action-buttons {
                display: flex;
                gap: 15px;
                justify-content: center;
                flex-wrap: wrap;
            }

            .btn-primary {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                border: none;
                padding: 12px 25px;
                border-radius: 8px;
                color: white;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: 8px;
                transition: all 0.3s ease;
                font-weight: 500;
            }

            .btn-primary:hover {
                transform: translateY(-2px);
                color: white;
                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            }

            .btn-outline-secondary {
                background: transparent;
                border: 2px solid #6c757d;
                padding: 12px 25px;
                border-radius: 8px;
                color: #6c757d;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: 8px;
                transition: all 0.3s ease;
                font-weight: 500;
            }

            .btn-outline-secondary:hover {
                background: #6c757d;
                color: white;
                transform: translateY(-2px);
            }

            .btn-danger {
                background: linear-gradient(135deg, #dc3545 0%, #c82333 100%);
                border: none;
                padding: 12px 25px;
                border-radius: 8px;
                color: white;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                gap: 8px;
                transition: all 0.3s ease;
                font-weight: 500;
            }

            .btn-danger:hover {
                transform: translateY(-2px);
                color: white;
                box-shadow: 0 5px 15px rgba(220, 53, 69, 0.4);
            }

            .info-box {
                border-radius: 10px;
                padding: 20px;
                margin: 20px 0;
            }

            .info-box.success {
                background: #e8f5e8;
                border: 1px solid #28a745;
            }

            .info-box.fail {
                background: #ffe6e6;
                border: 1px solid #dc3545;
            }

            .info-box h6 {
                margin-bottom: 10px;
                font-weight: 600;
            }

            .info-box.success h6 {
                color: #28a745;
            }

            .info-box.fail h6 {
                color: #dc3545;
            }

            .info-box p {
                margin-bottom: 0;
                font-size: 14px;
            }

            .info-box.success p {
                color: #155724;
            }

            .info-box.fail p {
                color: #721c24;
            }

            @media (max-width: 768px) {
                .result-container {
                    padding: 20px 10px;
                }

                .result-card {
                    padding: 30px 20px;
                }

                .result-title {
                    font-size: 24px;
                }

                .action-buttons {
                    flex-direction: column;
                    align-items: center;
                }

                .btn-primary, .btn-outline-secondary, .btn-danger {
                    width: 100%;
                    max-width: 300px;
                    justify-content: center;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp" />

        <div class="result-container">
            <div class="result-card">
                <div class="result-icon <%= success ? "success" : "fail" %>">
                    <i class="fas fa-<%= success ? "check" : "times" %>"></i>
                </div>

                <h1 class="result-title <%= success ? "success" : "fail" %>">
                    <%= success ? "Đặt hàng thành công!" : "Đặt hàng thất bại!" %>
                </h1>
                <p class="result-message">
                    <%= success 
                        ? "Cảm ơn bạn đã đặt hàng tại Thế Giới Công Nghệ. Chúng tôi đã nhận được đơn hàng của bạn và sẽ xử lý trong thời gian sớm nhất."
                        : "Rất tiếc, đơn hàng của bạn chưa được xử lý thành công. Vui lòng thử lại hoặc liên hệ hỗ trợ để được giúp đỡ."
                    %>
                </p>

                <div class="order-details">
                    <h5><i class="fas fa-receipt"></i> Thông tin đơn hàng</h5>

                    <div class="detail-item">
                        <span>Mã đơn hàng:</span>
                        <span>#<%= orderId%></span>
                    </div>
                    <div class="detail-item">
                        <span>Ngày đặt hàng:</span>
                        <span><%= orderDate%></span>
                    </div>
                    <div class="detail-item">
                        <span>Phương thức thanh toán:</span>
                        <span>Thanh toán khi nhận hàng (COD)</span>
                    </div>
                    <div class="detail-item <%= success ? "success" : "fail" %>">
                        <span>Tổng tiền:</span>
                        <span><%= total%> ₫</span>
                    </div>
                </div>

                <div class="info-box <%= success ? "success" : "fail" %>">
                    <h6><i class="fas fa-info-circle"></i> 
                        <%= success ? "Thông tin quan trọng" : "Hướng dẫn hỗ trợ" %>
                    </h6>
                    <p>
                        <%= success 
                            ? "• Chúng tôi sẽ liên hệ với bạn trong vòng 24 giờ để xác nhận đơn hàng<br>• Thời gian giao hàng dự kiến: 2-5 ngày làm việc<br>• Bạn có thể theo dõi đơn hàng qua email hoặc số điện thoại đã đăng ký"
                            : "• Vui lòng kiểm tra lại thông tin đơn hàng<br>• Liên hệ hotline 0775660817 để được hỗ trợ<br>• Hoặc thử đặt hàng lại sau vài phút"
                        %>
                    </p>
                </div>

                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/" class="btn-primary">
                        <i class="fas fa-home"></i> Về trang chủ
                    </a>
                    <% if (success) { %>
                        <a href="${pageContext.request.contextPath}/user/profile.jsp" class="btn-outline-secondary">
                            <i class="fas fa-user"></i> Xem đơn hàng
                        </a>
                    <% } else { %>
                        <a href="${pageContext.request.contextPath}/cart" class="btn-danger">
                            <i class="fas fa-shopping-cart"></i> Thử lại
                        </a>
                    <% } %>
                </div>
            </div>

            <% if (success) { %>
                <!-- Khuyến mãi chỉ hiển thị khi thành công -->
                <div class="result-card">
                    <h4><i class="fas fa-gift"></i> Khuyến mãi đặc biệt</h4>
                    <p class="result-message">
                        Để cảm ơn bạn đã mua hàng, chúng tôi tặng bạn mã giảm giá <strong>WELCOME10</strong> 
                        giảm 10% cho đơn hàng tiếp theo!
                    </p>
                    <div class="info-box success">
                        <h6><i class="fas fa-tag"></i> Mã giảm giá: WELCOME10</h6>
                        <p>Giảm 10% cho đơn hàng từ 500,000₫ trở lên. Hạn sử dụng: 30 ngày</p>
                    </div>
                </div>
            <% } %>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Clear cart after successful order
            document.addEventListener('DOMContentLoaded', function () {
                <% if (success) { %>
                    // Clear cart from session only if success
                    fetch('${pageContext.request.contextPath}/cart?action=clear', {
                        method: 'POST'
                    }).catch(error => {
                        console.log('Cart cleared');
                    });
                    
                    // Update cart badge
                    const badge = document.getElementById('cartBadge');
                    if (badge) {
                        badge.textContent = '0';
                        badge.style.display = 'none';
                    }
                <% } %>
                
                // Auto redirect countdown
                let timeLeft = 10;
                const countdownElement = document.createElement('div');
                countdownElement.style.position = 'fixed';
                countdownElement.style.top = '20px';
                countdownElement.style.right = '20px';
                countdownElement.style.background = '<%= success ? "rgba(40, 167, 69, 0.9)" : "rgba(220, 53, 69, 0.9)" %>';
                countdownElement.style.color = 'white';
                countdownElement.style.padding = '10px 15px';
                countdownElement.style.borderRadius = '25px';
                countdownElement.style.fontSize = '14px';
                countdownElement.style.fontWeight = '500';
                countdownElement.style.zIndex = '1000';
                countdownElement.style.boxShadow = '0 4px 12px rgba(0,0,0,0.15)';
                countdownElement.innerHTML = '⏰ Tự động chuyển về trang chủ sau <span id="timer">' + timeLeft + '</span> giây';
                document.body.appendChild(countdownElement);
                
                const timerElement = document.getElementById('timer');
                const countdown = setInterval(function() {
                    timeLeft--;
                    timerElement.textContent = timeLeft;
                    
                    if (timeLeft <= 0) {
                        clearInterval(countdown);
                        window.location.href = 'http://localhost:8080/PhoneStore/';
                    }
                }, 1000);
            });
        </script>
    </body>
</html> 
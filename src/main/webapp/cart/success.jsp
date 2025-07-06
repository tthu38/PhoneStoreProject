<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.time.LocalDate" %>
<%
    // Set current date and time for order details
    request.setAttribute("orderId", System.currentTimeMillis());
    request.setAttribute("orderDate", LocalDate.now());
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Đặt hàng thành công - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <style>
            .success-container {
                max-width: 800px;
                margin: 0 auto;
                padding: 40px 20px;
                text-align: center;
            }
            
            .success-card {
                background: white;
                border-radius: 20px;
                padding: 40px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                margin-bottom: 30px;
            }
            
            .success-icon {
                width: 100px;
                height: 100px;
                background: linear-gradient(135deg, #28a745 0%, #20c997 100%);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 30px;
                color: white;
                font-size: 40px;
                animation: pulse 2s infinite;
            }
            
            @keyframes pulse {
                0% { transform: scale(1); }
                50% { transform: scale(1.05); }
                100% { transform: scale(1); }
            }
            
            .success-title {
                color: #28a745;
                font-size: 28px;
                font-weight: 600;
                margin-bottom: 15px;
            }
            
            .success-message {
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
                color: #e74c3c;
                font-size: 18px;
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
            
            .info-box {
                background: #e3f2fd;
                border: 1px solid #2196f3;
                border-radius: 10px;
                padding: 20px;
                margin: 20px 0;
            }
            
            .info-box h6 {
                color: #1976d2;
                margin-bottom: 10px;
                font-weight: 600;
            }
            
            .info-box p {
                color: #424242;
                margin-bottom: 0;
                font-size: 14px;
            }
            
            @media (max-width: 768px) {
                .success-container {
                    padding: 20px 10px;
                }
                
                .success-card {
                    padding: 30px 20px;
                }
                
                .success-title {
                    font-size: 24px;
                }
                
                .action-buttons {
                    flex-direction: column;
                    align-items: center;
                }
                
                .btn-primary, .btn-outline-secondary {
                    width: 100%;
                    max-width: 300px;
                    justify-content: center;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp" />
        
        <div class="success-container">
            <div class="success-card">
                <div class="success-icon">
                    <i class="fas fa-check"></i>
                </div>
                
                <h1 class="success-title">Đặt hàng thành công!</h1>
                <p class="success-message">
                    Cảm ơn bạn đã đặt hàng tại Thế Giới Công Nghệ. Chúng tôi đã nhận được đơn hàng của bạn và sẽ xử lý trong thời gian sớm nhất.
                </p>
                
                <div class="order-details">
                    <h5><i class="fas fa-receipt"></i> Thông tin đơn hàng</h5>
                    
                    <div class="detail-item">
                        <span>Mã đơn hàng:</span>
                        <span>#${orderId}</span>
                    </div>
                    <div class="detail-item">
                        <span>Ngày đặt hàng:</span>
                        <span>${orderDate}</span>
                    </div>
                    <div class="detail-item">
                        <span>Phương thức thanh toán:</span>
                        <span>Thanh toán khi nhận hàng (COD)</span>
                    </div>
                    <div class="detail-item">
                        <span>Tổng tiền:</span>
                        <span>${cart != null ? cart.totalPrice : 0} ₫</span>
                    </div>
                </div>
                
                <div class="info-box">
                    <h6><i class="fas fa-info-circle"></i> Thông tin quan trọng</h6>
                    <p>
                        • Chúng tôi sẽ liên hệ với bạn trong vòng 24 giờ để xác nhận đơn hàng<br>
                        • Thời gian giao hàng dự kiến: 2-5 ngày làm việc<br>
                        • Bạn có thể theo dõi đơn hàng qua email hoặc số điện thoại đã đăng ký
                    </p>
                </div>
                
                <div class="action-buttons">
                    <a href="${pageContext.request.contextPath}/" class="btn-primary">
                        <i class="fas fa-home"></i> Về trang chủ
                    </a>
                    <a href="${pageContext.request.contextPath}/user/profile.jsp" class="btn-outline-secondary">
                        <i class="fas fa-user"></i> Xem đơn hàng
                    </a>
                </div>
            </div>
            
            <!-- Khuyến mãi -->
            <div class="success-card">
                <h4><i class="fas fa-gift"></i> Khuyến mãi đặc biệt</h4>
                <p class="success-message">
                    Để cảm ơn bạn đã mua hàng, chúng tôi tặng bạn mã giảm giá <strong>WELCOME10</strong> 
                    giảm 10% cho đơn hàng tiếp theo!
                </p>
                <div class="info-box">
                    <h6><i class="fas fa-tag"></i> Mã giảm giá: WELCOME10</h6>
                    <p>Giảm 10% cho đơn hàng từ 500,000₫ trở lên. Hạn sử dụng: 30 ngày</p>
                </div>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Clear cart after successful order
            document.addEventListener('DOMContentLoaded', function() {
                // Clear cart from session
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
            });
        </script>
    </body>
</html> 
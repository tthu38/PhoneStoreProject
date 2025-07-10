<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PayPal Thanh toán thành công - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <style>
            .success-container {
                max-width: 800px;
                margin: 50px auto;
                padding: 40px;
                background: white;
                border-radius: 20px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                text-align: center;
            }
            
            .success-icon {
                width: 100px;
                height: 100px;
                background: linear-gradient(135deg, #0070ba 0%, #1546a0 100%);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 30px;
                color: white;
                font-size: 50px;
            }
            
            .success-title {
                color: #0070ba;
                font-size: 2.5rem;
                font-weight: bold;
                margin-bottom: 20px;
            }
            
            .success-message {
                color: #666;
                font-size: 1.1rem;
                margin-bottom: 30px;
                line-height: 1.6;
            }
            
            .paypal-info {
                background: #f8f9fa;
                border-radius: 15px;
                padding: 25px;
                margin: 30px 0;
                text-align: left;
            }
            
            .info-item {
                display: flex;
                justify-content: space-between;
                margin-bottom: 15px;
                padding-bottom: 10px;
                border-bottom: 1px solid #eee;
            }
            
            .info-item:last-child {
                border-bottom: none;
                margin-bottom: 0;
                font-weight: bold;
                color: #0070ba;
            }
            
            .action-buttons {
                margin-top: 40px;
            }
            
            .btn-primary {
                background: linear-gradient(135deg, #0070ba 0%, #1546a0 100%);
                border: none;
                padding: 12px 30px;
                border-radius: 10px;
                font-weight: bold;
                margin: 0 10px;
                transition: all 0.3s ease;
            }
            
            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(0, 112, 186, 0.4);
            }
            
            .btn-outline-secondary {
                border: 2px solid #6c757d;
                color: #6c757d;
                padding: 12px 30px;
                border-radius: 10px;
                font-weight: bold;
                margin: 0 10px;
                transition: all 0.3s ease;
            }
            
            .btn-outline-secondary:hover {
                background: #6c757d;
                color: white;
                transform: translateY(-2px);
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp" />
        
        <div class="success-container">
            <div class="success-icon">
                <i class="fab fa-paypal"></i>
            </div>
            
            <h1 class="success-title">PayPal Thanh toán thành công!</h1>
            
            <p class="success-message">
                Cảm ơn bạn đã thanh toán qua PayPal! Giao dịch đã được xử lý thành công.
            </p>
            
            <div class="paypal-info">
                <h4><i class="fab fa-paypal"></i> Thông tin giao dịch PayPal</h4>
                
                <div class="info-item">
                    <span>Phương thức thanh toán:</span>
                    <span>PayPal</span>
                </div>
                
                <div class="info-item">
                    <span>Trạng thái:</span>
                    <span style="color: #28a745;">Thành công</span>
                </div>
                
                <div class="info-item">
                    <span>Thời gian:</span>
                    <span>${param.payment_date}</span>
                </div>
                
                <div class="info-item">
                    <span>Mã giao dịch:</span>
                    <span>${param.tx}</span>
                </div>
                
                <div class="info-item">
                    <span>Số tiền VNĐ:</span>
                    <span><fmt:formatNumber value="${amountVND}" type="currency" currencySymbol="₫" /></span>
                </div>
                
                <div class="info-item">
                    <span>Số tiền USD:</span>
                    <span>$${amountUSD}</span>
                </div>
                
                <div class="info-item">
                    <span>Số sản phẩm:</span>
                    <span>${itemCount} sản phẩm</span>
                </div>
                
                <div class="info-item">
                    <span>Mô tả:</span>
                    <span>${itemDescription}</span>
                </div>
            </div>
            
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                    <i class="fas fa-home"></i> Về trang chủ
                </a>
                <a href="${pageContext.request.contextPath}/user/profile.jsp" class="btn btn-outline-secondary">
                    <i class="fas fa-user"></i> Xem đơn hàng
                </a>
            </div>
            
            <div class="mt-4">
                <small class="text-muted">
                    <i class="fas fa-info-circle"></i> 
                    PayPal đã gửi email xác nhận đến địa chỉ email của bạn.
                </small>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PayPal Thanh toán thất bại - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <style>
            .cancel-container {
                max-width: 800px;
                margin: 50px auto;
                padding: 40px;
                background: white;
                border-radius: 20px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
                text-align: center;
            }
            
            .cancel-icon {
                width: 100px;
                height: 100px;
                background: linear-gradient(135deg, #dc3545 0%, #fd7e14 100%);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 30px;
                color: white;
                font-size: 50px;
            }
            
            .cancel-title {
                color: #dc3545;
                font-size: 2.5rem;
                font-weight: bold;
                margin-bottom: 20px;
            }
            
            .cancel-message {
                color: #666;
                font-size: 1.1rem;
                margin-bottom: 30px;
                line-height: 1.6;
            }
            
            .error-details {
                background: #fff5f5;
                border: 1px solid #fed7d7;
                border-radius: 15px;
                padding: 25px;
                margin: 30px 0;
                text-align: left;
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
            
            .btn-outline-danger {
                border: 2px solid #dc3545;
                color: #dc3545;
                padding: 12px 30px;
                border-radius: 10px;
                font-weight: bold;
                margin: 0 10px;
                transition: all 0.3s ease;
            }
            
            .btn-outline-danger:hover {
                background: #dc3545;
                color: white;
                transform: translateY(-2px);
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
        
        <div class="cancel-container">
            <div class="cancel-icon">
                <i class="fab fa-paypal"></i>
            </div>
            
            <h1 class="cancel-title">PayPal Thanh toán thất bại!</h1>
            
            <p class="cancel-message">
                Rất tiếc! Giao dịch PayPal đã bị hủy hoặc thất bại. Vui lòng thử lại.
            </p>
            
            <div class="error-details">
                <h4><i class="fab fa-paypal"></i> Thông tin lỗi PayPal</h4>
                <ul class="mb-0">
                    <li>Giao dịch đã bị hủy bởi người dùng</li>
                    <li>Tiền chưa được trừ khỏi tài khoản PayPal</li>
                    <li>Đơn hàng vẫn được lưu trong giỏ hàng</li>
                    <li>Bạn có thể thử thanh toán lại hoặc chọn phương thức khác</li>
                </ul>
            </div>
            
            <div class="action-buttons">
                <a href="${pageContext.request.contextPath}/cart/confirm.jsp" class="btn btn-primary">
                    <i class="fab fa-paypal"></i> Thử lại PayPal
                </a>
                <a href="${pageContext.request.contextPath}/carts" class="btn btn-outline-secondary">
                    <i class="fas fa-shopping-cart"></i> Về giỏ hàng
                </a>
                <a href="${pageContext.request.contextPath}/" class="btn btn-outline-danger">
                    <i class="fas fa-home"></i> Về trang chủ
                </a>
            </div>

                <div class="mt-4">
                    <small class="text-muted">
                    <i class="fas fa-info-circle"></i> 
                    Nếu vấn đề vẫn tiếp tục, vui lòng liên hệ hỗ trợ khách hàng.
                </small>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 
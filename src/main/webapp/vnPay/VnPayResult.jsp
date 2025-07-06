<%-- 
    Document   : VnPayResult
    Created on : Jul 5, 2025, 10:02:22 PM
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Kết quả thanh toán VnPay</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background: linear-gradient(135deg, #e0eafc 0%, #cfdef3 100%);
                min-height: 100vh;
            }
            .result-container {
                max-width: 420px;
                margin: 80px auto;
                background: #fff;
                border-radius: 18px;
                box-shadow: 0 8px 32px rgba(60, 60, 120, 0.15);
                padding: 40px 30px 32px 30px;
                text-align: center;
                position: relative;
                overflow: hidden;
            }
            .result-icon {
                font-size: 5rem;
                margin-bottom: 18px;
                animation: pop 0.7s cubic-bezier(.68,-0.55,.27,1.55);
            }
            @keyframes pop {
                0% { transform: scale(0.5); opacity: 0; }
                80% { transform: scale(1.15); }
                100% { transform: scale(1); opacity: 1; }
            }
            .success {
                color: #2ecc71;
                text-shadow: 0 2px 12px #b6f5d8;
            }
            .fail {
                color: #e74c3c;
                text-shadow: 0 2px 12px #f5b6b6;
            }
            .result-container h2 {
                font-weight: 700;
                margin-bottom: 18px;
            }
            .result-container p {
                font-size: 1.1rem;
                color: #444;
            }
            .btn-custom {
                border-radius: 25px;
                padding: 10px 32px;
                font-size: 1.1rem;
                margin-top: 18px;
                box-shadow: 0 2px 8px rgba(44, 62, 80, 0.08);
                transition: background 0.2s, color 0.2s;
            }
            .btn-success {
                background: linear-gradient(90deg, #43e97b 0%, #38f9d7 100%);
                border: none;
                color: #fff;
            }
            .btn-success:hover {
                background: linear-gradient(90deg, #38f9d7 0%, #43e97b 100%);
                color: #fff;
            }
            .btn-danger {
                background: linear-gradient(90deg, #ff5858 0%, #f09819 100%);
                border: none;
                color: #fff;
            }
            .btn-danger:hover {
                background: linear-gradient(90deg, #f09819 0%, #ff5858 100%);
                color: #fff;
            }
            .order-info {
                background: #f7fafd;
                border-radius: 10px;
                padding: 12px 0 8px 0;
                margin-bottom: 10px;
                margin-top: 10px;
            }
        </style>
    </head>
    <body>
        <div class="result-container shadow">
            <%
                Boolean success = (Boolean) request.getAttribute("success");
                String orderId = (String) request.getAttribute("orderId");
                String amount = (String) request.getAttribute("amount");
            %>
            <% if (success != null && success) { %>
                <div class="result-icon success">
                    <i class="fas fa-check-circle"></i>
                </div>
                <h2>Thanh toán thành công!</h2>
                <div class="order-info">
                    <p>Mã đơn hàng: <strong><%= orderId != null ? orderId : "N/A" %></strong></p>
                    <p>Số tiền: <strong><%= amount != null ? amount : "N/A" %></strong></p>
                </div>
                <p>Cảm ơn bạn đã mua hàng tại <b>Phone Store</b>.<br>Chúng tôi sẽ xử lý đơn hàng của bạn sớm nhất!</p>
                <a href="/home" class="btn btn-success btn-custom">Về trang chủ</a>
            <% } else { %>
                <div class="result-icon fail">
                    <i class="fas fa-times-circle"></i>
                </div>
                <h2>Thanh toán thất bại!</h2>
                <div class="order-info">
                    <p>Đơn hàng của bạn chưa được thanh toán thành công.</p>
                </div>
                <p>Vui lòng thử lại hoặc liên hệ hỗ trợ nếu cần giúp đỡ.</p>
                <a href="/cart" class="btn btn-danger btn-custom">Quay lại Trang chủ</a>
            <% } %>
        </div>
        <!-- Font Awesome for icons -->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/js/all.min.js"></script>
    </body>
</html>

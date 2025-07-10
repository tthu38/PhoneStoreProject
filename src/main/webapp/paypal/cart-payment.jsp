<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>PayPal Thanh toán từ giỏ hàng - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <style>
            .payment-container {
                max-width: 800px;
                margin: 50px auto;
                padding: 40px;
                background: white;
                border-radius: 20px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.1);
            }
            
            .paypal-header {
                text-align: center;
                margin-bottom: 30px;
            }
            
            .paypal-logo {
                width: 120px;
                height: 60px;
                background: linear-gradient(135deg, #0070ba 0%, #1546a0 100%);
                border-radius: 10px;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 20px;
                color: white;
                font-size: 24px;
                font-weight: bold;
            }
            
            .payment-title {
                color: #0070ba;
                font-size: 2rem;
                font-weight: bold;
                margin-bottom: 10px;
            }
            
            .payment-subtitle {
                color: #666;
                font-size: 1rem;
            }
            
            .cart-summary {
                background: #f8f9fa;
                border-radius: 15px;
                padding: 25px;
                margin: 30px 0;
            }
            
            .cart-item {
                display: flex;
                align-items: center;
                padding: 15px 0;
                border-bottom: 1px solid #eee;
            }
            
            .cart-item:last-child {
                border-bottom: none;
            }
            
            .item-image {
                width: 60px;
                height: 60px;
                object-fit: cover;
                border-radius: 8px;
                margin-right: 15px;
            }
            
            .item-info {
                flex: 1;
            }
            
            .item-name {
                font-weight: 500;
                margin-bottom: 5px;
            }
            
            .item-variant {
                color: #666;
                font-size: 14px;
            }
            
            .item-price {
                text-align: right;
                font-weight: 600;
                color: #0070ba;
            }
            
            .total-section {
                background: #e3f2fd;
                border-radius: 10px;
                padding: 20px;
                margin-top: 20px;
            }
            
            .total-item {
                display: flex;
                justify-content: space-between;
                margin-bottom: 10px;
            }
            
            .total-item:last-child {
                margin-bottom: 0;
                font-weight: bold;
                font-size: 18px;
                color: #0070ba;
                border-top: 1px solid #ddd;
                padding-top: 10px;
            }
            
            .btn-paypal {
                background: linear-gradient(135deg, #0070ba 0%, #1546a0 100%);
                border: none;
                color: white;
                padding: 15px 30px;
                border-radius: 10px;
                font-weight: bold;
                width: 100%;
                margin-top: 20px;
                transition: all 0.3s ease;
                font-size: 16px;
            }
            
            .btn-paypal:hover {
                transform: translateY(-2px);
                color: white;
                box-shadow: 0 5px 15px rgba(0, 112, 186, 0.4);
            }
            
            .btn-paypal:disabled {
                opacity: 0.6;
                cursor: not-allowed;
                transform: none;
            }
            
            .exchange-rate {
                background: #fff3cd;
                border: 1px solid #ffeaa7;
                border-radius: 8px;
                padding: 15px;
                margin: 20px 0;
                text-align: center;
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp" />
        
        <div class="payment-container">
            <div class="paypal-header">
                <div class="paypal-logo">
                    <i class="fab fa-paypal"></i> PayPal
                </div>
                <h1 class="payment-title">Thanh toán qua PayPal</h1>
                <p class="payment-subtitle">Thanh toán an toàn với PayPal</p>
            </div>
            
            <!-- Thông tin giao hàng từ session -->
            <c:if test="${not empty sessionScope.shippingInfo}">
                <div class="shipping-info" style="background: #e8f5e8; border-radius: 10px; padding: 20px; margin-bottom: 20px;">
                    <h5><i class="fas fa-shipping-fast"></i> Thông tin giao hàng</h5>
                    <div class="row">
                        <div class="col-md-6">
                            <strong>Họ và tên:</strong> ${sessionScope.shippingInfo[0]}<br>
                            <strong>Số điện thoại:</strong> ${sessionScope.shippingInfo[1]}
                        </div>
                        <div class="col-md-6">
                            <strong>Địa chỉ:</strong> ${sessionScope.shippingInfo[2]}<br>
                            <c:if test="${not empty sessionScope.shippingInfo[3]}">
                                <strong>Ghi chú:</strong> ${sessionScope.shippingInfo[3]}
                            </c:if>
                        </div>
                    </div>
                </div>
            </c:if>
            
            <c:if test="${not empty cart}">
                <div class="cart-summary">
                    <h4><i class="fas fa-shopping-cart"></i> Tóm tắt đơn hàng</h4>
                    
                    <c:set var="selectedTotal" value="0" />
                    <c:set var="selectedCount" value="0" />
                    
                    <c:forEach var="item" items="${cart}">
                        <c:if test="${item.selected}">
                            <c:set var="product" value="${item.productVariant.product}" />
                            <c:set var="variant" value="${item.productVariant}" />
                            
                            <div class="cart-item">
                                <c:choose>
                                    <c:when test="${not empty variant.imageURLs}">
                                        <img src="${variant.imageURLs}" 
                                             alt="${product.name}" class="item-image">
                                    </c:when>
                                    <c:when test="${not empty product.thumbnailImage}">
                                        <img src="${pageContext.request.contextPath}/images/${product.thumbnailImage}" 
                                             alt="${product.name}" class="item-image">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="${pageContext.request.contextPath}/images/default-product.jpg" 
                                             alt="${product.name}" class="item-image">
                                    </c:otherwise>
                                </c:choose>
                                
                                <div class="item-info">
                                    <div class="item-name">${product.name}</div>
                                    <div class="item-variant">
                                        ${variant.color} - ${variant.rom}GB x ${item.quantity}
                                    </div>
                                </div>
                                
                                <div class="item-price">
                                    <c:set var="hasDiscount" value="${not empty variant.discountPrice and not empty variant.discountExpiry and variant.discountExpiry > currentTime}" />
                                    <c:choose>
                                        <c:when test="${hasDiscount}">
                                            <fmt:formatNumber value="${variant.discountPrice * item.quantity}" type="currency" currencySymbol="₫" />
                                            <c:set var="selectedTotal" value="${selectedTotal + (variant.discountPrice * item.quantity)}" />
                                        </c:when>
                                        <c:otherwise>
                                            <fmt:formatNumber value="${variant.price * item.quantity}" type="currency" currencySymbol="₫" />
                                            <c:set var="selectedTotal" value="${selectedTotal + (variant.price * item.quantity)}" />
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            
                            <c:set var="selectedCount" value="${selectedCount + 1}" />
                        </c:if>
                    </c:forEach>
                    
                    <c:if test="${selectedCount > 0}">
                        <div class="total-section">
                            <div class="total-item">
                                <span>Tạm tính:</span>
                                <span><fmt:formatNumber value="${selectedTotal}" type="currency" currencySymbol="₫" /></span>
                            </div>
                            <div class="total-item">
                                <span>Phí vận chuyển:</span>
                                <span>Miễn phí</span>
                            </div>
                            <div class="total-item">
                                <span>Tổng cộng:</span>
                                <span><fmt:formatNumber value="${selectedTotal}" type="currency" currencySymbol="₫" /></span>
                            </div>
                        </div>
                        
                        <div class="exchange-rate">
                            <i class="fas fa-exchange-alt"></i>
                            <strong>Tỉ giá: 1 USD = 24,000 VNĐ</strong><br>
                            <small>Số tiền USD: $<fmt:formatNumber value="${selectedTotal / 24000}" pattern="#,##0.00" /></small>
                        </div>
                        
                        <form action="${pageContext.request.contextPath}/paypal" method="POST">
                            <button type="submit" class="btn btn-paypal">
                                <i class="fab fa-paypal"></i> Thanh toán qua PayPal
                            </button>
                        </form>
                    </c:if>
                    
                    <c:if test="${selectedCount == 0}">
                        <div class="alert alert-warning" role="alert">
                            <i class="fas fa-exclamation-triangle"></i> 
                            Vui lòng chọn ít nhất một sản phẩm để thanh toán.
                        </div>
                        <a href="${pageContext.request.contextPath}/carts" class="btn btn-outline-secondary">
                            <i class="fas fa-shopping-cart"></i> Về giỏ hàng
                        </a>
                    </c:if>
                </div>
            </c:if>
            
            <c:if test="${empty cart}">
                <div class="alert alert-info" role="alert">
                    <i class="fas fa-info-circle"></i> 
                    Giỏ hàng trống. Vui lòng thêm sản phẩm vào giỏ hàng.
                </div>
                <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                    <i class="fas fa-home"></i> Về trang chủ
                </a>
            </c:if>
            
            <div class="mt-4 text-center">
                <small class="text-muted">
                    <i class="fas fa-shield-alt"></i> 
                    Giao dịch được bảo mật bởi PayPal
                </small>
            </div>
        </div>
        
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 
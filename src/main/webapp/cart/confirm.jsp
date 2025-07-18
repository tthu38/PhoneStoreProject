<%-- 
    Document   : confirm
    Created on : Jul 5, 2025, 2:36:11 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.util.List" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.UserAddress" %>
<%@ page import="service.UserAddressService" %>
<%
    // Set current time for discount checking
    request.setAttribute("currentTime", LocalDateTime.now());
    // Set current user if not already set
    if (request.getAttribute("currentUser") == null) {
        request.setAttribute("currentUser", session.getAttribute("user"));
    }

    // Calculate selected total for confirm page
    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
    double selectedTotal = 0;
    if (cart != null && !cart.isEmpty()) {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        for (CartItem item : cart) {
            if (item.isSelected()) {
                java.math.BigDecimal price = item.getProductVariant().getDiscountPrice() != null
                        && item.getProductVariant().getDiscountExpiry() != null
                        && item.getProductVariant().getDiscountExpiry().isAfter(now)
                        ? item.getProductVariant().getDiscountPrice()
                        : item.getProductVariant().getPrice();
                selectedTotal += price.doubleValue() * item.getQuantity();
            }
        }
    }
    request.setAttribute("selectedTotal", selectedTotal);
    
    // Lấy thông tin địa chỉ từ user
    model.User currentUser = (model.User) session.getAttribute("user");
    String userCity = "";
    String userDistrict = "";
    String userWard = "";
    String userAddress = "";
    String userPhone = "";
    String userFullName = "";
    
    if (currentUser != null) {
        userPhone = currentUser.getPhoneNumber() != null ? currentUser.getPhoneNumber() : "";
        userFullName = currentUser.getFullName() != null ? currentUser.getFullName() : "";
        
        // Lấy địa chỉ mặc định của user
        UserAddressService userAddressService = new UserAddressService();
        UserAddress defaultAddress = userAddressService.getDefaultAddressByUserId(currentUser.getUserID());
        
        // Nếu không có địa chỉ mặc định, lấy địa chỉ đầu tiên
        if (defaultAddress == null) {
            defaultAddress = userAddressService.getFirstActiveAddressByUserId(currentUser.getUserID());
        }
        
        // Kiểm tra nếu người dùng không có địa chỉ nào
        if (defaultAddress == null) {
            // Chuyển hướng đến trang profile để cập nhật thông tin
            response.sendRedirect(request.getContextPath() + "/user/profile.jsp?error=no_address&message=" + 
                java.net.URLEncoder.encode("Vui lòng cập nhật địa chỉ giao hàng để tiếp tục thanh toán", "UTF-8"));
            return;
        }
        
        if (defaultAddress != null) {
            userAddress = defaultAddress.getAddress() != null ? defaultAddress.getAddress() : "";
            // Chỉ sử dụng số điện thoại từ địa chỉ nếu user không có số điện thoại
            boolean userPhoneEmpty = (userPhone == null || userPhone.trim().isEmpty());
            boolean addressPhoneExists = (defaultAddress.getPhoneNumber() != null && !defaultAddress.getPhoneNumber().trim().isEmpty());
            
            if (userPhoneEmpty && addressPhoneExists) {
                userPhone = defaultAddress.getPhoneNumber();
            }
            // Chỉ sử dụng tên từ địa chỉ nếu user không có tên
            if ((userFullName == null || userFullName.trim().isEmpty()) && defaultAddress.getFullName() != null) {
                userFullName = defaultAddress.getFullName();
            }
        }
    }
    
    request.setAttribute("userCity", userCity);
    request.setAttribute("userDistrict", userDistrict);
    request.setAttribute("userWard", userWard);
    request.setAttribute("userAddress", userAddress);
    request.setAttribute("userPhone", userPhone);
    request.setAttribute("userFullName", userFullName);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Xác nhận đơn hàng - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css">
        <style>
            .checkout-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
                background: #f8f9fa;
                min-height: 100vh;
            }

            .checkout-header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 30px;
                border-radius: 15px;
                margin-bottom: 30px;
                text-align: center;
            }

            .checkout-step {
                background: white;
                border-radius: 15px;
                padding: 25px;
                margin-bottom: 20px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            }

            .step-title {
                color: #333;
                margin-bottom: 20px;
                font-weight: 600;
                display: flex;
                align-items: center;
                gap: 10px;
            }

            .form-group {
                margin-bottom: 20px;
            }

            .form-label {
                font-weight: 500;
                color: #555;
                margin-bottom: 8px;
            }

            .form-control {
                border: 1px solid #ddd;
                border-radius: 8px;
                padding: 12px;
                transition: all 0.3s ease;
            }

            .form-control:focus {
                border-color: #667eea;
                box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.25);
            }

            .order-summary {
                background: white;
                border-radius: 15px;
                padding: 25px;
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
                position: sticky;
                top: 20px;
            }

            .order-item {
                display: flex;
                align-items: center;
                padding: 15px 0;
                border-bottom: 1px solid #eee;
            }

            .order-item:last-child {
                border-bottom: none;
            }

            .order-item-image {
                width: 60px;
                height: 60px;
                object-fit: cover;
                border-radius: 8px;
                margin-right: 15px;
            }

            .order-item-info {
                flex: 1;
            }

            .order-item-name {
                font-weight: 500;
                margin-bottom: 5px;
            }

            .order-item-variant {
                color: #666;
                font-size: 14px;
            }

            .order-item-price {
                text-align: right;
                font-weight: 600;
                color: #e74c3c;
            }

            .payment-methods {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 15px;
                margin-top: 20px;
            }

            .payment-method {
                border: 2px solid #ddd;
                border-radius: 10px;
                padding: 15px;
                cursor: pointer;
                transition: all 0.3s ease;
                text-align: center;
            }

            .payment-method:hover {
                border-color: #667eea;
                background: #f8f9ff;
            }

            .payment-method.selected {
                border-color: #667eea;
                background: #f0f2ff;
            }

            .payment-method i {
                font-size: 24px;
                margin-bottom: 10px;
                color: #667eea;
            }

            .place-order-btn {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

            .place-order-btn:hover {
                transform: translateY(-2px);
                color: white;
                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            }

            .place-order-btn:disabled {
                opacity: 0.6;
                cursor: not-allowed;
                transform: none;
            }

            .summary-item {
                display: flex;
                justify-content: space-between;
                margin-bottom: 15px;
                padding-bottom: 10px;
                border-bottom: 1px solid #eee;
            }

            .summary-item:last-child {
                border-bottom: none;
                margin-bottom: 0;
                font-weight: bold;
                color: #667eea;
                font-size: 18px;
            }

            .back-to-cart {
                background: #6c757d;
                color: white;
                padding: 12px 25px;
                border-radius: 8px;
                text-decoration: none;
                display: inline-block;
                margin-top: 20px;
                transition: all 0.3s ease;
            }

            .back-to-cart:hover {
                background: #5a6268;
                color: white;
                transform: translateY(-2px);
            }

            @media (max-width: 768px) {
                .checkout-container {
                    padding: 10px;
                }

                .checkout-header {
                    padding: 20px;
                    margin-bottom: 20px;
                }

                .order-summary {
                    position: static;
                    margin-top: 20px;
                }

                .payment-methods {
                    grid-template-columns: 1fr;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp" />

        <div class="checkout-container">
            <div class="checkout-header">
                <h1><i class="fas fa-credit-card"></i> Xác nhận đơn hàng</h1>
                <p class="mb-0">Hoàn tất thông tin để đặt hàng</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-triangle"></i> ${error}
                </div>
            </c:if>
            
            <c:if test="${param.error == 'insufficient_stock' and not empty param.message}">
                <div class="alert alert-warning" role="alert">
                    <i class="fas fa-exclamation-triangle"></i> 
                    <strong>Không đủ số lượng sản phẩm trong kho:</strong><br>
                    <pre style="white-space: pre-wrap; margin: 10px 0 0; font-family: inherit;">${param.message}</pre>
                </div>
            </c:if>

            <div class="row">
                <div class="col-lg-8">
                    <!-- Thông tin giao hàng -->
                    <div class="checkout-step">
                        <h4 class="step-title">
                            <i class="fas fa-shipping-fast"></i> Thông tin giao hàng
                        </h4>

                        <form id="checkoutForm" action="${pageContext.request.contextPath}/carts" method="POST">
                            <input type="hidden" name="action" value="checkout">
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">Họ và tên *</label>
                                        <input type="text" class="form-control" name="fullName" required 
                                               value="${userFullName}" placeholder="Nhập họ và tên">
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label">Số điện thoại *</label>
                                        <input type="tel" class="form-control" name="phone" required 
                                               value="${userPhone}" placeholder="Nhập số điện thoại">
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="form-label">Địa chỉ giao hàng *</label>
                                <textarea class="form-control" name="address" rows="3" required 
                                          placeholder="Nhập địa chỉ giao hàng chi tiết">${userAddress}</textarea>
                            </div>

                            <div class="form-group">
                                <label class="form-label">Ghi chú</label>
                                <textarea class="form-control" name="note" rows="2" 
                                          placeholder="Ghi chú cho đơn hàng (tùy chọn)"></textarea>
                            </div>
                            
                            <!-- Link cập nhật thông tin -->
                            <div class="form-group">
                                <div class="alert alert-info" role="alert">
                                    <i class="fas fa-info-circle"></i>
                                    <strong>Lưu ý:</strong> Thông tin trên được lấy từ tài khoản của bạn. 
                                    <a href="${pageContext.request.contextPath}/user/profile.jsp" class="alert-link">
                                        <i class="fas fa-edit"></i> Cập nhật thông tin cá nhân
                                    </a>
                                </div>
                            </div>
                        </form>
                    </div>

                    <!-- Phương thức thanh toán -->
                    <div class="checkout-step">
                        <h4 class="step-title">
                            <i class="fas fa-credit-card"></i> Phương thức thanh toán
                        </h4>

                        <div class="payment-methods">
                            <div class="payment-method" data-method="cod">
                                <i class="fas fa-money-bill-wave"></i>
                                <div>
                                    <strong>Thanh toán khi nhận hàng</strong>
                                    <small class="d-block text-muted">COD - Cash on Delivery</small>
                                </div>
                            </div>

                            <div class="payment-method" data-method="vnpay">
                                <i class="fas fa-credit-card"></i>
                                <div>
                                    <strong>VNPAY</strong>
                                    <small class="d-block text-muted">Thanh toán trực tuyến</small>
                                </div>
                            </div>

                            <div class="payment-method" data-method="paypal">
                                <span style="display:inline-block;width:32px;height:32px;margin-bottom:10px;vertical-align:middle;">
                                    <svg viewBox="0 0 32 32" width="32" height="32" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <rect width="32" height="32" rx="8" fill="#fff"/>
                                        <path d="M13.5 8.5h7.2c2.2 0 3.7 1.2 3.7 3.2 0 2.1-1.5 3.3-3.7 3.3h-4.2l-1.1 6.5h-2.1l1.1-6.5h-2.1l-1.1 6.5h-2.1l2.1-12h4.2zm2.1 5.2h3.7c1.1 0 1.7-0.5 1.7-1.4 0-0.9-0.6-1.3-1.7-1.3h-3.7l-0.7 4.1z" fill="#003087"/>
                                        <path d="M21.5 13.5c0.7 0 1.3-0.2 1.7-0.6 0.4-0.4 0.6-0.9 0.6-1.5 0-1.1-0.8-1.7-2.2-1.7h-3.7l-0.7 4.1h3.7c0.7 0 1.3-0.2 1.7-0.6z" fill="#009cde"/>
                                    </svg>
                                </span>
                                <div>
                                    <strong>PAYPAL</strong>
                                    <small class="d-block text-muted">Ví điện tử PayPal</small>
                                </div>
                            </div>
                        </div>
                    </div>

                    <input type="hidden" name="paymentMethod" id="paymentMethod" value="">
                    </form>

                    <!-- Nút điều hướng -->
                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/cart/cart.jsp" class="back-to-cart">
                            <i class="fas fa-arrow-left"></i> Quay lại giỏ hàng
                        </a>
                    </div>
                </div>

                <div class="col-lg-4">
                    <div class="order-summary">
                        <h4 class="step-title">
                            <i class="fas fa-shopping-bag"></i> Tóm tắt đơn hàng
                        </h4>

                        <c:if test="${not empty cart}">
                            <c:forEach var="item" items="${cart}">
                                <c:set var="product" value="${item.productVariant.product}" />
                                <c:set var="variant" value="${item.productVariant}" />

                                <c:if test="${item.selected}">
                                    <div class="order-item">
                                        <c:choose>
                                            <c:when test="${not empty variant.imageURLs}">
                                                <img src="${variant.imageURLs}" 
                                                     alt="${product.name}" class="order-item-image">
                                            </c:when>
                                            <c:when test="${not empty product.thumbnailImage}">
                                                <img src="${pageContext.request.contextPath}/images/${product.thumbnailImage}" 
                                                     alt="${product.name}" class="order-item-image">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/images/default-product.jpg" 
                                                     alt="${product.name}" class="order-item-image">
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="order-item-info">
                                            <div class="order-item-name">${product.name}</div>
                                            <div class="order-item-variant">
                                                ${variant.color} - ${variant.rom}GB x ${item.quantity}
                                            </div>
                                        </div>
                                        <div class="order-item-price">
                                            <c:set var="hasDiscount" value="${not empty variant.discountPrice and not empty variant.discountExpiry and variant.discountExpiry > currentTime}" />
                                            <c:choose>
                                                <c:when test="${hasDiscount}">
                                                    <fmt:formatNumber value="${variant.discountPrice * item.quantity}" type="currency" currencySymbol="₫" />
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${variant.price * item.quantity}" type="currency" currencySymbol="₫" />
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </c:if>
                            </c:forEach>

                            <hr>

                            <div class="summary-item">
                                <span>Tạm tính:</span>
                                <span><fmt:formatNumber value="${selectedTotal}" type="currency" currencySymbol="₫" /></span>
                            </div>
                            <div class="summary-item">
                                <span>Phí vận chuyển:</span>
                                <span>Miễn phí</span>
                            </div>
                            <div class="summary-item summary-total">
                                <span>Tổng cộng:</span>
                                <span><fmt:formatNumber value="${selectedTotal}" type="currency" currencySymbol="₫" /></span>
                            </div>

                            <button class="place-order-btn" id="placeOrderBtn">
                                <i class="fas fa-check"></i> Đặt hàng ngay
                            </button>

                            <div class="text-center mt-3">
                                <small class="text-muted">
                                    <i class="fas fa-shield-alt"></i> Bảo mật thông tin
                                </small>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Payment method selection
            document.querySelectorAll('.payment-method').forEach(method => {
                method.addEventListener('click', function () {
                    // Remove selected class from all methods
                    document.querySelectorAll('.payment-method').forEach(m => m.classList.remove('selected'));
                    // Add selected class to clicked method
                    this.classList.add('selected');

                    // Set payment method value
                    const paymentMethod = this.getAttribute('data-method');
                    document.getElementById('paymentMethod').value = paymentMethod;
                });
            });

                        // Place order button
            document.getElementById('placeOrderBtn').addEventListener('click', function () {
                const form = document.getElementById('checkoutForm');
                const selectedPayment = document.querySelector('.payment-method.selected');
                
                // Kiểm tra user đăng nhập
                const currentUser = '${sessionScope.user}';
                if (!currentUser || currentUser === 'null' || currentUser === '') {
                    alert('Vui lòng đăng nhập để tiếp tục thanh toán');
                    window.location.href = '${pageContext.request.contextPath}/user/login.jsp';
                    return;
                }
                
                if (!form.checkValidity()) {
                    form.reportValidity();
                    return;
                }
                
                if (!selectedPayment) {
                    alert('Vui lòng chọn phương thức thanh toán');
                    return;
                }
                
                // Disable button to prevent double submission
                this.disabled = true;
                this.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Đang xử lý...';
                
                // Xử lý theo phương thức thanh toán
                const paymentMethod = selectedPayment.getAttribute('data-method');
                
                if (paymentMethod === 'paypal') {
                    // Chuyển form action đến PayPalServlet
                    form.action = '${pageContext.request.contextPath}/paypal';
                } else if (paymentMethod === 'vnpay') {
                    // Chuyển form action đến VNPayServlet
                    form.action = '${pageContext.request.contextPath}/vnpay';
                } else {
                    // Chuyển form action đến OrderServlet cho COD
                    form.action = '${pageContext.request.contextPath}/order';
                }
                
                // Submit form
                form.submit();
            });

            // Form validation
            document.getElementById('checkoutForm').addEventListener('submit', function (e) {
                const selectedPayment = document.querySelector('.payment-method.selected');
                if (!selectedPayment) {
                    e.preventDefault();
                    alert('Vui lòng chọn phương thức thanh toán');
                    return;
                }
            });
        </script>
    </body>
</html>

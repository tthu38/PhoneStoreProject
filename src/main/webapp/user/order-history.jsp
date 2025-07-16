<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Lịch sử đặt hàng - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <style>
            .order-history-container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 20px;
                background: #fff;
                min-height: 100vh;
            }

            .page-header {
                background: #fff;
                color: #dc3545;
                padding: 40px;
                border-radius: 20px;
                margin-bottom: 30px;
                text-align: center;
                box-shadow: 0 10px 30px rgba(220,53,69,0.08);
                border: 1px solid #f8d7da;
            }

            .page-header h1 {
                font-size: 2.5rem;
                font-weight: 700;
                margin-bottom: 10px;
                color: #dc3545;
                background: none;
            }

            .page-header p {
                font-size: 1.1rem;
                color: #b5b5b5;
                margin: 0;
            }

            .order-card {
                background: #fff;
                border-radius: 20px;
                padding: 30px;
                margin-bottom: 25px;
                box-shadow: 0 8px 24px rgba(220,53,69,0.08);
                transition: all 0.3s ease;
                border: 1px solid #f8d7da;
            }

            .order-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 16px 32px rgba(220,53,69,0.13);
            }

            .order-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 25px;
                padding-bottom: 20px;
                border-bottom: 2px solid #f8d7da;
            }

            .order-id {
                font-size: 1.5rem;
                font-weight: 700;
                color: #dc3545;
                background: none;
            }

            .order-date {
                color: #b5b5b5;
                font-size: 0.9rem;
                margin-top: 5px;
            }

            .order-status {
                padding: 8px 20px;
                border-radius: 25px;
                font-size: 0.8rem;
                font-weight: 600;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }

            .status-pending {
                background: #fff3cd;
                color: #b85c00;
                border: 1px solid #ffe082;
            }

            .status-paid {
                background: #f8d7da;
                color: #dc3545;
                border: 1px solid #dc3545;
            }

            .status-shipped {
                background: #e9ecef;
                color: #dc3545;
                border: 1px solid #dc3545;
            }

            .status-delivered {
                background: #f8d7da;
                color: #dc3545;
                border: 1px solid #dc3545;
            }

            .status-cancelled {
                background: #fff0f1;
                color: #dc3545;
                border: 1px solid #dc3545;
            }

            .order-items {
                margin-bottom: 25px;
            }

            .order-items h6 {
                color: #dc3545;
                font-size: 1.1rem;
                font-weight: 600;
                margin-bottom: 20px;
                padding-bottom: 10px;
                border-bottom: 2px solid #f8d7da;
            }

            .order-item {
                display: flex;
                align-items: center;
                padding: 20px 0;
                border-bottom: 1px solid #f8d7da;
                transition: background-color 0.3s ease;
            }

            .order-item:hover {
                background: #fff0f1;
                border-radius: 10px;
                padding: 20px;
                margin: 0 -20px;
            }

            .order-item:last-child {
                border-bottom: none;
            }

            .item-image {
                width: 70px;
                height: 70px;
                object-fit: cover;
                border-radius: 15px;
                margin-right: 20px;
                box-shadow: 0 5px 15px rgba(220,53,69,0.08);
                border: 3px solid #f8d7da;
            }

            .item-info {
                flex: 1;
            }

            .item-name {
                font-weight: 600;
                margin-bottom: 8px;
                color: #dc3545;
                font-size: 1rem;
            }

            .item-variant {
                color: #b5b5b5;
                font-size: 0.9rem;
                background: #fff0f1;
                padding: 4px 12px;
                border-radius: 15px;
                display: inline-block;
            }

            .item-price {
                text-align: right;
                font-weight: 700;
                color: #dc3545;
                font-size: 1.1rem;
            }

            .order-summary {
                background: #fff0f1;
                border-radius: 15px;
                padding: 25px;
                margin-top: 25px;
                border: 1px solid #f8d7da;
            }

            .summary-row {
                display: flex;
                justify-content: space-between;
                margin-bottom: 15px;
                font-size: 1rem;
                color: #dc3545;
            }

            .summary-row:last-child {
                margin-bottom: 0;
                font-weight: 700;
                color: #dc3545;
                font-size: 1.3rem;
                border-top: 2px solid #f8d7da;
                padding-top: 15px;
                margin-top: 15px;
            }

            .order-actions {
                display: flex;
                flex-direction: column;
                gap: 15px;
                margin-top: 25px;
            }

            .btn-detail {
                background: #dc3545;
                color: #fff;
                border: none;
                padding: 12px 25px;
                border-radius: 25px;
                text-decoration: none;
                font-size: 0.95rem;
                font-weight: 600;
                transition: all 0.3s ease;
                text-align: center;
                box-shadow: 0 5px 15px rgba(220,53,69,0.13);
            }

            .btn-detail:hover {
                background: #b71c1c;
                color: #fff;
                transform: translateY(-2px);
                box-shadow: 0 8px 25px rgba(220,53,69,0.18);
            }

            .empty-state {
                text-align: center;
                padding: 80px 20px;
                color: #b5b5b5;
                background: #fff;
                border-radius: 20px;
                box-shadow: 0 15px 35px rgba(220,53,69,0.08);
                border: 1px solid #f8d7da;
            }

            .empty-state i {
                font-size: 5rem;
                color: #f8d7da;
                margin-bottom: 30px;
                opacity: 0.7;
            }

            .empty-state h3 {
                color: #dc3545;
                font-size: 1.8rem;
                margin-bottom: 15px;
                font-weight: 600;
            }

            .empty-state p {
                font-size: 1.1rem;
                margin-bottom: 30px;
                color: #b5b5b5;
            }

            .empty-state .btn {
                background: #dc3545;
                border: none;
                padding: 15px 30px;
                border-radius: 25px;
                font-weight: 600;
                box-shadow: 0 5px 15px rgba(220,53,69,0.13);
                color: #fff;
                transition: all 0.3s ease;
            }

            .empty-state .btn:hover {
                background: #b71c1c;
                color: #fff;
                transform: translateY(-2px);
                box-shadow: 0 8px 25px rgba(220,53,69,0.18);
            }

            .shipping-info {
                background: #fff0f1;
                border-radius: 15px;
                padding: 20px;
                margin-bottom: 20px;
                border: 1px solid #f8d7da;
            }

            .shipping-info h6 {
                color: #dc3545;
                margin-bottom: 15px;
                font-size: 1rem;
                font-weight: 600;
                display: flex;
                align-items: center;
                gap: 8px;
            }

            .shipping-info p {
                margin-bottom: 8px;
                color: #dc3545;
                font-size: 0.95rem;
                line-height: 1.5;
            }

            .alert {
                border-radius: 15px;
                border: none;
                padding: 20px;
                margin-bottom: 25px;
                box-shadow: 0 5px 15px rgba(220,53,69,0.08);
            }

            .alert-danger {
                background: #fff0f1;
                color: #dc3545;
                border-left: 4px solid #dc3545;
            }

            @media (max-width: 768px) {
                .order-history-container {
                    padding: 15px;
                }

                .page-header {
                    padding: 30px 20px;
                }

                .page-header h1 {
                    font-size: 2rem;
                }

                .order-header {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 15px;
                }

                .order-actions {
                    flex-direction: column;
                }

                .order-item {
                    flex-direction: column;
                    align-items: flex-start;
                    gap: 15px;
                    text-align: center;
                }

                .item-price {
                    text-align: center;
                    width: 100%;
                }

                .item-image {
                    margin-right: 0;
                    margin-bottom: 10px;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="../templates/header.jsp" />

        <div class="order-history-container">
            <div class="page-header">
                <h1><i class="fas fa-history"></i> Lịch sử đặt hàng</h1>
                <p class="mb-0">Xem lại các đơn hàng đã đặt</p>
            </div>

            <c:if test="${not empty error}">
                <div class="alert alert-danger" role="alert">
                    <i class="fas fa-exclamation-triangle"></i> ${error}
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty orders}">
                    <div class="empty-state">
                        <i class="fas fa-shopping-bag"></i>
                        <h3>Chưa có đơn hàng nào</h3>
                        <p>Bạn chưa có đơn hàng nào. Hãy bắt đầu mua sắm ngay!</p>
                        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                            <i class="fas fa-shopping-cart"></i> Mua sắm ngay
                        </a>
                    </div>
                </c:when>
                <c:otherwise>
                    <c:forEach var="order" items="${orders}">
                        <div class="order-card">
                            <div class="order-header">
                                <div>
                                    <div class="order-id">Đơn hàng #${order.id}</div>
                                    <div class="order-date">
                                        <i class="fas fa-calendar"></i>
                                        ${order.orderDateFormatted}
                                    </div>
                                </div>
                                <div>
                                    <span class="order-status status-${order.status.toLowerCase()}">
                                        ${order.status}
                                    </span>
                                </div>
                            </div>

                            <!-- Thông tin giao hàng -->
                            <div class="shipping-info">
                                <h6><i class="fas fa-shipping-fast"></i> Thông tin giao hàng</h6>
                                <p><strong>Địa chỉ:</strong> ${order.shippingAddress}</p>
                                <p><strong>Số điện thoại:</strong> ${order.phoneNumber}</p>
                                <c:if test="${not empty order.note}">
                                    <p><strong>Ghi chú:</strong> ${order.note}</p>
                                </c:if>
                            </div>

                            <!-- Danh sách sản phẩm -->
                            <div class="order-items">
                                <h6><i class="fas fa-box"></i> Sản phẩm đã đặt</h6>
                                <c:forEach var="item" items="${order.orderDetails}">
                                    <div class="order-item">
                                        <c:choose>
                                            <c:when test="${not empty item.productVariant.imageURLs}">
                                                <img src="${item.productVariant.imageURLs}" 
                                                     alt="${item.productVariant.product.name}" class="item-image">
                                            </c:when>
                                            <c:when test="${not empty item.productVariant.product.thumbnailImage}">
                                                <img src="${pageContext.request.contextPath}/images/${item.productVariant.product.thumbnailImage}" 
                                                     alt="${item.productVariant.product.name}" class="item-image">
                                            </c:when>
                                            <c:otherwise>
                                                <img src="${pageContext.request.contextPath}/images/default-product.jpg" 
                                                     alt="${item.productVariant.product.name}" class="item-image">
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="item-info">
                                            <div class="item-name">${item.productVariant.product.name}</div>
                                            <div class="item-variant">
                                                ${item.productVariant.color} - ${item.productVariant.rom}GB x ${item.quantity}
                                            </div>
                                        </div>
                                        <div class="item-price">
                                            <c:choose>
                                                <c:when test="${not empty item.discountPrice}">
                                                    <fmt:formatNumber value="${item.discountPrice * item.quantity}" type="currency" currencySymbol="₫" />
                                                </c:when>
                                                <c:otherwise>
                                                    <fmt:formatNumber value="${item.unitPrice * item.quantity}" type="currency" currencySymbol="₫" />
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>

                            <!-- Tóm tắt đơn hàng -->
                            <div class="order-summary">
                                <div class="summary-row">
                                    <span>Phương thức thanh toán:</span>
                                    <span>${order.paymentMethod}</span>
                                </div>
                                <div class="summary-row">
                                    <span>Tổng tiền:</span>
                                    <span><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫" /></span>
                                </div>
                            </div>

                            <!-- Các nút hành động -->
                            <div class="order-actions">
                                <button type="button" class="btn-detail" onclick="viewOrderDetails('${order.id}')">
                                    <i class="fas fa-eye"></i> Xem chi tiết
                                </button>
                            </div>
                        </div>
                    </c:forEach>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Modal cho xem chi tiết hóa đơn -->
        <div class="modal fade" id="invoiceModal" tabindex="-1" aria-labelledby="invoiceModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-xl">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="invoiceModalLabel">
                            <i class="fas fa-file-invoice"></i> Chi tiết đơn hàng
                        </h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body" id="invoiceContent">
                        <!-- Nội dung hóa đơn sẽ được load vào đây -->
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times"></i> Đóng
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        
        <script>
            function viewOrderDetails(orderId) {
                // Tìm order card chứa order ID
                var orderCards = document.querySelectorAll('.order-card');
                var targetCard = null;
                
                for (var i = 0; i < orderCards.length; i++) {
                    var orderIdElement = orderCards[i].querySelector('.order-id');
                    if (orderIdElement && orderIdElement.textContent.includes('#' + orderId)) {
                        targetCard = orderCards[i];
                        break;
                    }
                }
                
                if (!targetCard) {
                    alert('Không tìm thấy đơn hàng!');
                    return;
                }
                
                // Clone nội dung order card
                var invoiceContent = targetCard.cloneNode(true);
                
                // Ẩn các nút hành động trong modal
                var actionButtons = invoiceContent.querySelectorAll('.order-actions');
                actionButtons.forEach(function(button) {
                    button.style.display = 'none';
                });
                
                // Thêm CSS cho modal hóa đơn
                var modalStyle = document.createElement('style');
                modalStyle.textContent = `
                    .modal-xl { max-width: 90%; }
                    .modal-body { padding: 0; }
                    .invoice-container { 
                        background: white; 
                        border: 2px solid #333; 
                        border-radius: 0; 
                        margin: 0; 
                        padding: 40px; 
                        font-family: 'Times New Roman', serif;
                        min-height: 800px;
                    }
                    .invoice-header { 
                        text-align: center; 
                        border-bottom: 3px solid #333; 
                        padding-bottom: 30px; 
                        margin-bottom: 40px; 
                    }
                    .company-name { 
                        font-size: 32px; 
                        font-weight: bold; 
                        color: #333; 
                        margin-bottom: 10px; 
                        text-transform: uppercase;
                    }
                    .company-info { 
                        font-size: 14px; 
                        color: #666; 
                        line-height: 1.6;
                    }
                    .invoice-title { 
                        font-size: 24px; 
                        font-weight: bold; 
                        color: #333; 
                        margin: 20px 0; 
                        text-align: center;
                        text-transform: uppercase;
                    }
                    .order-info { 
                        display: flex; 
                        justify-content: space-between; 
                        margin-bottom: 30px; 
                        padding: 20px; 
                        background: #f8f9fa; 
                        border: 1px solid #dee2e6;
                    }
                    .order-info-left, .order-info-right { 
                        flex: 1; 
                    }
                    .order-info-left { 
                        padding-right: 20px; 
                    }
                    .order-info-right { 
                        padding-left: 20px; 
                        border-left: 1px solid #dee2e6;
                    }
                    .info-label { 
                        font-weight: bold; 
                        color: #333; 
                        margin-bottom: 5px; 
                        font-size: 14px;
                    }
                    .info-value { 
                        color: #666; 
                        margin-bottom: 15px; 
                        font-size: 14px;
                    }
                    .shipping-info { 
                        background: #f8f9fa; 
                        border: 1px solid #dee2e6; 
                        border-radius: 0; 
                        padding: 20px; 
                        margin-bottom: 30px;
                    }
                    .shipping-info h6 { 
                        color: #333; 
                        margin-bottom: 15px; 
                        font-size: 16px; 
                        font-weight: bold;
                        text-transform: uppercase;
                        border-bottom: 1px solid #333;
                        padding-bottom: 5px;
                    }
                    .shipping-info p { 
                        margin-bottom: 8px; 
                        color: #333; 
                        font-size: 14px; 
                        line-height: 1.4;
                    }
                    .order-items { 
                        margin-bottom: 30px; 
                    }
                    .order-items h6 { 
                        color: #333; 
                        margin-bottom: 20px; 
                        font-size: 16px; 
                        font-weight: bold;
                        text-transform: uppercase;
                        border-bottom: 1px solid #333;
                        padding-bottom: 5px;
                    }
                    .order-item { 
                        display: flex; 
                        align-items: center; 
                        padding: 15px 0; 
                        border-bottom: 1px solid #eee; 
                    }
                    .order-item:last-child { 
                        border-bottom: 2px solid #333; 
                    }
                    .item-image { 
                        width: 60px; 
                        height: 60px; 
                        object-fit: cover; 
                        border-radius: 0; 
                        margin-right: 20px; 
                        border: 1px solid #ddd; 
                    }
                    .item-info { 
                        flex: 1; 
                    }
                    .item-name { 
                        font-weight: bold; 
                        margin-bottom: 5px; 
                        font-size: 14px; 
                        color: #333; 
                    }
                    .item-variant { 
                        color: #666; 
                        font-size: 12px; 
                    }
                    .item-price { 
                        text-align: right; 
                        font-weight: bold; 
                        color: #333; 
                        font-size: 14px; 
                    }
                    .order-summary { 
                        background: #f8f9fa; 
                        border: 1px solid #dee2e6; 
                        border-radius: 0; 
                        padding: 25px; 
                        margin-top: 30px;
                    }
                    .summary-row { 
                        display: flex; 
                        justify-content: space-between; 
                        margin-bottom: 15px; 
                        font-size: 14px; 
                        padding: 5px 0;
                    }
                    .summary-row:last-child { 
                        margin-bottom: 0; 
                        font-weight: bold; 
                        color: #333; 
                        font-size: 18px; 
                        border-top: 2px solid #333; 
                        padding-top: 15px; 
                        margin-top: 15px;
                    }
                    .order-status { 
                        padding: 8px 20px; 
                        border-radius: 0; 
                        font-size: 12px; 
                        font-weight: bold; 
                        text-transform: uppercase; 
                        border: 2px solid;
                    }
                    .status-pending { 
                        background: #fff3cd; 
                        color: #856404; 
                        border-color: #856404; 
                    }
                    .status-paid { 
                        background: #d4edda; 
                        color: #155724; 
                        border-color: #155724; 
                    }
                    .status-shipped { 
                        background: #cce5ff; 
                        color: #004085; 
                        border-color: #004085; 
                    }
                    .status-delivered { 
                        background: #d1ecf1; 
                        color: #0c5460; 
                        border-color: #0c5460; 
                    }
                    .status-cancelled { 
                        background: #f8d7da; 
                        color: #721c24; 
                        border-color: #721c24; 
                    }
                `;
                
                // Thêm style vào modal
                var modalBody = document.getElementById('invoiceContent');
                modalBody.innerHTML = '';
                modalBody.appendChild(modalStyle);
                
                // Tạo container hóa đơn
                var invoiceContainer = document.createElement('div');
                invoiceContainer.className = 'invoice-container';
                
                // Thêm header hóa đơn
                var invoiceHeader = document.createElement('div');
                invoiceHeader.className = 'invoice-header';
                invoiceHeader.innerHTML = `
                    <div class="company-name">Thế Giới Công Nghệ</div>
                    <div class="company-info">
                        123 Đường Huỳnh Văn Nghệ, Quận Ngũ Hành Sơn, TP.Đà Nẵng<br>
                        Điện thoại: 0775660817 | Email: PhoneStore@gmail.com<br>
                        Website: www.thegioicongnghe.com
                    </div>
                    <div class="invoice-title">Hóa Đơn Bán Hàng</div>
                `;
                
                invoiceContainer.appendChild(invoiceHeader);
                invoiceContainer.appendChild(invoiceContent);
                
                modalBody.appendChild(invoiceContainer);
                
                // Hiển thị modal
                var modal = new bootstrap.Modal(document.getElementById('invoiceModal'));
                modal.show();
            }
            
            function getStatusColor(status) {
                switch(status.toLowerCase()) {
                    case 'pending': return 'warning';
                    case 'paid': return 'success';
                    case 'shipped': return 'info';
                    case 'delivered': return 'primary';
                    case 'cancelled': return 'danger';
                    default: return 'secondary';
                }
            }
            
            function formatCurrency(amount) {
                return new Intl.NumberFormat('vi-VN', {
                    style: 'currency',
                    currency: 'VND'
                }).format(amount);
            }

            // Add loading effect to buttons
            document.querySelectorAll('.btn-detail').forEach(button => {
                button.addEventListener('click', function(e) {
                    const icon = this.querySelector('i');
                    const originalIcon = icon.className;
                    
                    icon.className = 'fas fa-spinner fa-spin';
                    this.disabled = true;
                    
                    // Re-enable after a short delay
                    setTimeout(() => {
                        icon.className = originalIcon;
                        this.disabled = false;
                    }, 2000);
                });
            });
        </script>
    </body>
</html> 
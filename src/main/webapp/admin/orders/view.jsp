<%-- 
    Document   : view
    Created on : Jun 5, 2025, 11:40:10 AM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi tiết đơn hàng - Admin</title>
        
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        
        <style>
            body {
                background-color: #f8f9fa;
            }
            
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            }
            
            .card-header {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                border-radius: 15px 15px 0 0 !important;
                border: none;
            }
            
            .status-badge {
                font-size: 0.875rem;
                padding: 0.5rem 1rem;
                border-radius: 25px;
            }
            
            .status-pending {
                background-color: #fff3cd;
                color: #856404;
            }
            
            .status-paid {
                background-color: #d1edff;
                color: #0c5460;
            }
            
            .status-cancelled {
                background-color: #f8d7da;
                color: #721c24;
            }
            
            .info-item {
                padding: 0.75rem 0;
                border-bottom: 1px solid #e9ecef;
            }
            
            .info-item:last-child {
                border-bottom: none;
            }
            
            .info-label {
                font-weight: 600;
                color: #495057;
                min-width: 120px;
            }
            
            .info-value {
                color: #212529;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid py-4">
            <!-- Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <div>
                    <h2 class="mb-0">
                        <i class="fas fa-eye me-2"></i>
                        Chi tiết đơn hàng
                    </h2>
                    <p class="text-muted mb-0">Thông tin chi tiết đơn hàng #${order.id}</p>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-outline-secondary me-2">
                        <i class="fas fa-arrow-left me-2"></i>
                        Quay lại
                    </a>
                    <c:if test="${order.status == 'Pending'}">
                        <button class="btn btn-success me-2" onclick="updateOrderStatus('${order.id}', 'Paid')">
                            <i class="fas fa-check me-2"></i>
                            Đánh dấu đã thanh toán
                        </button>
                        <button class="btn btn-danger" onclick="updateOrderStatus('${order.id}', 'Cancelled')">
                            <i class="fas fa-times me-2"></i>
                            Hủy đơn hàng
                        </button>
                    </c:if>
                </div>
            </div>
            
            <c:if test="${empty order}">
                <div class="alert alert-warning" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    Không tìm thấy đơn hàng này.
                </div>
            </c:if>
            
            <c:if test="${not empty order}">
                <div class="row">
                    <!-- Order Information -->
                    <div class="col-lg-8">
                        <div class="card mb-4">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-info-circle me-2"></i>
                                    Thông tin đơn hàng
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="info-item d-flex">
                                            <span class="info-label">Mã đơn hàng:</span>
                                            <span class="info-value ms-3"><strong>#${order.id}</strong></span>
                                        </div>
                                        <div class="info-item d-flex">
                                            <span class="info-label">Ngày đặt:</span>
                                            <span class="info-value ms-3">
                                                <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/>
                                            </span>
                                        </div>
                                        <div class="info-item d-flex">
                                            <span class="info-label">Trạng thái:</span>
                                            <span class="info-value ms-3">
                                                <c:choose>
                                                    <c:when test="${order.status == 'Pending'}">
                                                        <span class="badge status-badge status-pending">
                                                            <i class="fas fa-clock me-1"></i>
                                                            Đang chờ
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${order.status == 'Paid'}">
                                                        <span class="badge status-badge status-paid">
                                                            <i class="fas fa-check me-1"></i>
                                                            Đã thanh toán
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${order.status == 'Cancelled'}">
                                                        <span class="badge status-badge status-cancelled">
                                                            <i class="fas fa-times me-1"></i>
                                                            Đã hủy
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary">
                                                            ${order.status}
                                                        </span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="info-item d-flex">
                                            <span class="info-label">Tổng tiền:</span>
                                            <span class="info-value ms-3">
                                                <strong>
                                                    <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/>
                                                </strong>
                                            </span>
                                        </div>
                                        <div class="info-item d-flex">
                                            <span class="info-label">Địa chỉ giao hàng:</span>
                                            <span class="info-value ms-3">${order.shippingAddress}</span>
                                        </div>
                                        <div class="info-item d-flex">
                                            <span class="info-label">Số điện thoại:</span>
                                            <span class="info-value ms-3">${order.phoneNumber}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Customer Information -->
                        <div class="card mb-4">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-user me-2"></i>
                                    Thông tin khách hàng
                                </h5>
                            </div>
                            <div class="card-body">
                                <c:if test="${not empty order.user}">
                                    <div class="row">
                                        <div class="col-md-6">
                                            <div class="info-item d-flex">
                                                <span class="info-label">Họ tên:</span>
                                                <span class="info-value ms-3">${order.user.fullName}</span>
                                            </div>
                                            <div class="info-item d-flex">
                                                <span class="info-label">Email:</span>
                                                <span class="info-value ms-3">${order.user.email}</span>
                                            </div>
                                        </div>
                                        <div class="col-md-6">
                                            <div class="info-item d-flex">
                                                <span class="info-label">Số điện thoại:</span>
                                                <span class="info-value ms-3">${order.user.phoneNumber}</span>
                                            </div>
                                            <div class="info-item d-flex">
                                                <span class="info-label">Địa chỉ:</span>
                                                <span class="info-value ms-3">${order.user.address}</span>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Order Summary -->
                    <div class="col-lg-4">
                        <div class="card">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-calculator me-2"></i>
                                    Tóm tắt đơn hàng
                                </h5>
                            </div>
                            <div class="card-body">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Tổng tiền hàng:</span>
                                    <span><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/></span>
                                </div>
                                <c:if test="${not empty order.note}">
                                    <div class="d-flex justify-content-between mb-2">
                                        <span>Ghi chú:</span>
                                        <span class="text-muted">${order.note}</span>
                                    </div>
                                </c:if>
                                <hr>
                                <div class="d-flex justify-content-between">
                                    <strong>Thành tiền:</strong>
                                    <strong class="text-primary">
                                        <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/>
                                    </strong>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
        
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        
        <script>
            function updateOrderStatus(orderId, status) {
                if (confirm('Bạn có chắc chắn muốn cập nhật trạng thái đơn hàng này?')) {
                    const form = document.createElement('form');
                    form.method = 'GET';
                    form.action = '${pageContext.request.contextPath}/admin/orders';
                    
                    const actionInput = document.createElement('input');
                    actionInput.type = 'hidden';
                    actionInput.name = 'action';
                    actionInput.value = 'updateStatus';
                    
                    const orderIdInput = document.createElement('input');
                    orderIdInput.type = 'hidden';
                    orderIdInput.name = 'orderId';
                    orderIdInput.value = orderId;
                    
                    const statusInput = document.createElement('input');
                    statusInput.type = 'hidden';
                    statusInput.name = 'status';
                    statusInput.value = status;
                    
                    form.appendChild(actionInput);
                    form.appendChild(orderIdInput);
                    form.appendChild(statusInput);
                    
                    document.body.appendChild(form);
                    form.submit();
                }
            }
        </script>
    </body>
</html>

<%-- 
    Document   : list
    Created on : Jun 5, 2025, 11:39:59 AM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page import="service.OrderService"%>
<%@page import="model.Order"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.time.Instant"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.ZoneId"%>
<%@page import="java.time.format.DateTimeFormatter"%>

<%
    // Khởi tạo OrderService
    OrderService orderService = new OrderService();

    // Lấy tham số filter từ request
    String statusFilter = request.getParameter("status");

    // Lấy danh sách đơn hàng từ database
    List<Order> orders = new ArrayList<>();
    try {
        if (statusFilter != null && !statusFilter.isEmpty()) {
            orders = orderService.getOrdersByStatus(statusFilter);
        } else {
            orders = orderService.getAllOrders();
        }
    } catch (Exception e) {
        System.out.println("Error loading orders: " + e.getMessage());
        e.printStackTrace();

    }

    // Đặt vào request attribute
    request.setAttribute("orders", orders);
    request.setAttribute("statusFilter", statusFilter);

    String detailId = request.getParameter("detailId");
    model.Order detailOrder = null;
    java.util.List<model.OrderDetails> detailList = null;
    if (detailId != null) {
        try {
            int id = Integer.parseInt(detailId);
            detailOrder = new service.OrderService().getOrderWithUserById(id);
            detailList = new service.OrderDetailService().getOrderDetailsByOrderId(id);
        } catch (Exception e) {
            detailOrder = null;
            detailList = null;
        }
    }
    request.setAttribute("detailOrder", detailOrder);
    request.setAttribute("detailList", detailList);
%>

<%!
    public static String formatInstant(Instant instant) {
        if (instant == null) {
            return "";
        }
        LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý đơn hàng - Admin</title>

        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <!-- DataTables -->
        <link href="https://cdn.datatables.net/1.13.0/css/dataTables.bootstrap5.min.css" rel="stylesheet">

        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }

            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 0.25rem 1rem rgba(0, 0, 0, 0.05);
                overflow: hidden;
            }

            .card-header {
                background: linear-gradient(135deg, #d32f2f, #9a0007);
                color: white;
                font-weight: bold;
                padding: 1rem 1.5rem;
                font-size: 1.25rem;
            }

            .card-body {
                background-color: #fff;
                padding: 1.5rem;
            }

            .table th {
                background-color: #f1f3f5;
                border-top: none;
                font-weight: 600;
                color: #343a40;
                vertical-align: middle;
            }

            .table td {
                vertical-align: middle;
            }

            .status-badge {
                font-size: 0.75rem;
                padding: 0.375rem 0.75rem;
                border-radius: 20px;
                font-weight: 500;
            }

            .status-pending {
                background-color: #ffe082;
                color: #7f6000;
            }

            .status-paid {
                background-color: #c8e6c9;
                color: #2e7d32;
            }

            .status-cancelled {
                background-color: #ffcdd2;
                color: #c62828;
            }

            .btn-action {
                padding: 0.25rem 0.6rem;
                font-size: 0.875rem;
                border-radius: 0.4rem;
                transition: all 0.2s ease-in-out;
            }

            .btn-action:hover {
                background-color: #efefef;
            }

            .alert {
                border-radius: 10px;
                border: none;
                font-size: 0.9rem;
                padding: 0.75rem 1rem;
            }

            .form-select, .form-label {
                font-size: 0.9rem;
            }

            .badge {
                font-size: 0.75rem;
                padding: 0.4rem 0.6rem;
            }

            .modal-header {
                background-color: #d32f2f;
                color: white;
            }

            .modal-body {
                background-color: #fafafa;
            }

            .modal-title {
                font-size: 1.1rem;
                font-weight: bold;
            }
            .h{
                padding-top: 20px;
            }
            .lm button{
                margin-right: 50px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid py-4">
            <!-- Header -->
            <div class="row align-items-center mb-4 h">
                <div class="col-md-4 text-start d-none d-md-block"></div>

                <div class="col-md-4 text-center">
                    <h2 class="mb-0">
                        <i class="fas fa-shopping-cart me-2"></i>
                        Quản lý đơn hàng
                    </h2>
                </div>

                <div class="col-md-4 text-end lm">
                    <form method="GET" action="${pageContext.request.contextPath}/admin/orders/list.jsp" class="d-inline-block">
                        <button class="btn btn-sm btn-success" onclick="refreshOrders()" type="button">
                            <i class="fas fa-sync-alt me-1"></i> Làm mới
                        </button>
                    </form>
                </div>
            </div>

            <!-- Alesrts -->
            <c:if test="${not empty param.success}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    ${param.success}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty param.error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    ${param.error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Orders Table -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="fas fa-list me-2"></i>
                        Danh sách đơn hàng
                        <span class="badge bg-light text-dark ms-2">${orders.size()}</span>
                    </h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty orders}">
                            <div class="text-center py-5">
                                <i class="fas fa-shopping-cart fa-3x text-muted mb-3"></i>
                                <h5 class="text-muted">Không có đơn hàng nào</h5>
                                <p class="text-muted">Chưa có đơn hàng nào trong hệ thống.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover" id="ordersTable">
                                    <thead>
                                        <tr>
                                            <th>Mã đơn hàng</th>
                                            <th>Khách hàng</th>
                                            <th>Ngày đặt</th>
                                            <th>Tổng tiền</th>
                                            <th>Phương thức thanh toán</th>
                                            <th>Trạng thái</th>
                                            <th>Thao tác</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="order" items="${orders}">
                                            <tr>
                                                <td>
                                                    <strong>#${order.id}</strong>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty order.user}">
                                                        <div>
                                                            <strong>${order.user.fullName}</strong>
                                                            <br>
                                                            <small class="text-muted">${order.user.email}</small>
                                                        </div>
                                                    </c:if>
                                                    <c:if test="${empty order.user}">
                                                        <div>
                                                            <strong>User ID: ${order.user.id}</strong>
                                                            <br>
                                                            <small class="text-muted">Thông tin user không có</small>
                                                        </div>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    ${order.orderDateFormatted}
                                                </td>
                                                <td>
                                                    <strong>
                                                        <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/>
                                                    </strong>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${order.paymentMethod == 'VNPay'}">
                                                            <span class="badge bg-primary">
                                                                <i class="fas fa-credit-card me-1"></i>
                                                                VNPay
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${order.paymentMethod == 'PayPal'}">
                                                            <span class="badge bg-info">
                                                                <i class="fab fa-paypal me-1"></i>
                                                                PayPal
                                                            </span>
                                                        </c:when>
                                                        <c:when test="${order.paymentMethod == 'COD'}">
                                                            <span class="badge bg-warning">
                                                                <i class="fas fa-money-bill-wave me-1"></i>
                                                                Thanh toán khi nhận hàng
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">
                                                                ${order.paymentMethod}
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
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
                                                </td>
                                                <td>
                                                    <div class="btn-group" role="group">
                                                        <form method="get" style="display:inline;">
                                                            <input type="hidden" name="detailId" value="${order.id}"/>
                                                            <button type="submit" class="btn btn-sm btn-outline-info btn-action" title="Xem chi tiết">
                                                                <i class="fas fa-eye"></i>
                                                            </button>
                                                        </form>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                                <!-- Order Details Sections (Outside DataTable) -->
                                <c:forEach var="order" items="${orders}">
                                    <div class="order-details-section" id="order-details-${order.id}" style="display: none;">
                                        <div class="card mt-2">
                                            <div class="card-body">
                                                <h6 class="card-title">Chi tiết đơn hàng #${order.id}</h6>
                                                <div class="row">
                                                    <div class="col-md-6">
                                                        <p><strong>Địa chỉ giao hàng:</strong> ${order.shippingAddress}</p>
                                                        <p><strong>Số điện thoại:</strong> ${order.phoneNumber}</p>
                                                        <p><strong>Phương thức thanh toán:</strong> 
                                                            <c:choose>
                                                                <c:when test="${order.paymentMethod == 'VNPay'}">
                                                                    <span class="badge bg-primary">VNPay</span>
                                                                </c:when>
                                                                <c:when test="${order.paymentMethod == 'PayPal'}">
                                                                    <span class="badge bg-info">PayPal</span>
                                                                </c:when>
                                                                <c:when test="${order.paymentMethod == 'COD'}">
                                                                    <span class="badge bg-warning">Thanh toán khi nhận hàng</span>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <span class="badge bg-secondary">${order.paymentMethod}</span>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </p>
                                                    </div>
                                                    <div class="col-md-6">
                                                        <p><strong>Ghi chú:</strong> ${order.note}</p>
                                                        <p><strong>Ngày đặt:</strong> ${order.orderDateFormatted}</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <!-- jQuery -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <!-- DataTables -->
        <script src="https://cdn.datatables.net/1.13.0/js/jquery.dataTables.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.0/js/dataTables.bootstrap5.min.js"></script>

        <script>
                // Get context path from JSP
                var contextPath = '${pageContext.request.contextPath}';

                $(document).ready(function () {
                    // Initialize DataTable
                    $('#ordersTable').DataTable({
                        language: {
                            url: '//cdn.datatables.net/plug-ins/1.13.0/i18n/vi.json'
                        },
                        pageLength: 25,
                        order: [[0, 'desc']],
                        responsive: true
                    });
                });

                function refreshOrders() {
                    window.location.reload();
                }

                function showOrderDetails(orderId) {
                    // Hiện modal và loading
                    var modal = new bootstrap.Modal(document.getElementById('orderDetailModal'));
                    modal.show();
                    document.getElementById('orderDetailModalBody').innerHTML = '<div class="text-center text-muted"><i class="fas fa-spinner fa-spin"></i> Đang tải...</div>';
                    // Gửi AJAX lấy chi tiết
                    fetch(contextPath + '/admin/orders?action=details&id=' + orderId)
                            .then(response => response.text())
                            .then(html => {
                                document.getElementById('orderDetailModalBody').innerHTML = html;
                            })
                            .catch(error => {
                                document.getElementById('orderDetailModalBody').innerHTML = '<div class="text-danger">Không thể tải chi tiết đơn hàng!</div>';
                            });
                }
        </script>

        <!-- Modal hiển thị chi tiết đơn hàng -->
        <div class="modal fade" id="orderDetailModal" tabindex="-1" aria-labelledby="orderDetailModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="orderDetailModalLabel">Chi tiết đơn hàng</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Đóng"></button>
                    </div>
                    <div class="modal-body">
                        <h5>Thông tin đơn hàng #${detailOrder.id}</h5>
                        <p>Khách hàng: <strong>${detailOrder.user.fullName}</strong></p>
                        <p>Ngày đặt: ${detailOrder.orderDateFormatted}</p>
                        <p>Địa chỉ giao hàng: ${detailOrder.shippingAddress}</p>
                        <p>Số điện thoại: ${detailOrder.phoneNumber}</p>
                        <p>Ghi chú: ${detailOrder.note}</p>
                        <hr>
                        <h6>Danh sách sản phẩm</h6>
                        <table class="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Sản phẩm</th>
                                    <th>Giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="d" items="${detailList}">
                                    <tr>
                                        <td>
                                            <c:choose>
                                                <c:when test="${not empty d.productVariant and not empty d.productVariant.product}">
                                                    ${d.productVariant.product.name} (${d.productVariant.variantName})
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-danger">Thiếu thông tin sản phẩm</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><fmt:formatNumber value="${d.unitPrice}" type="currency" currencySymbol="₫"/></td>
                                        <td>${d.quantity}</td>
                                        <td><fmt:formatNumber value="${d.totalPrice}" type="currency" currencySymbol="₫"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>

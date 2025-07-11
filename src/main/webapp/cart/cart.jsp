<%-- 
    Document   : cart
    Created on : Jul 5, 2025, 2:35:57 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page import="java.time.LocalDateTime" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
    // Set current time for discount checking
    request.setAttribute("currentTime", LocalDateTime.now());
    // Set current user if not already set
    if (request.getAttribute("currentUser") == null) {
        request.setAttribute("currentUser", session.getAttribute("user"));
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Giỏ hàng - Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cart.css?v=2.0">
    </head>
    <body>
        <jsp:include page="../templates/header.jsp" />

        <div class="cart-container">
            <div class="cart-header">
                <h1><i class="fas fa-shopping-cart"></i> Giỏ hàng của bạn</h1>
                <p class="mb-0">Quản lý sản phẩm trong giỏ hàng</p>
            </div>

            <div class="row">
                <div class="col-lg-8">                 
                    <c:choose>
                        <c:when test="${empty cart}">
                            <div class="empty-cart">
                                <i class="fas fa-shopping-cart"></i>
                                <h3>Giỏ hàng trống</h3>
                                <p>Bạn chưa có sản phẩm nào trong giỏ hàng</p>
                                <a href="${pageContext.request.contextPath}/" class="continue-shopping">
                                    <i class="fas fa-arrow-left"></i> Tiếp tục mua sắm
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="d-flex justify-content-between align-items-center mb-3">
                                <div class="d-flex align-items-center">
                                    <div class="form-check me-3">
                                        <form action="${pageContext.request.contextPath}/carts" method="post" style="display:inline;">
                                            <input type="hidden" name="action" value="selectAll"/>
                                            <input type="hidden" name="selectAll" id="selectAllValue" value="true"/>
                                            <input type="checkbox" id="selectAll" onchange="updateSelectAll(this);"/>
                                            <label for="selectAll"><strong>Chọn tất cả</strong></label>
                                        </form>
                                    </div>
                                    <h4 class="mb-0">Sản phẩm (${fn:length(cart)} sản phẩm)</h4>
                                </div>
                            </div>

                            <c:forEach var="item" items="${cart}">
                                <c:set var="variant" value="${item.productVariant}" />
                                <c:set var="product" value="${variant.product}" />
                                <div class="cart-item" id="cart-item-${variant.id}">
                                    <div class="row align-items-center">
                                        <div class="col-md-1 col-2">
                                            <div class="form-check">
                                                <form action="${pageContext.request.contextPath}/carts" method="post" style="display:inline;">
                                                    <input type="hidden" name="action" value="${item.selected ? 'unselect' : 'select'}"/>
                                                    <input type="hidden" name="variantId" value="${variant.id}"/>
                                                    <input class="form-check-input item-checkbox" type="checkbox" 
                                                           id="item-${variant.id}" 
                                                           data-variant-id="${variant.id}"
                                                           ${item.selected ? 'checked' : ''}
                                                           onchange="this.form.submit();">
                                                </form>
                                            </div>
                                        </div>
                                        <div class="col-md-2 col-3">
                                            <c:choose>
                                                <c:when test="${not empty variant.imageURLs}">
                                                    <img src="${variant.imageURLs}" 
                                                         alt="${product.name}" class="product-image">
                                                </c:when>
                                                <c:when test="${not empty product.thumbnailImage}">
                                                    <img src="${pageContext.request.contextPath}/images/${product.thumbnailImage}" 
                                                         alt="${product.name}" class="product-image">
                                                </c:when>
                                                <c:otherwise>
                                                    <img src="${pageContext.request.contextPath}/images/default-product.jpg" 
                                                         alt="${product.name}" class="product-image">
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="col-md-4 col-6">
                                            <div class="product-info">
                                                <h5>${product.name}</h5>
                                                <div class="product-variant">
                                                    <i class="fas fa-palette"></i> Màu: ${variant.color}
                                                </div>
                                                <div class="product-variant">
                                                    <i class="fas fa-hdd"></i> ROM: ${variant.rom}GB
                                                </div>
                                                <c:set var="hasDiscount" value="${not empty variant.discountPrice and not empty variant.discountExpiry and variant.discountExpiry > currentTime}" />
                                                <c:if test="${hasDiscount}">
                                                    <span class="discount-badge">
                                                        <i class="fas fa-tag"></i> Giảm giá
                                                    </span>
                                                </c:if>
                                            </div>
                                        </div>
                                        <div class="col-md-2 col-3">
                                            <div class="quantity-controls">
                                                <form action="${pageContext.request.contextPath}/carts" method="post" class="update-qty-form" style="display:inline;">
                                                    <input type="hidden" name="action" value="update"/>
                                                    <input type="hidden" name="variantId" value="${variant.id}"/>
                                                    <button type="button" class="quantity-btn" onclick="this.form.quantity.value=Math.max(1,parseInt(this.form.quantity.value)-1); this.form.submit();">
                                                        <i class="fas fa-minus"></i>
                                                    </button>
                                                    <input type="number" name="quantity" class="quantity-input" value="${item.quantity}" 
                                                           min="1" data-original-quantity="${item.quantity}" onchange="this.form.submit();">
                                                    <button type="button" class="quantity-btn" onclick="this.form.quantity.value=parseInt(this.form.quantity.value)+1; this.form.submit();">
                                                        <i class="fas fa-plus"></i>
                                                    </button>
                                                </form>
                                            </div>
                                        </div>
                                        <div class="col-md-3 col-6">
                                            <div class="price-info">
                                                <c:choose>
                                                    <c:when test="${hasDiscount}">
                                                        <div class="original-price">
                                                            <fmt:formatNumber value="${variant.price}" type="currency" currencySymbol="₫" />
                                                        </div>
                                                        <div class="current-price">
                                                            <fmt:formatNumber value="${variant.discountPrice * item.quantity}" type="currency" currencySymbol="₫" />
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="current-price">
                                                            <fmt:formatNumber value="${variant.price * item.quantity}" type="currency" currencySymbol="₫" />
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                        <div class="col-md-1 col-3 text-end">
                                            <form action="${pageContext.request.contextPath}/carts" method="post" style="display:inline;">
                                                <input type="hidden" name="action" value="remove"/>
                                                <input type="hidden" name="variantId" value="${variant.id}"/>
                                                <button type="submit" class="remove-btn" 
                                                        onclick="return removeItem(this.form);">
                                                    <i class="fas fa-trash"></i>
                                                </button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:otherwise>
                    </c:choose>
                </div>

                <div class="col-lg-4">
                    <div class="cart-summary">
                        <h4><i class="fas fa-calculator"></i> Tổng đơn hàng</h4>

                        <c:if test="${not empty cart}">
                            <div class="summary-item">
                                <span>Tạm tính:</span>
                                <span id="subtotal"><fmt:formatNumber value="${selectedTotal}" type="currency" currencySymbol="₫" /></span>
                            </div>
                            <div class="summary-item">
                                <span>Phí vận chuyển:</span>
                                <span>Miễn phí</span>
                            </div>
                            <div class="summary-item summary-total">
                                <span>Tổng cộng:</span>
                                <span id="total"><fmt:formatNumber value="${selectedTotal}" type="currency" currencySymbol="₫" /></span>
                            </div>

                            <a href="${pageContext.request.contextPath}/cart/confirm.jsp" class="checkout-btn">
                                <i class="fas fa-credit-card"></i> Tiến hành thanh toán
                            </a>

                            <div class="text-center mt-3">
                                <small class="text-muted">
                                    <i class="fas fa-shield-alt"></i> Bảo mật thanh toán
                                </small>
                            </div>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <!-- <script src="${pageContext.request.contextPath}/js/cart.js"></script> -->
        <script>
            // Initialize select all checkbox state and visual feedback
            document.addEventListener('DOMContentLoaded', function() {
                const selectAllCheckbox = document.getElementById('selectAll');
                const itemCheckboxes = document.querySelectorAll('.item-checkbox');
                
                if (selectAllCheckbox && itemCheckboxes.length > 0) {
                    const checkedCount = Array.from(itemCheckboxes).filter(cb => cb.checked).length;
                    const totalCount = itemCheckboxes.length;
                    
                    if (checkedCount === 0) {
                        selectAllCheckbox.indeterminate = false;
                        selectAllCheckbox.checked = false;
                    } else if (checkedCount === totalCount) {
                        selectAllCheckbox.indeterminate = false;
                        selectAllCheckbox.checked = true;
                    } else {
                        selectAllCheckbox.indeterminate = true;
                        selectAllCheckbox.checked = false;
                    }
                }
                
                // Set initial visual state for cart items
                document.querySelectorAll('.cart-item').forEach(item => {
                    const checkbox = item.querySelector('.item-checkbox');
                    if (checkbox && checkbox.checked) {
                        item.classList.add('selected');
                    } else {
                        item.classList.remove('selected');
                    }
                });
            });

            // Function to handle select all checkbox
            function updateSelectAll(checkbox) {
                const selectAllValue = document.getElementById('selectAllValue');
                selectAllValue.value = checkbox.checked ? 'true' : 'false';
                checkbox.form.submit();
            }

            // Function to handle remove item with confirmation
            function removeItem(form) {
                if (confirm('Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?')) {
                    // Disable button để tránh submit 2 lần
                    const submitBtn = form.querySelector('button[type="submit"]');
                    if (submitBtn) {
                        submitBtn.disabled = true;
                        submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
                    }
                    // Submit form ngay lập tức
                    form.submit();
                    return false; // Ngăn không cho form submit tự động
                }
                return false;
            }
        </script>
    </body>
</html>

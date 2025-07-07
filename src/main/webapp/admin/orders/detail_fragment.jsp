<%@page contentType="text/html" pageEncoding="UTF-8"%>
<meta charset="UTF-8">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h5>Thông tin đơn hàng #${order.id}</h5>
<p>Khách hàng: <strong>${order.user.fullName}</strong></p>
<p>Ngày đặt: ${order.orderDateFormatted}</p>
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
        <c:forEach var="d" items="${details}">
            <tr>
                <td>${d.productVariant.product.name} (${d.productVariant.variantName})</td>
                <td><fmt:formatNumber value="${d.unitPrice}" type="currency" currencySymbol="₫"/></td>
                <td>${d.quantity}</td>
                <td><fmt:formatNumber value="${d.totalPrice}" type="currency" currencySymbol="₫"/></td>
            </tr>
        </c:forEach>
    </tbody>
</table> 
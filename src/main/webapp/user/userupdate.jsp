<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ page import="model.User" %>
        <% User user=(User) session.getAttribute("user"); if (user==null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp" ); return; } %>
            <!DOCTYPE html>
            <html>

            <head>
                <title>Cập nhật thông tin</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
                <style>
                    .update-form-container {
                        max-width: 700px;
                        margin: 40px auto;
                        background: #fff;
                        border-radius: 15px;
                        box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
                        padding: 2.5rem 2.5rem 2rem 2.5rem;
                    }

                    .form-label {
                        font-weight: 500;
                    }

                    .btn-primary {
                        background: #ea1d25;
                        border: none;
                    }

                    .btn-primary:hover {
                        background: #b71c1c;
                    }

                    .form-check-label {
                        font-size: 0.95rem;
                    }

                    .update-title {
                        color: #ea1d25;
                        font-weight: bold;
                        text-align: center;
                        margin-bottom: 2rem;
                        font-size: 2rem;
                    }

                    .form-text {
                        font-size: 0.95rem;
                    }

                    .btn-outline-secondary {
                        border: 1px solid #ccc;
                    }

                    .form-control {
                        font-size: 1.1rem;
                        padding: 0.75rem 1rem;
                    }

                    .alert {
                        border-radius: 10px;
                        border: none;
                        padding: 15px 20px;
                        margin-bottom: 25px;
                    }

                    .alert-warning {
                        background: linear-gradient(135deg, #fff3cd, #ffeaa7);
                        color: #856404;
                        border-left: 4px solid #ffc107;
                    }

                    .alert-info {
                        background: linear-gradient(135deg, #d1ecf1, #bee5eb);
                        color: #0c5460;
                        border-left: 4px solid #17a2b8;
                    }

                    @media (max-width: 768px) {
                        .update-form-container {
                            padding: 1rem;
                        }
                    }
                </style>
            </head>

            <body style="background: #f8f9fa;">
                <jsp:include page="/templates/header.jsp" />
                <div class="update-form-container">
                    <div class="update-title">Cập nhật thông tin</div>
                    
                    <!-- Hiển thị thông báo lỗi nếu có -->
                    <c:if test="${param.error == 'no_address' and not empty param.message}">
                        <div class="alert alert-warning" role="alert">
                            <i class="fas fa-exclamation-triangle"></i>
                            <strong>Thông báo:</strong> ${param.message}
                        </div>
                    </c:if>
                    
                    <!-- Hiển thị thông báo thông thường -->
                    <c:if test="${not empty param.message and param.error != 'no_address'}">
                        <div class="alert alert-info" role="alert">
                            <i class="fas fa-info-circle"></i>
                            ${param.message}
                        </div>
                    </c:if>
                    
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <input type="hidden" name="action" value="update-profile">
                        <div class="row mb-3">
                            <div class="col">
                                <label class="form-label">Họ và tên</label>
                                <input type="text" class="form-control" name="fullname"
                                    value="<%= user.getFullName() %>" required>
                            </div>
                            <div class="col">
                                <label class="form-label">Ngày sinh</label>
                                <input type="date" class="form-control" name="dob"
                                    value="<%= user.getDob() != null ? user.getDob().toString() : "" %>">
                            </div>
                        </div>
                        <div class="row mb-3">
                            <div class="col">
                                <label class="form-label">Số điện thoại</label>
                                <input type="text" class="form-control" name="phone"
                                    value="<%= user.getPhoneNumber() != null ? user.getPhoneNumber() : "" %>">
                            </div>
                            <div class="col">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" style="min-width:260px;max-width:100%;"
                                    value="<%= user.getEmail() %>" readonly>
                            </div>
                        </div>
                        <div class="row mb-3 address-row">
                            <div class="col-md-4 mb-2">
                                <label class="form-label">Tỉnh/Thành phố</label>
                                <select class="form-select" id="province" name="province" required>
                                    <option value="">Chọn tỉnh/thành phố</option>
                                </select>
                            </div>
                            <div class="col-md-4 mb-2">
                                <label class="form-label">Quận/Huyện</label>
                                <select class="form-select" id="district" name="district" required disabled>
                                    <option value="">Chọn quận/huyện</option>
                                </select>
                            </div>
                            <div class="col-md-4 mb-2">
                                <label class="form-label">Phường/Xã</label>
                                <select class="form-select" id="ward" name="ward" required disabled>
                                    <option value="">Chọn phường/xã</option>
                                </select>
                            </div>
                        </div>
                        <input type="hidden" name="address" id="address">
                        <div class="form-check mb-3">
                            <input class="form-check-input" type="checkbox" name="subscribe" id="subscribe">
                            <label class="form-check-label" for="subscribe">
                                Đăng ký nhận tin khuyến mãi từ PhoneStore
                            </label>
                        </div>
                        <div class="form-text mb-3">
                            Bằng việc Đăng ký, bạn đã đọc và đồng ý với <a href="#">Điều khoản sử dụng</a> và <a
                                href="#">Chính sách bảo mật của PhoneStore</a>.
                        </div>
                        <div class="d-flex justify-content-between">
                            <c:choose>
                                <c:when test="${param.error == 'no_address'}">
                                    <a href="${pageContext.request.contextPath}/cart/confirm.jsp" 
                                       class="btn btn-outline-secondary">
                                        <i class="fas fa-arrow-left"></i> Quay lại giỏ hàng
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/user/login.jsp"
                                       class="btn btn-outline-secondary">&lt; Quay lại đăng nhập</a>
                                </c:otherwise>
                            </c:choose>
                            <button type="submit" class="btn btn-primary">Hoàn tất đăng ký</button>
                        </div>
                    </form>
                </div>
                <jsp:include page="/templates/footer.jsp" />
                <script src="${pageContext.request.contextPath}/js/address.js"></script>
            </body>

            </html>
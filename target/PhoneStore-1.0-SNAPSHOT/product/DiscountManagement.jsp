<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="java.util.List, model.Category" %>

<jsp:useBean id="productService" scope="page" class="service.ProductService"/>
<%
    if (request.getAttribute("categories") == null) {
        request.setAttribute("categories", productService.getAllCategories());
    }
%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Quản lý Khuyến Mãi</title>
        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <!-- Bootstrap Icons CDN -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
        <!-- Select2 CSS -->
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <!-- jQuery and Select2 JS -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

        <style>
            /* General body styling */
            body {
                background-color: #f5f5f5;
                color: #333;
                font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
                margin: 0;
                padding: 0;
            }

            /* Container styling */
            .container {
                background-color: #fff;
                padding: 2.5rem;
                border-radius: 12px;
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
                margin: 3rem auto;
                max-width: 1200px;
            }

            /* Header styling */
            h2.text-primary {
                color: #d32f2f !important;
                font-weight: 700;
                font-size: 2rem;
                margin-bottom: 2rem;
                text-align: center;
                text-transform: uppercase;
                position: relative;
            }

            h2.text-primary::after {
                content: '';
                position: absolute;
                left: 50%;
                bottom: -8px;
                transform: translateX(-50%);
                width: 80px;
                height: 4px;
                background: linear-gradient(to right, #ef5350, #d32f2f);
                border-radius: 2px;
            }

            /* Form card styling */
            .form-card {
                background-color: #fff;
                border-radius: 10px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
                padding: 1.5rem;
                margin-bottom: 2rem;
                transition: box-shadow 0.3s ease;
            }

            .form-card:hover {
                box-shadow: 0 6px 18px rgba(211, 47, 47, 0.15);
            }

            /* Form labels */
            .form-label {
                font-weight: 600;
                color: #d32f2f;
                margin-bottom: 0.5rem;
            }

            /* Form controls */
            .form-select, .form-control {
                border: 1px solid #dee2e6;
                border-radius: 8px;
                padding: 0.75rem;
                font-size: 0.95rem;
                transition: border-color 0.2s ease, box-shadow 0.2s ease;
            }

            .form-select:focus, .form-control:focus {
                border-color: #ef5350;
                box-shadow: 0 0 0 0.2rem rgba(239, 83, 80, 0.2);
                outline: none;
            }

            /* Select2 custom styling */
            .select2-container--default .select2-selection--single {
                border: 1px solid #dee2e6;
                border-radius: 8px;
                height: 38px;
                padding: 0.5rem;
            }

            .select2-container--default .select2-selection--single .select2-selection__rendered {
                line-height: 24px;
            }

            .select2-container--default .select2-selection--single .select2-selection__arrow {
                height: 36px;
            }

            /* Buttons */
            .btn-primary {
                background-color: #ef5350;
                border-color: #ef5350;
                font-weight: 600;
                padding: 0.75rem 1.5rem;
                border-radius: 8px;
                transition: all 0.3s ease;
            }

            .btn-primary:hover, .btn-primary:focus {
                background-color: #d32f2f;
                border-color: #d32f2f;
                box-shadow: 0 4px 12px rgba(211, 47, 47, 0.3);
                transform: translateY(-1px);
            }

            .btn-danger {
                background-color: #dc3545;
                border-color: #dc3545;
                font-weight: 600;
                padding: 0.75rem 1.5rem;
                border-radius: 8px;
                transition: all 0.3s ease;
            }

            .btn-danger:hover, .btn-danger:focus {
                background-color: #c82333;
                border-color: #c82333;
                box-shadow: 0 4px 12px rgba(220, 53, 69, 0.3);
                transform: translateY(-1px);
            }

            /* Custom style for View Discounted Products button */
            .btn-view-discounted {
                background-color: #c62828 !important;
                border-color: #c62828 !important;
                font-weight: 600;
                padding: 0.75rem 1.5rem;
                border-radius: 8px;
                color: #fff !important;
                display: inline-flex;
                align-items: center;
                gap: 0.5rem;
                transition: all 0.3s ease;
            }

            .btn-view-discounted:hover, .btn-view-discounted:focus {
                background-color: #b71c1c;
                border-color: #b71c1c;
                box-shadow: 0 4px 12px rgba(183, 28, 28, 0.3);
                transform: translateY(-1px);
                animation: pulse 0.6s ease-in-out;
            }

            /* Pulse animation */
            @keyframes pulse {
                0% {
                    transform: scale(1);
                }
                50% {
                    transform: scale(1.05);
                }
                100% {
                    transform: scale(1);
                }
            }

            /* Alerts */
            .alert {
                border-radius: 8px;
                padding: 1rem;
                font-size: 0.95rem;
            }

            .alert-success {
                background-color: #e8f4e8;
                border-color: #c3e6cb;
                color: #155724;
            }

            .alert-danger {
                background-color: #f8d7da;
                border-color: #f5c6cb;
                color: #721c24;
            }

            .alert-warning {
                background-color: #fff3cd;
                border-color: #ffeeba;
                color: #856404;
            }

            /* Responsive adjustments */
            @media (max-width: 768px) {
                .container {
                    padding: 1.5rem;
                    margin: 1.5rem;
                }

                h2.text-primary {
                    font-size: 1.75rem;
                }

                .form-card {
                    padding: 1rem;
                }

                .btn-primary, .btn-danger, .btn-view-discounted {
                    padding: 0.6rem 1.2rem;
                    font-size: 0.9rem;
                }
            }
        </style>
    </head>
    <body>

        <jsp:include page="/templates/header.jsp" />

        <div class="container">
            <h2 class="text-primary">Quản lý Khuyến Mãi</h2>


            <c:if test="${not empty successMessage}">
                <div class="alert alert-success">${successMessage}</div>
            </c:if>
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger">${errorMessage}</div>
            </c:if>

            <div class="row">
                <!-- appply Discount Form -->
                <div class="col-md-6">
                    <div class="form-card">
                        <h4 class="mb-4" style="color: #d32f2f;">Áp dụng Khuyến Mãi</h4>
                        <form action="products" method="post">
                            <input type="hidden" name="action" value="applyDiscount">

                            <div class="mb-3">
                                <label for="categoryId" class="form-label">Danh mục sản phẩm</label>
                                <select name="categoryId" id="categoryId" class="form-select">
                                    <option value="">Tất cả danh mục</option>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.id}">${category.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="existingProduct" class="form-label">Chọn sản phẩm</label>
                                <select id="existingProduct" name="productId" class="form-select" style="width: 100%;"></select>
                            </div>

                            <div class="mb-3">
                                <label for="discountPercent" class="form-label">Phần trăm giảm giá (%)</label>
                                <input type="number" name="discountPercent" id="discountPercent" class="form-control" min="1" max="99" required>
                            </div>

                            <div class="mb-3">
                                <label for="expireDate" class="form-label">Ngày hết hạn</label>
                                <input type="date" name="expireDate" id="expireDate" class="form-control" required>
                                <script>
                                    const today = new Date();
                                    const yyyy = today.getFullYear();
                                    const mm = String(today.getMonth() + 1).padStart(2, '0');
                                    const dd = String(today.getDate()).padStart(2, '0');
                                    document.getElementById('expireDate').min = `${yyyy}-${mm}-${dd}`;
                                </script>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">Áp dụng Khuyến Mãi</button>
                        </form>
                    </div>
                </div>

                <!-- Remove Discount Form -->
                <div class="col-md-6">
                    <div class="form-card">
                        <h4 class="mb-4" style="color: #d32f2f;">Xóa Khuyến Mãi</h4>
                        <form action="products" method="post">
                            <input type="hidden" name="action" value="removeDiscount">

                            <div class="mb-3">
                                <label for="removeCategory" class="form-label">Danh mục sản phẩm</label>
                                <select name="categoryId" id="removeCategory" class="form-select">
                                    <option value="">Tất cả danh mục</option>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.id}">${category.name}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label for="removeSearchName" class="form-label">Tên sản phẩm</label>
                                <input type="text" name="searchName" id="removeSearchName" class="form-control" placeholder="Để trống nếu muốn xóa cho tất cả">
                            </div>

                            <div class="alert alert-warning">
                                <strong>Lưu ý:</strong> Hành động này sẽ xóa tất cả khuyến mãi của các sản phẩm phù hợp với điều kiện trên.
                            </div>

                            <button type="submit" class="btn btn-danger w-100">Xóa Khuyến Mãi</button>
                        </form>
                    </div>
                </div>
            </div>

            <!-- View Discounted Products Link -->
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/products?action=showDiscountedProducts" class="btn btn-view-discounted">
                    <i class="bi bi-arrow-down-circle"></i> Xem Danh Sách Sản Phẩm Khuyến Mãi
                </a>
            </div>
        </div>

        <!-- Select2 AJAX Search -->
        <script>
            $(document).ready(function () {
                $('#existingProduct').select2({
                    placeholder: "Tìm sản phẩm...",
                    allowClear: true,
                    ajax: {
                        url: '${pageContext.request.contextPath}/products?action=search',
                        dataType: 'json',
                        delay: 250,
                        data: function (params) {
                            return {term: params.term};
                        },
                        processResults: function (data) {
                            return {
                                results: $.map(data, function (product) {
                                    return {id: product.id, text: product.text};
                                })
                            };
                        },
                        cache: true
                    },
                    minimumInputLength: 1
                });


                $('form').on('submit', function (e) {
                    const existing = $('#existingProduct').val();
                    if (!existing) {
                        alert('Vui lòng chọn sản phẩm.');
                        e.preventDefault();
                    }
                });
            });
        </script>


        <jsp:include page="/templates/footer.jsp" />
    </body>
</html>
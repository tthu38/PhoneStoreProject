<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thêm Sản Phẩm Mới</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- Select2 -->
        <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>

        <style>
            body {
                background-color: #F8FAFC;
                font-family: 'Segoe UI', sans-serif;
                padding: 20px;
            }
            .frm {
                max-width: 1280px;
                margin: 0 auto;
                background: #fff;
                border-radius: 16px;
                padding: 40px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.05);
            }
            .hdr {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 30px;
            }
            .hdr a {
                font-size: 16px;
                color: #DC2626;
                text-decoration: none;
                padding: 10px 14px;
                background: #FEE2E2;
                border-radius: 8px;
                font-weight: 500;
            }
            .hdr .ttl {
                font-size: 26px;
                font-weight: 600;
                color: #111827;
            }
            .hdr .act button {
                background: #FEE2E2;
                border: none;
                padding: 10px 14px;
                border-radius: 8px;
                margin-left: 10px;
                font-size: 14px;
                color: #DC2626;
                font-weight: 500;
            }
            .cnt {
                display: grid;
                grid-template-columns: 2fr 1fr;
                gap: 30px;
            }
            .sec {
                background: #F1F5F9;
                border-radius: 12px;
                padding: 25px;
                margin-bottom: 30px;
            }
            .sec h2 {
                font-size: 18px;
                font-weight: 600;
                margin-bottom: 20px;
                color: #1E293B;
            }
            .grp {
                margin-bottom: 22px;
            }
            .grp label {
                display: block;
                font-weight: 500;
                margin-bottom: 6px;
                color: #334155;
            }
            .grp input,
            .grp select,
            .grp textarea {
                width: 100%;
                border: 1px solid #CBD5E1;
                border-radius: 10px;
                padding: 12px 16px;
                font-size: 15px;
                background: #F8FAFC;
            }
            .grp textarea {
                resize: vertical;
                height: 100px;
            }
            .rom-buttons {
                display: flex;
                gap: 10px;
                flex-wrap: wrap;
            }
            .rom-buttons input[type="radio"] {
                display: none;
            }
            .rom-buttons label {
                padding: 10px 16px;
                background-color: #E2E8F0;
                border-radius: 8px;
                cursor: pointer;
                font-weight: 500;
            }
            .rom-buttons input[type="radio"]:checked + label {
                background-color: #DC2626;
                color: white;
            }
            .btns {
                display: flex;
                justify-content: flex-end;
                margin-top: 40px;
                gap: 15px;
            }
            .btns .discard {
                background: #FEE2E2;
                color: #B91C1C;
                padding: 12px 24px;
                border: none;
                border-radius: 10px;
                font-weight: 500;
            }
            .btns .add-product {
                background: #DC2626;
                color: white;
                padding: 12px 24px;
                border: none;
                border-radius: 10px;
                font-weight: 500;
            }
            .btns .add-product:hover {
                background: #B91C1C;
            }
            @media (max-width: 768px) {
                .cnt {
                    grid-template-columns: 1fr;
                }
            }
        </style>
    </head>
    <body>

        <form action="${pageContext.request.contextPath}/products?action=create" method="post" enctype="multipart/form-data">
            <div class="frm">
                <div class="hdr">
                    <a href="${pageContext.request.contextPath}/products">&larr; Danh sách sản phẩm</a>
                    <h1 class="ttl">THÊM SẢN PHẨM</h1>
                    <div class="act">
                        <button type="button">🔔</button>
                        <button type="button">👁️ Xem Shop</button>
                    </div>
                </div>

                <div class="cnt">
                    <div>
                        <div class="sec">
                            <h2>Thông tin sản phẩm</h2>
                            <div class="grp">
                                <label for="existingProduct">Chọn sản phẩm đã có (nếu có)</label>
                                <select id="existingProduct" name="productId" style="width: 100%"></select>
                            </div>
                            <div class="grp">
                                <label for="product-name">Tên sản phẩm (nếu mới)</label>
                                <input type="text" id="product-name" name="productName" placeholder="Ví dụ: iPhone 11" />
                            </div>
                            <div class="grp">
                                <label for="description">Mô tả</label>
                                <textarea id="description" name="description" placeholder="Mô tả sản phẩm..."></textarea>
                            </div>
                            <div class="grp">
                                <label>Dung lượng ROM</label>
                                <div class="rom-buttons">
                                    <input type="radio" id="rom64" name="rom" value="64" checked>
                                    <label for="rom64">64GB</label>
                                    <input type="radio" id="rom128" name="rom" value="128">
                                    <label for="rom128">128GB</label>
                                    <input type="radio" id="rom256" name="rom" value="256">
                                    <label for="rom256">256GB</label>
                                    <input type="radio" id="rom512" name="rom" value="512">
                                    <label for="rom512">512GB</label>
                                    <input type="radio" id="rom1024" name="rom" value="1024">
                                    <label for="rom1024">1TB</label>
                                </div>
                            </div>
                            <div class="grp">
                                <label for="color">Màu sắc</label>
                                <select id="color" name="color" required>
                                    <option value="Đen">Đen</option>
                                    <option value="Trắng">Trắng</option>
                                    <option value="Xanh">Xanh</option>
                                    <option value="Đỏ">Đỏ</option>
                                    <option value="Tím">Tím</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div>
                        <div class="sec">
                            <h2>Hình ảnh & thương hiệu</h2>
                            <div class="grp">
                                <label for="brand">Thương hiệu</label>
                                <select id="brand" name="brandID" required>
                                    <option value="1">Apple</option>
                                    <option value="2">Samsung</option>
                                    <option value="3">Xiaomi</option>
                                    <option value="4">Huawei</option>
                                    <option value="5">Oppo</option>
                                    <option value="6">Sony</option>
                                    <option value="7">Vivo</option>
                                </select>
                            </div>
                            <div class="grp">
                                <label for="thumbnailImage">Tải hình ảnh</label>
                                <input type="file" id="thumbnailImage" name="thumbnailImage" accept="image/*">
                            </div>
                        </div>
                        <div class="sec">
                            <h2>Thông tin kho và giá</h2>
                            <div class="grp">
                                <label for="stock">Số lượng tồn kho</label>
                                <input type="number" id="stock" name="stock" value="100" required />
                            </div>
                            <div class="grp">
                                <label for="price">Giá (VNĐ)</label>
                                <input type="number" id="price" name="price" value="10000000" required />
                            </div>
                        </div>
                    </div>
                </div>

                <div class="btns">
                    <button type="reset" class="discard">Hủy bỏ</button>
                    <button type="submit" class="add-product">Thêm sản phẩm</button>
                </div>
            </div>
        </form>

        <!-- AJAX Search Select2 -->
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
                            return {
                                term: params.term // Đúng tên servlet đang đọc
                            };
                        },

                        processResults: function (data) {
                            return {
                                results: $.map(data, function (product) {
                                    return {
                                        id: product.id,
                                        text: product.text
                                    };
                                })
                            };
                        },
                        cache: true
                    },
                    minimumInputLength: 1
                });
            });

            document.querySelector("form").addEventListener("submit", function (e) {
                const existing = document.querySelector("#existingProduct").value;
                const name = document.querySelector("#product-name").value;
                if (!existing && !name.trim()) {
                    alert("Vui lòng nhập tên sản phẩm mới hoặc chọn sản phẩm đã có.");
                    e.preventDefault();
                }
            });
        </script>
    </body>
</html>

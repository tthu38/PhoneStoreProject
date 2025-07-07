<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Chi Tiết Sản Phẩm</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://www.gstatic.com/dialogflow-console/fast/messenger/bootstrap.js?v=1"></script>
        <style>
            :root {
                --primary-color: #ff0000;
                --primary-light: #ff4d4d;
                --primary-dark: #cc0000;
                --accent-color: #f5f5f5;
            }

            body {
                background-color: var(--accent-color);
                color: #000000;
                font-family: 'Segoe UI', Roboto, 'Helvetica Neue', sans-serif;
                margin: 0;
                padding: 0;
            }

            .product-container {
                background-color: #fff;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
                margin: 20px auto;
                max-width: 1000px;
                display: flex;
                gap: 20px;
            }

            .product-image-container {
                flex: 1;
                height: 400px;
                position: relative;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: white;
                border-radius: 8px;
                overflow: hidden;
            }

            .main-product-image {
                max-height: 70%;
                max-width: 70%;
                object-fit: contain;
                transition: transform 0.3s ease;
            }

            .product-image-container:hover .main-product-image {
                transform: scale(1.03);
            }

            .nav-btn {
                position: absolute;
                top: 50%;
                transform: translateY(-50%);
                background-color: rgba(255, 255, 255, 0.8);
                border: 1px solid #ddd;
                color: #000000;
                padding: 10px;
                border-radius: 50%;
                cursor: pointer;
                transition: all 0.2s ease;
                z-index: 1;
            }

            .nav-btn:hover {
                background-color: #fff;
                border-color: var(--primary-light);
                color: var(--primary-color);
            }

            #prevBtn {
                left: 10px;
            }

            #nextBtn {
                right: 10px;
            }

            .product-info {
                flex: 1;
                padding: 10px;
            }

            .product-title {
                color: #000000;
                font-weight: 700;
                font-size: 1.8rem;
                margin-bottom: 1rem;
            }

            .rating {
                color: #000000;
                font-size: 1rem;
                margin-bottom: 1rem;
            }

            .actions {
                display: flex;
                gap: 10px;
                margin-bottom: 1rem;
            }

            .action-btn {
                border: 1px solid #ddd;
                background-color: #fff;
                color: #000000;
                padding: 8px 12px;
                border-radius: 6px;
                transition: all 0.2s ease;
                display: flex;
                align-items: center;
                gap: 5px;
            }

            .action-btn:hover {
                background-color: #f8f9fa;
                border-color: var(--primary-light);
                color: var(--primary-color);
            }

            .product-price {
                display: flex;
                align-items: center;
                gap: 10px;
                margin-bottom: 1rem;
                background: #e0e0e0;
                padding: 10px;
                border-radius: 6px;
            }

            .price {
                font-size: 1.2rem;
                color: #000000;
                text-decoration: line-through;
            }

            .discount-price {
                font-size: 1.5rem;
                color: #000000;
                font-weight: 700;
            }

            .option-label {
                font-weight: 600;
                color: #000000;
                font-size: 1.1rem;
                margin-top: 1rem;
            }

            .option-container {
                display: flex;
                gap: 10px;
                margin: 0.5rem 0 1rem;
            }

            .option-btn {
                padding: 8px 16px;
                border: 2px solid #ddd;
                cursor: pointer;
                border-radius: 6px;
                background-color: #fff;
                transition: all 0.2s ease;
                font-weight: 500;
                text-align: center;
                display: flex;
                align-items: center;
                gap: 8px;
                position: relative;
                color: #000000;
            }

            .option-btn img {
                width: 20px;
                height: 20px;
                object-fit: cover;
                border-radius: 4px;
            }

            .option-btn.active {
                border-color: red;
                background-color: red;
                color: white;
                box-shadow: 0 2px 6px rgba(255, 0, 0, 0.2);
            }

            .option-btn.active::after {
                content: '✓';
                position: absolute;
                top: 50%;
                right: 8px;
                transform: translateY(-50%);
                color: white;
                font-weight: bold;
            }

            .option-btn:hover {
                border-color: var(--primary-light);
                transform: translateY(-2px);
            }

            .stock-info {
                font-size: 0.9rem;
                color: #000000; /* Đổi màu thông tin kho thành đen */
                margin-bottom: 1rem;
            }

            .quantity-controls {
                display: flex;
                align-items: center;
                margin: 1rem 0;
            }

            .quantity-btn {
                border: 1px solid #ddd;
                background-color: #fff;
                color: #000000; /* Đổi màu nút điều chỉnh số lượng thành đen */
                width: 40px;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 1.1rem;
                font-weight: bold;
                border-radius: 6px;
                transition: all 0.2s ease;
            }

            .quantity-btn:hover {
                background-color: #e9ecef;
            }

            .quantity-input {
                width: 60px;
                text-align: center;
                font-weight: 600;
                font-size: 1rem;
                border: 1px solid #ddd;
                border-radius: 6px;
                height: 40px;
                margin: 0 10px;
                color: #000000; /* Đổi màu số lượng thành đen */
            }

            .action-buttons {
                display: flex;
                gap: 10px;
                margin-top: 1.5rem;
            }

            .btn-add-to-cart {
                flex: 1;
                border: 2px solid red;
                color: #000000; /* Đổi màu chữ nút thêm vào giỏ thành đen */
                font-weight: 600;
                padding: 10px;
                border-radius: 6px;
                background-color: white;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 8px;
            }

            .btn-add-to-cart:hover {
                background-color: rgba(255, 0, 0, 0.1);
                transform: translateY(-2px);
            }

            .btn-buy-now {
                flex: 1;
                background-color: red;
                border: none;
                color: #000000; /* Đổi màu chữ nút mua ngay thành đen */
                font-weight: 600;
                padding: 10px;
                border-radius: 6px;
                transition: all 0.3s ease;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 8px;
            }

            .btn-buy-now:hover {
                background-color: var(--primary-dark);
                transform: translateY(-2px);
            }

            .left-panel {
                flex: 1;
                display: flex;
                flex-direction: column;
                gap: 10px;
            }

            .thumbnail-wrapper {
                display: flex;
                align-items: center;
                margin-top: 10px;
                overflow-x: auto;
                scrollbar-width: thin;
                scrollbar-color: #ccc #f5f5f5;
            }

            .thumbnail-container {
                display: flex;
                gap: 10px;
                flex-wrap: nowrap;
                width: 100%;
            }

            .thumbnail-img {
                flex: 0 0 auto;
                width: 60px;
                height: 60px;
                object-fit: cover;
                cursor: pointer;
                border-radius: 6px;
                border: 2px solid #ccc;
                transition: border-color 0.3s, box-shadow 0.3s;
            }

            .thumbnail-img.selected {
                border: 2px solid red !important;
                box-shadow: 0 0 6px rgba(255, 0, 0, 0.4);
            }

            /* Ẩn thanh cuộn mặc định trên một số trình duyệt */
            .thumbnail-wrapper::-webkit-scrollbar {
                height: 6px;
            }

            .thumbnail-wrapper::-webkit-scrollbar-track {
                background: #f5f5f5;
            }

            .thumbnail-wrapper::-webkit-scrollbar-thumb {
                background-color: #ccc;
                border-radius: 3px;
            }

            @media (max-width: 768px) {
                .product-container {
                    flex-direction: column;
                    padding: 15px;
                }

                .product-image-container {
                    height: 300px;
                    margin-bottom: 20px;
                }

                .nav-btn {
                    padding: 5px;
                    font-size: 0.9rem;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="/templates/header.jsp"/>

        <div class="container product-container">
            <div class="left-panel">
                <div class="product-image-container">
                    <button id="prevBtn" class="nav-btn"><</button>
                    <img src="${productDetails[0].thumbnailImage}" alt="${productDetails[0].productName}" class="main-product-image" id="mainImage">
                    <button id="nextBtn" class="nav-btn">></button>
                </div>
                <div class="thumbnail-wrapper">
                    <div class="thumbnail-container">
                        <c:forEach var="variant" items="${productVariants}" varStatus="loop">
                            <img src="${variant.imageURLs}"
                                 alt="${variant.color} ${variant.rom}GB"
                                 class="thumbnail-img ${loop.index == 0 ? 'selected' : ''}"
                                 data-image="${variant.imageURLs}"
                                 data-index="${loop.index}">
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="product-info">
                <h1 class="product-title">${productDetails[0].productName}</h1>
                <h2 class="product-price">
                    <c:choose>
                        <c:when test="${productDetails[0].discountPrice != null}">
                            <span class="price text-muted text-decoration-line-through me-2">${productDetails[0].price} VND</span>
                            <span class="discount-price text-danger fw-bold">${productDetails[0].discountPrice} VND</span>
                        </c:when>
                        <c:otherwise>
                            <span class="price">${productDetails[0].price} VND</span>
                        </c:otherwise>
                    </c:choose>
                </h2>
                <div class="stock-info">Còn lại: ${productDetails[0].stock} sản phẩm</div>

                <form action="carts" method="get">
                    <input type="hidden" id="productId" name="productId" value="${productDetails[0].productId}">
                    <input type="hidden" id="rom" name="rom" value="">
                    <input type="hidden" id="color" name="color" value="">
                    <input type="hidden" id="quantity" name="quantity" value="1">

                    <div class="mb-3">
                        <label class="option-label">Phiên bản:</label>
                        <div class="option-container" id="romOptions">
                            <c:forEach var="variant" items="${productVariants}" varStatus="loop">
                                <button type="button" class="option-btn"
                                        data-rom="${variant.rom}"
                                        data-price="${variant.price}"
                                        data-discount="${variant.discountPrice}"
                                        <c:if test="${loop.index == 0}">data-active="true"</c:if>>
                                    ${variant.rom} GB
                                </button>
                            </c:forEach>

                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="option-label">Màu sắc:</label>
                        <div class="option-container" id="colorOptions">
                            <c:forEach var="variant" items="${productVariants}" varStatus="loop">
                                <button type="button" class="option-btn"
                                        data-color="${variant.color}"
                                        data-price="${variant.price}"
                                        data-discount="${variant.discountPrice}"
                                        data-image="${variant.imageURLs}"
                                        <c:if test="${loop.index == 0}">data-active="true"</c:if>>
                                    ${variant.color}
                                </button>

                            </c:forEach>

                        </div>
                    </div>

                    <div class="quantity-controls">
                        <button type="button" id="decreaseQty" class="quantity-btn">-</button>
                        <input type="number" id="quantityInput" value="1" min="1" max="${productDetails[0].stock}" class="quantity-input">
                        <button type="button" id="increaseQty" class="quantity-btn">+</button>
                    </div>

                        <div class="action-buttons">
                            <form action="/carts?action=add" method="get">
                                <input type="hidden" name="variantId" value="${variantId.id}">
                                <button type="button" id="addToCartBtn" class="btn-add-to-cart">
                                    <i class="fas fa-shopping-cart"></i> Thêm vào giỏ
                                </button>
                            </form>
                           
                        </div>
<!--                </form>-->
            </div>
        </div>

        <script>
            $(document).ready(function () {
                let selectedRom = "";
                let selectedColor = "";

                let quantity = 1;
                let $thumbnailItems = $(".thumbnail-img");
                let currentIndex = 0;

                // Select ROM and update price
                $("#romOptions .option-btn").click(function () {
                    $("#romOptions .option-btn").removeClass("active");
                    $(this).addClass("active");
                    selectedRom = $(this).data("rom");
                    $("#rom").val(selectedRom);
                    updatePrice();
                    updateMainImage();
                });

                // Select Color and update price
                $("#colorOptions .option-btn").click(function () {
                    $("#colorOptions .option-btn").removeClass("active");
                    $(this).addClass("active");
                    selectedColor = $(this).data("color");
                    $("#color").val(selectedColor);
                    updatePrice();

                    // Cập nhật ảnh chính khi chọn màu
                    let newImage = $(this).data("image");
                    if (newImage) {
                        $("#mainImage").attr("src", newImage);
                        // Đồng thời cập nhật ảnh đang chọn trong thumbnail
                        $(".thumbnail-img").removeClass("selected");
                        $(".thumbnail-img").each(function () {
                            if ($(this).data("image") === newImage) {
                                $(this).addClass("selected");
                                currentIndex = $(this).data("index");
                            }
                        });
                    }
                });


                // Initialize active buttons
                $("#romOptions .option-btn[data-active]").addClass("active").click();
                $("#colorOptions .option-btn[data-active]").addClass("active").click();

                // Quantity controls
                $("#increaseQty").click(function () {
                    let maxQty = parseInt($(".quantity-input").attr("max"));
                    let currentQty = parseInt($(".quantity-input").val());
                    if (currentQty < maxQty) {
                        $(".quantity-input").val(currentQty + 1);
                        quantity = currentQty + 1;
                        $("#quantity").val(quantity);
                    }
                });

                $("#decreaseQty").click(function () {
                    let currentQty = parseInt($(".quantity-input").val());
                    if (currentQty > 1) {
                        $(".quantity-input").val(currentQty - 1);
                        quantity = currentQty - 1;
                        $("#quantity").val(quantity);
                    }
                });

                $(".quantity-input").on('input', function () {
                    let maxQty = parseInt($(this).attr("max"));
                    let currentQty = parseInt($(this).val());
                    if (isNaN(currentQty) || currentQty < 1) {
                        $(this).val(1);
                        quantity = 1;
                    } else if (currentQty > maxQty) {
                        $(this).val(maxQty);
                        quantity = maxQty;
                    } else {
                        quantity = currentQty;
                    }
                    $("#quantity").val(quantity);
                });

                function updatePrice() {
                    let romBtn = $("#romOptions .option-btn.active");
                    let colorBtn = $("#colorOptions .option-btn.active");
                    let discountPrice = romBtn.data("discount") || colorBtn.data("discount") || "30.390.000";
                    let price = romBtn.data("price") || colorBtn.data("price") || "34.990.000";
                    $(".price").text(price + "đ");
                    $(".discount-price").text(discountPrice + "đ");
                }

                // Add to cart functionality
                $("#addToCartBtn").click(function (event) {
                    event.preventDefault();
                    if (!$("#rom").val() || !$("#color").val()) {
                        alert("Vui lòng chọn phiên bản và màu sắc!");
                        return;
                    }

                    let productId = $("#productId").val();
                    let rom = $("#rom").val();
                    let color = $("#color").val();
                    let quantity = $("#quantity").val();
                    $.ajax({
                        type: "POST",
                        url: "carts",
                        data: {
                            action: "addToCart",
                            productId: productId,
                            rom: rom,
                            color: color,
                            quantity: quantity
                        },
                        success: function (response) {
                            let newCartCount = response.cartCount || 0;
                            $("#cartCount").text(newCartCount);
                            const successToast = `
                                <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
                                    <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
                                        <div class="toast-header bg-danger text-white">
                                            <strong class="me-auto"><i class="fas fa-check-circle"></i> Thành công</strong>
                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="toast"></button>
                                        </div>
                                        <div class="toast-body">
                                            Đã thêm sản phẩm vào giỏ hàng thành công!
                                        </div>
                                    </div>
                                </div>
                            `;
                            $('body').append(successToast);
                            setTimeout(() => {
                                $('.toast').toast('hide');
                                setTimeout(() => {
                                    $('.toast').parent().remove();
                                }, 500);
                            }, 3000);
                        },
                        error: function () {
                            alert("Có lỗi xảy ra, vui lòng thử lại");
                        }
                    });
                });

                // Thumbnail and navigation logic
                $("#prevBtn").click(function () {
                    currentIndex = Math.max(0, currentIndex - 1);
                    updateMainImage();
                    scrollToThumbnail();
                });

                $("#nextBtn").click(function () {
                    currentIndex = Math.min($thumbnailItems.length - 1, currentIndex + 1);
                    updateMainImage();
                    scrollToThumbnail();
                });

                // Click thumbnail to update main image
                $thumbnailItems.click(function () {
                    currentIndex = $(this).data("index");
                    updateMainImage();
                    scrollToThumbnail();
                });

                function updateMainImage() {
                    let $currentThumbnail = $thumbnailItems.eq(currentIndex);
                    $("#mainImage").attr("src", $currentThumbnail.data("image"));
                    $thumbnailItems.removeClass("selected");
                    $currentThumbnail.addClass("selected");
                }

                function scrollToThumbnail() {
                    let $currentThumbnail = $thumbnailItems.eq(currentIndex);
                    let container = $(".thumbnail-wrapper");
                    let scrollPosition = $currentThumbnail.position().left + container.scrollLeft() - (container.width() / 2) + ($currentThumbnail.outerWidth() / 2);
                    container.animate({scrollLeft: scrollPosition}, 300);
                }

                // Initialize with first image
                updateMainImage();
            });
        </script>
    </body>
</html>
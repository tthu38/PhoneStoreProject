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
            --primary-color: #d32f2f;
            --primary-light: #ef5350;
            --primary-dark: #b71c1c;
            --accent-color: #f5f5f5;
        }

        body {
            background-color: var(--accent-color);
            color: #333;
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
            display: flex;
            align-items: center;
            justify-content: center;
            background-color: rgba(211, 47, 47, 0.05);
            border-radius: 8px;
            overflow: hidden;
        }

        .main-product-image {
            max-height: 100%;
            max-width: 100%;
            object-fit: contain;
            transition: transform 0.3s ease;
        }

        .product-image-container:hover .main-product-image {
            transform: scale(1.03);
        }

        .product-info {
            flex: 1;
            padding: 10px;
        }

        .product-title {
            color: var(--primary-color);
            font-weight: 700;
            font-size: 1.8rem;
            margin-bottom: 1rem;
        }

        .rating {
            color: #f4c430;
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
            color: #555;
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

        .original-price {
            font-size: 1.2rem;
            color: #888;
            text-decoration: line-through;
        }

        .discount-price {
            font-size: 1.5rem;
            color: var(--primary-color);
            font-weight: 700;
        }

        .option-label {
            font-weight: 600;
            color: #444;
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
        }

        .option-btn img {
            width: 20px;
            height: 20px;
            object-fit: cover;
            border-radius: 4px;
        }

        .option-btn.active {
            border-color: var(--primary-color);
            background-color: var(--primary-color);
            color: white;
            box-shadow: 0 2px 6px rgba(211, 47, 47, 0.2);
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
            color: #666;
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
            color: #333;
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
        }

        .action-buttons {
            display: flex;
            gap: 10px;
            margin-top: 1.5rem;
        }

        .btn-add-to-cart {
            flex: 1;
            border: 2px solid var(--primary-color);
            color: var(--primary-color);
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
            background-color: rgba(211, 47, 47, 0.1);
            transform: translateY(-2px);
        }

        .btn-buy-now {
            flex: 1;
            background-color: var(--primary-color);
            border: none;
            color: #fff;
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

        @media (max-width: 768px) {
            .product-container {
                flex-direction: column;
                padding: 15px;
            }

            .product-image-container {
                height: 300px;
                margin-bottom: 20px;
            }
        }
    </style>
</head>
<body>
<jsp:include page="/templates/header.jsp"/>

<div class="container product-container">
    <div class="product-image-container">
        <img src="${productDetails[0].imageURL}" alt="${productDetails[0].productName}" class="main-product-image">
    </div>
    <div class="product-info">
        <h1 class="product-title">${productDetails[0].productName}</h1>
        <h2 class="product-price">
                <c:choose>
                    <c:when test="${productDetails[0].discountPrice != null}">
                        <span class="original-price text-muted text-decoration-line-through me-2">${productDetails[0].originalPrice} VND</span>
                        <span class="discount-price text-success fw-bold">${productDetails[0].discountPrice} VND</span>
                    </c:when>
                    <c:otherwise>
                        <span class="original-price">${productDetails[0].originalPrice} VND</span>
                    </c:otherwise>
                </c:choose>
            </h2>
        <div class="stock-info">Còn lại: ${productDetails[0].stock} sản phẩm</div>

        <form action="carts" method="get">
            <input type="hidden" id="productId" name="productId" value="${productDetails[0].productId}">
            <input type="hidden" id="ram" name="ram" value="">
            <input type="hidden" id="color" name="color" value="">
            <input type="hidden" id="quantity" name="quantity" value="1">

            <div class="mb-3">
                <label class="option-label">Phiên bản:</label>
                <div class="option-container" id="ramOptions">
                    <c:forEach var="variant" items="${productVariants}">
                        <button type="button" class="option-btn" data-ram="${variant.ram}" data-price="${variant.originalPrice}" data-discount="${variant.discountPrice}" ${variant.ram == '256GB' ? 'data-active="true"' : ''}>
                            ${variant.ram}
                        </button>
                    </c:forEach>
                </div>
            </div>

            <div class="mb-3">
                <label class="option-label">Màu sắc:</label>
                <div class="option-container" id="colorOptions">
                    <c:forEach var="variant" items="${productVariants}">
                        <button type="button" class="option-btn" data-color="${variant.color}" data-price="${variant.originalPrice}" data-discount="${variant.discountPrice}" ${variant.color == 'Titan Sa Mac' ? 'data-active="true"' : ''}>
                            ${variant.color} <img src="${variant.colorImageURL}" alt="${variant.color}">
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
                <button type="button" id="addToCartBtn" class="btn-add-to-cart">
                    <i class="fas fa-shopping-cart"></i> Thêm vào giỏ
                </button>
                <button type="submit" name="action" value="buyNow" class="btn-buy-now">
                    <i class="fas fa-bolt"></i> Mua ngay
                </button>
            </div>
        </form>
    </div>
</div>

<script>
    $(document).ready(function () {
        let selectedRam = "256GB";
        let selectedColor = "Titan Sa Mac";
        let quantity = 1;

        // Select RAM and update price
        $("#ramOptions .option-btn").click(function () {
            $("#ramOptions .option-btn").removeClass("active");
            $(this).addClass("active");
            selectedRam = $(this).data("ram");
            $("#ram").val(selectedRam);
            updatePrice();
        });

        // Select Color and update price
        $("#colorOptions .option-btn").click(function () {
            $("#colorOptions .option-btn").removeClass("active");
            $(this).addClass("active");
            selectedColor = $(this).data("color");
            $("#color").val(selectedColor);
            updatePrice();
        });

        // Initialize active buttons
        $("#ramOptions .option-btn[data-active]").addClass("active").click();
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
            let ramBtn = $("#ramOptions .option-btn.active");
            let colorBtn = $("#colorOptions .option-btn.active");
            let discountPrice = ramBtn.data("discount") || colorBtn.data("discount") || "30.390.000";
            let originalPrice = ramBtn.data("price") || colorBtn.data("price") || "34.990.000";
            $(".original-price").text(originalPrice + "đ");
            $(".discount-price").text(discountPrice + "đ");
        }

        // Add to cart functionality
        $("#addToCartBtn").click(function (event) {
            event.preventDefault();
            if (!$("#ram").val() || !$("#color").val()) {
                alert("Vui lòng chọn phiên bản và màu sắc!");
                return;
            }

            let productId = $("#productId").val();
            let ram = $("#ram").val();
            let color = $("#color").val();
            let quantity = $("#quantity").val();

            $.ajax({
                type: "POST",
                url: "carts",
                data: {
                    action: "addToCart",
                    productId: productId,
                    ram: ram,
                    color: color,
                    quantity: quantity
                },
                success: function (response) {
                    let newCartCount = response.cartCount || 0;
                    $("#cartCount").text(newCartCount);

                    const successToast = `
                        <div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
                            <div class="toast show" role="alert" aria-live="assertive" aria-atomic="true">
                                <div class="toast-header bg-success text-white">
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
    });
</script>

<jsp:include page="/templates/footer.jsp"/>
</body>
</html>
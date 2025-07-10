<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="service.ProductService" %>

<jsp:useBean id="productService" class="service.ProductService" scope="page"/>
<c:set var="discountedProducts" value="${productService.getMostDiscountedProducts(10)}"/>
<c:set var="bestsellerProducts" value="${productService.getMostOrderedProducts(10)}"/>

<!DOCTYPE html>

<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thế Giới Công Nghệ</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            .content-frame {
                max-width: 1200px;
                margin: 20px auto;
                padding: 20px;
                border: 1px solid #ddd;
                border-radius: 10px;
                background-color: #f9f9f9;
                box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            }
            .col-custom-5 {
                flex: 0 0 20%;
                max-width: 20%;
                padding-left: 10px;
                padding-right: 10px;
            }
            .discount-badge {
                width: 60px;
                height: 60px;
                font-size: 1rem;
            }
            .card-img-top {
                border-radius: 15px 15px 0 0;
                width: 100%;
                height: 200px;
                object-fit: contain;
                background-color: #f9f9f9;
                padding: 1rem;
                transition: transform 0.5s ease;
            }
            .card:hover .card-img-top {
                transform: scale(1.05);
            }

            .card-body {
                padding: 1.25rem;
                flex-grow: 1;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }
            .card-title {
                font-family: 'Poppins', sans-serif;
                color: #ff2e63;
                font-weight: 600;
                font-size: 1.2rem;
                margin-bottom: 0.8rem;
                height: 2.8em;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
            }
            .card-text {
                color: #555;
                font-weight: 400;
                margin-bottom: 1rem;
                font-size: 0.95rem;
            }

            .card-text .text-decoration-line-through {
                color: #aaa;
                font-size: 0.8rem;
                margin-bottom: 0.3rem;
                display: block;
                text-decoration: none;
            }


            .card-text .discount-price {
                color: #ff2e63;
                font-weight: 700;
                font-size: 1.3rem;
            }
            .card-footer {
                background-color: #f8f9fa;
                border-top: 1px solid rgba(255, 46, 99, 0.1);
                padding: 0.8rem;
                text-align: center;
            }

            /* Discount badge */
            .discount-badge {
                position: absolute;
                top: 15px;
                right: 15px;
                background: linear-gradient(45deg, #ff2e63, #ff6b6b);
                color: white;
                border-radius: 50%;
                width: 60px;
                height: 60px;
                display: flex;
                justify-content: center;
                align-items: center;
                font-weight: 700;
                font-size: 1rem;
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
                z-index: 10;
                animation: pulse 1.5s infinite;
            }
            @keyframes pulse {
                0% {
                    transform: scale(1);
                }
                50% {
                    transform: scale(1.1);
                }
                100% {
                    transform: scale(1);
                }
            }

            /* Buttons */
            .btn-primary {
                background: linear-gradient(90deg, #ff2e63, #ff6b6b);
                border: red !important;
                color: #fff;
                font-weight: 600;
                padding: 0.7rem 1.5rem;
                border-radius: 50px;
                transition: all 0.3s ease;
                box-shadow: 0 5px 15px rgba(255, 46, 99, 0.3);
                font-size: 0.9rem;
            }

            .btn-primary:hover, .btn-primary:focus {
                background: linear-gradient(90deg, #e71e53, #ff4d4d);
                transform: translateY(-3px);
                box-shadow: 0 8px 20px rgba(255, 46, 99, 0.4);
            }


            .carousel-img {
                width: 100%;
                height: 400px;
                object-fit: cover;
            }
            .carousel-item {
                position: relative;
            }
            .overlay-center {
                position: absolute;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.5);
                display: flex;
                justify-content: center;
                align-items: center;
                flex-direction: column;
                color: white;
                text-align: center;
            }
            .scroll-btn {
                background: #d4a373;
                color: white;
                border: none;
                padding: 10px 20px;
                border-radius: 25px;
                font-weight: bold;
                margin-top: 15px;
            }
            .scroll-btn:hover {
                background: #b08563;
            }

            .product-card {
                border: 1px solid #ddd;
                border-radius: 10px;
                padding: 10px;
                text-align: center;
                background: #fff;
                transition: transform 0.2s;
                position: relative;
            }
            .product-card:hover {
                transform: translateY(-5px);
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            }
            .product-img {
                max-width: 100%;
                height: 200px;
                object-fit: cover;
                margin-bottom: 10px;
            }
            .hot-sale, .ai-logo {
                position: absolute;
                top: 10px;
                padding: 5px 10px;
                border-radius: 5px;
                font-size: 0.75rem;
                font-weight: bold;
            }
            .hot-sale {
                left: 10px;
                background: #e74c3c;
                color: white;
            }
            .ai-logo {
                right: 10px;
                background: #ffeb3b;
                color: #333;
            }
            .spec {
                font-size: 0.9rem;
                color: #7f8c8d;
            }
            .price {
                color: #e74c3c;
                font-weight: bold;
                font-size: 1.1rem;
            }
            .original-price {
                text-decoration: line-through;
                color: #7f8c8d;
                font-size: 0.9rem;
                margin-left: 5px;
            }
            .discount {
                color: #27ae60;
                font-size: 0.9rem;
            }
            .rating {
                color: #ffd700;
            }
            .compare-btn {
                background: #3498db;
                color: white;
                border: none;
                padding: 5px 10px;
                border-radius: 5px;
                margin-top: 8px;
            }
            .compare-btn:hover {
                background: #2980b9;
            }
            .filter-buttons .btn {
                margin-right: 10px;
                margin-bottom: 5px;
            }
            .sort-options .btn-link {
                margin-right: 10px;
                text-decoration: none;
                color: #007bff;
            }
            .sort-options .btn-link:hover {
                text-decoration: underline;
            }
            .logo {
                width: 60px;
                height: 25px;
                vertical-align: middle;
            }
            .text-head {
                color: black !important;
            }

            .card a {
                text-decoration: none !important;
                color: inherit;
            }


            /* Style cho footer trải dài toàn màn hình */
            .footer {
                background-color: #111;
                color: #fff;
                padding: 20px 0;
                width: 100%;
                margin-top: 20px;
            }
            .footer-container {
                display: grid;
                grid-template-columns: repeat(4, 1fr);
                gap: 30px;
                padding: 0 20px;
            }
            .footer-col iframe {
                max-width: 100%;
                height: 200px;
                border: 0;
                margin-top: 10px;
            }
            .footer-bottom {
                text-align: center;
                border-top: 1px solid #333;
                padding-top: 15px;
                margin-top: 20px;
                font-size: 14px;
                color: #aaa;
            }
            @media (max-width: 768px) {
                .footer-container {
                    grid-template-columns: repeat(2, 1fr);
                }
            }
            @media (max-width: 480px) {
                .footer-container {
                    grid-template-columns: 1fr;
                }
            }
        </style>
    </head>
    <body>
        <jsp:include page="/templates/header.jsp"/>


        <!-- Carousel -->
        <div class="content-frame">
            <div id="prodCar" class="carousel slide" data-bs-ride="carousel">
                <div class="carousel-inner">
                    <div class="carousel-item active">
                        <img src="https://www.digitaltrends.com/wp-content/uploads/2022/09/Apple-iPhone-14_colors.jpg?fit=1920%2C1080&p=1" class="carousel-img">
                        <div class="overlay-center">
                            <h5>Nổi bật cho thế hệ mới</h5>
                            <p>Khám phá các mẫu điện thoại hiện đại nhất.</p>
                            <button class="scroll-btn" onclick="scrollToProducts()">Xem sản phẩm</button>
                        </div>
                    </div>
                    <div class="carousel-item">
                        <img src="https://www.business.maxis.com.my/content/dam/enterprise/images/campaign/iphone-series/iphone-16e/herobanner-desktop-2x.webp" class="carousel-img">
                        <div class="overlay-center">
                            <h5>Ưu đãi đặc biệt</h5>
                            <p>Giảm giá lên đến 30% cho các sản phẩm hot.</p>
                            <button class="scroll-btn" onclick="scrollToProducts()">Xem sản phẩm</button>
                        </div>
                    </div>
                    <div class="carousel-item">
                        <img src="https://vivonewsroom.b-cdn.net/wp-content/uploads/2024/09/vivo-V40E-Press-Release-Banner.jpg" class="carousel-img">
                        <div class="overlay-center">
                            <h5>Sản phẩm mới ra mắt</h5>
                            <p>Trải nghiệm công nghệ tiên tiến ngay hôm nay.</p>
                            <button class="scroll-btn" onclick="scrollToProducts()">Xem sản phẩm</button>
                        </div>
                    </div>
                </div>
                <button class="carousel-control-prev" type="button" data-bs-target="#prodCar" data-bs-slide="prev">
                    <span class="carousel-control-prev-icon"></span>
                </button>
                <button class="carousel-control-next" type="button" data-bs-target="#prodCar" data-bs-slide="next">
                    <span class="carousel-control-next-icon"></span>
                </button>
            </div>
        </div>

        <!-- Bộ lọc hãng -->
        <div class="content-frame">
            <div class="filter-buttons filter-container">
                <button class="btn btn-outline-primary">Lọc</button>
                <button class="btn btn-outline-primary">
                    <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/3f/68/3f68e22880dd800e9e34d55245048a0f.png" alt="Samsung" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/57/03/5703d996359650c57421b72f3f7ff5cd.png" alt="Iphone" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/2c/ea/2cea467041fb9effb3a6d3dcc88f38f8.png" alt="Oppo" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/e9/df/e9df3ae9fb60a1460e9030975d0e024a.png" alt="Xiaomi" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/54/2a/542a235b0e366a11fd855108dd9c499c.png" alt="Realme" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/78/38/783870ef310908b123c50cb43b8f6f92.png" alt="Vivo" class="logo">
                </button>
                <button class="btn btn-outline-primary">
                    <img src="https://cdnv2.tgdd.vn/mwg-static/common/Category/b5/68/b5686886a3142a87df546889d8c14402.png" alt="Nokia" class="logo">
                </button>
            </div>

            <!-- Bộ lọc sắp xếp -->
            <div class="sort-options filter-container2">
                <strong>Sắp xếp theo:</strong>
                <button class="btn btn-link text-primary">Nổi bật</button>
                <button class="btn btn-link text-primary">Bán chạy</button>
                <button class="btn btn-link text-primary">Giảm giá</button>
                <button class="btn btn-link text-primary">Giá</button>
            </div>
        </div>
        <!--<div class="content-frame">
        <c:if test="${not empty recommendedProducts}">
            <section class="products-section bg-light py-5">
                <div class="container">
                    <h2 class="text-success text-center mb-4">Gợi Ý Dành Riêng Cho Bạn</h2>
                    <div class="row">
            <c:forEach var="product" items="${recommendedProducts}">
                <div class="col-custom-5 mb-4 d-flex">
                    <div class="card w-100">
                        <a href="products?action=productDetail&productId=${product.id}">
                            <img src="${product.thumbnailImage}" class="card-img-top" alt="${product.productName}">
                            <div class="card-body">
                                <h5 class="card-title">${product.productName}</h5>
                                <p class="card-text">
                                    <span class="text-danger fw-bold">${product.price}₫</span>
                                </p>
                            </div>
                        </a>
                        <div class="card-footer">
                            <form action="${pageContext.request.contextPath}/carts?action=add" method="get">
                                <input type="hidden" name="variantId" value="${product.variantId}">
                                <button type="submit" class="btn btn-success">Thêm vào giỏ hàng</button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</section>
        </c:if>
</div>-->
        <div class="content-frame">
            <section id="products-section" class="products-section">
                <div class="container">
                    <h2 class="text-head text-center mb-4">Best Seller</h2>
                    <div class="row">
                        <c:forEach var="product" items="${bestsellerProducts}">
                            <div class="col-custom-5 mb-4 d-flex">
                                <div class="card w-100">
                                    <div class="discount-badge">
                                        -${product.discountPercent}%
                                    </div>
                                    <a href="products?action=productDetail&productId=${product.id}">
                                        <img src="${product.thumbnailImage}" class="card-img-top" alt="${product.name}">
                                        <div class="card-body">
                                            <h5 class="card-title">${product.name}</h5>
                                            <p class="card-text">
                                                <span class="discount-price">${product.discountPrice}₫</span><br>
                                                <span class="text-decoration-line-through">${product.originalPrice}₫</span>
                                            </p>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Nút "Xem thêm" -->
                    <div class="view-more-container text-center mt-3">
                        <a href="${pageContext.request.contextPath}/products?action=productBestSeller" class="btn btn-view-more">
                            Xem Thêm Sản Phẩm <i class="bi bi-arrow-right"></i>
                        </a>
                    </div>
                </div>
            </section>
        </div>
        <div class="content-frame">
            <section id="products-section" class="products-section">
                <div class="container">
                    <h2 class="text-head text-center mb-4">Sản Phẩm Khuyến Mãi</h2>
                    <div class="row">
                        <c:forEach var="product" items="${discountedProducts}">
                            <div class="col-custom-5 mb-4 d-flex">
                                <div class="card w-100">
                                    <div class="discount-badge">
                                        -${product.discountPercent}%
                                    </div>
                                    <a href="products?action=productDetail&productId=${product.id}">
                                        <img src="${product.thumbnailImage}" class="card-img-top" alt="${product.name}">
                                        <div class="card-body">
                                            <h5 class="card-title">${product.name}</h5>
                                            <p class="card-text">
                                                <span class="discount-price">${product.discountPrice}₫</span><br>
                                                <span class="text-decoration-line-through">${product.originalPrice}₫</span>
                                            </p>
                                        </div>
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Nút "Xem thêm" -->
                    <div class="view-more-container text-center mt-3">
                        <a href="${pageContext.request.contextPath}/products?action=showDiscountedProducts" class="btn btn-view-more">
                            Xem Thêm Sản Phẩm <i class="bi bi-arrow-right"></i>
                        </a>
                    </div>
                </div>
            </section>
        </div>



        <jsp:include page="/templates/footer.jsp"/>
        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <script>
                                function scrollToProducts() {
                                    document.querySelector('#products-section')?.scrollIntoView({behavior: 'smooth'});
                                }
        </script>
    </body>
</html>
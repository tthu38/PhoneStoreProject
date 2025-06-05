
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Welcome</title>

    <!-- Bootstrap CSS -->
    <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        .carousel-item {
    height: 600px;
    position: relative;
}

.carousel-item img {
    width: 80%;
    height: 60%;
    object-fit: cover; /* Rất quan trọng để ảnh không méo */
    opacity: 0.85;
}

.carousel-caption {
    position: absolute;
    top: 30%;
    transform: translateY(-30%);
    background-color: rgba(0, 0, 0, 0.3); /* nền mờ */
    padding: 30px;
    border-radius: 10px;
    color: white;
}

.carousel-caption h1 {
    font-size: 48px;
    font-weight: bold;
    text-shadow: 1px 1px 3px #000;
}

.carousel-caption p {
    font-size: 18px;
}

.carousel-caption .btn {
    background-color: #a3c112;
    border: none;
    padding: 12px 25px;
    color: white;
    font-weight: bold;
    border-radius: 5px;
}

    </style>
</head>
<body>
<jsp:include page="/templates/header.jsp"/>
<!-- Carousel -->
<div id="carouselExample" class="carousel slide" data-bs-ride="carousel">
  <div class="carousel-inner">
    <div class="carousel-item active">
      <img src="images/banner1.jpg" class="d-block w-100" alt="...">
      <div class="carousel-caption d-none d-md-block">
        <h1>Welcome To PhoneStore</h1>
        <p>Latest Phones | Best Prices | Fast Delivery</p>
        <a class="btn btn-primary" href="#">Shop Now</a>
      </div>
    </div>
    <div class="carousel-item">
      <img src="images/banner2.jpg" class="d-block w-100" alt="...">
      <div class="carousel-caption d-none d-md-block">
        <h1>iPhone 15 Pro Max</h1>
        <p>Pre-order Now - Get Free Gift</p>
        <a class="btn btn-primary" href="#">Buy Now</a>
      </div>
    </div>
    <div class="carousel-item">
      <img src="images/banner3.jpg" class="d-block w-100" alt="...">
      <div class="carousel-caption d-none d-md-block">
        <h1>Galaxy S24 Ultra</h1>
        <p>Experience the Next Level</p>
        <a class="btn btn-primary" href="#">Learn More</a>
      </div>
    </div>
  </div>
  <button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
    <span class="carousel-control-next-icon" aria-hidden="true"></span>
  </button>
</div>


<!-- Bootstrap JS và JS riêng -->
<script src="${pageContext.request.contextPath}/js/slideshow.js"></script>
<script src="${pageContext.request.contextPath}/js/bootstrap/bootstrap.bundle.min.js"></script>

</body>
</html>
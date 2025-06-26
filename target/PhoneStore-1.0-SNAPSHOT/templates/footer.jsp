<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Footer</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">
        <style>
            .footer {
                background-color: #111;
                color: #fff;
                padding: 20px 0 10px;
                font-family: Arial, sans-serif;
                width: 100%;
            }

            .footer-container {
                max-width: 1200px; /* Giới hạn chiều rộng khớp với header */
                margin: 0 auto; /* Căn giữa */
                display: grid;
                grid-template-columns: repeat(4, 1fr);
                gap: 30px;
                padding: 0 20px; /* Padding hợp lý */
            }

            .footer-col h3 {
                margin-bottom: 15px;
                font-size: 16px;
                border-bottom: 1px solid #fff;
                padding-bottom: 10px;
            }

            .footer-col ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }

            .footer-col ul li {
                margin-bottom: 10px;
            }

            .footer-col ul li a {
                text-decoration: none;
                color: #ccc;
                transition: color 0.3s;
            }

            .footer-col ul li a:hover {
                color: #fff;
            }

            .connect {
                margin-top: 33px;
            }

            .social-links {
                display: flex;
                gap: 15px;
                padding: 10px 0;
            }

            .social-icon {
                width: 32px;
                height: 32px;
                display: flex;
                align-items: center;
                justify-content: center;
                transition: transform 0.3s ease;
            }

            .social-icon:hover {
                transform: translateY(-5px);
            }

            .social-img-icon {
                width: 32px;
                height: 32px;
                border-radius: 50%;
                padding-top: 3px;
                margin-left: -2px;
                object-fit: cover;
                transition: transform 0.3s ease;
            }

            .social-img-icon:hover {
                transform: translateY(-3px);
            }

            .hotline p {
                font-size: 14px;
                margin: 10px 0;
            }

            .introduce li {
                text-align: justify;
                padding-bottom: 5px;
            }

            .Bct img {
                max-width: 120px;
                height: auto;
            }

            .payment-methods img,
            .certifications img {
                max-width: 100%;
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

            .footer-col a {
                display: block;
                margin: 5px 0;
                color: #fff;
                text-decoration: none;
                transition: color 0.3s ease;
            }

            .footer-col a:hover {
                color: #ccc;
            }

            .vnpay-icon {
                width: 32px;
                height: 32px;
                margin-left: -2px;
                margin-top: -5px;
                object-fit: cover;
                transition: transform 0.3s ease;
            }

            .footer-col iframe {
                max-width: 100%; /* Đảm bảo iframe không tràn */
                height: 200px;
                border: 0;
                margin-top: 10px;
            }

            /* Responsive adjustments */
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
        <footer class="footer">
            <div class="footer-container">
                <div class="footer-col Logo-footer">
                    <div class="hotline">
                        <h3>CHĂM SÓC KHÁCH HÀNG</h3>
                        <p><i class="fas fa-home"></i> Đại Học FPT Đà Nẵng</p>
                        <p><i class="fas fa-phone"></i> 0901285777</p>
                        <p><i class="fas fa-envelope"></i> PHONESTORE@gmail.com</p>
                    </div>
                    <div class="connect">
                        <h3>KẾT NỐI VỚI CHÚNG TÔI</h3>
                        <div class="social-links">
                            <a href="#" class="social-icon"><i class="fab fa-facebook fa-2x" style="color: #1877F2;"></i></a>
                            <a href="#" class="social-icon"><i class="fab fa-instagram fa-2x" style="color: #E1306C;"></i></a>
                            <a href="#" class="social-icon"><img src="${pageContext.request.contextPath}/images/tiktok-logo.jpg" alt="TikTok" class="social-img-icon"></a>
                        </div>
                    </div>
                </div>

                <div class="footer-col">
                    <h3>GIỚI THIỆU</h3>
                    <ul class="introduce">
                        <li>PhoneStore là cửa hàng chuyên cung cấp các sản phẩm điện thoại chính hãng, phụ kiện công nghệ và dịch vụ hậu mãi uy tín, cam kết mang đến trải nghiệm mua sắm tiện lợi và đáng tin cậy cho khách hàng trên toàn quốc.</li>
                    </ul>
                    <h3>CHỨNG NHẬN</h3>
                    <div class="Bct">
                        <a href="#"><img src="${pageContext.request.contextPath}/images/chungnhan.png" alt="Chứng nhận Bộ Công Thương"></a>
                    </div>
                </div>

                <div class="footer-col">
                    <h3>CHÍNH SÁCH</h3>
                    <div>
                        <a>Chính sách bảo hành</a>
                        <a>Chính sách đổi trả</a>
                        <a>Chính sách bảo mật</a>
                        <a>Chính sách trả góp</a>
                    </div>
                </div>

                <div class="footer-col">
                    <h3>THANH TOÁN</h3>
                    <a href="#"><img src="${pageContext.request.contextPath}/images/VNPAY.png" alt="VNPAY" class="vnpay-icon"></a>
                    <iframe src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3835.8561678280007!2d108.2608913!3d15.968885900000002!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x3142116949840599%3A0x365b35580f52e8d5!2sFPT%20University%20Danang!5e0!3m2!1sen!2s!4v1748509098035!5m2!1sen!2s" 
                            width="100%" height="200px" style="border:0; margin-top: 10px" allowfullscreen="" loading="lazy" referrerpolicy="no-referrer-when-downgrade"></iframe>
                </div>
            </div>

            <div class="footer-bottom">
                <p>© 2025 <strong>PhoneStore</strong>. All rights reserved.</p>
            </div>
        </footer>
    </body>
</html>
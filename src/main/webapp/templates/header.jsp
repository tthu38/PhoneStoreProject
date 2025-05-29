<%-- 
    Document   : header
    Created on : May 27, 2025, 7:26:04 PM
    Author     : ThienThu
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Ocean SmartPhone</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        body { background: #f8f9fa; }
        .header-main {
            background: #fff;
            box-shadow: 0 4px 24px 0 rgba(234,109,34,0.10);
            border-radius: 0 0 1.5rem 1.5rem;
            margin-bottom: 1.5rem;
            overflow: hidden;
        }
        .header-topbar {
            display: flex;
            justify-content: flex-end;
            align-items: center;
            gap: 1.5rem;
            font-size: 1rem;
            padding: 0.5rem 2rem 0.2rem 2rem;
            color: #EA6D22;
            background: #fffbe7;
            border-bottom: 1px solid #ffe0c2;
        }
        .header-topbar .hotline-mini {
            font-weight: bold;
            color: #EA6D22;
            background: #fff;
            border-radius: 1.2rem;
            padding: 0.3rem 1.1rem;
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 1.05rem;
            margin-right: 1.5rem;
        }
        .header-topbar .hotline {
            font-weight: bold;
            color: #EA6D22;
            background: #fffbe7;
            border-radius: 1.2rem;
            padding: 0.3rem 1.1rem;
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 1.05rem;
        }
        .header-topbar .header-icon-link {
            background: #fff;
            color: #EA6D22;
            border-radius: 50%;
            width: 40px; height: 40px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.3rem;
            position: relative;
            box-shadow: 0 2px 8px 0 rgba(234,109,34,0.08);
            transition: background 0.2s, color 0.2s, box-shadow 0.2s;
        }
        .header-topbar .header-icon-link:hover {
            background: #EA6D22;
            color: #fff;
            box-shadow: 0 4px 16px 0 rgba(234,109,34,0.18);
        }
        .header-badge {
            position: absolute;
            top: 4px;
            right: 4px;
            background: #EA6D22;
            color: #fff;
            font-size: 0.8rem;
            font-weight: bold;
            border-radius: 50%;
            padding: 2px 6px;
            border: 1.5px solid #fff;
        }
        .header-content {
            display: flex;
            align-items: flex-end;
            justify-content: space-between;
            padding: 0.7rem 2rem 0.7rem 2rem;
            flex-wrap: wrap;
            gap: 1.2rem;
        }
        .header-logo {
            font-size: 2.1rem;
            font-weight: bold;
            color: #EA6D22;
            display: flex;
            align-items: center;
            gap: 10px;
            letter-spacing: 2px;
            text-decoration: none;
            position: relative;
        }
        .header-logo i {
            font-size: 2.3rem;
            color: #EA6D22;
            animation: bounce 1.2s infinite alternate;
        }
        @keyframes bounce {
            0% { transform: translateY(0); }
            100% { transform: translateY(-8px); }
        }
        .header-logo .logo-slogan {
            font-size: 1.05rem;
            color: #EA6D22;
            font-style: italic;
            font-weight: 600;
            margin-left: 10px;
            margin-bottom: 0;
            position: absolute;
            left: 100%;
            top: 50%;
            transform: translateY(-50%);
            white-space: nowrap;
        }
        .header-slogan {
            font-size: 0.98rem;
            color: #ffb86c;
            margin-left: 0.2rem;
            font-style: italic;
            font-weight: 500;
        }
        .header-search {
            flex: 1 1 350px;
            max-width: 520px;
            margin: 0 2rem;
        }
        .search-bar {
            display: flex;
            align-items: center;
            border-radius: 2rem;
            box-shadow: 0 2px 12px 0 rgba(234,109,34,0.13);
            border: 2px solid #EA6D22;
            background: #fff;
            overflow: hidden;
            position: relative;
            transition: box-shadow 0.3s, border-color 0.3s;
        }
        .search-bar:focus-within {
            box-shadow: 0 4px 24px 0 rgba(234,109,34,0.18);
            border-color: #ffb86c;
        }
        .search-bar input {
            border: none;
            border-radius: 2rem 0 0 2rem;
            background: #fff;
            color: #EA6D22;
            font-size: 1.1rem;
            padding: 0.7rem 1.2rem;
            flex: 1 1 auto;
            transition: box-shadow 0.2s, background 0.2s, transform 0.2s;
        }
        .search-bar input:focus {
            outline: none;
            background: #fffbe7;
            transform: scale(1.04);
        }
        .search-bar button {
            border: none;
            border-radius: 0 2rem 2rem 0;
            background: #fff;
            color: #EA6D22;
            font-size: 1.3rem;
            padding: 0 1.2rem;
            transition: color 0.2s, background 0.2s;
            position: relative;
            overflow: hidden;
            box-shadow: none;
        }
        .search-bar button:active::after {
            content: '';
            position: absolute;
            left: 50%; top: 50%;
            width: 120%; height: 120%;
            background: rgba(255,255,255,0.3);
            border-radius: 50%;
            transform: translate(-50%,-50%) scale(1);
            animation: ripple 0.4s linear;
            pointer-events: none;
        }
        @keyframes ripple {
            0% { opacity: 1; transform: translate(-50%,-50%) scale(0.5); }
            100% { opacity: 0; transform: translate(-50%,-50%) scale(1.5); }
        }
        .search-bar button i {
            color: #EA6D22;
            transition: transform 0.4s cubic-bezier(.4,2,.6,1), color 0.2s;
        }
        .search-bar button:hover {
            background: #fff;
        }
        .search-bar button:hover i {
            color: #ffb86c;
            transform: rotate(20deg) scale(1.2);
        }
        .search-bar input::placeholder {
            color: #EA6D22;
            opacity: 1;
            font-style: italic;
            transition: color 0.3s;
        }
        .search-bar input:focus::placeholder {
            color: #ffb86c;
        }
        .main-menu-bar {
            max-height: 38px;
            background: linear-gradient(90deg, #fffbe7 0%, #ffd6b0 100%);
            border-radius: 0;
            box-shadow: 0 2px 8px 0 rgba(234,109,34,0.08);
            margin: 0 0 0.5rem 0;
            padding: 0.1rem 0 0.1rem 0;
            width: 100vw;
            left: 50%;
            right: 50%;
            transform: translateX(-50%);
            position: relative;
        }
        .main-menu {
            display: flex;
            justify-content: center;
            gap: 0.7rem;
            padding: 0.2rem 0;
            margin: 0;
        }
        .main-menu .nav-link {
            color: #EA6D22;
            font-weight: 700;
            padding: 0.35rem 0.9rem;
            border-radius: 12px;
            transition: background 0.2s, color 0.2s;
            font-size: 0.98rem;
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .main-menu .nav-link i {
            font-size: 1.25em;
        }
        .main-menu .nav-link.active,
        .main-menu .nav-link:hover {
            background: #EA6D22;
            color: #fff;
        }
        /* Banner mini quảng cáo */
        .mini-banner {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 2.5rem;
            background: #fffbe7;
            color: #EA6D22;
            font-weight: 600;
            font-size: 1.08rem;
            padding: 0.5rem 0 0.3rem 0;
            border-radius: 0 0 0 0;
            margin: 0 0 0.5rem 0;
            box-shadow: 0 1px 4px 0 rgba(234,109,34,0.06);
            width: 100vw;
            left: 50%;
            right: 50%;
            transform: translateX(-50%);
            position: relative;
        }
        .mini-banner i {
            margin-right: 6px;
            color: #EA6D22;
        }
        @media (max-width: 991px) {
            .header-main { border-radius: 0 0 1rem 1rem; margin-bottom: 0.5rem; }
            .header-topbar { flex-direction: column; padding: 0.5rem 0.5rem 0.2rem 0.5rem; gap: 0.7rem; }
            .header-content { flex-direction: column; align-items: flex-start; padding: 0.7rem 0.5rem 0.7rem 0.5rem; }
            .header-logo { font-size: 1.3rem; }
            .header-logo i { font-size: 1.7rem; }
            .header-search { max-width: 100%; margin: 10px 0; }
            .main-menu-bar { margin: 0 0.5rem 0.5rem 0.5rem; border-radius: 0.7rem; }
            .main-menu { flex-wrap: wrap; }
            .main-menu .nav-link { padding: 0.5rem 1rem; font-size: 1rem; }
        }
        .header-topbar .hotline,
        .header-topbar .hotline-mini,
        .header-icon-link {
            text-decoration: none !important;
        }
    </style>
</head>
<body>
    <div class="header-main">
        <div class="header-topbar">
            <span class="hotline-mini d-none d-lg-flex"><i class="fa fa-headset"></i> Hỗ trợ 24/7</span>
            <a href="tel:18001060" class="hotline"><i class="fa fa-phone-volume"></i> 1800 1060</a>
            <a href="/account" class="header-icon-link" title="Tài khoản">
                <i class="fa fa-user-circle"></i>
            </a>
            <a href="/cart" class="header-icon-link position-relative" title="Giỏ hàng">
                <i class="fa fa-shopping-cart"></i>
                <span class="header-badge" id="cartBadge">2</span>
            </a>
        </div>
        <div class="header-content">
            <a href="/" class="header-logo text-decoration-none">
                <i class="fa-solid fa-mobile-screen-button"></i>
                Ocean SmartPhone
                <span class="logo-slogan d-none d-md-inline">Công nghệ trong tầm tay bạn</span>
            </a>
            <div class="header-search">
                <form class="search-bar" action="/search" method="get" autocomplete="off">
                    <input class="form-control" type="search" name="q" id="searchInput" placeholder="Tìm kiếm sản phẩm, hãng, phụ kiện..." aria-label="Search">
                    <button class="btn d-flex align-items-center justify-content-center" type="submit" tabindex="-1">
                        <i class="fa fa-search"></i>
                    </button>
                </form>
            </div>
        </div>
        <div class="main-menu-bar">
            <ul class="nav main-menu flex-wrap">
                <li class="nav-item"><a class="nav-link active" href="/"><i class="fa fa-home"></i> Trang chủ</a></li>
                <li class="nav-item"><a class="nav-link" href="/category"><i class="fa fa-th-large"></i> Tất cả sản phẩm</a></li>
                <li class="nav-item"><a class="nav-link" href="/promo"><i class="fa fa-gift"></i> Khuyến mãi</a></li>
                <li class="nav-item"><a class="nav-link" href="/guide"><i class="fa fa-book"></i> Hướng dẫn</a></li>
                <li class="nav-item"><a class="nav-link" href="/about"><i class="fa fa-info-circle"></i> Giới thiệu</a></li>
                <li class="nav-item"><a class="nav-link" href="/contact"><i class="fa fa-phone"></i> Liên hệ</a></li>
            </ul>
        </div>
        <div class="mini-banner">
            <span><i class="fa fa-truck"></i> Freeship toàn quốc</span>
            <span><i class="fa fa-credit-card"></i> Trả góp 0%</span>
            <span><i class="fa fa-shield-alt"></i> Bảo hành 12 tháng</span>
            <span><i class="fa fa-sync-alt"></i> Đổi trả 30 ngày</span>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script>
    // Placeholder động cho thanh tìm kiếm
    const placeholders = [
        "Tìm kiếm sản phẩm, hãng, phụ kiện...",
        "iPhone 16 Pro Max, Samsung S25, Xiaomi 14T...",
        "Ốp lưng, sạc nhanh, tai nghe, MacBook...",
        "Khuyến mãi, trả góp, bảo hành..."
    ];
    let idx = 0;
    setInterval(() => {
        document.getElementById('searchInput').placeholder = placeholders[idx];
        idx = (idx + 1) % placeholders.length;
    }, 2500);

    // Badge động (ví dụ, tăng số lượng giỏ hàng sau 5s)
    setTimeout(() => {
        document.getElementById('cartBadge').textContent = '3';
    }, 5000);
    </script>
</body>
</html>

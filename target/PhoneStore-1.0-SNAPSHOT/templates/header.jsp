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
<body style="margin:0; padding:0; background:#f8f9fa;">
    <header style="position:sticky;top:0;z-index:1000;width:100vw;background:#FFD600;box-shadow:0 2px 12px 0 rgba(234,109,34,0.10);border-radius:0 0 1.2rem 1.2rem;">
        <div style="max-width:1200px;margin:auto;display:flex;align-items:center;justify-content:space-between;padding:10px 24px 6px 24px;">
            <!-- Logo -->
            <a href="/" class="d-flex align-items-center text-decoration-none" style="gap:8px;">
                <img src="/images/logo.png" alt="Ocean SmartPhone" style="height:38px;width:auto;object-fit:contain;">
                <span style="font-size:1.45rem;font-weight:700;color:#222;letter-spacing:1px;">Ocean SmartPhone</span>
            </a>
            <!-- Search -->
            <form style="flex:1;max-width:500px;margin:0 32px;" action="/search" method="get" autocomplete="off">
                <div style="display:flex;align-items:center;background:#fff;border-radius:2rem;padding:2px 12px;box-shadow:0 1px 4px 0 rgba(0,0,0,0.04);">
                    <input class="form-control border-0" type="search" name="q" id="searchInput" placeholder="Tìm kiếm sản phẩm, hãng, phụ kiện..." aria-label="Search" style="background:transparent;color:#222;font-size:1.08rem;padding:0.5rem 0.7rem;box-shadow:none;outline:none;border:none;flex:1;min-width:0;::placeholder{color:#aaa;}">
                    <button class="btn border-0 bg-white rounded-pill" type="submit" tabindex="-1" style="color:#222;font-size:1.3rem;"><i class="fa fa-search"></i></button>
                </div>
            </form>
            <!-- Icons -->
            <div class="d-flex align-items-center gap-3">
                <a href="/account" class="d-flex flex-column align-items-center text-decoration-none" style="color:#222;font-size:1.25rem;">
                    <i class="fa fa-user-circle"></i>
                    <span style="font-size:0.85rem;font-weight:500;">Tài khoản</span>
                </a>
                <a href="/cart" class="d-flex flex-column align-items-center text-decoration-none position-relative" style="color:#222;font-size:1.25rem;">
                    <i class="fa fa-shopping-cart"></i>
                    <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger" id="cartBadge" style="font-size:0.8rem;">2</span>
                    <span style="font-size:0.85rem;font-weight:500;">Giỏ hàng</span>
                </a>
            </div>
        </div>
        <!-- Main menu -->
        <nav class="main-menu-bar" style="background:#FFD600;border-radius:0 0 1.2rem 1.2rem;box-shadow:0 1px 4px 0 rgba(234,109,34,0.06);">
            <div style="max-width:1200px;margin:auto;">
                <ul class="nav main-menu flex-nowrap justify-content-center" style="gap:0.2rem;padding:0.2rem 0;margin:0;">
                    <li class="nav-item"><a class="nav-link" href="/" style="color:#222;font-weight:450;">Trang chủ</a></li>
                    <li class="nav-item"><a class="nav-link" href="/category?type=phone" style="color:#222;font-weight:450;">Điện thoại</a></li>
                    <li class="nav-item"><a class="nav-link" href="/category?type=accessory" style="color:#222;font-weight:450;">Phụ kiện</a></li>
                    <li class="nav-item"><a class="nav-link" href="/promo" style="color:#222;font-weight:450;">Khuyến mãi</a></li>
                    <li class="nav-item"><a class="nav-link" href="/faq" style="color:#222;font-weight:450;">Hỏi đáp</a></li>
                    <li class="nav-item"><a class="nav-link" href="/contact" style="color:#222;font-weight:450;">Liên hệ</a></li>
                </ul>
            </div>
        </nav>
    </header>
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

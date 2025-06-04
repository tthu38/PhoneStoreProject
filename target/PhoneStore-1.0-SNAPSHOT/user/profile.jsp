<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.User" %>
<%
    User user = (User) session.getAttribute("userObject");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/user/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Thông tin cá nhân - Ocean SmartPhone</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
    <style>
        .profile-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
            background: white;
            border-radius: 15px;
            box-shadow: 0 0 20px rgba(0,0,0,0.1);
        }
        
        .profile-header {
            display: flex;
            align-items: center;
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 2px solid #f0f0f0;
        }
        
        .profile-avatar {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            margin-right: 2rem;
            object-fit: cover;
            border: 3px solid #EA6D22;
            padding: 3px;
        }
        
        .profile-title {
            flex: 1;
        }
        
        .profile-name {
            font-size: 2rem;
            color: #333;
            margin: 0;
        }
        
        .profile-email {
            color: #666;
            margin: 0.5rem 0;
        }
        
        .profile-verified {
            display: inline-flex;
            align-items: center;
            background: #e8f5e9;
            color: #2e7d32;
            padding: 0.3rem 0.8rem;
            border-radius: 20px;
            font-size: 0.9rem;
        }
        
        .profile-verified i {
            margin-right: 5px;
        }
        
        .profile-section {
            margin-bottom: 2rem;
        }
        
        .profile-section h3 {
            color: #EA6D22;
            margin-bottom: 1rem;
            font-size: 1.5rem;
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 1rem;
        }
        
        .info-item {
            background: #f8f9fa;
            padding: 1rem;
            border-radius: 10px;
            border-left: 4px solid #EA6D22;
        }
        
        .info-label {
            color: #666;
            font-size: 0.9rem;
            margin-bottom: 0.3rem;
        }
        
        .info-value {
            color: #333;
            font-weight: 500;
        }
        
        .google-info {
            background: #fff8e1;
            padding: 1.5rem;
            border-radius: 10px;
            margin-top: 1rem;
        }
        
        .google-link {
            color: #EA6D22;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            margin-top: 1rem;
            padding: 0.5rem 1rem;
            background: white;
            border-radius: 20px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            transition: all 0.3s ease;
        }
        
        .google-link:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(0,0,0,0.15);
        }
        
        .edit-profile-btn {
            background: #EA6D22;
            color: white;
            border: none;
            padding: 0.8rem 2rem;
            border-radius: 25px;
            font-weight: 500;
            transition: all 0.3s ease;
            margin-top: 1rem;
        }
        
        .edit-profile-btn:hover {
            background: #d15d1b;
            transform: translateY(-2px);
        }
    </style>
</head>
<body style="background: #f8f9fa;">
    <jsp:include page="/templates/header.jsp"/>
    
    <div class="profile-container">
        <div class="profile-header">
            <% if (user.getPicture() != null) { %>
                <img src="<%= user.getPicture() %>" alt="Avatar" class="profile-avatar">
            <% } else { %>
                <i class="fa-solid fa-user-circle fa-5x" style="color: #EA6D22;"></i>
            <% } %>
            
            <div class="profile-title">
                <h1 class="profile-name"><%= user.getFullname() %></h1>
                <div class="profile-email"><%= user.getEmail() %></div>
                <% if (user.isVerifiedEmail()) { %>
                    <div class="profile-verified">
                        <i class="fas fa-check-circle"></i>
                        Email đã xác thực
                    </div>
                <% } %>
            </div>
        </div>
        
        <div class="profile-section">
            <h3><i class="fas fa-info-circle"></i> Thông tin cơ bản</h3>
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">Họ và tên</div>
                    <div class="info-value"><%= user.getFullname() %></div>
                </div>
                <div class="info-item">
                    <div class="info-label">Email</div>
                    <div class="info-value"><%= user.getEmail() %></div>
                </div>
                <% if (user.getPhone() != null && !user.getPhone().isEmpty()) { %>
                    <div class="info-item">
                        <div class="info-label">Số điện thoại</div>
                        <div class="info-value"><%= user.getPhone() %></div>
                    </div>
                <% } %>
                <div class="info-item">
                    <div class="info-label">Loại tài khoản</div>
                    <div class="info-value">
                        <% if (user.isAdmin()) { %>
                            <span class="badge bg-danger">Admin</span>
                        <% } else { %>
                            <span class="badge bg-primary">Khách hàng</span>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
        
        <% if (user.getIsOauthUser() && "GOOGLE".equals(user.getOauthProvider())) { %>
            <div class="profile-section">
                <h3><i class="fab fa-google"></i> Thông tin Google</h3>
                <div class="google-info">
                    <div class="info-grid">
                        <div class="info-item">
                            <div class="info-label">Google ID</div>
                            <div class="info-value"><%= user.getGoogleId() %></div>
                        </div>
                        <% if (user.getGivenName() != null) { %>
                            <div class="info-item">
                                <div class="info-label">Tên</div>
                                <div class="info-value"><%= user.getGivenName() %></div>
                            </div>
                        <% } %>
                        <% if (user.getFamilyName() != null) { %>
                            <div class="info-item">
                                <div class="info-label">Họ</div>
                                <div class="info-value"><%= user.getFamilyName() %></div>
                            </div>
                        <% } %>
                    </div>
                    <% if (user.getGoogleLink() != null) { %>
                        <a href="<%= user.getGoogleLink() %>" target="_blank" class="google-link">
                            <i class="fab fa-google"></i>
                            Xem trang Google của tôi
                        </a>
                    <% } %>
                </div>
            </div>
        <% } %>
        
        <form action="${pageContext.request.contextPath}/logout" method="post" style="display:inline;">
            <button type="submit" class="edit-profile-btn" style="background:#dc3545;">
                <i class="fas fa-sign-out-alt"></i> Đăng xuất
            </button>
        </form>
    </div>
    
    <jsp:include page="/templates/footer.jsp"/>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html> 
<%-- 
    Document   : login
    Created on : May 29, 2025, 11:25:27 AM
    Author     : dangt
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Ocean SmartPhone - Đăng nhập</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css">
        <style>
            .login-container {
                min-height: 100vh;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                display: flex;
                align-items: center;
                justify-content: center;
                padding: 20px;
            }
            .card {
                border: none;
                border-radius: 15px;
                box-shadow: 0 15px 35px rgba(0,0,0,0.2);
                overflow: hidden;
                max-width: 400px;
                width: 100%;
            }
            .card-header {
                background: transparent;
                border-bottom: none;
                padding-top: 30px;
            }
            .card-body {
                padding: 30px;
            }
            .form-control {
                border-radius: 10px;
                padding: 12px;
                border: 1px solid #ddd;
                transition: all 0.3s;
            }
            .form-control:focus {
                box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.25);
                border-color: #667eea;
            }
            .btn-primary {
                background: linear-gradient(45deg, #667eea, #764ba2);
                border: none;
                border-radius: 10px;
                padding: 12px;
                font-weight: 600;
                letter-spacing: 0.5px;
                transition: all 0.3s;
            }
            .btn-primary:hover {
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
            }
            .input-group-text {
                background: transparent;
                border-right: none;
                padding-right: 0;
            }
            .input-group .form-control {
                border-left: none;
                padding-left: 0;
            }
            .input-group {
                background: white;
                border-radius: 10px;
                overflow: hidden;
            }
            .social-login {
                margin-top: 20px;
                padding-top: 20px;
                border-top: 1px solid #eee;
            }
            .btn-google {
                background: white;
                color: #757575;
                border: 1px solid #ddd;
                padding: 12px;
                border-radius: 10px;
                transition: all 0.3s;
            }
            .btn-google:hover {
                background: #f8f9fa;
                transform: translateY(-2px);
                box-shadow: 0 5px 15px rgba(0,0,0,0.1);
            }
            .logo-text {
                font-size: 24px;
                font-weight: bold;
                background: linear-gradient(45deg, #667eea, #764ba2);
                -webkit-background-clip: text;
                -webkit-text-fill-color: transparent;
                margin-bottom: 10px;
            }
        </style>
    </head>
    <body>
        <div class="login-container">
            <div class="card">
                <div class="card-header text-center">
                    <div class="logo-text">Phone Store</div>
                    <p class="text-muted">Chào mừng bạn trở lại!</p>
                </div>
                <div class="card-body">
                    <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                            <i class="fas fa-exclamation-circle me-2"></i>
                            <%= request.getAttribute("error") %>
                            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                        </div>
                    <% } %>
                    
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-4">
                            <div class="input-group">
                                <span class="input-group-text border-0">
                                    <i class="fas fa-envelope text-muted"></i>
                                </span>
                                <input type="email" class="form-control border-0" id="email" name="email" 
                                       placeholder="Nhập email của bạn" required>
                            </div>
                        </div>
                        
                        <div class="mb-4">
                            <div class="input-group">
                                <span class="input-group-text border-0">
                                    <i class="fas fa-lock text-muted"></i>
                                </span>
                                <input type="password" class="form-control border-0" id="password" name="password" 
                                       placeholder="Nhập mật khẩu" required>
                            </div>
                        </div>

                        <div class="mb-4 form-check">
                            <input type="checkbox" class="form-check-input" id="remember">
                            <label class="form-check-label" for="remember">Ghi nhớ đăng nhập</label>
                            <a href="${pageContext.request.contextPath}/forgot-password" class="float-end text-decoration-none">
                                Quên mật khẩu?
                            </a>
                        </div>

                        <button type="submit" class="btn btn-primary w-100">
                            <i class="fas fa-sign-in-alt me-2"></i>Đăng nhập
                        </button>
                    </form>

                    <div class="social-login">
                        <p class="text-center text-muted mb-4">Hoặc đăng nhập bằng</p>
                        <a href="${pageContext.request.contextPath}/oauth2/google" class="btn btn-google w-100">
                            <img src="https://www.google.com/favicon.ico" alt="Google" width="20" class="me-2">
                            Đăng nhập với Google
                        </a>
                    </div>

                    <div class="text-center mt-4">
                        <p class="mb-0">Chưa có tài khoản? 
                            <a href="${pageContext.request.contextPath}/register" class="text-decoration-none">
                                Đăng ký ngay
                            </a>
                        </p>
                    </div>
                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
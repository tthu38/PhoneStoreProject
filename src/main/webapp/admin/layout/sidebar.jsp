<%-- 
    Document   : sidebar
    Created on : Jun 5, 2025, 11:39:36 AM
    Author     : ASUS
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="d-flex flex-column p-3">
            <a href="${pageContext.request.contextPath}/admin" class="text-decoration-none text-white ">
                <h4 class="text-center">Phone Store Admin</h4>
            </a>
            <hr class="text-white">
            <ul class="nav nav-pills flex-column mb-auto">
                <li class="nav-item mb-2">
                    <a href= "${pageContext.request.contextPath}/admin?menu=dashboard" class="nav-link text-white ${param.menu == 'dashboard' ? 'active' : ''}" aria-current="page">
                        <i class="fas fa-home me-2"></i>
                        <span>Dashboard</span>
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a href="${pageContext.request.contextPath}/admin?menu=products" 
                       class="nav-link text-white ${param.menu == 'products' ? 'active' : ''}">
                        <i class="fas fa-mobile-alt me-2"></i>
                        <span>Products</span>
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a href="${pageContext.request.contextPath}/admin?menu=customers" class="nav-link text-white ${param.menu == 'customers' ? 'active' : ''}">
                        <i class="fas fa-users me-2"></i>
                        <span>Customers</span>
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a href="${pageContext.request.contextPath}/admin?menu=discounts" class="nav-link text-white ${param.menu == 'discounts' ? 'active' : ''}">
                        <i class="fas fa-list me-2"></i>
                        <span>Discount</span>
                    </a>
                </li>
                <li class="nav-item mb-2">
                    <a href="${pageContext.request.contextPath}/admin?menu=orders" class="nav-link text-white ${param.menu == 'orders' ? 'active' : ''}">
                        <i class="fas fa-shopping-cart me-2"></i>
                        <span>Orders</span>
                    </a>
                </li>
                
            </ul>
        </div>

        <style>
            .text-center{
                margin-bottom: 0px;
            }
            .nav-link {
                border-radius: 8px;
                transition: all 0.3s;
                padding: 10px 15px;
            }
            
            .nav-link:hover {
                background-color: rgba(255, 255, 255, 0.1);
                transform: translateX(5px);
            }
            
            .nav-link.active {
                background-color: #0d6efd !important;
                font-weight: bold;
            }
            
            .nav-link span {
                font-size: 14px;
            }
            
            .nav-link i {
                width: 20px;
                text-align: center;
            }
        </style>
    </body>
</html>

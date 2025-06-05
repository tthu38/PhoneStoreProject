<%-- 
    Document   : header
    Created on : Jun 5, 2025, 11:39:26 AM
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
        <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
            <div class="container-fluid">
                <button class="btn btn-link" id="menu-toggle">
                    <i class="fas fa-bars"></i>
                </button>

                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown">
                                <i class="fas fa-user-circle me-1"></i>
                                Admin
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end">
                                <li><a class="dropdown-item" href="#"><i class="fas fa-user-cog me-2"></i>Profile</a></li>
                                <li><a class="dropdown-item" href="#"><i class="fas fa-cog me-2"></i>Settings</a></li>
                                <li><hr class="dropdown-divider"></li>
                                <li><a class="dropdown-item" href="#"><i class="fas fa-sign-out-alt me-2"></i>Logout</a></li>
                            </ul>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#" data-bs-toggle="tooltip" title="Notifications">
                                <i class="fas fa-bell"></i>
                                <span class="badge bg-danger">3</span>
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    </body>
</html>

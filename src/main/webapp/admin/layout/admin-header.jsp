<%-- 
    Document   : admin-layout
    Created on : Jun 5, 2025, 11:39:10 AM
    Author     : ASUS
--%>

<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Phone Store Admin</title>
        <!-- Bootstrap CSS -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <!-- Font Awesome -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
        <style>
            .sidebar {
                min-height: 100vh;
                background: #343a40;
                color: white;
            }
            .content {
                padding: 20px;
            }
            .nav-link {
                color: rgba(255,255,255,.8);
            }
            .nav-link:hover {
                color: white;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <div class="col-md-3 col-lg-2 px-0 sidebar">
                    <jsp:include page="sidebar.jsp" />
                </div>
                
                <!-- Main Content -->
                <div class="col-md-9 col-lg-10 px-0">
                    <!-- Header -->
                    <jsp:include page="header.jsp" />
                    
                    <!-- Content Area -->
                    <main class="content">
                        <jsp:include page="${contentPage}" />
                    </main>
                </div>
            </div>
        </div>

        <!-- Bootstrap Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 
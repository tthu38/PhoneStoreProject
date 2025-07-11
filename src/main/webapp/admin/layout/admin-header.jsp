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
            html, body {
                height: 100%;
                margin: 0;
                overflow: hidden; /* Ngăn cuộn toàn trang */
            }

            .sidebar {    
                position: fixed;
                top: 0;
                bottom: 0;
                left: 0;
                background: #343a40;
                color: white;
                z-index: 1000;
                overflow-y: auto;
            }

            .header {
                height: 50px;
                background: #f8f9fa;
                position: fixed;
                top: 0;
                left: 250px; /* bằng width của sidebar */
                right: 0;
                z-index: 999;
                border-bottom: 1px solid #ddd;
                padding: 10px 0px;
            }

            .main-area {
                margin-left: 250px; /* để tránh đè lên sidebar */
                padding-top: 40px;  /* để tránh đè lên header */
                height: 100vh;
                display: flex;
                flex-direction: column;
            }

            .content-area {
                flex: 1;
                overflow-y: auto;
               
            }
        </style>
    </head>
    <body>

        <div class="d-flex">
            <!-- Sidebar (fixed) -->
            <div class="sidebar">
                <jsp:include page="sidebar.jsp" />
            </div>

            <!-- Right content -->
            <div class="main-area flex-grow-1">
                <!-- Header (fixed) -->
                <div class="header">
                    <jsp:include page="header.jsp" />
                </div>

                <!-- Content area (scrollable) -->
                <main class="content-area">
                    <jsp:include page="${contentPage}" />
                </main>
            </div>
        </div>


        <!-- Bootstrap Bundle with Popper -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 
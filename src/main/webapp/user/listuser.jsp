<%-- 
    Document   : listuser
    Created on : Jul 11, 2025, 9:12:45 PM
    Author     : dangt
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Danh sách người dùng</title>
    <style>
        table { width: 100%; border-collapse: collapse; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
        th { background: #f5f5f5; }
        .btn { padding: 4px 12px; margin-right: 4px; }
        .btn-add { background: #28a745; color: #fff; border: none; }
        .btn-edit { background: #ffc107; color: #fff; border: none; }
        .btn-delete { background: #dc3545; color: #fff; border: none; }
    </style>
</head>
<body>
    <h2>Danh sách người dùng</h2>
    <a href="${pageContext.request.contextPath}/users?action=add" class="btn btn-add">Thêm mới</a>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Email</th>
                <th>Họ tên</th>
                <th>SĐT</th>
                <th>Ngày sinh</th>
                <th>Địa chỉ</th>
                <th>Quyền</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
        </thead>
        <tbody>
        <%
            List<User> users = (List<User>) request.getAttribute("users");
            if (users != null && !users.isEmpty()) {
                for (User u : users) {
        %>
            <tr>
                <td><%= u.getUserID() %></td>
                <td><%= u.getEmail() %></td>
                <td><%= u.getFullName() %></td>
                <td><%= u.getPhoneNumber() %></td>
                <td><%= u.getDob() != null ? u.getDob().toString() : "" %></td>
                <td>
<%
    Map<Integer, model.UserAddress> addressMap = (Map<Integer, model.UserAddress>) request.getAttribute("addressMap");
    model.UserAddress addr = addressMap != null ? addressMap.get(u.getUserID()) : null;
    out.print(addr != null ? addr.getAddress() : "");
%>
</td>
                <td><%= u.getRoleID() %></td>
                <td><%= u.getIsActive() ? "Hoạt động" : "Khóa" %></td>
                <td>
                    <a href="${pageContext.request.contextPath}/users?action=edit&id=<%=u.getUserID()%>" class="btn btn-edit">Chỉnh sửa</a>
                    <a href="${pageContext.request.contextPath}/users?action=delete&id=<%=u.getUserID()%>" class="btn btn-delete" onclick="return confirm('Xóa user này?');">Xóa</a>
                </td>
            </tr>
        <%
                }
            } else {
        %>
            <tr><td colspan="9">Không có dữ liệu người dùng.</td></tr>
        <%
            }
        %>
        </tbody>
    </table>
</body>
</html>

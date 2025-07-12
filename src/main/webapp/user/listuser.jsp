<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="model.User"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Danh sách người dùng</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/listuser.css">
        
</head>
<body>
    <div class="container">
        <h2 style="margin-bottom: 24px;">Danh sách người dùng</h2>
        <form action="admin" method="get">
            <input type="hidden" name="menu" value="customers"/>
            <div class="filters-row">
                <select name="sort">
                    <option value="">Sắp xếp</option>
                    <option value="name_asc">Tên: A → Z</option>
                    <option value="name_desc">Tên: Z → A</option>
                    <option value="id_asc">ID: Tăng dần</option>
                    <option value="id_desc">ID: Giảm dần</option>
                </select>
                <select name="status">
                    <option value="">Trạng thái</option>
                    <option value="active">Hoạt động</option>
                    <option value="inactive">Khóa</option>
                </select>
                <input type="text" name="searchName" placeholder="Tìm theo tên hoặc email..." />
                <button type="submit"><i class="fas fa-filter"></i> Lọc</button>
                <a href="${pageContext.request.contextPath}/users?action=add" class="add-user-btn">
                    <i class="fas fa-plus"></i> Thêm mới
                </a>
            </div>
        </form>
        <div class="table-responsive">
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
                    Map<Integer, model.UserAddress> addressMap = (Map<Integer, model.UserAddress>) request.getAttribute("addressMap");
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
                                model.UserAddress addr = addressMap != null ? addressMap.get(u.getUserID()) : null;
                                out.print(addr != null ? addr.getAddress() : "");
                            %>
                        </td>
                        <td><%= u.getRoleID() %></td>
                        <td>
                            <span class="status-badge <%= u.getIsActive() ? "active" : "inactive" %>">
                                <%= u.getIsActive() ? "Hoạt động" : "Khóa" %>
                            </span>
                        </td>
                        <td class="action-links">
                            <a href="${pageContext.request.contextPath}/users?action=edit&id=<%=u.getUserID()%>" class="edit">
                                <i class="fas fa-edit"></i> Chỉnh sửa
                            </a>
                            <a href="${pageContext.request.contextPath}/users?action=delete&id=<%=u.getUserID()%>" class="delete"
                               onclick="return confirm('Xóa user này?');">Xóa</a>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="9" style="text-align: center; color: red; padding: 16px;">Không có dữ liệu người dùng.</td>
                    </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>

package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import model.UserAddress;
import service.UserService;
import service.UserAddressService;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import utils.ValidationUtils;

@WebServlet(name = "UserServlet", urlPatterns = {"/users"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 10 * 1024 * 1024)
public class UserServlet extends HttpServlet {

    private UserService userService = new UserService();
    private UserAddressService addressService = new UserAddressService();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<h1>UserServlet working...</h1>");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        switch (action == null ? "" : action) {
            case "edit-profile":
                showEditProfile(request, response, session);
                break;
            case "list":
                showUserList(request, response);
                break;
            case "add":
                showAddUserForm(request, response);
                break;
            case "edit":
                showEditUserForm(request, response);
                break;
            case "delete":
                handleDeleteUser(request, response);
                break;
            default:
                processRequest(request, response);
        }
    }

    private void showEditProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        User user = (User) session.getAttribute("user");
        List<UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
        UserAddress userAddress = (addresses != null && !addresses.isEmpty()) ? addresses.get(0) : null;
        request.setAttribute("user", user);
        request.setAttribute("userAddress", userAddress);
        request.getRequestDispatcher("/user/editprofile.jsp").forward(request, response);
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        Map<Integer, UserAddress> addressMap = new HashMap<>();
        for (User u : users) {
            List<UserAddress> addresses = addressService.getAllAddressesByUserId(u.getUserID());
            if (addresses != null && !addresses.isEmpty()) {
                addressMap.put(u.getUserID(), addresses.get(0));
            }
        }

        request.setAttribute("users", users);
        request.setAttribute("addressMap", addressMap);
        request.getRequestDispatcher("/user/listuser.jsp").forward(request, response);
    }

    private void showAddUserForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/user/adduser.jsp").forward(request, response);
    }

    private void showEditUserForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            User user = userService.getUserById(id).orElse(null);
            if (user != null) {
                List<UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
                UserAddress userAddress = (addresses != null && !addresses.isEmpty()) ? addresses.get(0) : null;
                request.setAttribute("user", user);
                request.setAttribute("userAddress", userAddress);
                request.getRequestDispatcher("/user/edituser.jsp").forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/users?action=list");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/users?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser.getRoleID() != 1 && !"update-profile".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            return;
        }

        switch (action == null ? "" : action) {
            case "add":
                handleAddUser(request, response);
                break;
            case "edit":
                handleEditUser(request, response);
                break;
            case "delete":
                handleDeleteUser(request, response);
                break;
            case "update-profile":
                handleUpdateProfile(request, response, session);
                break;
        }
    }

    private void handleAddUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String phoneNumber = request.getParameter("phoneNumber");
        String dobStr = request.getParameter("dob");
        String roleIdStr = request.getParameter("roleID");
        String address = request.getParameter("address");

        if (userService.getUserByEmail(email).isPresent()) {
            request.setAttribute("error", "Email đã tồn tại. Vui lòng chọn email khác.");
            request.setAttribute("fullName", fullName);
            request.setAttribute("phoneNumber", phoneNumber);
            request.setAttribute("dob", dobStr);
            request.setAttribute("address", address);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/user/adduser.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setIsActive(true);
        user.setRoleID(roleIdStr != null ? Integer.parseInt(roleIdStr) : 2);
        if (dobStr != null && !dobStr.isEmpty()) {
            user.setDob(java.time.LocalDate.parse(dobStr));
        }
        userService.addUser(user);

        User addedUser = userService.getUserByEmail(email).orElse(null);
        if (addedUser != null && address != null && !address.trim().isEmpty()) {
            model.UserAddress userAddress = new model.UserAddress();
            userAddress.setUser(addedUser);
            userAddress.setFullName(fullName);
            userAddress.setPhoneNumber(phoneNumber);
            userAddress.setAddress(address);
            userAddress.setIsDefault(true);
            userAddress.setIsActive(true);
            userAddress.setCreatedAt(java.time.Instant.now());
            service.UserAddressService addressService = new service.UserAddressService();
            addressService.addAddress(userAddress);
        }

        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    private void handleEditUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("userID"));
            User user = userService.getUserById(id).orElse(null);

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/users?action=list");
                return;
            }

            String email = request.getParameter("email").trim();
            String fullName = request.getParameter("fullName").trim();
            String phoneNumber = request.getParameter("phoneNumber").trim();
            String dobStr = request.getParameter("dob");
            String roleIdStr = request.getParameter("roleID");
            String fullAddress = request.getParameter("address");
            String newPassword = request.getParameter("password"); // thêm nếu có ô mật khẩu

            Optional<User> existing = userService.getUserByEmail(email);
            boolean emailExists = existing.isPresent() && existing.get().getUserID() != user.getUserID();

            // Load địa chỉ cũ
            UserAddress oldAddress = addressService.getAllAddressesByUserId(user.getUserID()).stream().findFirst().orElse(null);

            // Nếu có lỗi thì forward về lại form
            if (emailExists || !ValidationUtils.isValidEmail(email)) {
                request.setAttribute("error", emailExists ? "Email đã tồn tại, vui lòng nhập email khác." : "Email không hợp lệ.");
            } else if (!ValidationUtils.isValidPhoneNumber(phoneNumber)) {
                request.setAttribute("error", "Số điện thoại phải bắt đầu bằng 0 và có 10 hoặc 11 chữ số.");
            } else if (!ValidationUtils.isValidFullName(fullName)) {
                request.setAttribute("error", "Họ và tên không hợp lệ.");
            } else if (!ValidationUtils.isValidAddress(fullAddress)) {
                request.setAttribute("error", "Địa chỉ không hợp lệ.");
            } else if (ValidationUtils.isNotBlank(dobStr)) {
                LocalDate dob = LocalDate.parse(dobStr);
                if (dob.isAfter(LocalDate.now())) {
                    request.setAttribute("error", "Ngày sinh không được lớn hơn ngày hiện tại.");
                } else {
                    user.setDob(dob);
                }
            }

            // Nếu có lỗi, quay lại trang
            if (request.getAttribute("error") != null) {
                request.setAttribute("user", user);
                request.setAttribute("address", oldAddress);

                // Tách địa chỉ để pre-select tỉnh/huyện/xã
                if (oldAddress != null) {
                    String[] parts = oldAddress.getAddress().split(",\\s*");
                    request.setAttribute("ward", parts.length > 0 ? parts[0] : "");
                    request.setAttribute("district", parts.length > 1 ? parts[1] : "");
                    request.setAttribute("province", parts.length > 2 ? parts[2] : "");
                }

                request.getRequestDispatcher("/user/edituser.jsp").forward(request, response);
                return;
            }

            // Gán dữ liệu mới
            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);
            if (ValidationUtils.isNotBlank(roleIdStr)) {
                user.setRoleID(Integer.parseInt(roleIdStr));
            }
            if (ValidationUtils.isNotBlank(newPassword)) {
                user.setPassword(newPassword); // bạn có thể mã hóa nếu cần
            }

            userService.updateUser(user);

            List<UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
            UserAddress userAddress;
            if (!addresses.isEmpty()) {
                userAddress = addresses.get(0);
                userAddress.setFullName(fullName);
                userAddress.setPhoneNumber(phoneNumber);
                userAddress.setAddress(fullAddress);
                addressService.updateAddress(userAddress);
            } else {
                userAddress = new UserAddress();
                userAddress.setUser(user);
                userAddress.setFullName(fullName);
                userAddress.setPhoneNumber(phoneNumber);
                userAddress.setAddress(fullAddress);
                userAddress.setIsDefault(true);
                userAddress.setIsActive(true);
                userAddress.setCreatedAt(Instant.now());
                addressService.addAddress(userAddress);
            }

            response.sendRedirect(request.getContextPath() + "/users?action=list");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            request.getRequestDispatcher("/user/edituser.jsp").forward(request, response);
        }
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
    }

    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Optional<User> optionalUser = userService.getUserById(id);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                user.setIsActive(false); // cập nhật trạng thái thay vì xóa
                userService.updateUser(user);
            }

        } catch (NumberFormatException e) {
            e.printStackTrace(); // log lỗi nếu cần
        }

        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    @Override
    public String getServletInfo() {
        return "User Management Servlet with Add/Edit/Delete/Upload";
    }
}

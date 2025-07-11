/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import service.UserService;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ThienThu
 */
@WebServlet(name = "UserServlet", urlPatterns = {"/users"})
public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UserServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UserServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void showEditProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        User user = (User) session.getAttribute("user");
        service.UserAddressService addressService = new service.UserAddressService();
        List<model.UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
        model.UserAddress userAddress = (addresses != null && !addresses.isEmpty()) ? addresses.get(0) : null;
        request.setAttribute("user", user);
        request.setAttribute("userAddress", userAddress);
        request.getRequestDispatcher("/user/editprofile.jsp").forward(request, response);
    }

    private void showUserList(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        List<User> users = userService.getAllUsers();
        service.UserAddressService addressService = new service.UserAddressService();

        // Tạo Map<UserID, UserAddress>
        Map<Integer, model.UserAddress> addressMap = new HashMap<>();
        for (User u : users) {
            List<model.UserAddress> addresses = addressService.getAllAddressesByUserId(u.getUserID());
            if (addresses != null && !addresses.isEmpty()) {
                addressMap.put(u.getUserID(), addresses.get(0));
            }
        }

        request.setAttribute("users", users);
        request.setAttribute("addressMap", addressMap); // Truyền map vào request
        request.getRequestDispatcher("/user/listuser.jsp").forward(request, response);
        System.out.println("User count: " + users.size());
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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
            case "add":
            case "edit":
            case "delete":
                showUserList(request, response);
                break;
            default:
                processRequest(request, response);
                break;
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
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
            default:
                break;
        }
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws ServletException, IOException {
        User user = (User) session.getAttribute("user");
        String fullName = request.getParameter("fullname");
        String phoneNumber = request.getParameter("phone");
        String dobStr = request.getParameter("dob");
        String address = request.getParameter("address");

        if (fullName == null || fullName.trim().isEmpty() ||
            phoneNumber == null || phoneNumber.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ họ tên và số điện thoại.");
            // Lấy lại địa chỉ để hiển thị lại form khi có lỗi
            service.UserAddressService addressService = new service.UserAddressService();
            List<model.UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
            model.UserAddress userAddress = (addresses != null && !addresses.isEmpty()) ? addresses.get(0) : null;
            request.setAttribute("userAddress", userAddress);
            request.getRequestDispatcher("/user/editprofile.jsp").forward(request, response);
            return;
        }

        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);

        if (dobStr != null && !dobStr.isEmpty()) {
            try {
                LocalDate dob = java.time.LocalDate.parse(dobStr);
                LocalDate today = LocalDate.now();
                if (dob.isAfter(today)) {
                    request.setAttribute("error", "Ngày sinh không được ở tương lai.");
                    request.getRequestDispatcher("/user/editprofile.jsp").forward(request, response);
                    return;
                }
                Period age = Period.between(dob, today);
                if (age.getYears() < 16) {
                    request.setAttribute("error", "Bạn phải đủ 16 tuổi trở lên.");
                    request.getRequestDispatcher("/user/editprofile.jsp").forward(request, response);
                    return;
                }
                user.setDob(dob);
            } catch (Exception e) {
                user.setDob(null);
                request.setAttribute("error", "Định dạng ngày sinh không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd.");
                request.getRequestDispatcher("/user/editprofile.jsp").forward(request, response);
                return;
            }
        } else {
            user.setDob(null);
        }

        userService.updateUser(user);
        User updatedUser = userService.getUserById(user.getUserID()).orElse(user);
        session.setAttribute("user", updatedUser);

        // Cập nhật địa chỉ
        service.UserAddressService addressService = new service.UserAddressService();
        List<model.UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
        model.UserAddress userAddress;
        if (addresses != null && !addresses.isEmpty()) {
            userAddress = addresses.get(0);
            userAddress.setAddress(address);
            addressService.updateAddress(userAddress);
        } else {
            userAddress = new model.UserAddress();
            userAddress.setUser(updatedUser);
            userAddress.setFullName(updatedUser.getFullName());
            userAddress.setPhoneNumber(updatedUser.getPhoneNumber());
            userAddress.setAddress(address);
            userAddress.setIsDefault(true);
            userAddress.setIsActive(true);
            userAddress.setCreatedAt(Instant.now());
            addressService.addAddress(userAddress);
        }
        System.out.println("userID khi add address: " + updatedUser.getUserID());
        response.sendRedirect(request.getContextPath() + "/user/profile.jsp");
    }

    // Thêm user mới
    private void handleAddUser(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullname");
        String phoneNumber = request.getParameter("phone");
        String dobStr = request.getParameter("dob");
        String roleIdStr = request.getParameter("roleID");
        String address = request.getParameter("address");

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

    // Sửa user
    private void handleEditUser(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        User user = userService.getUserById(id).orElse(null);
        if (user != null) {
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullname");
            String phoneNumber = request.getParameter("phone");
            String dobStr = request.getParameter("dob");
            String roleIdStr = request.getParameter("roleID");
            String address = request.getParameter("address");

            user.setEmail(email);
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);
            user.setRoleID(roleIdStr != null ? Integer.parseInt(roleIdStr) : user.getRoleID());
            if (dobStr != null && !dobStr.isEmpty()) {
                user.setDob(java.time.LocalDate.parse(dobStr));
            }
            userService.updateUser(user);

            service.UserAddressService addressService = new service.UserAddressService();
            List<model.UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
            model.UserAddress userAddress;
            if (addresses != null && !addresses.isEmpty()) {
                userAddress = addresses.get(0);
                userAddress.setFullName(fullName);
                userAddress.setPhoneNumber(phoneNumber);
                userAddress.setAddress(address);
                addressService.updateAddress(userAddress);
            } else if (address != null && !address.trim().isEmpty()) {
                userAddress = new model.UserAddress();
                userAddress.setUser(user);
                userAddress.setFullName(fullName);
                userAddress.setPhoneNumber(phoneNumber);
                userAddress.setAddress(address);
                userAddress.setIsDefault(true);
                userAddress.setIsActive(true);
                userAddress.setCreatedAt(java.time.Instant.now());
                addressService.addAddress(userAddress);
            }
        }
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    // Xóa user
    private void handleDeleteUser(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String idStr = request.getParameter("id");
        int id = Integer.parseInt(idStr);
        userService.deleteUser(id);
        response.sendRedirect(request.getContextPath() + "/users?action=list");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

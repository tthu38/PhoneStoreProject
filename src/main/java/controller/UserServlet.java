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
import java.util.List;

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
        if (action == null) action = "";

        switch (action) {
            case "edit-profile":
                showEditProfile(request, response);
                break;
            default:
                showProfile(request, response);
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
        if (action == null) action = "";

        switch (action) {
            case "update-profile":
                updateProfile(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/users");
        }
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
    }

    private void showEditProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        request.getRequestDispatcher("/user/editprofile.jsp").forward(request, response);
    }

    private void updateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");
        String fullName = request.getParameter("fullname");
        String phoneNumber = request.getParameter("phone");
        String dobStr = request.getParameter("dob");
        String address = request.getParameter("address");

        if (fullName == null || fullName.trim().isEmpty() ||
            phoneNumber == null || phoneNumber.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ họ tên và số điện thoại.");
            request.getRequestDispatcher("/user/editprofile.jsp").forward(request, response);
            return;
        }

        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        if (dobStr != null && !dobStr.isEmpty()) {
            user.setDob(java.time.LocalDate.parse(dobStr));
        }

        userService.updateUser(user);
        session.setAttribute("user", user);

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
            userAddress.setUser(user);
            userAddress.setAddress(address);
            userAddress.setIsDefault(true);
            userAddress.setIsActive(true);
            addressService.addAddress(userAddress);
        }

        response.sendRedirect(request.getContextPath() + "/user/profile.jsp");
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

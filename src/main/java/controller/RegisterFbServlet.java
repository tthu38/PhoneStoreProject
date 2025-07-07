/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import model.UserAddress;
import service.UserAddressService;
import service.UserService;

/**
 *
 * @author dangt
 */
@WebServlet(name = "RegisterFbServlet", urlPatterns = {"/registerfb"})
public class RegisterFbServlet extends HttpServlet {

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
        processRequest(request, response);
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
        String phone = request.getParameter("phone");
        String dobStr = request.getParameter("dob");
        String address = request.getParameter("address");

        HttpSession session = request.getSession();
        User fbUser = (User) session.getAttribute("fbUser");
        if (fbUser == null) {
            response.sendRedirect("user/register.jsp?error=fb_session");
            return;
        }

        fbUser.setPhoneNumber(phone);
        if (dobStr != null && !dobStr.isEmpty()) {
            fbUser.setDob(LocalDate.parse(dobStr));
        }
        
        if (address != null && !address.isEmpty()) {
            UserAddress userAddress = new UserAddress();
            userAddress.setUser(fbUser);
            userAddress.setAddress(address);
            UserAddressService userAddressService = new UserAddressService();
            userAddressService.addAddress(userAddress);
        }

        UserService userService = new UserService();
        userService.updateUserInfoFromFacebook(fbUser);
        User updatedUser = userService.getUserByEmail(fbUser.getEmail()).orElse(fbUser);
        session.setAttribute("user", updatedUser);
        session.removeAttribute("fbUser");
        response.sendRedirect(request.getContextPath() + "/indexFirst.jsp");
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

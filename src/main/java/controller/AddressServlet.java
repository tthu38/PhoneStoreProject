/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import model.UserAddress;
import service.UserAddressService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author dangt
 */
@WebServlet(name = "AddressServlet", urlPatterns = {"/AddressServlet"})
public class AddressServlet extends HttpServlet {
    private final UserAddressService addressService = new UserAddressService();

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
            out.println("<title>Servlet AddressServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AddressServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userObject");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        if ("detail".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    Integer addressId = Integer.parseInt(idParam);
                    UserAddress address = addressService.getAddressById(addressId);
                    // Kiểm tra địa chỉ này có thuộc về user hiện tại không
                    if (address != null && address.getUser().getUserID().equals(user.getUserID())) {
                        request.setAttribute("userAddress", address);
                        request.getRequestDispatcher("/user/address-detail.jsp").forward(request, response);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Address not found");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid address id");
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Address id is required");
            }
        } else {
            // Hiển thị danh sách địa chỉ của user hiện tại
            List<UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
            request.setAttribute("addresses", addresses);
            request.getRequestDispatcher("/user/address-list.jsp").forward(request, response);
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("userObject");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        if ("add".equals(action)) {
            String fullName = request.getParameter("fullName");
            String phoneNumber = request.getParameter("phoneNumber");
            String addressStr = request.getParameter("address");

            UserAddress address = new UserAddress();
            address.setUser(user);
            address.setFullName(fullName);
            address.setPhoneNumber(phoneNumber);
            address.setAddress(addressStr);

            addressService.addAddress(address);
            response.sendRedirect(request.getContextPath() + "/user/address");
        } else if ("edit".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    Integer addressId = Integer.parseInt(idParam);
                    UserAddress address = addressService.getAddressById(addressId);
                    if (address != null && address.getUser().getUserID().equals(user.getUserID())) {
                        address.setFullName(request.getParameter("fullName"));
                        address.setPhoneNumber(request.getParameter("phoneNumber"));
                        address.setAddress(request.getParameter("address"));
                        addressService.updateAddress(address);
                        response.sendRedirect(request.getContextPath() + "/user/address");
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Address not found");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid address id");
                }
            }
        } else if ("delete".equals(action)) {
            String idParam = request.getParameter("id");
            if (idParam != null) {
                try {
                    Integer addressId = Integer.parseInt(idParam);
                    UserAddress address = addressService.getAddressById(addressId);
                    if (address != null && address.getUser().getUserID().equals(user.getUserID())) {
                        addressService.deleteAddress(addressId);
                        response.sendRedirect(request.getContextPath() + "/user/address");
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Address not found");
                    }
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid address id");
                }
            }
        }
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

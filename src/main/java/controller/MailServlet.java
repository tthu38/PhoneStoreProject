///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//package controller;
//
//import java.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import service.MailService;
//import model.User;
//import model.Order;
//import model.OrderDetails;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * @author ThienThu
// */
//@WebServlet(name = "MailServlet", urlPatterns = {"/mails"})
//public class MailServlet extends HttpServlet {
//
//    private final MailService mailService = new MailService();
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if a I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        System.out.println("MailServlet processRequest called");
//    }
//    
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if a I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        System.out.println("MailServlet is running!");
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if a I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String action = request.getParameter("action");
//        String email = request.getParameter("email");
//
//        if (action == null) {
//            System.out.println("Error: No action specified");
//            return;
//        }
//
//        switch (action) {
//            case "register_otp" -> {
//                String otp = mailService.generateOTP(email);
//                boolean sent = mailService.sendOtpForRegister(email, otp);
//                System.out.println("Register OTP sent: " + (sent ? "success" : "fail") + " for email: " + email);
//            }
//            case "reset_otp" -> {
//                String otp = mailService.generateOTP(email);
//                boolean sent = mailService.sendOtpForResetPassword(email, otp);
//                if (sent) {
//                    request.setAttribute("message", "Mã xác nhận mới đã được gửi về email của bạn.");
//                } else {
//                    request.setAttribute("error", "Không thể gửi lại mã xác nhận. Vui lòng thử lại.");
//                }
//                request.getRequestDispatcher("/templates/googleotp.jsp").forward(request, response);
//                return;
//            }
//            case "order_confirm" -> {
//                User user = (User) request.getSession().getAttribute("user");
//                if (user == null) {
//                    System.out.println("Error: User not logged in");
//                    return;
//                }
//                Order order = new Order();
//                int orderID = Integer.parseInt(request.getParameter("orderID"));
//                BigDecimal totalAmount = new BigDecimal(request.getParameter("totalAmount"));
//                List<OrderDetails> orderDetails = new ArrayList<>();
//
//                boolean sent = mailService.sendOrderConfirmation(user, order, orderID, orderDetails, totalAmount);
//                System.out.println("Order confirmation sent: " + (sent ? "success" : "fail") + " for order: " + orderID);
//            }
//            default -> System.out.println("Error: Unknown action - " + action);
//        }
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}

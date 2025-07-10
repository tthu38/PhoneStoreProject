/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import Payment.VnpayConfig;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import model.Order;
import service.OrderService;

/**
 *
 * @author Admin
 */
@WebServlet(name = "VnpayReturn", urlPatterns = {"/vnpayreturn"})
public class VnpayReturn extends HttpServlet {
    
    private final OrderService orderService = new OrderService();
    
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            Map fields = new HashMap();
            for (Enumeration params = request.getParameterNames(); params.hasMoreElements();) {
                String fieldName = URLEncoder.encode((String) params.nextElement(), StandardCharsets.US_ASCII.toString());
                String fieldValue = URLEncoder.encode(request.getParameter(fieldName), StandardCharsets.US_ASCII.toString());
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    fields.put(fieldName, fieldValue);
                }
            }

            String vnp_SecureHash = request.getParameter("vnp_SecureHash");
            if (fields.containsKey("vnp_SecureHashType")) {
                fields.remove("vnp_SecureHashType");
            }
            if (fields.containsKey("vnp_SecureHash")) {
                fields.remove("vnp_SecureHash");
            }
            String signValue = VnpayConfig.hashAllFields(fields);
            
            if (signValue.equals(vnp_SecureHash)) {
                String paymentCode = request.getParameter("vnp_TransactionNo");
                String orderIdStr = request.getParameter("vnp_TxnRef");
                String transactionStatus = request.getParameter("vnp_TransactionStatus");
                
                boolean transSuccess = false;
                String message = "";
                
                try {
                    int orderId = Integer.parseInt(orderIdStr);
                    
                    if ("00".equals(transactionStatus)) {
                        // Thanh toán thành công
                        boolean updated = orderService.updateOrderStatus(orderId, "Paid");
                        if (updated) {
                            transSuccess = true;
                            message = "Thanh toán thành công! Đơn hàng #" + orderId + " đã được xác nhận.";
                        } else {
                            message = "Thanh toán thành công nhưng không thể cập nhật trạng thái đơn hàng.";
                        }
                    } else {
                        // Thanh toán thất bại
                        orderService.updateOrderStatus(orderId, "Cancelled");
                        message = "Thanh toán thất bại! Đơn hàng #" + orderId + " đã bị hủy.";
                    }
                    
                } catch (NumberFormatException e) {
                    message = "Mã đơn hàng không hợp lệ!";
                } catch (Exception e) {
                    message = "Có lỗi xảy ra khi xử lý kết quả thanh toán!";
                    e.printStackTrace();
                }
                
                request.setAttribute("transResult", transSuccess);
                request.setAttribute("message", message);
                request.setAttribute("orderId", orderIdStr);
                request.setAttribute("paymentCode", paymentCode);
                
                // Chuyển hướng đến trang kết quả
                request.getRequestDispatcher("/vnPay/VnPayResult.jsp").forward(request, response);
                
            } else {
                // Chữ ký không hợp lệ
                System.out.println("GD KO HOP LE (invalid signature)");
                request.setAttribute("transResult", false);
                request.setAttribute("message", "Chữ ký không hợp lệ! Giao dịch có thể bị giả mạo.");
                request.getRequestDispatcher("/vnPay/VnPayResult.jsp").forward(request, response);
            }
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "VNPay Return Handler";
    }// </editor-fold>
}

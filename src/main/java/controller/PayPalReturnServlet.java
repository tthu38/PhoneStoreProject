package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Order;
import service.OrderService;

@WebServlet(name = "PayPalReturnServlet", urlPatterns = {"/paypalreturn"})
public class PayPalReturnServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String paymentStatus = request.getParameter("st"); // PayPal trả về 'Completed' nếu thành công
        String orderIdStr = request.getParameter("cm"); // Custom field (nếu có)
        boolean transResult = false;
        String message = "";
        Integer orderId = null;
        try {
            if (orderIdStr != null) {
                orderId = Integer.parseInt(orderIdStr);
            }
        } catch (Exception e) {
            orderId = null;
        }
        if ("Completed".equalsIgnoreCase(paymentStatus)) {
            // Thanh toán thành công
            if (orderId != null) {
                orderService.updateOrderStatus(orderId, "Paid");
            }
            transResult = true;
            message = "Thanh toán PayPal thành công! Đơn hàng #" + orderId + " đã được xác nhận.";
        } else {
            // Thanh toán thất bại hoặc bị hủy
            if (orderId != null) {
                orderService.updateOrderStatus(orderId, "Cancelled");
            }
            transResult = false;
            message = "Thanh toán PayPal thất bại hoặc bị hủy!";
        }
        request.setAttribute("transResult", transResult);
        request.setAttribute("orderId", orderId);
        request.setAttribute("message", message);
        request.getRequestDispatcher("/paypal/PayPalResult.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "PayPal Return Handler";
    }
} 
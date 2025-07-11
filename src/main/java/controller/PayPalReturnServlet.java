package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import model.Order;
import model.OrderDetails;
import model.User;
import model.ProductStock;
import service.OrderService;
import service.OrderDetailService;
import service.MailService;
import service.ProductStockService;

@WebServlet(name = "PayPalReturnServlet", urlPatterns = {"/paypalreturn"})
public class PayPalReturnServlet extends HttpServlet {
    private final OrderService orderService = new OrderService();
    private final OrderDetailService orderDetailService = new OrderDetailService();
    private final ProductStockService productStockService = new ProductStockService();

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
                boolean updated = orderService.updateOrderStatus(orderId, "Paid");
                if (updated) {
                    // Trừ số lượng trong kho sau khi thanh toán thành công
                    List<OrderDetails> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
                    if (orderDetails != null) {
                        for (OrderDetails orderDetail : orderDetails) {
                            boolean stockUpdated = productStockService.updateStockAfterPayment(
                                orderDetail.getProductVariant().getId(), 
                                orderDetail.getQuantity()
                            );
                            if (stockUpdated) {
                                System.out.println("[PayPalReturnServlet] Đã trừ " + orderDetail.getQuantity() + 
                                                 " sản phẩm khỏi kho cho variant ID: " + orderDetail.getProductVariant().getId());
                            } else {
                                System.out.println("[PayPalReturnServlet] Lỗi khi trừ số lượng trong kho cho variant ID: " + 
                                                 orderDetail.getProductVariant().getId());
                            }
                        }
                    }
                    
                    // Gửi email xác nhận đơn hàng
                    try {
                        Order order = orderService.getOrderWithUserById(orderId);
                        User user = order.getUser();
                        
                        MailService mailService = new MailService();
                        boolean emailSent = mailService.sendOrderConfirmation(user, order, orderId, orderDetails, order.getTotalAmount());
                        
                        if (emailSent) {
                            System.out.println("[PayPalReturnServlet] Email xác nhận đơn hàng #" + orderId + " đã được gửi thành công");
                        } else {
                            System.out.println("[PayPalReturnServlet] Không thể gửi email xác nhận đơn hàng #" + orderId);
                        }
                    } catch (Exception e) {
                        System.out.println("[PayPalReturnServlet] Lỗi khi gửi email: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
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
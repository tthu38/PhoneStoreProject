package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.OrderService;
import model.Order;
import java.util.List;
import model.OrderDetails;

@WebServlet(name = "AdminOrderServlet", urlPatterns = {"/admin/orders"})
public class AdminOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle GET requests (view orders, etc.)
        String action = request.getParameter("action");
        
        if ("view".equals(action)) {
            // View order details
            String orderId = request.getParameter("id");
            if (orderId != null) {
                try {
                    int id = Integer.parseInt(orderId);
                    Order order = orderService.getOrderWithUserById(id);
                    service.OrderDetailService orderDetailService = new service.OrderDetailService();
                    java.util.List<OrderDetails> details = orderDetailService.getOrderDetailsByOrderId(id);
                    request.setAttribute("order", order);
                    request.setAttribute("details", details);
                    request.getRequestDispatcher("/admin/orders/view.jsp").forward(request, response);
                    return;
                } catch (NumberFormatException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
                    return;
                }
            }
        }
        
        if ("details".equals(action)) {
            String orderId = request.getParameter("id");
            if (orderId != null) {
                int id = Integer.parseInt(orderId);
                Order order = orderService.getOrderWithUserById(id);
                service.OrderDetailService orderDetailService = new service.OrderDetailService();
                java.util.List<OrderDetails> details = orderDetailService.getOrderDetailsByOrderId(id);
                request.setAttribute("order", order);
                request.setAttribute("details", details);
                request.getRequestDispatcher("/admin/orders/detail_fragment.jsp").forward(request, response);
                return;
            }
        }
        
        // Default: redirect to orders list
        response.sendRedirect(request.getContextPath() + "/admin/orders/list.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Handle POST requests (update status, etc.)
        String action = request.getParameter("action");
        
        if ("updateStatus".equals(action)) {
            updateOrderStatus(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
        }
    }

    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String orderIdStr = request.getParameter("orderId");
        String newStatus = request.getParameter("status");
        
        if (orderIdStr == null || newStatus == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing parameters");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            boolean success = orderService.updateOrderStatus(orderId, newStatus);
            
            if (success) {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("Success");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            }
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }
} 
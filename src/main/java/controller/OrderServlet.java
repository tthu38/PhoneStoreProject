package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import model.User;
import model.Order;
import model.OrderDetails;
import model.CartItem;
import model.ProductStock;
import service.OrderService;
import service.OrderDetailService;
import service.ProductStockService;
import service.MailService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 * @author Admin
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/order-history", "/order"})
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final OrderDetailService orderDetailService = new OrderDetailService();
    private final ProductStockService productStockService = new ProductStockService();
    private final MailService mailService = new MailService();
    private final Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[OrderServlet] doGet called");
        
        HttpSession session = request.getSession();
        
        // Kiểm tra user đã đăng nhập chưa
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("[OrderServlet] User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/user/login.jsp?error=login_required");
            return;
        }
        
        System.out.println("[OrderServlet] User logged in: " + user.getFullName() + " (ID: " + user.getUserID() + ")");
        
        // Kiểm tra action parameter để xác định chức năng
        String action = request.getParameter("action");
        
        if ("detail".equals(action)) {
            // Xử lý xem chi tiết đơn hàng
            handleOrderDetail(request, response, user);
        } else {
            // Xử lý hiển thị danh sách đơn hàng (mặc định)
            handleOrderHistory(request, response, user);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("[OrderServlet] doPost called");
        
        // Xác định URL pattern để xử lý
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        
        if (requestURI.equals(contextPath + "/order")) {
            // Xử lý thanh toán COD
            processCODOrder(request, response);
        } else {
            // Xử lý order history (GET request)
            doGet(request, response);
        }
    }
    
    private void processCODOrder(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        System.out.println("[OrderServlet] Processing COD order");
        
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            request.setAttribute("error", "Vui lòng đăng nhập để thanh toán");
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("error", "Giỏ hàng trống");
            response.sendRedirect(request.getContextPath() + "/carts");
            return;
        }

        // Lọc các sản phẩm đã chọn
        List<CartItem> selectedItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (CartItem item : cart) {
            if (item.isSelected()) {
                selectedItems.add(item);
                // Tính giá (ưu tiên giá khuyến mãi nếu có)
                BigDecimal price = item.getProductVariant().getDiscountPrice() != null
                        ? item.getProductVariant().getDiscountPrice()
                        : item.getProductVariant().getPrice();
                totalAmount = totalAmount.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        
        if (selectedItems.isEmpty()) {
            request.setAttribute("error", "Vui lòng chọn ít nhất một sản phẩm để thanh toán");
            response.sendRedirect(request.getContextPath() + "/carts");
            return;
        }

        // Kiểm tra số lượng sản phẩm trong kho trước khi thanh toán
        StringBuilder stockError = new StringBuilder();
        
        for (CartItem item : selectedItems) {
            ProductStock stock = productStockService.getStockByVariantId(item.getProductVariant().getId());
            if (stock != null && stock.getId() != null) {
                if (item.getQuantity() > stock.getAmount()) {
                    stockError.append("Sản phẩm '").append(item.getProductVariant().getProduct().getName())
                            .append(" (").append(item.getProductVariant().getColor()).append(" - ")
                            .append(item.getProductVariant().getRom()).append("GB)")
                            .append("' chỉ còn ").append(stock.getAmount())
                            .append(" sản phẩm trong kho. Bạn đã chọn ").append(item.getQuantity()).append(" sản phẩm.\n");
                }
            } else {
                stockError.append("Sản phẩm '").append(item.getProductVariant().getProduct().getName())
                        .append(" (").append(item.getProductVariant().getColor()).append(" - ")
                        .append(item.getProductVariant().getRom()).append("GB)")
                        .append("' hiện không có trong kho.\n");
            }
        }
        
        if (stockError.length() > 0) {
            request.setAttribute("error", "Không đủ số lượng sản phẩm trong kho:\n" + stockError.toString());
            response.sendRedirect(request.getContextPath() + "/cart/confirm.jsp?error=insufficient_stock&message=" + 
                java.net.URLEncoder.encode(stockError.toString(), "UTF-8"));
            return;
        }

        // Lấy thông tin giao hàng từ form
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String note = request.getParameter("note");
        
        // Tạo địa chỉ đầy đủ
        String shippingAddress = address;

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setPhoneNumber(phone != null ? phone : "");
        order.setShippingAddress(shippingAddress);
        order.setNote(note);
        order.setTotalAmount(totalAmount);
        order.setStatus("Pending");
        order.setPaymentMethod("COD"); // Cash on Delivery

        // Lưu đơn hàng
        boolean orderCreated = orderService.addOrder(order);
        
        if (!orderCreated) {
            request.setAttribute("error", "Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại.");
            response.sendRedirect(request.getContextPath() + "/carts");
            return;
        }

        // Tạo order details
        for (CartItem cartItem : selectedItems) {
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setOrder(order);
            orderDetail.setProductVariant(cartItem.getProductVariant());
            orderDetail.setQuantity(cartItem.getQuantity());

            // Lấy giá gốc và giá khuyến mãi (nếu có)
            BigDecimal unitPrice = cartItem.getProductVariant().getPrice();
            BigDecimal discountPrice = cartItem.getProductVariant().getDiscountPrice();

            orderDetail.setUnitPrice(unitPrice);
            orderDetail.setDiscountPrice(discountPrice);

            // Không set totalPrice vì là computed column
            orderDetailService.addOrderDetail(orderDetail);
        }

        // Trừ số lượng trong kho sau khi đặt hàng thành công
        for (CartItem item : selectedItems) {
            boolean stockUpdated = productStockService.updateStockAfterPayment(
                item.getProductVariant().getId(), 
                item.getQuantity()
            );
            if (stockUpdated) {
                System.out.println("[OrderServlet] Đã trừ " + item.getQuantity() + 
                                 " sản phẩm khỏi kho cho variant ID: " + item.getProductVariant().getId());
            } else {
                System.out.println("[OrderServlet] Lỗi khi trừ số lượng trong kho cho variant ID: " + 
                                 item.getProductVariant().getId());
            }
        }

        // Gửi email xác nhận đơn hàng
        try {
            List<OrderDetails> orderDetails = orderDetailService.getOrderDetailsByOrderId(order.getId());
            boolean emailSent = mailService.sendOrderConfirmation(user, order, order.getId(), orderDetails, order.getTotalAmount());
            
            if (emailSent) {
                System.out.println("[OrderServlet] Email xác nhận đơn hàng #" + order.getId() + " đã được gửi thành công");
            } else {
                System.out.println("[OrderServlet] Không thể gửi email xác nhận đơn hàng #" + order.getId());
            }
        } catch (Exception e) {
            System.out.println("[OrderServlet] Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
        }

        // Xóa các sản phẩm đã thanh toán khỏi giỏ hàng
        cart.removeAll(selectedItems);
        session.setAttribute("cart", cart);

        // Chuyển hướng đến trang thành công
        request.setAttribute("orderId", order.getId());
        request.setAttribute("totalAmount", totalAmount);
        request.getRequestDispatcher("/order/success.jsp").forward(request, response);
    }
    
    private void handleOrderDetail(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        // Lấy order ID từ parameter
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            sendErrorResponse(response, "Order ID is required", 400);
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            
            // Lấy thông tin đơn hàng
            Order order = orderService.getOrderWithUserById(orderId);
            if (order == null) {
                sendErrorResponse(response, "Order not found", 404);
                return;
            }
            
            // Kiểm tra xem đơn hàng có thuộc về user hiện tại không
            if (order.getUser().getUserID() != user.getUserID()) {
                sendErrorResponse(response, "Access denied", 403);
                return;
            }
            
            // Lấy chi tiết đơn hàng
            List<OrderDetails> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
            order.setOrderDetails(orderDetails);
            
            // Tạo response JSON
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("success", true);
            responseJson.addProperty("orderId", order.getId());
            responseJson.addProperty("orderDate", order.getOrderDateFormatted());
            responseJson.addProperty("status", order.getStatus());
            responseJson.addProperty("totalAmount", order.getTotalAmount().toString());
            responseJson.addProperty("shippingAddress", order.getShippingAddress());
            responseJson.addProperty("phoneNumber", order.getPhoneNumber());
            responseJson.addProperty("paymentMethod", order.getPaymentMethod());
            responseJson.addProperty("note", order.getNote() != null ? order.getNote() : "");
            
            // Thêm thông tin user
            JsonObject userJson = new JsonObject();
            userJson.addProperty("fullName", order.getUser().getFullName());
            userJson.addProperty("email", order.getUser().getEmail());
            responseJson.add("user", userJson);
            
            // Thêm chi tiết sản phẩm
            responseJson.add("orderDetails", gson.toJsonTree(orderDetails));
            
            // Gửi response
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(gson.toJson(responseJson));
            
        } catch (NumberFormatException e) {
            sendErrorResponse(response, "Invalid order ID format", 400);
        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, "Internal server error", 500);
        }
    }
    
    private void handleOrderHistory(HttpServletRequest request, HttpServletResponse response, User user) 
            throws ServletException, IOException {
        try {
            // Lấy danh sách đơn hàng của user
            List<Order> orders = orderService.getOrdersByUserId(user.getUserID());
            System.out.println("[OrderServlet] Found " + (orders != null ? orders.size() : 0) + " orders for user");
            
            // Lấy chi tiết đơn hàng cho từng đơn hàng
            if (orders != null) {
                for (Order order : orders) {
                    if (order != null && order.getId() != null) {
                        List<OrderDetails> orderDetails = orderDetailService.getOrderDetailsByOrderId(order.getId());
                        order.setOrderDetails(orderDetails);
                        System.out.println("[OrderServlet] Order #" + order.getId() + " has " + 
                                         (orderDetails != null ? orderDetails.size() : 0) + " details");
                    }
                }
            }
            
            request.setAttribute("orders", orders);
            request.setAttribute("user", user);
            
            System.out.println("[OrderServlet] Forwarding to order-history.jsp");
            // Forward đến trang JSP
            request.getRequestDispatcher("/user/order-history.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.out.println("[OrderServlet] Error occurred: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Có lỗi xảy ra khi tải lịch sử đặt hàng: " + e.getMessage());
            request.getRequestDispatcher("/user/order-history.jsp").forward(request, response);
        }
    }
    
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        JsonObject errorJson = new JsonObject();
        errorJson.addProperty("success", false);
        errorJson.addProperty("message", message);
        
        response.getWriter().write(gson.toJson(errorJson));
    }

    @Override
    public String getServletInfo() {
        return "Order Servlet";
    }
} 
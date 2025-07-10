package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import model.CartItem;
import model.Order;
import model.OrderDetails;
import model.User;
import service.OrderService;
import service.OrderDetailService;
import java.time.Instant;

/**
 *
 * @author Admin
 */
@WebServlet(name = "PayPalServlet", urlPatterns = {"/paypal/*"})
public class PayPalServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null) {
            response.sendRedirect(request.getContextPath() + "/carts");
            return;
        }
        
        switch (pathInfo) {
            case "/success":
                showPayPalSuccessPage(request, response);
                break;
            case "/cancel":
                showPayPalCancelPage(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/carts");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Lấy thông tin giao hàng từ form (nếu có)
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String note = request.getParameter("note");
        
        // Lưu thông tin giao hàng vào session nếu có
        if (fullName != null && !fullName.trim().isEmpty()) {
            session.setAttribute("shippingInfo", new String[]{
                fullName, phone, address + ", " + ward + ", " + district + ", " + city, note
            });
        }
        
        // Lấy cart từ session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("error", "Giỏ hàng trống");
            response.sendRedirect(request.getContextPath() + "/carts");
            return;
        }
        
        // Tính tổng tiền từ các sản phẩm đã chọn
        BigDecimal totalAmountVND = BigDecimal.ZERO;
        StringBuilder itemNames = new StringBuilder();
        int selectedItemCount = 0;
        List<CartItem> selectedItems = new ArrayList<>();
        
        for (CartItem item : cart) {
            if (item.isSelected()) {
                // Tính giá sản phẩm (có discount nếu có)
                BigDecimal price = item.getProductVariant().getDiscountPrice() != null
                        ? item.getProductVariant().getDiscountPrice()
                        : item.getProductVariant().getPrice();
                
                // Cộng vào tổng tiền
                totalAmountVND = totalAmountVND.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
                
                // Thêm tên sản phẩm vào danh sách
                if (selectedItemCount > 0) {
                    itemNames.append(", ");
                }
                itemNames.append(item.getProductVariant().getProduct().getName());
                selectedItemCount++;
                selectedItems.add(item);
            }
        }
        
        if (totalAmountVND.compareTo(BigDecimal.ZERO) == 0) {
            request.setAttribute("error", "Vui lòng chọn ít nhất một sản phẩm để thanh toán");
            response.sendRedirect(request.getContextPath() + "/carts");
            return;
        }
        
        // Tạo đơn hàng trong database
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "Vui lòng đăng nhập để tiếp tục thanh toán");
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        
        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setPhoneNumber(phone != null ? phone : "");
        order.setShippingAddress(address + ", " + ward + ", " + district + ", " + city);
        order.setNote(note);
        order.setTotalAmount(totalAmountVND);
        order.setStatus("pending");
        order.setOrderDate(Instant.now());
        
        // Lưu đơn hàng
        OrderService orderService = new OrderService();
        boolean orderCreated = orderService.addOrder(order);
        
        if (!orderCreated) {
            request.setAttribute("error", "Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại.");
            response.sendRedirect(request.getContextPath() + "/carts");
            return;
        }
        
        // Tạo order details
        OrderDetailService orderDetailService = new OrderDetailService();
        for (CartItem cartItem : selectedItems) {
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setOrder(order);
            orderDetail.setProductVariant(cartItem.getProductVariant());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setUnitPrice(cartItem.getProductVariant().getPrice());
            orderDetail.setDiscountPrice(cartItem.getProductVariant().getDiscountPrice());
            orderDetailService.addOrderDetail(orderDetail);
        }
        
        // Lưu thông tin đơn hàng vào session
        session.setAttribute("currentOrderId", order.getId());
        session.setAttribute("selectedItems", selectedItems);
        
        // Chuyển đổi VNĐ sang USD
        double exchangeRate = 24000.0;
        double amountUSD = totalAmountVND.doubleValue() / exchangeRate;
        String formattedAmount = String.format("%.2f", amountUSD);
        
        // Tạo mô tả sản phẩm
        String itemDescription = selectedItemCount > 1 
            ? itemNames.toString() + " và " + (selectedItemCount - 1) + " sản phẩm khác"
            : itemNames.toString();
        
        // Tài khoản business sandbox PayPal (bạn phải thay đúng tài khoản của bạn)
        String businessEmail = "nhatnam@business.example.com"; // THAY email sandbox

        String returnURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/paypal/success";
        String cancelURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/paypal/cancel";

        // Tạo URL chuyển hướng sang PayPal
        String paypalURL = "https://www.sandbox.paypal.com/cgi-bin/webscr"
                + "?cmd=_xclick"
                + "&business=" + URLEncoder.encode(businessEmail, "UTF-8")
                + "&item_name=" + URLEncoder.encode(itemDescription, "UTF-8")
                + "&amount=" + formattedAmount
                + "&currency_code=USD"
                + "&return=" + URLEncoder.encode(returnURL, "UTF-8")
                + "&cancel_return=" + URLEncoder.encode(cancelURL, "UTF-8");

        // Lưu thông tin giao dịch vào session để sử dụng ở trang success
        session.setAttribute("paypalAmountVND", totalAmountVND);
        session.setAttribute("paypalAmountUSD", formattedAmount);
        session.setAttribute("paypalItemCount", selectedItemCount);
        session.setAttribute("paypalItemDescription", itemDescription);

        // Chuyển hướng người dùng đến trang PayPal
        try {
            response.sendRedirect(paypalURL);
        } catch (Exception e) {
            response.sendRedirect("error.jsp");
        }
    }
    
    private void showPayPalSuccessPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Lấy thông tin giao dịch từ session
        request.setAttribute("amountVND", session.getAttribute("paypalAmountVND"));
        request.setAttribute("amountUSD", session.getAttribute("paypalAmountUSD"));
        request.setAttribute("itemCount", session.getAttribute("paypalItemCount"));
        request.setAttribute("itemDescription", session.getAttribute("paypalItemDescription"));
        
        // Cập nhật trạng thái đơn hàng thành "paid"
        Integer orderId = (Integer) session.getAttribute("currentOrderId");
        if (orderId != null) {
            OrderService orderService = new OrderService();
            orderService.updateOrderStatus(orderId, "paid");
        }
        
        // Xóa các sản phẩm đã thanh toán khỏi giỏ hàng
        List<CartItem> selectedItems = (List<CartItem>) session.getAttribute("selectedItems");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart != null && selectedItems != null) {
            // Xóa các sản phẩm đã thanh toán
            cart.removeAll(selectedItems);
            session.setAttribute("cart", cart);
        }
        
        // Xóa thông tin giao dịch khỏi session
        session.removeAttribute("paypalAmountVND");
        session.removeAttribute("paypalAmountUSD");
        session.removeAttribute("paypalItemCount");
        session.removeAttribute("paypalItemDescription");
        session.removeAttribute("currentOrderId");
        session.removeAttribute("selectedItems");
        session.removeAttribute("shippingInfo");
        
        request.getRequestDispatcher("/paypal/success.jsp").forward(request, response);
    }
    
    private void showPayPalCancelPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        
        // Xóa thông tin giao dịch khỏi session nếu hủy
        session.removeAttribute("currentOrderId");
        session.removeAttribute("selectedItems");
        session.removeAttribute("shippingInfo");
        
        request.getRequestDispatcher("/paypal/cancel.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "PayPal Payment Servlet";
    }
} 
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
import model.ProductStock;
import service.OrderService;
import service.OrderDetailService;
import service.MailService;
import service.ProductStockService;
import Payment.PaypalConfig;
import model.UserAddress;
import service.UserAddressService;

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
        // Thêm log để kiểm tra pathInfo khi PayPal redirect về
        System.out.println("[PayPalServlet] doGet pathInfo: " + request.getPathInfo());
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
        
        // Kiểm tra user đã đăng nhập chưa
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("error", "Vui lòng đăng nhập để tiếp tục thanh toán");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            return;
        }
        
        // Kiểm tra user có địa chỉ không
        UserAddressService userAddressService = new UserAddressService();
        UserAddress defaultAddress = userAddressService.getDefaultAddressByUserId(user.getUserID());
        if (defaultAddress == null) {
            defaultAddress = userAddressService.getFirstActiveAddressByUserId(user.getUserID());
        }
        
        if (defaultAddress == null) {
            // Chuyển hướng đến trang profile để cập nhật thông tin
            response.sendRedirect(request.getContextPath() + "/user/profile.jsp?error=no_address&message=" + 
                java.net.URLEncoder.encode("Vui lòng cập nhật địa chỉ giao hàng để tiếp tục thanh toán", "UTF-8"));
            return;
        }
        
        // Lấy thông tin giao hàng từ form (nếu có)
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String note = request.getParameter("note");
        
        // Lưu thông tin giao hàng vào session nếu có
        if (fullName != null && !fullName.trim().isEmpty()) {
            session.setAttribute("shippingInfo", new String[]{
                fullName, phone, address, note
            });
        }
        
        // Lấy cart từ session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            request.setAttribute("error", "Giỏ hàng trống");
            request.getRequestDispatcher("/carts").forward(request, response);
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
            request.getRequestDispatcher("/carts").forward(request, response);
            return;
        }
        
        // Kiểm tra số lượng sản phẩm trong kho trước khi thanh toán
        ProductStockService productStockService = new ProductStockService();
        StringBuilder stockError = new StringBuilder();
        
        System.out.println("[PayPalServlet] Kiểm tra số lượng trong kho cho " + selectedItems.size() + " sản phẩm");
        
        for (CartItem item : selectedItems) {
            ProductStock stock = productStockService.getStockByVariantId(item.getProductVariant().getId());
            System.out.println("[PayPalServlet] Kiểm tra sản phẩm: " + item.getProductVariant().getProduct().getName() + 
                             " - Số lượng yêu cầu: " + item.getQuantity() + 
                             " - Số lượng trong kho: " + (stock != null ? stock.getAmount() : "null"));
            
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
            System.out.println("[PayPalServlet] Lỗi số lượng trong kho: " + stockError.toString());
            request.setAttribute("error", "Không đủ số lượng sản phẩm trong kho:\n" + stockError.toString());
            request.getRequestDispatcher("/cart/confirm.jsp").forward(request, response);
            return;
        }
        
        System.out.println("[PayPalServlet] Kiểm tra số lượng trong kho thành công, tiếp tục xử lý thanh toán");
        
        // Tạo đơn hàng trong database
        Order order = new Order();
        order.setUser(user);
        order.setPhoneNumber(phone != null ? phone : "");
        order.setShippingAddress(address);
        order.setNote(note);
        order.setTotalAmount(totalAmountVND);
        order.setStatus("pending");
        order.setPaymentMethod("PayPal"); // Set payment method
        
        // Lưu đơn hàng
        OrderService orderService = new OrderService();
        boolean orderCreated = orderService.addOrder(order);
        
        if (!orderCreated) {
            request.setAttribute("error", "Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại.");
            request.getRequestDispatcher("/carts").forward(request, response);
            return;
        }
        
        // Tạo order details
        OrderDetailService orderDetailService = new OrderDetailService();
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

            // Không set totalPrice
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
        String businessEmail = PaypalConfig.BUSINESS_EMAIL;

        String returnURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + PaypalConfig.RETURN_PATH;
        String cancelURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + PaypalConfig.CANCEL_PATH;

        // Tạo URL chuyển hướng sang PayPal
        String paypalURL = PaypalConfig.PAYPAL_URL
                + "?cmd=_xclick"
                + "&business=" + URLEncoder.encode(businessEmail, "UTF-8")
                + "&item_name=" + URLEncoder.encode(itemDescription, "UTF-8")
                + "&amount=" + formattedAmount
                + "&currency_code=" + PaypalConfig.CURRENCY_CODE
                + "&return=" + URLEncoder.encode(returnURL, "UTF-8")
                + "&cancel_return=" + URLEncoder.encode(cancelURL, "UTF-8");

        // Log các URL trả về để kiểm tra
        System.out.println("[PayPalServlet] returnURL: " + returnURL);
        System.out.println("[PayPalServlet] cancelURL: " + cancelURL);
        System.out.println("[PayPalServlet] paypalURL: " + paypalURL);
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
            boolean updated = orderService.updateOrderStatus(orderId, "paid");
            
            if (updated) {
                // Trừ số lượng trong kho sau khi thanh toán thành công
                List<CartItem> selectedItems = (List<CartItem>) session.getAttribute("selectedItems");
                if (selectedItems != null) {
                    ProductStockService productStockService = new ProductStockService();
                    for (CartItem item : selectedItems) {
                        boolean stockUpdated = productStockService.updateStockAfterPayment(
                            item.getProductVariant().getId(), 
                            item.getQuantity()
                        );
                        if (stockUpdated) {
                            System.out.println("[PayPalServlet] Đã trừ " + item.getQuantity() + 
                                             " sản phẩm khỏi kho cho variant ID: " + item.getProductVariant().getId());
                        } else {
                            System.out.println("[PayPalServlet] Lỗi khi trừ số lượng trong kho cho variant ID: " + 
                                             item.getProductVariant().getId());
                        }
                    }
                }
                
                // Gửi email xác nhận đơn hàng
                try {
                    User user = (User) session.getAttribute("user");
                    Order order = orderService.getOrderWithUserById(orderId);
                    OrderDetailService orderDetailService = new OrderDetailService();
                    List<OrderDetails> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
                    
                    MailService mailService = new MailService();
                    boolean emailSent = mailService.sendOrderConfirmation(user, order, orderId, orderDetails, order.getTotalAmount());
                    
                    if (emailSent) {
                        System.out.println("[PayPalServlet] Email xác nhận đơn hàng #" + orderId + " đã được gửi thành công");
                    } else {
                        System.out.println("[PayPalServlet] Không thể gửi email xác nhận đơn hàng #" + orderId);
                    }
                } catch (Exception e) {
                    System.out.println("[PayPalServlet] Lỗi khi gửi email: " + e.getMessage());
                    e.printStackTrace();
                }
            }
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
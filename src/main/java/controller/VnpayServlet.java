/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import model.Order;
import model.OrderDetails;
import model.CartItem;
import model.User;
import model.ProductVariant;
import service.OrderService;
import service.OrderDetailService;
import Payment.VnpayConfig;
import model.Product;

/**
 *
 * @author Admin
 */
@WebServlet(name = "VnpayServlet", urlPatterns = {"/vnpay"})
public class VnpayServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final OrderDetailService orderDetailService = new OrderDetailService();

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
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>VNPay Payment</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>VNPay Payment Processing</h1>");
            out.println("<p>Please wait while we process your payment...</p>");
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        HttpSession session = req.getSession();
        
        // Kiểm tra user đã đăng nhập chưa
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/user/login.jsp?error=login_required");
            return;
        }
        
        // Lấy cart từ session
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart/confirm.jsp?error=empty_cart");
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
            resp.sendRedirect(req.getContextPath() + "/cart/confirm.jsp?error=no_items_selected");
            return;
        }
        
        // Lấy thông tin đơn hàng từ form
        String shippingAddress = req.getParameter("address"); // Changed from shippingAddress to address
        String phoneNumber = req.getParameter("phone"); // Changed from phoneNumber to phone
        String note = req.getParameter("note");
        
        // Lấy thông tin địa chỉ chi tiết
        String city = req.getParameter("city");
        String district = req.getParameter("district");
        String ward = req.getParameter("ward");
        
        // Tạo địa chỉ đầy đủ nếu có thông tin chi tiết
        if (shippingAddress != null && !shippingAddress.trim().isEmpty()) {
            StringBuilder fullAddress = new StringBuilder(shippingAddress);
            if (ward != null && !ward.trim().isEmpty()) {
                fullAddress.append(", ").append(ward);
            }
            if (district != null && !district.trim().isEmpty()) {
                fullAddress.append(", ").append(district);
            }
            if (city != null && !city.trim().isEmpty()) {
                fullAddress.append(", ").append(city);
            }
            shippingAddress = fullAddress.toString();
        }
        
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart/confirm.jsp?error=missing_address");
            return;
        }
        
        // Validate phone number
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            phoneNumber = currentUser.getPhoneNumber(); // Use user's phone if not provided
        }
        
        try {
            // Tạo đơn hàng mới
            Order order = new Order();
            order.setUser(currentUser);
            order.setTotalAmount(totalAmount);
            order.setStatus("Pending");
            order.setShippingAddress(shippingAddress);
            order.setPhoneNumber(phoneNumber);
            order.setNote(note);
            order.setPaymentMethod("VNPay"); // Set payment method
            
            // Lưu đơn hàng vào database
            boolean orderSaved = orderService.addOrder(order);
            if (!orderSaved) {
                resp.sendRedirect(req.getContextPath() + "/cart/confirm.jsp?error=order_creation_failed");
                return;
            }
            
            // Lấy order ID vừa tạo
            int orderId = order.getId();
            
            System.out.println("=== VNPay Payment Processing ===");
            System.out.println("Order ID: " + orderId);
            System.out.println("Total Amount: " + totalAmount);
            System.out.println("Shipping Address: " + shippingAddress);
            System.out.println("Phone Number: " + phoneNumber);
            System.out.println("Selected Items: " + selectedItems.size());
            
            // Tạo OrderDetails cho từng sản phẩm
            for (CartItem item : selectedItems) {
                OrderDetails orderDetail = new OrderDetails();
                orderDetail.setOrder(order);
                orderDetail.setProductVariant(item.getProductVariant());
                orderDetail.setQuantity(item.getQuantity());

                // Lấy giá gốc và giá khuyến mãi (nếu có)
                BigDecimal unitPrice = item.getProductVariant().getPrice();
                BigDecimal discountPrice = item.getProductVariant().getDiscountPrice();

                orderDetail.setUnitPrice(unitPrice);
                orderDetail.setDiscountPrice(discountPrice);

                // Không cần setTotalPrice, entity sẽ tự tính hoặc SQL Server tự tính
                orderDetailService.addOrderDetail(orderDetail);
            }
            
            System.out.println("Order details created successfully");
            
            // Xóa các sản phẩm đã thanh toán khỏi cart
            cart.removeAll(selectedItems);
            session.setAttribute("cart", cart);
            
            // Chuẩn bị thông tin cho VNPay
            String bankCode = req.getParameter("bankCode");
            String language = req.getParameter("language");
            String vnp_Version = "2.1.0";
            String vnp_Command = "pay";
            String orderType = "other";
            
            // Chuyển đổi amount sang VND (VNPay yêu cầu số tiền nhân với 100)
            long amount = totalAmount.multiply(BigDecimal.valueOf(100)).longValue();
            String vnp_TxnRef = String.valueOf(orderId);
            String vnp_IpAddr = VnpayConfig.getIpAddress(req);
            String vnp_TmnCode = VnpayConfig.vnp_TmnCode;
            
            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", vnp_Version);
            vnp_Params.put("vnp_Command", vnp_Command);
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", String.valueOf(amount));
            vnp_Params.put("vnp_CurrCode", "VND");
            
            if (bankCode != null && !bankCode.isEmpty()) {
                vnp_Params.put("vnp_BankCode", bankCode);
            }
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
            vnp_Params.put("vnp_OrderType", orderType);
            
            if (language != null && !language.isEmpty()) {
                vnp_Params.put("vnp_Locale", language);
            } else {
                vnp_Params.put("vnp_Locale", "vn");
            }
            vnp_Params.put("vnp_ReturnUrl", VnpayConfig.vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            
            Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            String vnp_CreateDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
            
            cld.add(Calendar.MINUTE, 15);
            String vnp_ExpireDate = formatter.format(cld.getTime());
            vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
            
            // Tạo chuỗi hash
            List fieldNames = new ArrayList(vnp_Params.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = (String) itr.next();
                String fieldValue = (String) vnp_Params.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = VnpayConfig.hmacSHA512(VnpayConfig.secretKey, hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = VnpayConfig.vnp_PayUrl + "?" + queryUrl;
            
            System.out.println("Final Payment URL: " + paymentUrl);
            
            // Chuyển hướng đến VNPay
            resp.sendRedirect(paymentUrl);
            
            System.out.println("=== VNPay Payment Processing Completed ===");
            
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/cart/confirm.jsp?error=payment_error");
        }
    }
    
    @Override
    public String getServletInfo() {
        return "VNPay Payment Servlet";
    }// </editor-fold>
}

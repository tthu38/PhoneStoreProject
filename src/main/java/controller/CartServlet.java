/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.PrintWriter;
import model.CartItem;
import model.ProductVariant;
import service.ProductVariantService;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import model.Order;
import model.OrderDetails;
import model.User;
import model.ProductStock;
import service.OrderService;
import service.OrderDetailService;
import service.MailService;
import service.ProductStockService;

/**
 *
 * @author ThienThu
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/carts"})
public class CartServlet extends HttpServlet {

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
            out.println("<title>Servlet CartServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CartServlet at " + request.getContextPath() + "</h1>");
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
        listCart(request, response);
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
        System.out.println("=== DEBUG POST ===");
        System.out.println("Action received: " + action);
        System.out.println("All parameters: " + request.getParameterMap());
        
        switch (action) {
            case "add":
                addToCart(request, response);
                break;
            case "remove":
                removeFromCart(request, response);
                break;
            case "update":
                updateQuantity(request, response);
                break;
            case "select":
                selectItem(request, response, true);
                break;
            case "unselect":
                selectItem(request, response, false);
                break;
            case "selectAll":
                selectAllItems(request, response);
                break;
            case "checkout":
                processCheckout(request, response);
                break;
            case "list":
            default:
                listCart(request, response);
                break;
        }
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int variantId = Integer.parseInt(request.getParameter("variantId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        // Lấy variant
        ProductVariantService variantService = new ProductVariantService();
        ProductVariant variant = variantService.getProductVariantById(variantId);

        // Kiểm tra đã có trong giỏ chưa
        boolean found = false;
        for (CartItem item : cart) {
            if (item.getProductVariant().getId() == variantId) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) {
            cart.add(new CartItem(variant, quantity));
        }

        // Tính tổng số lượng
        int totalQuantity = 0;
        for (CartItem item : cart) {
            totalQuantity += item.getQuantity();
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"cartCount\": " + totalQuantity + "}");
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String variantIdStr = request.getParameter("variantId");
            System.out.println("=== DEBUG REMOVE ===");
            System.out.println("variantIdStr: " + variantIdStr);
            
            if (variantIdStr == null || variantIdStr.trim().isEmpty()) {
                System.out.println("variantId is null or empty");
                response.sendRedirect(request.getContextPath() + "/carts");
                return;
            }
            
            try {
                int variantId = Integer.parseInt(variantIdStr);
                System.out.println("Parsed variantId: " + variantId);
                
                HttpSession session = request.getSession();
                List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
                System.out.println("Cart from session: " + (cart != null ? cart.size() + " items" : "null"));
                
                if (cart != null && !cart.isEmpty()) {
                    System.out.println("Before removal - Cart items:");
                    for (CartItem item : cart) {
                        System.out.println("  Item variantId: " + item.getProductVariant().getId() + 
                                        ", Product: " + item.getProductVariant().getProduct().getName());
                    }
                    
                    // Xóa sản phẩm có variantId tương ứng
                    boolean removed = cart.removeIf(item -> {
                        boolean matches = item.getProductVariant().getId() == variantId;
                        System.out.println("Checking item " + item.getProductVariant().getId() + " == " + variantId + " = " + matches);
                        return matches;
                    });
                    
                    System.out.println("Removed: " + removed);
                    System.out.println("After removal - Cart size: " + cart.size());
                    
                    // Cập nhật lại session
                    session.setAttribute("cart", cart);
                }
                
                response.sendRedirect(request.getContextPath() + "/carts");
            } catch (NumberFormatException e) {
                System.out.println("Error parsing variantId: " + variantIdStr);
                // Nếu variantId không phải số, chuyển về trang giỏ hàng
                response.sendRedirect(request.getContextPath() + "/carts");
            }
        } catch (Exception e) {
            System.out.println("=== ERROR IN REMOVE ===");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/carts");
        }
    }

    private void updateQuantity(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int variantId = Integer.parseInt(request.getParameter("variantId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            for (CartItem item : cart) {
                if (item.getProductVariant().getId() == variantId) {
                    item.setQuantity(quantity);
                    break;
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/carts");
    }

    private void selectItem(HttpServletRequest request, HttpServletResponse response, boolean selected) throws IOException {
        int variantId = Integer.parseInt(request.getParameter("variantId"));
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            for (CartItem item : cart) {
                if (item.getProductVariant().getId() == variantId) {
                    item.setSelected(selected);
                    break;
                }
            }
        }
        response.sendRedirect(request.getContextPath() + "/carts");
    }

    private void selectAllItems(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart != null) {
            boolean selectAll = Boolean.parseBoolean(request.getParameter("selectAll"));
            for (CartItem item : cart) {
                item.setSelected(selectAll);
            }
        }
        response.sendRedirect(request.getContextPath() + "/carts");
    }

    private void listCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        // Tính tổng tiền các sản phẩm đã chọn
        BigDecimal selectedTotal = BigDecimal.ZERO;
        for (CartItem item : cart) {
            if (item.isSelected()) {
                BigDecimal price = item.getProductVariant().getDiscountPrice() != null
                        ? item.getProductVariant().getDiscountPrice()
                        : item.getProductVariant().getPrice();
                selectedTotal = selectedTotal.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
        request.setAttribute("cart", cart);
        request.setAttribute("selectedTotal", selectedTotal);
        request.getRequestDispatcher("/cart/cart.jsp").forward(request, response);
    }

    private void processCheckout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        ProductStockService productStockService = new ProductStockService();
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
            response.sendRedirect(request.getContextPath() + "/cart/confirm.jsp");
            return;
        }

        // Lấy thông tin giao hàng từ form
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String note = request.getParameter("note");
        
        // Tạo địa chỉ đầy đủ
        String shippingAddress = address + ", " + ward + ", " + district + ", " + city;

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
                System.out.println("[CartServlet] Đã trừ " + item.getQuantity() + 
                                 " sản phẩm khỏi kho cho variant ID: " + item.getProductVariant().getId());
            } else {
                System.out.println("[CartServlet] Lỗi khi trừ số lượng trong kho cho variant ID: " + 
                                 item.getProductVariant().getId());
            }
        }

        // Gửi email xác nhận đơn hàng
        try {
            List<OrderDetails> orderDetails = orderDetailService.getOrderDetailsByOrderId(order.getId());
            MailService mailService = new MailService();
            boolean emailSent = mailService.sendOrderConfirmation(user, order, order.getId(), orderDetails, order.getTotalAmount());
            
            if (emailSent) {
                System.out.println("[CartServlet] Email xác nhận đơn hàng #" + order.getId() + " đã được gửi thành công");
            } else {
                System.out.println("[CartServlet] Không thể gửi email xác nhận đơn hàng #" + order.getId());
            }
        } catch (Exception e) {
            System.out.println("[CartServlet] Lỗi khi gửi email: " + e.getMessage());
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Cart Servlet";
    }// </editor-fold>

}

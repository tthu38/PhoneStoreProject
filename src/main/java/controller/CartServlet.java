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
import java.util.HashMap;
import model.Cart;
import model.CartItem;
import model.ProductVariant;
import service.ProductVariantService;
import java.math.BigDecimal;

/**
 *
 * @author ThienThu
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/carts", "/carts/*"})
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

    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart(new HashMap<>());
            session.setAttribute("cart", cart);
        }

        // Tính tổng giá trị giỏ hàng với discount
        calculateCartTotal(cart);

        request.getRequestDispatcher("/cart/cart.jsp").forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart(new HashMap<>());
            session.setAttribute("cart", cart);
        }

        try {
            int variantId = Integer.parseInt(request.getParameter("variantId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity <= 0) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Số lượng phải lớn hơn 0");
                return;
            }

            ProductVariantService variantService = new ProductVariantService();
            ProductVariant variant = variantService.getProductVariantById(variantId);

            if (variant == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sản phẩm không tồn tại");
                return;
            }

            // Kiểm tra tồn kho - tạm thời bỏ qua kiểm tra stock
            // TODO: Implement stock checking using ProductStock service
            // Thêm vào giỏ hàng
            CartItem existingItem = cart.getCartItems().get(variantId);
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + quantity);
            } else {
                CartItem newItem = new CartItem(quantity, variant);
                cart.getCartItems().put(variantId, newItem);
            }

            // Cập nhật số lượng trong giỏ hàng
            updateCartBadge(session);
            
            // Tính lại tổng giá trị giỏ hàng với discount
            calculateCartTotal(cart);

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"Đã thêm vào giỏ hàng\"}");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
        }
    }

    private void updateCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Giỏ hàng trống");
            return;
        }

        try {
            int variantId = Integer.parseInt(request.getParameter("variantId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            if (quantity <= 0) {
                removeFromCart(request, response);
                return;
            }

            CartItem item = cart.getCartItems().get(variantId);
            if (item == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sản phẩm không có trong giỏ hàng");
                return;
            }

            // Kiểm tra tồn kho - tạm thời bỏ qua kiểm tra stock
            // TODO: Implement stock checking using ProductStock service    
            item.setQuantity(quantity);
            updateCartBadge(session);
            
            // Tính lại tổng giá trị giỏ hàng với discount
            calculateCartTotal(cart);

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"Đã cập nhật giỏ hàng\"}");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
        }
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Giỏ hàng trống");
            return;
        }

        try {
            int variantId = Integer.parseInt(request.getParameter("variantId"));

            cart.getCartItems().remove(variantId);
            updateCartBadge(session);
            
            // Tính lại tổng giá trị giỏ hàng với discount
            calculateCartTotal(cart);

            response.setContentType("application/json");
            response.getWriter().write("{\"success\": true, \"message\": \"Đã xóa sản phẩm khỏi giỏ hàng\"}");

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ");
        }
    }

    private void clearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart != null) {
            cart.getCartItems().clear();
            updateCartBadge(session);
            calculateCartTotal(cart);
        }

        response.setContentType("application/json");
        response.getWriter().write("{\"success\": true, \"message\": \"Đã xóa toàn bộ giỏ hàng\"}");
    }

    private void updateCartBadge(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        int totalItems = 0;
        if (cart != null && cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems().values()) {
                totalItems += item.getQuantity();
            }
        }
        session.setAttribute("cartItemCount", totalItems);
    }
    
    private void calculateCartTotal(Cart cart) {
        double totalPrice = 0;
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        for (CartItem item : cart.getCartItems().values()) {
            BigDecimal price = item.getProductVariant().getDiscountPrice() != null
                    && item.getProductVariant().getDiscountExpiry() != null
                    && item.getProductVariant().getDiscountExpiry().isAfter(now)
                    ? item.getProductVariant().getDiscountPrice()
                    : item.getProductVariant().getPrice();
            totalPrice += price.doubleValue() * item.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
    }
    
    private void processCheckout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null || cart.getCartItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart/cart.jsp");
            return;
        }
        
        // Lấy thông tin từ form
        String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String district = request.getParameter("district");
        String ward = request.getParameter("ward");
        String note = request.getParameter("note");
        String paymentMethod = request.getParameter("paymentMethod");
        
        // Validate required fields
        if (fullName == null || fullName.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            address == null || address.trim().isEmpty() ||
            city == null || city.trim().isEmpty() ||
            district == null || district.trim().isEmpty() ||
            ward == null || ward.trim().isEmpty() ||
            paymentMethod == null || paymentMethod.trim().isEmpty()) {
            
            request.setAttribute("error", "Vui lòng điền đầy đủ thông tin bắt buộc");
            request.getRequestDispatcher("/cart/confirm.jsp").forward(request, response);
            return;
        }
        
        try {
            // TODO: Lưu đơn hàng vào database
            // OrderService orderService = new OrderService();
            // Order order = orderService.createOrder(cart, fullName, phone, address, city, district, ward, note, paymentMethod);
            
            // Xử lý thanh toán theo phương thức
            switch (paymentMethod) {
                case "cod":
                    // Thanh toán khi nhận hàng - chuyển đến trang thành công
                    response.sendRedirect(request.getContextPath() + "/cart/success.jsp");
                    break;
                case "vnpay":
                    // TODO: Chuyển đến VNPAY payment gateway
                    response.sendRedirect(request.getContextPath() + "/payment/vnpay");
                    break;
                case "paypal":
                    // TODO: Chuyển đến PayPal payment gateway
                    response.sendRedirect(request.getContextPath() + "/payment/paypal");
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/cart/success.jsp");
                    break;
            }
            
            // Xóa giỏ hàng sau khi đặt hàng thành công
            session.removeAttribute("cart");
            session.setAttribute("cartItemCount", 0);
            
        } catch (Exception e) {
            request.setAttribute("error", "Có lỗi xảy ra khi xử lý đơn hàng: " + e.getMessage());
            request.getRequestDispatcher("/cart/confirm.jsp").forward(request, response);
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
        viewCart(request, response);
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
        if (action == null) {
            action = "view";
        }

        switch (action) {
            case "add":
                addToCart(request, response);
                break;
            case "update":
                updateCart(request, response);
                break;
            case "remove":
                removeFromCart(request, response);
                break;
            case "clear":
                clearCart(request, response);
                break;
            case "checkout":
                processCheckout(request, response);
                break;
            default:
                viewCart(request, response);
                break;
        }
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

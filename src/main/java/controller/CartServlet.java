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
            case "demo":
                createDemoData(request, response);
                break;
            default:
                viewCart(request, response);
                break;
        }
    }

    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new Cart(new HashMap<>());
            session.setAttribute("cart", cart);
        }
        
        // Tạo dữ liệu demo nếu giỏ hàng trống
        if (cart.getCartItems().isEmpty()) {
            createDemoCartData(cart);
        }
        
        // Tính tổng giá trị giỏ hàng
        double totalPrice = 0;
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        for (CartItem item : cart.getCartItems().values()) {
            BigDecimal price = item.getProductVariant().getDiscountPrice() != null && 
                             item.getProductVariant().getDiscountExpiry() != null &&
                             item.getProductVariant().getDiscountExpiry().isAfter(now) 
                             ? item.getProductVariant().getDiscountPrice() 
                             : item.getProductVariant().getPrice();
            totalPrice += price.doubleValue() * item.getQuantity();
        }
        cart.setTotalPrice(totalPrice);
        
        request.getRequestDispatcher("/cart/cart.jsp").forward(request, response);
    }
    
    private void createDemoCartData(Cart cart) {
        // Tạo sản phẩm demo 1: iPhone 15 Pro
        model.Product iphone15Pro = new model.Product();
        iphone15Pro.setId(1);
        iphone15Pro.setName("iPhone 15 Pro");
        iphone15Pro.setThumbnailImage("iphone15pro.jpg");
        
        ProductVariant iphone15ProVariant = new ProductVariant();
        iphone15ProVariant.setId(1);
        iphone15ProVariant.setColor("Titan tự nhiên");
        iphone15ProVariant.setRom(256);
        iphone15ProVariant.setPrice(new BigDecimal("29990000"));
        iphone15ProVariant.setDiscountPrice(new BigDecimal("27990000"));
        iphone15ProVariant.setDiscountExpiry(java.time.LocalDateTime.now().plusDays(7));
        iphone15ProVariant.setProduct(iphone15Pro);
        
        CartItem iphoneItem = new CartItem(1, iphone15ProVariant);
        cart.getCartItems().put(1, iphoneItem);
        
        // Tạo sản phẩm demo 2: Samsung Galaxy S24
        model.Product samsungS24 = new model.Product();
        samsungS24.setId(2);
        samsungS24.setName("Samsung Galaxy S24 Ultra");
        samsungS24.setThumbnailImage("ssgalazy21.jpg");
        
        ProductVariant samsungVariant = new ProductVariant();
        samsungVariant.setId(2);
        samsungVariant.setColor("Titanium Gray");
        samsungVariant.setRom(512);
        samsungVariant.setPrice(new BigDecimal("25990000"));
        samsungVariant.setProduct(samsungS24);
        
        CartItem samsungItem = new CartItem(2, samsungVariant);
        cart.getCartItems().put(2, samsungItem);
        
        // Tạo sản phẩm demo 3: Xiaomi 13
        model.Product xiaomi13 = new model.Product();
        xiaomi13.setId(3);
        xiaomi13.setName("Xiaomi 13 Ultra");
        xiaomi13.setThumbnailImage("xiaomi13.jpg");
        
        ProductVariant xiaomiVariant = new ProductVariant();
        xiaomiVariant.setId(3);
        xiaomiVariant.setColor("Đen");
        xiaomiVariant.setRom(256);
        xiaomiVariant.setPrice(new BigDecimal("18990000"));
        xiaomiVariant.setDiscountPrice(new BigDecimal("16990000"));
        xiaomiVariant.setDiscountExpiry(java.time.LocalDateTime.now().plusDays(3));
        xiaomiVariant.setProduct(xiaomi13);
        
        CartItem xiaomiItem = new CartItem(1, xiaomiVariant);
        cart.getCartItems().put(3, xiaomiItem);
    }
    
    private void createDemoData(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new Cart(new HashMap<>());
            session.setAttribute("cart", cart);
        }
        
        // Xóa dữ liệu cũ và tạo mới
        cart.getCartItems().clear();
        createDemoCartData(cart);
        updateCartBadge(session);
        
        response.setContentType("application/json");
        response.getWriter().write("{\"success\": true, \"message\": \"Đã tạo dữ liệu demo\"}");
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
        return "Cart Servlet";
    }// </editor-fold>

}

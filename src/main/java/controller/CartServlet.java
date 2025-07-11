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

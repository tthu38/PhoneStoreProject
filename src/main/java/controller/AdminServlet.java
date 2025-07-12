/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import model.Category;
import model.Product;
import model.User;
import service.ProductService;
import service.UserService;
import service.OrderService;
import service.ProductStockService;

/**
 *
 * @author Admin
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

    UserService userService = new UserService();
    OrderService orderService = new OrderService();
    ProductStockService productStockService = new ProductStockService();

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
            out.println("<title>Servlet AdminServelt</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AdminServelt at " + request.getContextPath() + "</h1>");
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
        String menu = request.getParameter("menu");
        String contentPage;

        if (menu == null || menu.equals("dashboard")) {
            contentPage = handleDashboard(request);
        } else if (menu.equals("products")) {
            contentPage = handleProductList(request);
        } else if (menu.equals("customers")) {
            contentPage = handleCustomerList(request);
        } else if (menu.equals("orders")) {
            contentPage = handleOrders(request);
        } else if (menu.equals("discounts")) {
            contentPage = handleDiscounts(request);
        } else if (menu.equals("report")) {
            contentPage = handleReport(request);
        } else {
            contentPage = "../dashboard/dashboard.jsp"; // fallback
        }

        request.setAttribute("contentPage", contentPage);
        request.getRequestDispatcher("/admin/layout/admin-header.jsp").forward(request, response);
    }

    private String handleDashboard(HttpServletRequest request) {
        Map<String, Double> monthlyRevenue = orderService.getMonthlyRevenue();
        request.setAttribute("monthlyRevenue", monthlyRevenue);

//        List<Map<String, Object>> lowStockVariants = productStockService.getVariantDetailsWithLowStock(10);
//        request.setAttribute("lowStockVariants", lowStockVariants);
        return "../dashboard/dashboard.jsp";
    }

    private String handleProductList(HttpServletRequest request) {
        ProductService productService = new ProductService();
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);

        return "/product/ProductList.jsp";
    }

    private String handleCustomerList(HttpServletRequest request) {
        List<User> users = userService.getAllUsers();
        service.UserAddressService addressService = new service.UserAddressService();

        Map<Integer, model.UserAddress> addressMap = new HashMap<>();
        for (User u : users) {
            List<model.UserAddress> addresses = addressService.getAllAddressesByUserId(u.getUserID());
            if (addresses != null && !addresses.isEmpty()) {
                addressMap.put(u.getUserID(), addresses.get(0));
            }
        }
        request.setAttribute("users", users);
        request.setAttribute("addressMap", addressMap);
        return "/user/listuser.jsp";
    }

    private String handleOrders(HttpServletRequest request) {
        // TODO: xử lý hiển thị danh sách đơn hàng
        return "/admin/orders/list.jsp"; // hoặc một file tương ứng
    }

    private String handleDiscounts(HttpServletRequest request) {
        return "/product/DiscountManagement.jsp";
    }

    private String handleReport(HttpServletRequest request) {
        // TODO: xử lý báo cáo
        return "/WEB-INF/views/report/report.jsp"; // hoặc một file tương ứng
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
        return "Short description";
    }// </editor-fold>

}

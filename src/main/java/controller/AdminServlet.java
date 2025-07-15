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
import model.UserAddress;
import java.util.Comparator;

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
        String action = request.getParameter("action");
        String contentPage;

        if (action != null && action.equals("editadmin")) {
            User admin = (User) request.getSession().getAttribute("user");
            if (admin == null || admin.getRoleID() != 1) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }
            UserAddress address = null;
            List<UserAddress> addresses = new service.UserAddressService().getAllAddressesByUserId(admin.getUserID());
            if (addresses != null && !addresses.isEmpty()) {
                address = addresses.get(0);
                if (address.getAddress() != null) {
                    String[] parts = address.getAddress().split(",\\s*");
                    request.setAttribute("ward", parts.length > 0 ? parts[0] : "");
                    request.setAttribute("district", parts.length > 1 ? parts[1] : "");
                    request.setAttribute("province", parts.length > 2 ? parts[2] : "");
                }
            }
            request.setAttribute("user", admin);
            request.setAttribute("address", address);
            request.setAttribute("contentPage", "../../user/edituser.jsp");
            request.getRequestDispatcher("/admin/layout/admin-header.jsp").forward(request, response);
            return;
        } else if (menu == null || menu.equals("dashboard")) {
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
        Map<String, Double> brandSalesData = orderService.getBrandSalesData();
        
        request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.setAttribute("brandSalesData", brandSalesData);

        List<Map<String, Object>> lowStockVariants = productStockService.getVariantDetailsWithLowStock(10);
        request.setAttribute("lowStockVariants", lowStockVariants);
        return "../dashboard/dashboard.jsp";
    }

    private String handleProductList(HttpServletRequest request) {
        ProductService productService = new ProductService();
        List<Product> products = productService.getAllProducts();
        Map<Integer, Integer> stockMap = productStockService.getProductStockQuantities();
        request.setAttribute("products", products);
        request.setAttribute("productStockQuantity", stockMap);

        
        return "/product/ProductList.jsp";
    }

    private String handleCustomerList(HttpServletRequest request) {
        List<User> users = userService.getAllUsers();
        

        String sort = request.getParameter("sort");
        String status = request.getParameter("status");
        String searchName = request.getParameter("searchName");

        if ("active".equals(status)) {
            users.removeIf(u -> !u.getIsActive());
        } else if ("inactive".equals(status)) {
            users.removeIf(User::getIsActive);
        }

        if (searchName != null && !searchName.trim().isEmpty()) {
            String search = searchName.trim().toLowerCase();
            users.removeIf(u -> !(u.getFullName().toLowerCase().contains(search) || u.getEmail().toLowerCase().contains(search)));
        }

        if ("name_asc".equals(sort)) {
            users.sort(Comparator.comparing(u -> getLastName(((User)u).getFullName()), String.CASE_INSENSITIVE_ORDER));
        } else if ("name_desc".equals(sort)) {
            users.sort(Comparator.comparing(u -> getLastName(((User)u).getFullName()), String.CASE_INSENSITIVE_ORDER).reversed());
        } else if ("id_asc".equals(sort)) {
            users.sort(Comparator.comparingInt(User::getUserID));
        } else if ("id_desc".equals(sort)) {
            users.sort(Comparator.comparingInt(User::getUserID).reversed());
        }

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

        request.setAttribute("sort", sort);
        request.setAttribute("status", status);
        request.setAttribute("searchName", searchName);

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
        String action = request.getParameter("action");
        if ("editadmin".equals(action)) {
            handleEditAdmin(request, response);
        } else {
            processRequest(request, response);
        }
    }

    private void handleEditAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User admin = (User) request.getSession().getAttribute("user");
        if (admin == null || admin.getRoleID() != 1) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String phoneNumber = request.getParameter("phoneNumber");
        String dob = request.getParameter("dob");
        String addressStr = request.getParameter("address");

        admin.setFullName(fullName);
        admin.setEmail(email);
        admin.setPhoneNumber(phoneNumber);
        if (dob != null && !dob.isEmpty()) {
            admin.setDob(java.time.LocalDate.parse(dob));
        }
        if (password != null && !password.isEmpty()) {
            admin.setPassword(password);
        }

        boolean success = userService.updateUser(admin);

        // Cập nhật địa chỉ
        service.UserAddressService addressService = new service.UserAddressService();
        List<UserAddress> addresses = addressService.getAllAddressesByUserId(admin.getUserID());
        UserAddress address;
        if (addresses != null && !addresses.isEmpty()) {
            address = addresses.get(0);
            address.setFullName(fullName);
            address.setPhoneNumber(phoneNumber);
            address.setAddress(addressStr);
            addressService.updateAddress(address);
        } else {
            address = new UserAddress();
            address.setUser(admin);
            address.setFullName(fullName);
            address.setPhoneNumber(phoneNumber);
            address.setAddress(addressStr);
            address.setIsDefault(true);
            address.setIsActive(true);
            address.setCreatedAt(java.time.Instant.now());
            addressService.addAddress(address);
        }

        // Tách lại địa chỉ để set vào request
        if (address != null && address.getAddress() != null) {
            String[] parts = address.getAddress().split(",\\s*");
            request.setAttribute("ward", parts.length > 0 ? parts[0] : "");
            request.setAttribute("district", parts.length > 1 ? parts[1] : "");
            request.setAttribute("province", parts.length > 2 ? parts[2] : "");
        }

        request.getSession().setAttribute("user", admin);

        if (success) {
            request.setAttribute("message", "Cập nhật thông tin thành công!");
        } else {
            request.setAttribute("error", "Có lỗi xảy ra, vui lòng thử lại.");
        }

        request.setAttribute("user", admin);
        request.setAttribute("address", address);
        request.setAttribute("contentPage", "../../user/edituser.jsp");
        request.getRequestDispatcher("/admin/layout/admin-header.jsp").forward(request, response);
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

    private String getLastName(String fullName) {
        if (fullName == null || fullName.isEmpty()) return "";
        String[] parts = fullName.trim().split("\\s+");
        return parts[parts.length - 1];
    }
}

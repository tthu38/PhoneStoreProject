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
import model.Product;
import service.ProductService;

/**
 *
 * @author Admin
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {

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
        String contentPage = null;

        if (menu == null || menu.equals("dashboard")) {
            contentPage = "../dashboard/dashboard.jsp";
        } else if (menu.equals("products")) {
            // Lấy danh sách sản phẩm
            ProductService productService = new ProductService();
            List<Product> products = productService.getAllProducts();
            request.setAttribute("products", products);
            contentPage = "../../product/ProductList.jsp";
        } else if (menu.equals("categories")) {
            // TODO: Lấy danh sách category, set vào request
            contentPage = "../../product/CategoryList.jsp";
        } else if (menu.equals("orders")) {
            // TODO: Lấy danh sách orders, set vào request
            contentPage = "../orders/orders.jsp";
        } else if (menu.equals("customers")) {
            // TODO: Lấy danh sách customers, set vào request
            contentPage = "../users/customers.jsp";
        } else if (menu.equals("reports")) {
            contentPage = "../reports/reports.jsp";
        } else if (menu.equals("settings")) {
            contentPage = "../settings/settings.jsp";
        } else {
            contentPage = "../dashboard/dashboard.jsp";
        }

        request.setAttribute("contentPage", contentPage);
        request.getRequestDispatcher("/admin/layout/admin-header.jsp").forward(request, response);
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

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
import java.util.List;
import java.util.Map;
import service.ProductService;

/**
 *
 * @author ThienThu
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Lấy sản phẩm giảm giá
        List<Map<String, Object>> discountedProducts = productService.getMostDiscountedProducts(10);
        request.setAttribute("discountedProducts", discountedProducts);

        // Gợi ý sản phẩm nếu người dùng đã đăng nhập
        Integer userId = (Integer) request.getSession().getAttribute("userId");
        if (userId != null) {
            List<Map<String, Object>> recommendedProducts = utils.ProductRecommendationClient.getRecommendations(userId);
            request.setAttribute("recommendedProducts", recommendedProducts);
        }

        request.getRequestDispatcher("indexFirst.jsp").forward(request, response);
    }
}


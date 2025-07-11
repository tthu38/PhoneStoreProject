/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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

        // ====== GỢI Ý SẢN PHẨM TỪ FLASK API =======
        // Kiểm tra nếu user đã đăng nhập
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            int userId = (int) session.getAttribute("userId");

// Gọi reload Flask
            try {
                URL reloadUrl = new URL("http://localhost:5555/api/reload");
                HttpURLConnection reloadConn = (HttpURLConnection) reloadUrl.openConnection();
                reloadConn.setRequestMethod("POST");
                int reloadCode = reloadConn.getResponseCode();
                System.out.println("Reload response code: " + reloadCode);
            } catch (Exception ex) {
                System.out.println("Không thể reload từ Flask: " + ex.getMessage());
            }

// Gọi API gợi ý sản phẩm
            String apiUrl = "http://localhost:5555/api?user_id=" + userId + "&method=hybrid";
            try {
                URL apiCallUrl = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) apiCallUrl.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder responseStr = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    responseStr.append(inputLine);
                }
                in.close();

                org.json.JSONObject jsonResponse = new org.json.JSONObject(responseStr.toString());
                org.json.JSONArray productArray = jsonResponse.getJSONArray("san pham goi y");

                List<Map<String, Object>> recommendedProducts = new ArrayList<>();
                for (int i = 0; i < productArray.length(); i++) {
                    org.json.JSONObject p = productArray.getJSONObject(i);
                    Map<String, Object> product = new HashMap<>();
                    product.put("id", p.getInt("id"));
                    product.put("name", p.getString("name"));
                    product.put("description", p.getString("description"));
                    product.put("price", p.getString("price"));
                    product.put("image", p.getString("image"));
                    recommendedProducts.add(product);
                }

                request.setAttribute("recommendedProducts", recommendedProducts);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // Forward sang trang chính
        request.getRequestDispatcher("indexFirst.jsp").forward(request, response);
    }
}

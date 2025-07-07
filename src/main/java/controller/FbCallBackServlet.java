/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.User;
import service.UserService;
import org.json.JSONObject;

@WebServlet(name = "FbCallBackServlet", urlPatterns = {"/fbcallback"})
public class FbCallBackServlet extends HttpServlet {

    private static final ResourceBundle bundle = ResourceBundle.getBundle("application");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("=== FbCallBackServlet doGet called ===");
        String code = request.getParameter("code");
        System.out.println("Received code: " + code);

        if (code == null || code.isEmpty()) {
            System.out.println("No code received, redirecting...");
            response.sendRedirect("user/register.jsp?error=fb");
            return;
        }

        // Đọc cấu hình từ application.properties
        String FB_APP_ID = bundle.getString("facebook.app.id");
        String FB_APP_SECRET = bundle.getString("facebook.app.secret");
        String REDIRECT_URI = bundle.getString("facebook.redirect.uri");
        String TOKEN_URL = bundle.getString("facebook.token.url");
        String USERINFO_URL = bundle.getString("facebook.userinfo.url");

        // 1. Lấy access_token từ Facebook
        String tokenUrl = TOKEN_URL
                + "?client_id=" + FB_APP_ID
                + "&redirect_uri=" + REDIRECT_URI
                + "&client_secret=" + FB_APP_SECRET
                + "&code=" + code;

        String accessToken = null;
        try {
            String tokenResponse = sendGet(tokenUrl);
            JSONObject json = new JSONObject(tokenResponse);
            accessToken = json.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace(); // Thêm dòng này để xem lỗi trên console/log
            response.sendRedirect("user/register.jsp?error=fb_token");
            return;
        }

        System.out.println("FB code: " + code);
        System.out.println("FB accessToken: " + accessToken);

        // 2. Lấy thông tin user từ Facebook
        try {
            User fbUserInfo = utils.FacebookUtils.getUserInfo(accessToken);
            if (fbUserInfo == null) {
                response.sendRedirect("user/register.jsp?error=fb_info");
                return;
            }

            UserService userService = new UserService();
            User user = userService.findOrCreateOauthUser(
                fbUserInfo.getEmail(),
                fbUserInfo.getFullName(),
                fbUserInfo.getPicture(),
                fbUserInfo.getOauthId(),
                "facebook",
                true
            );

            if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
                // Chưa có số điện thoại, chuyển sang trang bổ sung
                request.getSession().setAttribute("fbUser", user);
                response.sendRedirect("user/registerfb.jsp");
                return;
            }
            // Đủ thông tin, đăng nhập luôn
            request.getSession().setAttribute("user", user);
            response.sendRedirect("indexFirst.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("user/register.jsp?error=fb_info");
        }
    }

    // Hàm gửi GET request đơn giản
    private String sendGet(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }
}

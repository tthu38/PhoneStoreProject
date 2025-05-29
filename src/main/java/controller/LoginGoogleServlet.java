package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.GooglePojo;
import model.User;
import service.UserService;
import utils.GoogleUtils;
import service.IUserService;

@WebServlet("/login-google")
public class LoginGoogleServlet extends HttpServlet {
    private IUserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        
        if (code == null || code.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            String accessToken = GoogleUtils.getToken(code);
            GooglePojo googlePojo = GoogleUtils.getUserInfo(accessToken);
            
            User user = userService.loginWithGoogle(
                googlePojo.getId(),
                googlePojo.getEmail(),
                googlePojo.getName(),
                googlePojo.getPicture()
            );
            
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/home");
            } else {
                request.setAttribute("error", "Không thể đăng nhập bằng Google. Vui lòng thử lại.");
                request.getRequestDispatcher("/templates/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi đăng nhập bằng Google: " + e.getMessage());
            request.getRequestDispatcher("/templates/login.jsp").forward(request, response);
        }
    }
} 
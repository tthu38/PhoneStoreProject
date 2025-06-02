package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import service.UserService;
import utils.GoogleUtils;
import service.IUserService;
import utils.GoogleConstants;

@WebServlet(urlPatterns = {"/login-google", "/oauth2/google"})
public class LoginGoogleServlet extends HttpServlet {
    private IUserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String servletPath = request.getServletPath();
        
        if ("/oauth2/google".equals(servletPath)) {
            handleInitialGoogleLogin(request, response);
        } else {
            handleGoogleCallback(request, response);
        }
    }
    
    private void handleInitialGoogleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String googleLoginUrl = "https://accounts.google.com/o/oauth2/auth?"
                + "client_id=" + GoogleConstants.GOOGLE_CLIENT_ID
                + "&redirect_uri=" + GoogleConstants.GOOGLE_REDIRECT_URI
                + "&response_type=code"
                + "&scope=email%20profile"
                + "&access_type=offline";

        response.sendRedirect(googleLoginUrl);
    }
    
    private void handleGoogleCallback(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");
        
        if (code == null || code.isEmpty()) {
            request.setAttribute("error", "Không nhận được mã xác thực từ Google. Vui lòng thử lại.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            return;
        }
        
        try {
            String accessToken = GoogleUtils.getToken(code);
            User googleUser = GoogleUtils.getUserInfo(accessToken);
            
            if (googleUser == null || googleUser.getEmail() == null) {
                request.setAttribute("error", "Không thể lấy thông tin từ tài khoản Google. Vui lòng thử lại.");
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
                return;
            }
            
            User user = userService.loginWithGoogle(
                googleUser.getGoogleId(),
                googleUser.getEmail(),
                googleUser.getFullname(),
                googleUser.getPicture()
            );
            
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                
                // Nếu user là admin, chuyển đến trang admin
                if (user.isAdmin()) {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                } else {
                    // Ngược lại chuyển đến trang index
                    response.sendRedirect(request.getContextPath() + "/templates/header.jsp");
                }
            } else {
                request.setAttribute("error", "Không thể đăng nhập bằng Google. Email này chưa được đăng ký hoặc tài khoản đã bị khóa.");
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = "Lỗi khi đăng nhập bằng Google: ";
            
            // Xử lý các loại lỗi cụ thể
            if (e.getMessage().contains("invalid_grant")) {
                errorMessage += "Mã xác thực không hợp lệ hoặc đã hết hạn. Vui lòng thử lại.";
            } else if (e.getMessage().contains("invalid_client")) {
                errorMessage += "Lỗi cấu hình ứng dụng. Vui lòng liên hệ quản trị viên.";
            } else {
                errorMessage += e.getMessage();
            }
            
            request.setAttribute("error", errorMessage);
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        }
    }
} 
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import model.User;
import service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author ThienThu
 */
@WebServlet(urlPatterns = {"/login", "/login-google", "/oauth2/google", "/update-profile"})
public class LoginServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

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
            out.println("<title>Servlet LoginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LoginServlet at " + request.getContextPath() + "</h1>");
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
        if (request.getSession().getAttribute("userObject") == null) {
            java.util.Optional<User> rememberedUser = userService.checkRememberToken(request);
            if (rememberedUser.isPresent()) {
                userService.setupUserSession(request, response, rememberedUser.get());
                request.getSession().setAttribute("userObject", rememberedUser.get());
                response.sendRedirect(request.getContextPath() + "/index.jsp");
                return;
            }
        }
        String servletPath = request.getServletPath();
        if ("/login-google".equals(servletPath)) {
            String rememberMe = request.getParameter("rememberMe");
            if ("true".equals(rememberMe)) {
                request.getSession().setAttribute("rememberMe", true);
            } else {
                request.getSession().removeAttribute("rememberMe");
            }
            String googleLoginUrl = "https://accounts.google.com/o/oauth2/auth?"
                    + "client_id=" + utils.GoogleUtils.getConfig("google.client.id")
                    + "&redirect_uri=" + utils.GoogleUtils.getConfig("google.redirect.uri")
                    + "&response_type=code"
                    + "&scope=email%20profile"
                    + "&access_type=offline";
            System.out.println("Google Login URL: " + googleLoginUrl);
            response.sendRedirect(googleLoginUrl);
            return;
        } else if ("/oauth2/google".equals(servletPath)) {
            String error = request.getParameter("error");
            if (error != null) {
                request.setAttribute("error", "Bạn đã từ chối cấp quyền cho ứng dụng hoặc có lỗi xác thực: " + error);
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
                return;
            }
            String code = request.getParameter("code");
            if (code == null || code.isEmpty()) {
                request.setAttribute("error", "Không nhận được mã xác thực từ Google. Vui lòng thử lại.");
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
                return;
            }
            try {
                String accessToken = utils.GoogleUtils.getToken(code);
                User googleUserInfo = utils.GoogleUtils.getUserInfo(accessToken);
                User googleUser = userService.findOrCreateGoogleUser(
                    googleUserInfo.getEmail(),
                    googleUserInfo.getFullName(),
                    googleUserInfo.getPicture(),
                    "GOOGLE",
                    true
                );
                if (googleUser != null) {
                    Boolean rememberMe = (Boolean) request.getSession().getAttribute("rememberMe");
                    if (Boolean.TRUE.equals(rememberMe)) {
                        String token = java.util.UUID.randomUUID().toString();
                        userService.saveRememberToken(googleUser.getId(), token);
                        jakarta.servlet.http.Cookie rememberCookie = new jakarta.servlet.http.Cookie("remember_token", token);
                        rememberCookie.setMaxAge(60 * 60 * 24 * 30); // 30 ngày
                        rememberCookie.setPath("/");
                        rememberCookie.setHttpOnly(true);
                        response.addCookie(rememberCookie);
                    }
                    request.getSession().removeAttribute("rememberMe");

                    userService.setupUserSession(request, response, googleUser);
                    request.getSession().setAttribute("userObject", googleUser);

                    response.sendRedirect(request.getContextPath() + "/index.jsp");
                    return;
                } else {
                    request.setAttribute("error", "Không thể đăng nhập bằng Google. Email này chưa được đăng ký hoặc tài khoản đã bị khóa.");
                    request.getRequestDispatcher("/user/login.jsp").forward(request, response);
                }
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Lỗi khi đăng nhập bằng Google: " + e.getMessage());
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            }
            return;
        }
        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
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
        String servletPath = request.getServletPath();
        if ("/update-profile".equals(servletPath)) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("userObject") == null) {
                response.sendRedirect(request.getContextPath() + "/user/login.jsp");
                return;
            }
            User user = (User) session.getAttribute("userObject");
            String fullName = request.getParameter("fullname");
            String phoneNumber = request.getParameter("phone");
            user.setFullName(fullName);
            user.setPhoneNumber(phoneNumber);
            userService.updateUser(user);
            session.setAttribute("userObject", user);
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userService.login(email, password);
        if (user != null) {
            userService.setupUserSession(request, response, user);
            request.getSession().setAttribute("userObject", user);
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            request.setAttribute("error", "Invalid email or password");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        }
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

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
import java.util.List;
import java.util.Optional;
import model.UserAddress;

/**
 *
 * @author ThienThu
 */
@WebServlet(urlPatterns = {"/login"})
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

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the
    // + sign on the left to edit the code.">
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
        String action = request.getParameter("action");
        if (action == null) action = "";
        switch (action) {
            case "login-google":
                handleLoginGoogle(request, response);
                break;
            case "oauth2-google":
                handleOauth2Google(request, response);
                break;
            default:
                handleLoginGet(request, response);
                break;
        }
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
        if (action == null) action = "";
        switch (action) {
            case "verify-google-otp":
                handleVerifyGoogleOtp(request, response);
                break;
            case "update-profile":
                handleUpdateProfile(request, response);
                break;
            case "forgot-password-send-otp":
                handleForgotPasswordSendOtp(request, response);
                break;
            case "forgot-password-verify-otp":
                handleForgotPasswordVerifyOtp(request, response);
                break;
            case "forgot-password-reset":
                handleForgotPasswordReset(request, response);
                break;
            default:
                handleLoginPost(request, response);
                break;
        }
    }

    private void handleLoginGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("user") == null) {
            Optional<User> rememberedUser = userService.checkRememberToken(request);
            if (rememberedUser.isPresent()) {
                request.getSession().setAttribute("user", rememberedUser.get());
                response.sendRedirect(request.getContextPath() + "/indexFirst.jsp");
                return;
            }
        }
        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
    }

    private void handleLoginPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userService.findByEmailOrPhoneAndPassword(username, password);

        if (user != null) {
            request.getSession().setAttribute("user", user);

            String rememberMe = request.getParameter("rememberMe");
            if ("on".equals(rememberMe)) {
                String token = java.util.UUID.randomUUID().toString();
                userService.saveRememberToken(user.getUserID(), token);

                jakarta.servlet.http.Cookie tokenCookie = new jakarta.servlet.http.Cookie("remember_token", token);
                tokenCookie.setMaxAge(60 * 60 * 24 * 30);
                tokenCookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
                tokenCookie.setHttpOnly(true);
                response.addCookie(tokenCookie);

                jakarta.servlet.http.Cookie userCookie = new jakarta.servlet.http.Cookie("remember_username", username);
                jakarta.servlet.http.Cookie passCookie = new jakarta.servlet.http.Cookie("remember_password", password);
                userCookie.setMaxAge(7 * 24 * 60 * 60); // 7 ngày
                passCookie.setMaxAge(7 * 24 * 60 * 60);
                userCookie.setPath("/");
                passCookie.setPath("/");
                response.addCookie(userCookie);
                response.addCookie(passCookie);
            } else {
                jakarta.servlet.http.Cookie userCookie = new jakarta.servlet.http.Cookie("remember_username", "");
                jakarta.servlet.http.Cookie passCookie = new jakarta.servlet.http.Cookie("remember_password", "");
                userCookie.setMaxAge(0);
                passCookie.setMaxAge(0);
                userCookie.setPath("/");
                passCookie.setPath("/");
                response.addCookie(userCookie);
                response.addCookie(passCookie);
            }

            response.sendRedirect(request.getContextPath() + "/indexFirst.jsp");
        } else {
            request.setAttribute("error", "Sai tài khoản hoặc mật khẩu");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        }
    }

    private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        User sessionUser = (User) session.getAttribute("user");
        Optional<User> userOpt = userService.getUserById(sessionUser.getUserID());

        if (!userOpt.isPresent()) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }

        User user = userOpt.get();
        String fullName = request.getParameter("fullname");
        String phoneNumber = request.getParameter("phone");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dob");

        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);

        if (dobStr != null && !dobStr.trim().isEmpty()) {
            try {
                user.setDob(java.time.LocalDate.parse(dobStr));
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("error", "Định dạng ngày sinh không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd.");
                request.getRequestDispatcher("/user/profile.jsp").forward(request, response);
                return;
            }
        }

        userService.updateUser(user);
        session.setAttribute("user", user);

        if (address != null && !address.trim().isEmpty()) {
            service.UserAddressService addressService = new service.UserAddressService();
            List<UserAddress> addresses = addressService.getAllAddressesByUserId(user.getUserID());
            UserAddress userAddress;
            if (addresses != null && !addresses.isEmpty()) {
                userAddress = addresses.get(0);
                userAddress.setAddress(address);
                addressService.updateAddress(userAddress);
            } else {
                userAddress = new UserAddress();
                userAddress.setUser(user);
                userAddress.setAddress(address);
                userAddress.setIsDefault(true);
                userAddress.setIsActive(true);
                userAddress.setFullName(user.getFullName());
                userAddress.setPhoneNumber(user.getPhoneNumber());
                addressService.addAddress(userAddress);
            }
        }

        response.sendRedirect(request.getContextPath() + "/indexFirst.jsp");
    }

    private void handleLoginGoogle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        response.sendRedirect(googleLoginUrl);
    }

    private void handleOauth2Google(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            Optional<User> existedUser = userService.getUserByEmail(googleUserInfo.getEmail());
            User googleUser = userService.findOrCreateGoogleUser(
                    googleUserInfo.getEmail(),
                    googleUserInfo.getFullName(),
                    googleUserInfo.getPicture(),
                    "GOOGLE",
                    true
            );

            List<UserAddress> addresses = userService.getUserAddressesByUserId(googleUser.getUserID());
            googleUser.setAddresses(addresses);

            if (googleUser != null) {
                request.getSession().setAttribute("user", googleUser);
                request.getSession().setAttribute("userName", googleUser.getFullName());
                request.getSession().setAttribute("userEmail", googleUser.getEmail());
                Boolean rememberMe = (Boolean) request.getSession().getAttribute("rememberMe");
                if (Boolean.TRUE.equals(rememberMe)) {
                    String token = java.util.UUID.randomUUID().toString();
                    userService.saveRememberToken(googleUser.getUserID(), token);
                    jakarta.servlet.http.Cookie rememberCookie = new jakarta.servlet.http.Cookie("remember_token", token);
                    rememberCookie.setMaxAge(60 * 60 * 24 * 30);
                    rememberCookie.setPath("/");
                    rememberCookie.setHttpOnly(true);
                    response.addCookie(rememberCookie);
                }
                request.getSession().removeAttribute("rememberMe");

                if (existedUser.isEmpty()) {
                    service.MailService mailService = new service.MailService();
                    String otp = mailService.generateOTP(googleUser.getEmail());
                    boolean sent = mailService.sendOtpForRegister(googleUser.getEmail(), otp);
                    if (sent) {
                        request.getSession().setAttribute("google_otp", otp);
                        response.sendRedirect(request.getContextPath() + "/templates/googleotp.jsp");
                    } else {
                        request.setAttribute("error", "Không thể gửi email xác nhận. Vui lòng thử lại.");
                        request.getRequestDispatcher("/user/login.jsp").forward(request, response);
                    }
                    return;
                } else {
                    response.sendRedirect(request.getContextPath() + "/indexFirst.jsp");
                    return;
                }
            } else {
                request.setAttribute("error", "Không thể đăng nhập bằng Google. Email này chưa được đăng ký hoặc tài khoản đã bị khóa.");
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi khi đăng nhập bằng Google: " + e.getMessage());
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        }
    }

    private void handleVerifyGoogleOtp(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String otpInput = request.getParameter("otp");
    User user = (User) request.getSession().getAttribute("user");
    String otpSession = (String) request.getSession().getAttribute("google_otp");

    if (user != null && otpSession != null && otpSession.equals(otpInput)) {
        request.getSession().removeAttribute("google_otp");
        response.sendRedirect(request.getContextPath() + "/user/userupdate.jsp");
    } else {
        request.setAttribute("error", "Mã xác nhận không đúng hoặc đã hết hạn.");
        request.getRequestDispatcher("/templates/googleotp.jsp").forward(request, response);
    }
}

    private void handleForgotPasswordSendOtp(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String email = request.getParameter("email");
    Optional<User> userOpt = userService.getUserByEmail(email);
    if (userOpt.isPresent()) {
        service.MailService mailService = new service.MailService();
        String otp = mailService.generateOTP(email);
        boolean sent = mailService.sendOtpForResetPassword(email, otp);
        if (sent) {
            request.getSession().setAttribute("reset_otp", otp);
            request.getSession().setAttribute("reset_email", email);
            request.setAttribute("message", "Mã xác nhận đã được gửi về email của bạn.");
            request.getRequestDispatcher("/user/forgotpassword.jsp?step=otp&email=" + email).forward(request, response);
            return;
        } else {
            request.setAttribute("error", "Không thể gửi mã xác nhận. Vui lòng thử lại.");
        }
    } else {
        request.setAttribute("error", "Email không tồn tại trong hệ thống.");
    }
    request.getRequestDispatcher("/user/forgotpassword.jsp").forward(request, response);
}

    private void handleForgotPasswordVerifyOtp(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String otpInput = request.getParameter("otp");
    String otpSession = (String) request.getSession().getAttribute("reset_otp");
    if (otpSession != null && otpSession.equals(otpInput)) {
        request.getRequestDispatcher("/user/reset-password.jsp").forward(request, response);
    } else {
        request.setAttribute("error", "Mã xác nhận không đúng hoặc đã hết hạn.");
        request.getRequestDispatcher("/user/forgotpassword.jsp").forward(request, response);
    }
}

    private void handleForgotPasswordReset(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    String email = request.getParameter("email");
    String otpInput = request.getParameter("otp");
    String newPassword = request.getParameter("newPassword");
    String otpSession = (String) request.getSession().getAttribute("reset_otp");
    String emailSession = (String) request.getSession().getAttribute("reset_email");

    if (emailSession != null && emailSession.equals(email)
            && otpSession != null && otpSession.equals(otpInput)) {
        boolean updated = userService.updatePasswordByEmail(email, newPassword);
        if (updated) {
            request.getSession().removeAttribute("reset_otp");
            request.getSession().removeAttribute("reset_email");
            request.setAttribute("message", "Đổi mật khẩu thành công. Bạn có thể đăng nhập lại.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            return;
        } else {
            request.setAttribute("error", "Không thể đổi mật khẩu. Vui lòng thử lại.");
        }
    } else {
        request.setAttribute("error", "Mã xác nhận không đúng hoặc đã hết hạn.");
    }
    request.getRequestDispatcher("/user/forgotpassword.jsp?step=otp&email=" + email).forward(request, response);
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
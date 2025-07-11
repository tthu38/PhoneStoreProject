package controller;

import model.User;
import service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("verify-otp".equals(action)) {
            handleVerifyRegisterOtp(request, response);
            return;
        }

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullname");
        String phoneNumber = request.getParameter("phone");
        String address = request.getParameter("address");
        String dobStr = request.getParameter("dob");

        request.setAttribute("fullname", fullName);
        request.setAttribute("email", email);
        request.setAttribute("password", password);
        request.setAttribute("confirmPassword", confirmPassword);
        request.setAttribute("phone", phoneNumber);
        request.setAttribute("dob", dobStr);
        request.setAttribute("address", address);

        String error = null;

        // Check email
        if (email == null || email.trim().isEmpty()) {
            error = "Email không được để trống.";
            request.setAttribute("email", "");
        } else if (userService.getUserByEmail(email).isPresent()) {
            error = "Email đã tồn tại.";
            request.setAttribute("email", "");
        }

        // Check phone number
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            error = "Số điện thoại không được để trống.";
            request.setAttribute("phone", "");
        } else if (!phoneNumber.matches("^0\\d{9}$")) {
            error = "Số điện thoại phải bắt đầu bằng số 0 và gồm đúng 10 số.";
            request.setAttribute("phone", "");
        }

        // Check password
        if (password == null || password.length() < 6) {
            error = "Mật khẩu phải từ 6 ký tự trở lên.";
            request.setAttribute("password", "");
        } else if (confirmPassword == null || !confirmPassword.equals(password)) {
            error = "Mật khẩu nhập lại không khớp.";
            request.setAttribute("confirmPassword", "");
        }

        // Check date of birth
        if (dobStr != null && !dobStr.isEmpty()) {
            try {
                java.time.LocalDate dob = java.time.LocalDate.parse(dobStr);
                java.time.LocalDate today = java.time.LocalDate.now();
                if (dob.isAfter(today)) {
                    error = "Ngày sinh không được ở tương lai.";
                    request.setAttribute("dob", "");
                }
                java.time.Period age = java.time.Period.between(dob, today);
                if (age.getYears() < 16) {
                    error = "Bạn phải đủ 16 tuổi trở lên.";
                    request.setAttribute("dob", "");
                }
            } catch (Exception e) {
                error = "Định dạng ngày sinh không hợp lệ. Vui lòng sử dụng định dạng yyyy-MM-dd.";
                request.setAttribute("dob", "");
            }
        }

        if (error != null) {
            request.setAttribute("error", error);
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
            return;
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);
        user.setCreatedAt(Instant.now());
        user.setIsActive(true);
        user.setRoleID(2);

        if (dobStr != null && !dobStr.isEmpty()) {
            try {
                user.setDob(java.time.LocalDate.parse(dobStr));
            } catch (Exception e) {
                user.setDob(null);
            }
        }

        model.UserAddress userAddress = new model.UserAddress();
        userAddress.setFullName(fullName);
        userAddress.setPhoneNumber(phoneNumber);
        userAddress.setAddress(address);
        userAddress.setIsDefault(true);
        userAddress.setIsActive(true);
        userAddress.setCreatedAt(Instant.now());

        request.getSession().setAttribute("register_user", user);
        request.getSession().setAttribute("register_address", userAddress);

        service.MailService mailService = new service.MailService();
        String otp = mailService.generateOTP(email);
        boolean sent = mailService.sendOtpForRegister(email, otp);
        if (sent) {
            request.getSession().setAttribute("register_otp", otp);
            request.getSession().setAttribute("register_email", email);
            request.setAttribute("message", "Mã xác thực đã được gửi về email của bạn. Vui lòng kiểm tra email để xác nhận.");
            request.getRequestDispatcher("/templates/googleotp.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không thể gửi mã xác thực. Vui lòng thử lại.");
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
        }
    }

    private void handleVerifyRegisterOtp(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String otpInput = request.getParameter("otp");
        String otpSession = (String) request.getSession().getAttribute("register_otp");
        String email = (String) request.getSession().getAttribute("register_email");
        User user = (User) request.getSession().getAttribute("register_user");
        model.UserAddress userAddress = (model.UserAddress) request.getSession().getAttribute("register_address");

        if (otpSession != null && otpSession.equals(otpInput) && user != null && userAddress != null) {
            if (userService.register(user)) {
                service.UserAddressService addressService = new service.UserAddressService();
                User createdUser = userService.getUserByEmail(email).orElse(user);
                userAddress.setUser(createdUser);
                addressService.addAddress(userAddress);

                request.getSession().removeAttribute("register_otp");
                request.getSession().removeAttribute("register_email");
                request.getSession().removeAttribute("register_user");
                request.getSession().removeAttribute("register_address");

                request.setAttribute("message", "Đăng ký thành công! Bạn có thể đăng nhập.");
                request.getRequestDispatcher("/user/login.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Có lỗi khi lưu thông tin. Vui lòng thử lại.");
                request.getRequestDispatcher("/user/register.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Mã xác nhận không đúng hoặc đã hết hạn.");
            request.getRequestDispatcher("/templates/googleotp.jsp").forward(request, response);
        }
    }

    private void handleRegisterWithGoogle(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User googleUserInfo = (User) request.getSession().getAttribute("google_user");
        if (googleUserInfo == null) {
            response.sendRedirect(request.getContextPath() + "/user/register.jsp");
            return;
        }
        Optional<User> existedUser = userService.getUserByEmail(googleUserInfo.getEmail());
        if (existedUser.isPresent()) {
            request.setAttribute("error", "Email đã tồn tại. Vui lòng đăng nhập.");
            request.getRequestDispatcher("/user/login.jsp").forward(request, response);
        } else {
            // Chuyển sang trang cập nhật thông tin bổ sung
            request.getSession().setAttribute("google_user", googleUserInfo);
            response.sendRedirect(request.getContextPath() + "/user/userupdate.jsp");
        }
    }
}
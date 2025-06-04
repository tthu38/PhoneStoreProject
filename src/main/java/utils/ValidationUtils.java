package utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}\\s'-]{2,100}$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$");

    /**
     * Kiểm tra xem người dùng đã đăng nhập hay chưa
     */
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }

    /**
     * Kiểm tra xem người dùng có phải là admin không
     */
    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            return user != null && user.getRoleId() == 1;
        }
        return false;
    }

    /**
     * Kiểm tra xem người dùng có remember token không
     */
    public static String getRememberToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("remember_token".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Kiểm tra email có hợp lệ không
     */
    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Kiểm tra mật khẩu có đủ mạnh không
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }
        
        return hasUpper && hasLower && hasDigit && hasSpecial;
    }

    /**
     * Kiểm tra số điện thoại có hợp lệ không
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Kiểm tra tên có hợp lệ không
     */
    public static boolean isValidName(String name) {
        return name != null && NAME_PATTERN.matcher(name).matches();
    }

    /**
     * Kiểm tra địa chỉ có hợp lệ không
     */
    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty() && address.length() <= 255;
    }

    /**
     * Kiểm tra URL hình ảnh có hợp lệ không
     */
    public static boolean isValidImageUrl(String url) {
        return url == null || URL_PATTERN.matcher(url).matches();
    }

    /**
     * Kiểm tra OAuth provider có hợp lệ không
     */
    public static boolean isValidOAuthProvider(String provider) {
        return "GOOGLE".equals(provider) || "FACEBOOK".equals(provider);
    }

    /**
     * Kiểm tra trạng thái người dùng có hợp lệ không
     */
    public static boolean isValidUserStatus(Boolean status) {
        return status != null;
    }

    public static boolean isNotBlank(String str) {
        return str != null && !str.trim().isEmpty();
    }
    
    public static boolean isValidPrice(Double price) {
        return price != null && price >= 0;
    }
    
    public static boolean isValidQuantity(Integer quantity) {
        return quantity != null && quantity >= 0;
    }

    public static boolean isValidPhoneNumber(String phone) {
        return phone == null || PHONE_PATTERN.matcher(phone).matches();
    }
} 
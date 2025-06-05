package utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.User;
import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^0[0-9]{9,10}$");
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}\\s'-]{2,100}$");
    private static final Pattern URL_PATTERN = Pattern.compile("^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})[/\\w .-]*/?$", Pattern.CASE_INSENSITIVE);

    
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("user") != null;
    }

    
    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
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

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

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

    public static boolean isValidPhoneNumber(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidFullName(String fullName) {
        return fullName != null && fullName.trim().length() >= 2 && NAME_PATTERN.matcher(fullName).matches();
    }

    public static boolean isValidAddress(String address) {
        return address != null && !address.trim().isEmpty() && address.length() <= 255;
    }

    public static boolean isValidImageUrl(String url) {
        return url == null || URL_PATTERN.matcher(url).matches();
    }

    public static boolean isValidOAuthProvider(String provider) {
        return "GOOGLE".equalsIgnoreCase(provider) || "FACEBOOK".equalsIgnoreCase(provider);
    }

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
} 
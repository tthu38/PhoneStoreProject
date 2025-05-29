package utils;

public class GoogleConstants {
    // Client ID từ Google Cloud Console
    public static String GOOGLE_CLIENT_ID = "";
    
    // Client Secret từ Google Cloud Console
    public static String GOOGLE_CLIENT_SECRET = "";
    
    // URL callback - phải khớp với URL đã đăng ký trong Google Cloud Console
    public static String GOOGLE_REDIRECT_URI = "http://localhost:8080/PhoneStore/login-google";
    
    // Các URL và thông số khác - KHÔNG THAY ĐỔI
    public static String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";
    public static String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=";
    public static String GOOGLE_GRANT_TYPE = "authorization_code";
} 
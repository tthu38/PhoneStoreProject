package Payment;

public class PaypalConfig {
    public static final String PAYPAL_URL = "https://www.sandbox.paypal.com/cgi-bin/webscr";
    public static final String BUSINESS_EMAIL = "nhatnam@business.example.com"; // Thay bằng email sandbox của bạn
    public static final String RETURN_PATH = "/paypal/success";
    public static final String CANCEL_PATH = "/paypal/cancel";
    public static final String CURRENCY_CODE = "USD";
    public static final double EXCHANGE_RATE = 24000.0; // 1 USD = 24,000 VND
} 
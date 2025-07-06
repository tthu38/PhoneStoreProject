package service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import model.OrderDetails;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.Order;
import model.User;

public class MailService {

//    private static final String EMAIL_SENDER = WebConfigLoader.getProperty("email.sender");
//    private static final String EMAIL_PASSWORD = WebConfigLoader.getProperty("email.password");
    private static final String EMAIL_SENDER = "de180497luongdanghoangluu@gmail.com";
    private static final String EMAIL_PASSWORD = "lryj mowk yoml wvhw";
    private static final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    ProductVariantService productVariantService = new ProductVariantService();

    public String generateOTP(String email) {
        String otp = String.format("%04d", new Random().nextInt(10000)); // Tạo mã 4 số

        otpStore.put(email, otp); // Lưu OTP tạm thời

        // Tự động xóa sau 30 giây
        scheduler.schedule(() -> otpStore.remove(email), 30, TimeUnit.SECONDS);

        return otp;
    }

    public boolean verifyOTP(String email, String userInputOtp) {
        String validOtp = otpStore.get(email);
        return validOtp != null && validOtp.equals(userInputOtp);
    }

    public boolean sendOtpForRegister(String recipientEmail, String otp) {
    try {
        String subject = "🔐 Mã OTP đăng ký tài khoản - THEGIOICONGNGHE.COM";
        String htmlContent = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;border-radius:12px;
                        border:1px solid #ddd;overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,0.1);">
                <div style="background:#b71c1c;padding:20px;text-align:center;">
                    <h2 style="color:#fff;margin:0;font-size:24px;">THEGIOICONGNGHE.COM</h2>
                    <p style="color:#fbc02d;margin:8px 0 0;font-weight:bold;">Xác minh tài khoản</p>
                </div>
                <div style="padding:30px 20px;background:#fff;">
                    <p style="font-size:16px;color:#333;">Chào bạn,</p>
                    <p style="font-size:16px;color:#333;">Mã OTP của bạn để <strong>tạo tài khoản</strong> là:</p>
                    <div style="text-align:center;margin:30px 0;">
                        <span style="display:inline-block;background:#ffebee;color:#c62828;
                                     font-size:32px;font-weight:bold;padding:14px 28px;
                                     border-radius:8px;letter-spacing:6px;border:2px dashed #f44336;">
                            %s
                        </span>
                    </div>
                    <p style="font-size:14px;color:#555;text-align:center;">
                        Mã này có hiệu lực trong <strong>5 phút</strong>. Không chia sẻ cho bất kỳ ai.
                    </p>
                    <div style="margin-top:30px;border-top:1px solid #eee;padding-top:15px;">
                        <p style="font-size:12px;color:#888;text-align:center;">
                            Đây là email tự động từ <strong>THEGIOICONGNGHE.COM</strong>.<br>
                            Vui lòng không trả lời email này.
                        </p>
                    </div>
                </div>
            </div>
            """.formatted(otp);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_SENDER, EMAIL_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_SENDER, "THEGIOICONGNGHE.COM"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

public boolean sendOtpForResetPassword(String recipientEmail, String otp) {
    try {
        String subject = "🔐 Mã OTP khôi phục mật khẩu - THEGIOICONGNGHE.COM";
        String htmlContent = """
            <div style="font-family:Arial,sans-serif;max-width:600px;margin:auto;border-radius:12px;
                        border:1px solid #ddd;overflow:hidden;box-shadow:0 4px 12px rgba(0,0,0,0.1);">
                <div style="background:#b71c1c;padding:20px;text-align:center;">
                    <h2 style="color:#fff;margin:0;font-size:24px;">THEGIOICONGNGHE.COM</h2>
                    <p style="color:#fbc02d;margin:8px 0 0;font-weight:bold;">Xác minh tài khoản</p>
                </div>
                <div style="padding:30px 20px;background:#fff;">
                    <p style="font-size:16px;color:#333;">Chào bạn,</p>
                    <p style="font-size:16px;color:#333;">Mã OTP của bạn để <strong>khôi phục mật khẩu</strong> là:</p>
                    <div style="text-align:center;margin:30px 0;">
                        <span style="display:inline-block;background:#ffebee;color:#c62828;
                                     font-size:32px;font-weight:bold;padding:14px 28px;
                                     border-radius:8px;letter-spacing:6px;border:2px dashed #f44336;">
                            %s
                        </span>
                    </div>
                    <p style="font-size:14px;color:#555;text-align:center;">
                        Mã này có hiệu lực trong <strong>5 phút</strong>. Không chia sẻ cho bất kỳ ai.
                    </p>
                    <div style="margin-top:30px;border-top:1px solid #eee;padding-top:15px;">
                        <p style="font-size:12px;color:#888;text-align:center;">
                            Đây là email tự động từ <strong>THEGIOICONGNGHE.COM</strong>.<br>
                            Vui lòng không trả lời email này.
                        </p>
                    </div>
                </div>
            </div>
            """.formatted(otp);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_SENDER, EMAIL_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EMAIL_SENDER, "THEGIOICONGNGHE.COM"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject(subject);
        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}


    public boolean sendOrderConfirmation(User user, Order order, int orderID, List<OrderDetails> orderDetails, BigDecimal totalAmount) throws UnsupportedEncodingException {
        // Kiểm tra email trước khi gửi
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            System.out.println("Email không hợp lệ: " + user.getEmail());
            return false;
        }

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(EMAIL_SENDER, EMAIL_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL_SENDER, "THEGIOICONGNGHE.COM"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            message.setSubject("Xác nhận đơn hàng #" + orderID);

            // Nội dung email
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("<h2>Chào ").append(user.getFullName()).append(" yêu dấu,</h2>")
                    .append("<p>Cảm ơn bạn đã đặt hàng tại cửa hàng của chúng tôi!</p>")
                    .append("<p><b>Mã đơn hàng: #</b> ").append(orderID).append(".</p>")
                    .append("<p><b>Tổng tiền:</b> ").append(totalAmount).append(" $.</p>")
                    .append("<h3>Chi tiết đơn hàng:</h3>")
                    .append("<table border='1' cellpadding='8' cellspacing='0' style='border-collapse: collapse; width: 100%;'>")
                    .append("<tr style='background-color: #f2f2f2;'>")
                    .append("<th>Ảnh sản phẩm</th><th>Tên sản phẩm</th><th>Số lượng</th><th>Giá</th><th>Tổng</th></tr>");

            for (OrderDetails item : orderDetails) {
                // ProductVariant  product = productVariantService.getProductVariantById(item.getProductVariantID());
                emailContent.append("<tr>")
                        .append("<td><img src='")
                        // .append(product.getImgPath())
                        .append("' alt='Ảnh sản phẩm' width='60' height='60'/></td>")
                        //    .append("<td>").append(product.getName()).append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        //     .append("<td>").append(item.getPrice()).append(" VND</td>")
                        //    .append("<td>").append(item.getSubtotal()).append(" VND</td>")
                        .append("<tr>");
            }

            emailContent.append("</table>")
                    .append("<p>Chúng tôi sẽ xử lý đơn hàng của bạn sớm nhất có thể.</p>")
                    .append("<p>Trân trọng,</p>")
                    .append("<p>Cảm ơn bạn đã mua hàng!</p>")
                    .append("<p><b>Đội ngũ hỗ trợ</b></p>");

            message.setContent(emailContent.toString(), "text/html; charset=UTF-8");

            Transport.send(message);
            return true;  // ✅ Gửi thành công
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // ❌ Gửi thất bại
        }
    }

}

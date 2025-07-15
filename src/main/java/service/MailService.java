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

    private static final String EMAIL_SENDER = WebConfigLoader.getProperty("email.sender");
    private static final String EMAIL_PASSWORD = WebConfigLoader.getProperty("email.password");
//    private static final String EMAIL_SENDER = "de180497luongdanghoangluu@gmail.com";
//    private static final String EMAIL_PASSWORD = "lryj mowk yoml wvhw";
    private static final Map<String, String> otpStore = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    ProductVariantService productVariantService = new ProductVariantService();

    public String generateOTP(String email) {
        String otp = String.format("%04d", new Random().nextInt(10000)); // Tạo mã 4 số

        otpStore.put(email, otp); // Lưu OTP tạm thời

        // Tự động xóa sau 5 phút
        scheduler.schedule(() -> otpStore.remove(email), 300, TimeUnit.SECONDS);
        // Tự động xóa sau 30 giây
        scheduler.schedule(() -> otpStore.remove(email), 60 * 5, TimeUnit.SECONDS);

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
            message.setSubject("🎉 Xác nhận đơn hàng #" + orderID + " - THEGIOICONGNGHE.COM");

            // Tạo nội dung email HTML đẹp hơn
            StringBuilder emailContent = new StringBuilder();
            emailContent.append("""
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; background: #f8f9fa; padding: 20px;">
                    <div style="background: #b71c1c; color: white; padding: 20px; text-align: center; border-radius: 10px 10px 0 0;">
                        <h1 style="margin: 0; font-size: 24px;">THEGIOICONGNGHE.COM</h1>
                        <p style="margin: 5px 0 0; color: #fbc02d; font-weight: bold;">Xác nhận đơn hàng thành công</p>
                    </div>
                    
                    <div style="background: white; padding: 30px; border-radius: 0 0 10px 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);">
                        <p style="font-size: 16px; color: #333; margin-bottom: 20px;">
                            Chào <strong>""").append(user.getFullName()).append("</strong>,<br><br>")
                    .append("Cảm ơn bạn đã đặt hàng tại <strong>THEGIOICONGNGHE.COM</strong>! Đơn hàng của bạn đã được xác nhận và đang được xử lý.")
                    .append("</p>")
                    .append("<div style='background: #e8f5e8; border: 1px solid #4caf50; border-radius: 8px; padding: 15px; margin: 20px 0;'>")
                    .append("<h3 style='color: #2e7d32; margin: 0 0 10px;'>📋 Thông tin đơn hàng</h3>")
                    .append("<p style='margin: 5px 0;'><strong>Mã đơn hàng:</strong> #").append(orderID).append("</p>")
                    .append("<p style='margin: 5px 0;'><strong>Ngày đặt:</strong> ").append(order.getOrderDateFormatted()).append("</p>")
                    .append("<p style='margin: 5px 0;'><strong>Phương thức thanh toán:</strong> ").append(order.getPaymentMethod()).append("</p>")
                    .append("<p style='margin: 5px 0;'><strong>Địa chỉ giao hàng:</strong> ").append(order.getShippingAddress()).append("</p>")
                    .append("<p style='margin: 5px 0;'><strong>Số điện thoại:</strong> ").append(order.getPhoneNumber()).append("</p>")
                    .append("</div>");

            // Thêm chi tiết sản phẩm
            if (orderDetails != null && !orderDetails.isEmpty()) {
                emailContent.append("<h3 style='color: #333; margin: 30px 0 15px;'>🛍️ Chi tiết sản phẩm</h3>")
                        .append("<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>")
                        .append("<thead><tr style='background: #f5f5f5;'>")
                        .append("<th style='padding: 12px; text-align: left; border: 1px solid #ddd;'>Sản phẩm</th>")
                        .append("<th style='padding: 12px; text-align: center; border: 1px solid #ddd;'>Số lượng</th>")
                        .append("<th style='padding: 12px; text-align: right; border: 1px solid #ddd;'>Đơn giá</th>")
                        .append("<th style='padding: 12px; text-align: right; border: 1px solid #ddd;'>Thành tiền</th>")
                        .append("</tr></thead><tbody>");

                for (OrderDetails item : orderDetails) {
                    String productName = item.getProductVariant().getProduct().getName();
                    String variantInfo = item.getProductVariant().getColor() + " - " + item.getProductVariant().getRom() + "GB";
                    BigDecimal unitPrice = item.getDiscountPrice() != null ? item.getDiscountPrice() : item.getUnitPrice();
                    BigDecimal totalPrice = item.getTotalPrice();

                    emailContent.append("<tr>")
                            .append("<td style='padding: 12px; border: 1px solid #ddd;'>")
                            .append("<strong>").append(productName).append("</strong><br>")
                            .append("<small style='color: #666;'>").append(variantInfo).append("</small>")
                            .append("</td>")
                            .append("<td style='padding: 12px; text-align: center; border: 1px solid #ddd;'>").append(item.getQuantity()).append("</td>")
                            .append("<td style='padding: 12px; text-align: right; border: 1px solid #ddd;'>")
                            .append(String.format("%,.0f", unitPrice)).append(" ₫")
                            .append("</td>")
                            .append("<td style='padding: 12px; text-align: right; border: 1px solid #ddd; font-weight: bold;'>")
                            .append(String.format("%,.0f", totalPrice)).append(" ₫")
                            .append("</td>")
                            .append("</tr>");
                }

                emailContent.append("</tbody></table>");
            }

            // Thêm tổng tiền
            emailContent.append("<div style='background: #f8f9fa; border-radius: 8px; padding: 20px; margin: 20px 0; text-align: right;'>")
                    .append("<h3 style='color: #333; margin: 0 0 10px;'>💰 Tổng thanh toán</h3>")
                    .append("<p style='font-size: 24px; font-weight: bold; color: #b71c1c; margin: 0;'>")
                    .append(String.format("%,.0f", totalAmount)).append(" ₫")
                    .append("</p>")
                    .append("</div>");

            // Thêm thông tin bổ sung
            emailContent.append("<div style='background: #fff3cd; border: 1px solid #ffeaa7; border-radius: 8px; padding: 15px; margin: 20px 0;'>")
                    .append("<h4 style='color: #856404; margin: 0 0 10px;'>📢 Thông tin bổ sung</h4>")
                    .append("<ul style='margin: 0; padding-left: 20px; color: #856404;'>")
                    .append("<li>Đơn hàng sẽ được xử lý trong vòng 24-48 giờ</li>")
                    .append("<li>Bạn sẽ nhận được thông báo khi đơn hàng được giao</li>")
                    .append("<li>Nếu có thắc mắc, vui lòng liên hệ: support@thegioicongnghe.com</li>")
                    .append("</ul>")
                    .append("</div>");

            // Footer
            emailContent.append("<div style='text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;'>")
                    .append("<p style='color: #666; font-size: 14px; margin: 0;'>")
                    .append("Trân trọng,<br>")
                    .append("<strong>Đội ngũ THEGIOICONGNGHE.COM</strong><br>")
                    .append("📧 support@thegioicongnghe.com | 📞 1900-xxxx")
                    .append("</p>")
                    .append("</div>")
                    .append("</div></div>");

            message.setContent(emailContent.toString(), "text/html; charset=UTF-8");

            Transport.send(message);
            return true;  // ✅ Gửi thành công
        } catch (MessagingException e) {
            e.printStackTrace();
            return false; // ❌ Gửi thất bại
        }
    }

}

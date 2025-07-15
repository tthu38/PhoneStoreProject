package utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class InteractionLogger {
    private static final String INTERACTION_API_URL = "http://localhost:5555/api/interaction";

    public static void logInteraction(int userId, int productId, String interactionType) {
        HttpURLConnection conn = null;
        try {
            // Tạo JSON payload
            String jsonPayload = String.format(
                "{\"user_id\":%d,\"product_id\":%d,\"interaction_type\":\"%s\"}",
                userId, productId, interactionType
            );

            // Mở kết nối
            URL url = new URL(INTERACTION_API_URL);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            // Gửi dữ liệu
            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            // Xử lý phản hồi
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String errorResponse = br.lines().collect(Collectors.joining());
                    System.err.printf("❌ Gọi API thất bại: [%d] %s%n", responseCode, errorResponse);
                }
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String response = br.lines().collect(Collectors.joining());
                    System.out.printf("✅ Ghi nhận tương tác [%s]: user=%d, product=%d%n", interactionType, userId, productId);
                }
            }

        } catch (Exception e) {
            System.err.printf("❌ Lỗi gọi API: user=%d, product=%d, type=%s, error=%s%n",
                    userId, productId, interactionType, e.getMessage());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}

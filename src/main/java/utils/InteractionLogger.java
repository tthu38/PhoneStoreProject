package utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class InteractionLogger {
    private static final String INTERACTION_API_URL = "http://localhost:5555/api/interaction";

    public static void logInteraction(int userId, int productId, String interactionType) {
        try {
            String jsonPayload = String.format(
                "{\"user_id\":%d,\"product_id\":%d,\"interaction_type\":\"%s\"}",
                userId, productId, interactionType
            );

            URL url = new URL(INTERACTION_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String errorResponse = br.lines().collect(Collectors.joining());
                    System.out.println("Lỗi API /api/interaction: Code=" + responseCode + ", Response=" + errorResponse);
                }
            } else {
                System.out.println("Tương tác " + interactionType + " đã lưu: UserID=" + userId + ", ProductID=" + productId);
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Lỗi gọi API /api/interaction: UserID=" + userId + ", ProductID=" + productId + 
                               ", Type=" + interactionType + ", Error=" + e.getMessage());
        }
    }
}

package utils;

import com.fasterxml.jackson.databind.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class ProductRecommendationClient {
    public static List<Map<String, Object>> getRecommendations(int userId) {
        List<Map<String, Object>> productList = new ArrayList<>();
        try {
            URL url = new URL("http://localhost:5000/recommend/" + userId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream inputStream = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            productList = mapper.readValue(inputStream, List.class);

        } catch (Exception e) {
            System.out.println("❌ Không lấy được gợi ý sản phẩm: " + e.getMessage());
        }

        return productList;
    }
     public static void callRetrainAPI() {
        try {
            URL url = new URL("http://localhost:5000/retrain");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = "{}".getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("📡 Gọi retrain Flask API → code: " + code);
        } catch (Exception e) {
            System.out.println("❌ Gọi retrain API lỗi: " + e.getMessage());
        }
    }
}

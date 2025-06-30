package utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ProductUtils {

    public static String saveImage(Part filePart, ServletContext context, String defaultFile) throws IOException {
        if (filePart != null && filePart.getSize() > 0
                && filePart.getSubmittedFileName() != null
                && !filePart.getSubmittedFileName().isEmpty()) {

            // Kiểm tra định dạng ảnh
            String contentType = filePart.getContentType();
            if (!contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Chỉ được tải lên tệp hình ảnh.");
            }

            // Lấy tên file và tạo đường dẫn
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String relativePath = "images/" + fileName;
            String absolutePath = context.getRealPath("/") + relativePath;

            File imageDir = new File(context.getRealPath("/images"));
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }

            // Lưu file ảnh vào thư mục
            filePart.write(absolutePath);
            return relativePath;
        }

        return "images/" + defaultFile;
    }
    public static String formatInstantForDateTimeLocal(Instant instant) {
        if (instant == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author ThienThu
 */
public class ProductUtils {
    public static String saveImage(Part filePart, ServletContext context, String defaultFile) throws IOException {
        if (filePart != null && filePart.getSize() > 0
                && filePart.getSubmittedFileName() != null
                && !filePart.getSubmittedFileName().isEmpty()) {

            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String relativePath = "images/" + fileName;
            String absolutePath = context.getRealPath("/") + relativePath;

            File imageDir = new File(context.getRealPath("/images"));
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }

            filePart.write(absolutePath);
            return relativePath;
        }

        return "images/" + defaultFile;
    }
}

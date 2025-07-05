/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import model.User;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author dangt
 */
public class FacebookUtils {
    // Hàm lấy thông tin user từ Facebook access token
    public static User getUserInfo(String accessToken) {
        try {
            String url = "https://graph.facebook.com/me?fields=id,name,email,picture&access_token=" + accessToken;
            String response = sendGet(url);
            JSONObject userJson = new JSONObject(response);

            User user = new User();
            user.setOauthId(userJson.getString("id"));
            user.setFullName(userJson.getString("name"));
            user.setEmail(userJson.has("email") ? userJson.getString("email") : "");
            if (userJson.has("picture")) {
                user.setPicture(userJson.getJSONObject("picture").getJSONObject("data").getString("url"));
            }
            user.setOauthProvider("facebook");
            user.setIsOauthUser(true);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String sendGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder content = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        return content.toString();
    }
}

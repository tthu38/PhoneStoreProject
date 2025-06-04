package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.User;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import java.util.ResourceBundle;

public class GoogleUtils {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("application");

    public static String getToken(final String code) throws Exception {
        System.out.println("client_id: " + bundle.getString("google.client.id"));
        System.out.println("client_secret: " + bundle.getString("google.client.secret"));
        System.out.println("redirect_uri: " + bundle.getString("google.redirect.uri"));
        System.out.println("code: " + code);
        System.out.println("grant_type: " + bundle.getString("google.grant.type"));

        String response = Request.Post(bundle.getString("google.token.url"))
                .bodyForm(Form.form()
                        .add("client_id", bundle.getString("google.client.id"))
                        .add("client_secret", bundle.getString("google.client.secret"))
                        .add("redirect_uri", bundle.getString("google.redirect.uri"))
                        .add("code", code)
                        .add("grant_type", bundle.getString("google.grant.type"))
                        .build())
                .execute().returnContent().asString();

        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    public static User getUserInfo(final String accessToken) throws Exception {
        String link = bundle.getString("google.userinfo.url") + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        
        JsonObject googleUser = new Gson().fromJson(response, JsonObject.class);
        
        User user = new User();
        user.setEmail(googleUser.get("email").getAsString());
        user.setFullName(googleUser.get("name").getAsString());
        user.setPicture(googleUser.get("picture").getAsString());
        user.setIsOauthUser(true);
        user.setOauthProvider("GOOGLE");
        
        return user;
    }

    public static String getConfig(String key) {
        return bundle.getString(key);
    }
}

package utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.User;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;

public class GoogleUtils {
    public static String getToken(String code) throws Exception {
        String response = Request.Post(GoogleConstants.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form()
                        .add("client_id", GoogleConstants.GOOGLE_CLIENT_ID)
                        .add("client_secret", GoogleConstants.GOOGLE_CLIENT_SECRET)
                        .add("redirect_uri", GoogleConstants.GOOGLE_REDIRECT_URI)
                        .add("code", code)
                        .add("grant_type", GoogleConstants.GOOGLE_GRANT_TYPE)
                        .build())
                .execute().returnContent().asString();
        
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    public static User getUserInfo(String accessToken) throws Exception {
        String link = GoogleConstants.GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(link).execute().returnContent().asString();
        Gson gson = new Gson();
        // Parse JSON manually to map Google fields to User fields
        JsonObject json = gson.fromJson(response, JsonObject.class);
        User user = new User();
        user.setGoogleId(json.has("id") ? json.get("id").getAsString() : null);
        user.setEmail(json.has("email") ? json.get("email").getAsString() : null);
        user.setVerifiedEmail(json.has("verified_email") ? json.get("verified_email").getAsBoolean() : false);
        user.setFullname(json.has("name") ? json.get("name").getAsString() : null);
        user.setGivenName(json.has("given_name") ? json.get("given_name").getAsString() : null);
        user.setFamilyName(json.has("family_name") ? json.get("family_name").getAsString() : null);
        user.setGoogleLink(json.has("link") ? json.get("link").getAsString() : null);
        user.setPicture(json.has("picture") ? json.get("picture").getAsString() : null);
        return user;
    }
} 
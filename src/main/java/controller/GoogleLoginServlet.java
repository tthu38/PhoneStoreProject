package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.GoogleConstants;

@WebServlet("/oauth2/google")
public class GoogleLoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String googleLoginUrl = "https://accounts.google.com/o/oauth2/auth?"
                + "client_id=" + GoogleConstants.GOOGLE_CLIENT_ID
                + "&redirect_uri=" + GoogleConstants.GOOGLE_REDIRECT_URI
                + "&response_type=code"
                + "&scope=email%20profile"
                + "&access_type=offline";

        response.sendRedirect(googleLoginUrl);
    }
}

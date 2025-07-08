package controller;

import model.User;
import service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = new UserService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/user/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullname");
        String phoneNumber = request.getParameter("phone");
        String address = request.getParameter("address");

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setPhoneNumber(phoneNumber);

        if (userService.register(user)) {
            service.UserAddressService addressService = new service.UserAddressService();
            model.UserAddress userAddress = new model.UserAddress();
            User createdUser = userService.getUserByEmail(email).orElse(user);
            userAddress.setUser(createdUser);
            userAddress.setFullName(fullName);
            userAddress.setPhoneNumber(phoneNumber);
            userAddress.setAddress(address);
            userAddress.setIsDefault(true);
            userAddress.setIsActive(true);
            addressService.addAddress(userAddress);

            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("error", "Email already exists");
            request.getRequestDispatcher("/user/register.jsp").forward(request, response);
        }
    }
}
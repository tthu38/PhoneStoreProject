package service;

import dao.UserDAO;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class UserService implements IUserService {
    private UserDAO userDAO;

    public UserService() {
        userDAO = new UserDAO();
    }

    @Override
    public boolean register(User user) throws SQLException {
        // Validate user data
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().trim().isEmpty() ||
            user.getFullname() == null || user.getFullname().trim().isEmpty()) {
            return false;
        }

        // Hash password before saving
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        
        // Set default values
        user.setStatus("ACTIVE");
        user.setRole("Customer");
        user.setIsOauthUser(false);
        
        return userDAO.register(user);
    }

    @Override
    public User login(String email, String password) throws SQLException {
        User user = userDAO.findByEmail(email);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        return userDAO.findByEmail(email);
    }

    @Override
    public boolean updatePassword(String email, String newPassword) throws SQLException {
        return userDAO.updatePassword(email, BCrypt.hashpw(newPassword, BCrypt.gensalt()));
    }

    @Override
    public User loginWithGoogle(String googleId, String email, String name, String picture) throws SQLException {
        User user = userDAO.findByEmail(email);
        
        if (user == null) {
            // Create new user for Google login
            user = new User();
            user.setEmail(email);
            user.setFullname(name);
            user.setGoogleId(googleId);
            user.setPicture(picture);
            user.setStatus("ACTIVE");
            user.setRole("Customer");
            user.setIsOauthUser(true);
            user.setOauthProvider("GOOGLE");
            user.setVerifiedEmail(true);
            
            if (!userDAO.register(user)) {
                return null;
            }
        } else {
            // Update existing user's Google info
            user.setGoogleId(googleId);
            user.setPicture(picture);
            user.setIsOauthUser(true);
            user.setOauthProvider("GOOGLE");
            userDAO.updateUser(user);
        }
        
        return user;
    }
} 
package service;

import dao.UserDAO;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.SQLException;

public class UserService implements IUserService {
    private UserDAO userDAO;
    private static final int CUSTOMER_ROLE_ID = 2;  

    public UserService() {
        userDAO = new UserDAO();
    }

    @Override
    public boolean register(User user) throws SQLException {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty() ||
            user.getPassword() == null || user.getPassword().trim().isEmpty() ||
            user.getFullname() == null || user.getFullname().trim().isEmpty()) {
            return false;
        }

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        
        user.setIsActive(true);
        user.setRoleId(CUSTOMER_ROLE_ID); 
        user.setIsOauthUser(false);
        
        return userDAO.register(user);
    }

    @Override
    public User login(String email, String password) throws SQLException {
        User user = userDAO.findByEmail(email);
        if (user != null && user.isActive() && BCrypt.checkpw(password, user.getPassword())) {
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
            user = new User();
            user.setEmail(email);
            user.setFullname(name);
            user.setGoogleId(googleId);
            user.setPicture(picture);
            user.setIsActive(true);
            user.setRoleId(CUSTOMER_ROLE_ID); 
            user.setIsOauthUser(true);
            user.setOauthProvider("GOOGLE");
            user.setVerifiedEmail(true);
            
            if (!userDAO.register(user)) {
                return null;
            }
        } else {
            user.setGoogleId(googleId);
            user.setPicture(picture);
            user.setIsOauthUser(true);
            user.setOauthProvider("GOOGLE");
            userDAO.updateUser(user);
        }
        
        return user.isActive() ? user : null;
    }
} 
package dao;

import model.User;
import utils.DBUtils;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean register(User user) throws SQLException {
        String sql = "INSERT INTO Users (Email, Password, FullName, PhoneNumber, Status, Role, IsOauthUser, OauthProvider, GoogleId, Picture, VerifiedEmail) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // Check if email already exists
            if (findByEmail(user.getEmail()) != null) {
                return false;
            }
            
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullname());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getStatus());
            ps.setString(6, user.getRole());
            ps.setBoolean(7, user.getIsOauthUser());
            ps.setString(8, user.getOauthProvider());
            ps.setString(9, user.getGoogleId());
            ps.setString(10, user.getPicture());
            ps.setBoolean(11, user.isVerifiedEmail());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    public User login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM Users WHERE email = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapUser(rs);
            }
        }
        return null;
    }
    
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM Users WHERE email = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                return mapUser(rs);
            }
        }
        return null;
    }
    
    public boolean updatePassword(String email, String newPassword) throws SQLException {
        String sql = "UPDATE Users SET Password = ? WHERE email = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, newPassword);
            ps.setString(2, email);
            
            return ps.executeUpdate() > 0;
        }
    }

    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE Users SET FullName = ?, PhoneNumber = ?, Status = ?, Role = ?, " +
                    "IsOauthUser = ?, OauthProvider = ?, GoogleId = ?, Picture = ?, VerifiedEmail = ? " +
                    "WHERE Email = ?";
        
        try (Connection conn = DBUtils.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getFullname());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getStatus());
            ps.setString(4, user.getRole());
            ps.setBoolean(5, user.getIsOauthUser());
            ps.setString(6, user.getOauthProvider());
            ps.setString(7, user.getGoogleId());
            ps.setString(8, user.getPicture());
            ps.setBoolean(9, user.isVerifiedEmail());
            ps.setString(10, user.getEmail());
            
            return ps.executeUpdate() > 0;
        }
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("UserID"));
        user.setEmail(rs.getString("Email"));
        user.setPassword(rs.getString("Password"));
        user.setFullname(rs.getString("FullName"));
        user.setPhone(rs.getString("PhoneNumber"));
        user.setStatus(rs.getString("Status"));
        user.setRole(rs.getString("Role"));
        user.setIsOauthUser(rs.getBoolean("IsOauthUser"));
        user.setOauthProvider(rs.getString("OauthProvider"));
        user.setGoogleId(rs.getString("GoogleId"));
        user.setPicture(rs.getString("Picture"));
        user.setVerifiedEmail(rs.getBoolean("VerifiedEmail"));
        return user;
    }
} 
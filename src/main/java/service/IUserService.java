/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import model.User;
import java.sql.SQLException;

public interface IUserService {
    boolean register(User user) throws SQLException;
    User login(String email, String password) throws SQLException;
    User findByEmail(String email) throws SQLException;
    boolean updatePassword(String email, String newPassword) throws SQLException;
    User loginWithGoogle(String googleId, String email, String name, String picture) throws SQLException;
}

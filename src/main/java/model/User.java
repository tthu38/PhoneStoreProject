/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * @author ThienThu
 */
@Entity
@Table(name = "Users")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.userID = :userID"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByIsActive", query = "SELECT u FROM User u WHERE u.isActive = :isActive"),
    @NamedQuery(name = "User.findByRoleID", query = "SELECT u FROM User u WHERE u.roleID = :roleID"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.fullName LIKE :name"),
    @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "User.findByRememberToken", query = "SELECT u FROM User u WHERE u.rememberToken = :token AND u.isActive = true")
})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userID;

    @Column(name = "Email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "Password", length = 255)
    private String password;

    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;

    @Column(name = "Picture", length = 255)
    private String picture;

    @Column(name = "RoleID", nullable = false)
    private int roleID;

    @Column(name = "IsOauthUser")
    private Boolean isOauthUser = false;

    @OneToMany(mappedBy = "user")
    private List<UserAddress> addresses;

    @Column(name = "OauthProvider", length = 50)
    private String oauthProvider;

    @Column(name = "IsActive")
    private Boolean isActive = true;

    @Column(name = "RememberToken", length = 255)
    private String rememberToken;

    @Column(name = "CreatedAt")
    private Instant createdAt;

    public User() {}

    // Getters and setters
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public int getRoleID() { return roleID; }
    public void setRoleID(int roleID) { this.roleID = roleID; }

    public Boolean getIsOauthUser() { return isOauthUser; }
    public void setIsOauthUser(Boolean isOauthUser) { this.isOauthUser = isOauthUser; }

    public List<UserAddress> getAddresses() { return addresses; }
    public void setAddresses(List<UserAddress> addresses) { this.addresses = addresses; }

    public String getOauthProvider() { return oauthProvider; }
    public void setOauthProvider(String oauthProvider) { this.oauthProvider = oauthProvider; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public String getRememberToken() { return rememberToken; }
    public void setRememberToken(String rememberToken) { this.rememberToken = rememberToken; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

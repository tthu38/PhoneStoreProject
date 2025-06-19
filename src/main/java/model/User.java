/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 *
 * @author ThienThu
 */
@Entity
@Table(name = "Users")
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
    @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
    @NamedQuery(name = "User.findByIsActive", query = "SELECT u FROM User u WHERE u.isActive = :isActive"),
    @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role"),
    @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.fullName LIKE :name"),
    @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber"),
    @NamedQuery(name = "User.findByRememberToken", query = "SELECT u FROM User u WHERE u.rememberToken = :token AND u.isActive = true")
})
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private Long id;

    @Column(name = "Email", nullable = false, length = 100, unique = true)
    private String email;

    @Column(name = "Password", length = 255)
    private String password;

    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "PhoneNumber", length = 20)
    private String phoneNumber;

    @Column(name = "Role", length = 50)
    private String role = "CUSTOMER";

    @Column(name = "RememberToken", length = 255)
    private String rememberToken;

    @Column(name = "Picture", length = 255)
    private String picture;

    @Column(name = "IsOauthUser")
    private Boolean isOauthUser = false;

    @Column(name = "OauthProvider", length = 50)
    private String oauthProvider;

    @Column(name = "IsActive")
    private Boolean isActive = true;

    @Column(name = "CreatedAt")
    private Instant createdAt;

    private Integer userID;

    public User() {}

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getRememberToken() { return rememberToken; }
    public void setRememberToken(String rememberToken) { this.rememberToken = rememberToken; }

    public String getPicture() { return picture; }
    public void setPicture(String picture) { this.picture = picture; }

    public Boolean getIsOauthUser() { return isOauthUser; }
    public void setIsOauthUser(Boolean isOauthUser) { this.isOauthUser = isOauthUser; }

    public String getOauthProvider() { return oauthProvider; }
    public void setOauthProvider(String oauthProvider) { this.oauthProvider = oauthProvider; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Integer getUserID() {
        return userID;
    }
}

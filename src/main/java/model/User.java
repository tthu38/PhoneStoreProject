/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.fluent.Request;
import java.util.ResourceBundle;

/**
 *
 * @author ThienThu
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email LIKE :email"),
        @NamedQuery(name = "User.findByIsActive", query = "SELECT u FROM User u WHERE u.isActive = :isActive"),
        @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.roleId = :roleId"),
        @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.fullname LIKE :name"),
        @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phone = :phone"),
        @NamedQuery(name = "User.findByRememberToken",
                query = "SELECT u FROM User u WHERE u.rememberToken = :token AND u.isActive = true")
})
@Table(name = "Users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID", nullable = false)
    private Long id;
    
    @Nationalized
    @Column(name = "FullName", nullable = false, length = 100)
    private String fullname;
    
    @Nationalized
    @Column(name = "Email", nullable = false, length = 100)
    private String email;
    
    @Column(name = "Password")
    private String password;
    
    @Nationalized
    @Column(name = "PhoneNumber", length = 15)
    private String phone;
    
    @Nationalized
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAddress> address;
    
    @Column(name = "IsActive", nullable = false)
    private Boolean isActive = true;
    
    @ColumnDefault("getdate()")
    @Column(name = "CreatedAt")
    private Instant createDate;
    
    @Nationalized
    @Column(name = "RoleID", nullable = false)
    private Integer roleId;// đỏi thành string 
    
    @Column(name = "RememberToken")
    private String rememberToken;
    
    @Column(name = "Picture")
    private String picture;
    
    @Column(name = "IsOauthUser")
    private Boolean isOauthUser = false;
    
    @Column(name = "OauthProvider", length = 50)
    private String oauthProvider;

    @Column(name = "GoogleId")
    private String googleId;

    @Column(name = "VerifiedEmail")
    private boolean verifiedEmail;

    @Column(name = "GivenName")
    private String givenName;

    @Column(name = "FamilyName")
    private String familyName;

    @Column(name = "GoogleLink")
    private String googleLink;

    public User() {
    }

    public User(String email, String password, String fullName, String phoneNumber, List<UserAddress> address) {
        this.email = email;
        this.password = password;
        this.fullname = fullName;
        this.phone = phoneNumber;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<UserAddress> getAddress() {
        return address;
    }

    public void setAddress(List<UserAddress> address) {
        this.address = address;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Boolean getIsOauthUser() {
        return isOauthUser;
    }

    public void setIsOauthUser(Boolean isOauthUser) {
        this.isOauthUser = isOauthUser;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public void setOauthProvider(String oauthProvider) {
        this.oauthProvider = oauthProvider;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public boolean isVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGoogleLink() {
        return googleLink;
    }

    public void setGoogleLink(String googleLink) {
        this.googleLink = googleLink;
    }

    public boolean isActive() {
        return isActive != null && isActive;
    }

    public boolean isAdmin() {
        return roleId != null && roleId == 1; // Giả sử Admin có ID = 1
    }

    public boolean isCustomer() {
        return roleId != null && roleId == 2; // Giả sử Customer có ID = 2
    }

}
         
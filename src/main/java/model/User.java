/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

/**
 *
 * @author ThienThu
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
        @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email LIKE :email"),
        @NamedQuery(name = "User.findByStatus", query = "SELECT u FROM User u WHERE u.status = :status"),
        @NamedQuery(name = "User.findByRole", query = "SELECT u FROM User u WHERE u.role = :role"),
        @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name LIKE :name"),
        @NamedQuery(name = "User.findByPhone", query = "SELECT u FROM User u WHERE u.phone = :phone"),
        @NamedQuery(name = "User.findByRememberToken",
                query = "SELECT u FROM User u WHERE u.rememberToken = :token AND u.status = 'ACTIVE'")
})
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID", nullable = false)
    private int id;
    
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
    
    @ColumnDefault("'ACTIVE'")
    @Column(name = "Status", nullable = false, length = 15)
    private String status;
    
    @ColumnDefault("getdate()")
    @Column(name = "CreateAt")
    private Instant createDate;
    
    @Nationalized
    @ColumnDefault("'Customer'")
    @Column(name = "Role", length = 15)
    private String role;
    
    @Column(name = "RememberToken")
    private String rememberToken;
    
    @Column(name = "Picture")
    private String picture;
    
    @ColumnDefault("0")
    @Column(name = "is_oauth_user")
    private Boolean isOauthUser;
    
    @Column(name = "oauth_provider", length = 50)
    private String oauthProvider;

    // Google OAuth specific fields
    @Column(name = "google_id")
    private String googleId;

    @Column(name = "verified_email")
    private boolean verifiedEmail;

    @Column(name = "given_name")
    private String givenName;

    @Column(name = "family_name")
    private String familyName;

    @Column(name = "google_link")
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
}

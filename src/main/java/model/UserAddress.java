/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

/**
 *
 * @author ThienThu
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "UserAddress.findByUserId",
        query = "SELECT ua FROM UserAddress ua WHERE ua.user.userID = :userId"
    ),
    @NamedQuery(
        name = "UserAddress.findDefaultByUserId",
        query = "SELECT ua FROM UserAddress ua WHERE ua.user.userID = :userId AND ua.isDefault = true"
    ),
    @NamedQuery(
        name = "UserAddress.findActiveByUserId",
        query = "SELECT ua FROM UserAddress ua WHERE ua.user.userID = :userId AND ua.isActive = true"
    )
})

@Table(name = "UserAddresses")
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer addressID;
    
    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private User user;
    
    @Nationalized
    @Column(nullable = false, length = 255)
    private String fullName;
    
    @Column(nullable = false, length = 20)
    private String phoneNumber;
        
    
    @Column(nullable = false, length = 500)
    private String address;

    @Column(nullable = false)
    private Boolean isDefault = false;

    @Column(nullable = false)
    private Boolean isActive = true;
    
    @ColumnDefault("getdate()")
    @Column(name = "CreatedAt")
    private Instant createAt;
    
    @Column(name = "UpdatedAt")
    private LocalDateTime updateAt;

    public Integer getAddressID() {
        return addressID;
    }

    public void setAddressID(Integer addressID) {
        this.addressID = addressID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
    
    
    
}

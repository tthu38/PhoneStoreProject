/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

/**
 *
 * @author ThienThu
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Warehouse.findAll",query = "SELECT w FROM Warehouse w WHERE w.isActive = true"
    ),
    @NamedQuery(name = "Warehouse.findWarehouseId",query = "SELECT w FROM Warehouse w WHERE w.id = :id"
    ),
    @NamedQuery(name = "Warehouse.findWarehouseName",query = "SELECT w FROM Warehouse w WHERE w.name LIKE :name AND w.isActive = true")
})
@Table(name = "Warehouses")
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WarehouseID")
    private int id;
    
    @Nationalized
    @Column(name = "WarehouseName", nullable = false, length = 100)
    private String name;
    
    @Nationalized
    @Lob
    @Column(name = "Address")
    private String address;
    
    @Column(name = "PhoneNumber", nullable = false, length = 20)
    private String phoneNumber;
    
    @ColumnDefault("True")
    @Column(name = "IsActive")
    private Boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    
}

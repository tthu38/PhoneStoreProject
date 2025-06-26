/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.Basic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;



/**
 *
 * @author ThienThu
 */
@Entity
@Table(name = "Inventory")
@NamedQueries({
    @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i"),
    @NamedQuery(name = "Inventory.findByInventoryID", query = "SELECT i FROM Inventory i WHERE i.inventoryID = :inventoryID"),
    @NamedQuery(name = "Inventory.findByStoreLocation", query = "SELECT i FROM Inventory i WHERE i.storeLocation = :storeLocation")})
public class Inventory{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "InventoryID")
    private Integer inventoryID;
    
    @Nationalized
    @ColumnDefault("'Main WareHouse'")
    @Basic(optional = false)
    @Column(name = "StoreLocation")
    private String storeLocation;

    public Inventory() {
    }

    public Inventory(Integer inventoryID) {
        this.inventoryID = inventoryID;
    }

    public Inventory(Integer inventoryID, String storeLocation) {
        this.inventoryID = inventoryID;
        this.storeLocation = storeLocation;
    }

    public Integer getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Integer inventoryID) {
        this.inventoryID = inventoryID;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }


    @Override
    public String toString() {
        return "model.Inventory[ inventoryID=" + inventoryID + " ]";
    }
    
}

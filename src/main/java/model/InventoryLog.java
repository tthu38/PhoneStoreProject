/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.time.Instant;
import org.hibernate.annotations.ColumnDefault;

/**
 *
 * @author ThienThu
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "InventoryLog.findAll", query = "SELECT i FROM InventoryLog i"),
    @NamedQuery(name = "InventoryLog.findByLogId", query = "SELECT i FROM InventoryLog i WHERE i.id = :id"),
    @NamedQuery(name = "InventoryLog.findByWarehouse", query = "SELECT i FROM InventoryLog i WHERE i.warehouse.id = :warehouseId"),
    @NamedQuery(name = "InventoryLog.findByVariant", query = "SELECT i FROM InventoryLog i WHERE i.variant.id = :variantId"),
})

@Table(name = "InventoryLogs")

public class InventoryLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogID")
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WarehouseID", nullable = false)
    private Warehouse warehouse;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "VariantID", nullable = false)
    private ProductVariant variant;

    @Column(name = "ChangeType", nullable = false, length = 20)
    private String changeType;
    
    @Column(name = "Quantity", nullable = false)
    private int quantity;
    
    @Column(name = "PreviousQuantity", nullable = false)
    private int previousQuantity;

    @Column(name = "NewQuantity", nullable = false)
    private int newQuantity;
    
    @ColumnDefault("getdate()")
    @Column(name = "CreatedAt")
    private Instant createAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPreviousQuantity() {
        return previousQuantity;
    }

    public void setPreviousQuantity(int previousQuantity) {
        this.previousQuantity = previousQuantity;
    }

    public int getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(int newQuantity) {
        this.newQuantity = newQuantity;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }
    
    
    
}

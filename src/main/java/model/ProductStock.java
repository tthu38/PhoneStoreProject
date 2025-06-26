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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;


/**
 *
 * @author ThienThu
 */
@Entity
@Table(name = "ProductStock")
@NamedQueries({
    @NamedQuery(name = "ProductStock.findAll", query = "SELECT p FROM ProductStock p"),
    @NamedQuery(name = "ProductStock.findById", query = "SELECT p FROM ProductStock p WHERE p.id = :id"),
    @NamedQuery(name = "ProductStock.findByAmount", query = "SELECT p FROM ProductStock p WHERE p.amount = :amount")})
public class ProductStock {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "Amount")
    private int amount;
    @JoinColumn(name = "InventoryID", referencedColumnName = "InventoryID")
    @ManyToOne(optional = false)
    private Inventory inventoryID;
    @ManyToOne(optional = false)
    @JoinColumn(name = "VariantID", referencedColumnName = "VariantID")
    private ProductVariant variant;

    public ProductStock() {
    }

    public ProductStock(Integer id) {
        this.id = id;
    }

    public ProductStock(Integer id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Inventory getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Inventory inventoryID) {
        this.inventoryID = inventoryID;
    }

    public ProductVariant getVariant() {
        return variant;
    }

    public void setVariant(ProductVariant variant) {
        this.variant = variant;
    }

    
    @Override
    public String toString() {
        return "model.ProductStock[ id=" + id + " ]";
    }
    
}

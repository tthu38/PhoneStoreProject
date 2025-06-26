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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Date;

/**
 *
 * @author ThienThu
 */
@Entity
@Table(name = "InventoryLog")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "InventoryLog.findAll", query = "SELECT i FROM InventoryLog i"),
    @NamedQuery(name = "InventoryLog.findByLogID", query = "SELECT i FROM InventoryLog i WHERE i.logID = :logID"),
    @NamedQuery(name = "InventoryLog.findByActionType", query = "SELECT i FROM InventoryLog i WHERE i.actionType = :actionType"),
    @NamedQuery(name = "InventoryLog.findByQuantityChanged", query = "SELECT i FROM InventoryLog i WHERE i.quantityChanged = :quantityChanged"),
    @NamedQuery(name = "InventoryLog.findByActionDate", query = "SELECT i FROM InventoryLog i WHERE i.actionDate = :actionDate"),
    @NamedQuery(name = "InventoryLog.findByNote", query = "SELECT i FROM InventoryLog i WHERE i.note = :note")
})
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "LogID")
    private Integer logID;

    @Basic(optional = false)
    @Column(name = "ActionType")
    private String actionType;

    @Basic(optional = false)
    @Column(name = "QuantityChanged")
    private int quantityChanged;

    @Column(name = "ActionDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actionDate;

    @Column(name = "Note")
    private String note;

    @ManyToOne(optional = false)
    @JoinColumn(name = "InventoryID", referencedColumnName = "InventoryID")
    private Inventory inventoryID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "VariantID", referencedColumnName = "VariantID")
    private ProductVariant variantID;

    public InventoryLog() {
    }

    public Integer getLogID() {
        return logID;
    }

    public void setLogID(Integer logID) {
        this.logID = logID;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getQuantityChanged() {
        return quantityChanged;
    }

    public void setQuantityChanged(int quantityChanged) {
        this.quantityChanged = quantityChanged;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Inventory getInventoryID() {
        return inventoryID;
    }

    public void setInventoryID(Inventory inventoryID) {
        this.inventoryID = inventoryID;
    }

    public ProductVariant getVariantID() {
        return variantID;
    }

    public void setVariantID(ProductVariant variantID) {
        this.variantID = variantID;
    }
    

    @Override
    public String toString() {
        return "model.InventoryLog[ logID=" + logID + " ]";
    }

}

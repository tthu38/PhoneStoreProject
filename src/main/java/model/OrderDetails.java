/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;



/**
 *
 * @author ThienThu
 */

@Entity
@NamedQueries({
    @NamedQuery(name = "OrderDetails.findAll", query = "SELECT o FROM OrderDetails o"),
    @NamedQuery(name = "OrderDetails.findByOrderDetailID", query = "SELECT o FROM OrderDetails o WHERE o.id = :orderDetailID"),
    @NamedQuery(name = "OrderDetails.findByQuantity", query = "SELECT o FROM OrderDetails o WHERE o.quantity = :quantity"),
    @NamedQuery(name = "OrderDetails.findByUnitPrice", query = "SELECT o FROM OrderDetails o WHERE o.unitPrice = :unitPrice"),
    @NamedQuery(name = "OrderDetails.findByProductVariantID", query = "SELECT o FROM OrderDetails o WHERE o.productVariantID.id = :productVariantID"),
    @NamedQuery(name = "OrderDetails.listWithOffset", query = "SELECT o FROM OrderDetails o ORDER BY o.id")
})

@Table(name = "OrderDetails")
public class OrderDetails {
    @Id
    @Column(name = "OrderDetailID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động tăng ID
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "OrderID", nullable = false)
    private Order orderID;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductVariantID")
    private ProductVariant productVariantID;
    
    @Column(name = "Quantity", nullable = false)
    private Integer quantity;
    
    @Column(name = "UnitPrice", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(name = "TotalPrice", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Order getOrderID() {
        return orderID;
    }

    public void setOrderID(Order orderID) {
        this.orderID = orderID;
    }

    public ProductVariant getProductVariantID() {
        return productVariantID;
    }

    public void setProductVariantID(ProductVariant productVariantID) {
        this.productVariantID = productVariantID;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    
}

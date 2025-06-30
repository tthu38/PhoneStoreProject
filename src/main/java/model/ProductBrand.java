/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;

/**
 *
 * @author ThienThu
 */
@Entity
@Table(name = "Brands")
@NamedQueries({
    @NamedQuery(
        name = "ProductBrand.findAll",
        query = "SELECT b FROM ProductBrand b WHERE b.isActive = true"
    ),
    @NamedQuery(
        name = "ProductBrand.findById",
        query = "SELECT b FROM ProductBrand b WHERE b.id = :id"
    )
})
public class ProductBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BrandID")
    private int id;
    
    @Column(name = "BrandName", nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(name = "IsActive", nullable = false)
    private boolean isActive = true;

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

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    
}

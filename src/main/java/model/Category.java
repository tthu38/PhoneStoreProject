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
    @NamedQuery(name = "Product.findAll",query = "SELECT p FROM Product p WHERE p.isDeleted = false"),
    @NamedQuery(name = "Product.findByCategoryId",query = "SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.isDeleted = false"),
    @NamedQuery(name = "Product.findByName",query = "SELECT p FROM Product p WHERE p.name LIKE :name AND p.isDeleted = false")
})
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CategoryID", nullable = false)
    private int id;

    @Nationalized
    @Column(name = "CategoryName", nullable = false, length = 100)
    private String name;

    @OneToMany(mappedBy = "categories")
    private List<Product> products;
    
    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}

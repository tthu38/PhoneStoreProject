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
    @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name LIKE :name AND p.isDeleted = false"),
    @NamedQuery(name = "Product.findByBrandId",
                query = "SELECT p FROM Product p WHERE p.brand.id = :brandId AND p.isDeleted = false"),
    @NamedQuery(name = "Product.findByCategoryId",
                query = "SELECT p FROM Product p JOIN p.categories c WHERE c.id = :categoryId AND p.isDeleted = false"),
    @NamedQuery(name = "Product.findActiveByBrandName",
                query = "SELECT p FROM Product p WHERE p.brand.name = :brandName AND p.isDeleted = false"
    )
})
@Table(name = "ProductBase")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductBaseID", nullable = false)
    private int id;

    @Nationalized
    @Column(name = "Name", nullable = false, length = 200)
    private String name;

    @OneToMany
    @JoinTable(
            name = "ProductsCategories",
            joinColumns = @JoinColumn(name = "ProductBaseID"),
            inverseJoinColumns = @JoinColumn(name = "categoryID")
    )
    private List<Category> categories;

    @ManyToOne
    @JoinColumn(name = "BrandID", nullable = false)
    private ProductBrand brand;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @Nationalized
    @Lob
    @Column(name = "Specifications")
    private String specifications;

    @ColumnDefault("0")
    @Column(name = "IsDeleted", nullable = false)
    private boolean isDeleted;

    @ColumnDefault("getdate()")
    @Column(name = "CreatedAt")
    private Instant createAt;

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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public ProductBrand getBrand() {
        return brand;
    }

    public void setBrand(ProductBrand brand) {
        this.brand = brand;
    }
    
    


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public boolean isIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }
}

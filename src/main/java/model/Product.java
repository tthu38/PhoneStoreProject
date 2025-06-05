package model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Date;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@Entity
@Table(name = "ProductBase")
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p WHERE p.isDeleted = false"),
    @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name LIKE :name AND p.isDeleted = false"),
    @NamedQuery(name = "Product.findByBrandId", query = "SELECT p FROM Product p WHERE p.brand.id = :brandId AND p.isDeleted = false"),
    @NamedQuery(name = "Product.findByCategoryId", query = "SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.isDeleted = false"),
    @NamedQuery(name = "Product.findActiveByBrandName", query = "SELECT p FROM Product p WHERE p.brand.name = :brandName AND p.isDeleted = false")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductBaseID", nullable = false)
    private int id;

    @Nationalized
    @Column(name = "ProductName", nullable = false, length = 200) // Sửa từ "Name" thành "ProductName"
    private String name;

    @ManyToOne
    @JoinColumn(name = "CategoryID", nullable = false) // Sửa thành @ManyToOne, ánh xạ trực tiếp với cột CategoryID
    private Category category;

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

    @ColumnDefault("1")
    @Column(name = "IsActive", nullable = false) // Thêm ánh xạ cho cột IsActive
    private boolean isActive;

    @ColumnDefault("getdate()")
    @Column(name = "CreatedAt")
    private Instant createAt;

    // Getters và setters
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    // Getter để chuyển Instant thành Date an toàn
    public Date getCreateAtAsDate() {
        return createAt != null ? Date.from(createAt) : null;
    }
}
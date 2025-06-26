package model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Date;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@Entity(name = "Product")
@Table(name = "ProductBase")
@NamedQueries({
    @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p WHERE p.isActive = true"),
    @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name LIKE :name AND p.isActive = true"),
    @NamedQuery(name = "Product.findByBrandId", query = "SELECT p FROM Product p WHERE p.brand.id = :brandId AND p.isActive = true"),
    @NamedQuery(name = "Product.findByCategoryId", query = "SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.isActive = true"),
    @NamedQuery(name = "Product.findActiveByBrandName", query = "SELECT p FROM Product p WHERE p.brand.name = :brandName AND p.isActive = true")
})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProductBaseID", nullable = false)
    private int id;

    @Nationalized
    @Column(name = "ProductName", nullable = false, length = 200)
    private String name;

    @ManyToOne
    @JoinColumn(name = "CategoryID", nullable = false)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "BrandID", nullable = false)
    private ProductBrand brand;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @Nationalized
    @Column(name = "ThumbnailImage") 
    private String thumbnailImage;

    @ColumnDefault("1")
    @Column(name = "IsActive", nullable = false)
    private boolean isActive;

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

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
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

    public Date getCreateAtAsDate() {
        return createAt != null ? Date.from(createAt) : null;
    }
}

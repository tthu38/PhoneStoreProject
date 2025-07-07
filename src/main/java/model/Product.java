package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

@Entity(name = "Product")
@Table(name = "ProductBase")
@NamedQueries({
        @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
        @NamedQuery(name = "Product.findByProductBaseID", query = "SELECT p FROM Product p WHERE p.id = :productBaseID"),
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
    @Column(name = "IsActive", nullable = false, insertable = true, updatable = true)
    private boolean isActive;

    @ColumnDefault("getdate()")
    @Column(name = "CreatedAt")
    private Instant createAt;

    @Transient
    private BigDecimal originalPrice;

    @Transient
    private BigDecimal discountPrice;

    @Transient
    private int discountPercent;

    @Transient
    private Integer displayStorage;

    @Transient
    private String displayColor;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductVariant> variants;

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public String getDisplayColor() {
        return displayColor;
    }

    public void setDisplayColor(String displayColor) {
        this.displayColor = displayColor;
    }

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

    public void setIsActive(boolean actived) {
        isActive = actived;
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

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public int getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        this.discountPercent = discountPercent;
    }

    public Integer getDisplayStorage() {
        return displayStorage;
    }

    public void setDisplayStorage(Integer displayStorage) {
        this.displayStorage = displayStorage;
    }

}

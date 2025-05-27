/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


/**
 *
 * @author ThienThu
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "ProductVariant.findAllActive",query = "SELECT pv FROM ProductVariant pv WHERE pv.isActive = true"),
    @NamedQuery(name = "ProductVariant.findByProductId",
               query = "SELECT pv FROM ProductVariant pv WHERE pv.product.id = :productId AND pv.isActive = true"),
    @NamedQuery(name = "ProductVariant.findBySKU", query = "SELECT pv FROM ProductVariant pv WHERE pv.sku = :sku"),
    @NamedQuery(name = "ProductVariant.findWithDiscount",
               query = "SELECT pv FROM ProductVariant pv WHERE pv.discountPrice IS NOT NULL AND pv.discountExpiry > CURRENT_TIMESTAMP"),
    @NamedQuery(name = "ProductVariant.findByColor",query = "SELECT pv FROM ProductVariant pv WHERE pv.color = :color AND pv.isActive = true"),
    @NamedQuery( name = "ProductVariant.findByRam",query = "SELECT pv FROM ProductVariant pv WHERE pv.ram = :ram AND pv.isActive = true"),
    @NamedQuery(name = "ProductVariant.findByRom",query = "SELECT pv FROM ProductVariant pv WHERE pv.rom = :rom AND pv.isActive = true"),
    @NamedQuery(name = "ProductVariant.findByPriceRange",query = "SELECT pv FROM ProductVariant pv WHERE pv.price BETWEEN :minPrice AND :maxPrice AND pv.isActive = true")

})

@Table(name = "ProductVariants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VariantID")
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;
    
    @Column(name = "Color", nullable = false, length = 50)
    private String color;
    
    @Column(name = "RAM")
    private Integer ram;

    @Column(name = "ROM")
    private Integer rom;
    
    @Column(name = "SKU", nullable = false, unique = true, length = 50)
    private String sku;
    
    @Column(name = "Price", nullable = false, precision = 18, scale = 2)
    private BigDecimal price;
    
    @ColumnDefault("NULL")
    @Column(name = "discountPrice", precision = 10, scale = 2)
    private BigDecimal discountPrice;

    @Column(name = "discountExpiry")
    private Instant discountExpiry;
    
    @ColumnDefault("0")
    @Column(name = "StockQuantity")
    private Integer stockQuantity;
    
    @Lob
    @Column(name = "ImageURLs")
    private String imageURLs; 
        
    @Column(name = "IsActive")
    private Boolean isActive;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getRam() {
        return ram;
    }

    public void setRam(Integer ram) {
        this.ram = ram;
    }

    public Integer getRom() {
        return rom;
    }

    public void setRom(Integer rom) {
        this.rom = rom;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(BigDecimal discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Instant getDiscountExpiry() {
        return discountExpiry;
    }

    public void setDiscountExpiry(Instant discountExpiry) {
        this.discountExpiry = discountExpiry;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageURLs() {
        return imageURLs;
    }

    public void setImageURLs(String imageURLs) {
        this.imageURLs = imageURLs;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    
    
}

package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@NamedQueries({
        @NamedQuery(name = "ProductVariant.findAll", query = "SELECT pv FROM ProductVariant pv"),
        
        @NamedQuery(name = "ProductVariant.findWithDiscount", query = "SELECT pv FROM ProductVariant pv WHERE pv.discountPrice IS NOT NULL AND pv.discountExpiry > CURRENT_TIMESTAMP"),
        @NamedQuery(name = "ProductVariant.findByColor", query = "SELECT pv FROM ProductVariant pv WHERE pv.color = :color AND pv.isActive = true"),
        @NamedQuery(name = "ProductVariant.findByRom", query = "SELECT pv FROM ProductVariant pv WHERE pv.rom = :rom AND pv.isActive = true"),
        @NamedQuery(name = "ProductVariant.findByPriceRange", query = "SELECT pv FROM ProductVariant pv WHERE pv.price BETWEEN :minPrice AND :maxPrice AND pv.isActive = true"),
        @NamedQuery(name = "ProductVariant.findById",
                query = "SELECT p FROM ProductVariant p WHERE p.id = :id"),

        @NamedQuery(name = "ProductVariant.findByProductID",
                query = "SELECT p FROM ProductVariant p WHERE p.product.id = :productBaseID"),

})
@Table(name = "ProductVariants")
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VariantID")
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "ProductBaseID", nullable = false)
    private Product product;

    @Column(name = "Color", nullable = false, length = 50)
    private String color;

    @Column(name = "ROM", nullable = false)
    private Integer rom;

    @Column(name = "Price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "DiscountPrice", precision = 10, scale = 2)
    private BigDecimal discountPrice;

    @Column(name = "DiscountExpiry")
    private LocalDateTime discountExpiry;

    @Lob
    @Column(name = "ImageURLs")
    private String imageURLs;

    @Column(name = "IsActive")
    @ColumnDefault("1")
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

    public Integer getRom() {
        return rom;
    }

    public void setRom(Integer rom) {
        this.rom = rom;
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

    public LocalDateTime getDiscountExpiry() {
        return discountExpiry;
    }

    public void setDiscountExpiry(LocalDateTime discountExpiry) {
        this.discountExpiry = discountExpiry;
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ProductVariant))
            return false;
        ProductVariant that = (ProductVariant) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

}

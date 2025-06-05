package model;

import jakarta.persistence.*;
import java.util.List;
import org.hibernate.annotations.Nationalized;

/**
 *
 * @author ThienThu
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c"),
    @NamedQuery(name = "Category.findByName", query = "SELECT c FROM Category c WHERE c.name LIKE :name")
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

    @OneToMany(mappedBy = "category") // Sửa từ "categories" thành "category"
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
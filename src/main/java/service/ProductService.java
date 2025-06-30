/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.math.BigDecimal;
import java.util.HashSet;

import java.util.List;
import java.util.Set;

import model.Product;
import model.ProductBrand;
import model.ProductStock;
import model.ProductVariant;

/**
 *
 * @author ThienThu
 */
public class ProductService {

    private final GenericDAO<Product> productDAO = new GenericDAO<>(Product.class);
    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");

    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    public boolean hasNextPage(int page, int pageSize) {
        return productDAO.hasNextPage(page, pageSize);
    }

    public Product getProductById(int id) {
        return productDAO.findById(id);
    }

    public void addProduct(Product product) {
        if (!product.getIsActive()) {
            product.setIsActive(true);
        }
        productDAO.insert(product);
    }

    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }

    public void deleteProduct(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            // Lấy entity gắn với EntityManager hiện tại
            Product product = em.find(Product.class, id);

            if (product != null) {
                product.setIsActive(false); // Đánh dấu là INACTIVE
                em.merge(product); // Cập nhật lại
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Product> getProductsByName(String name) {
        return productDAO.findByName(name);
    }

    public void updateProductDetails(int productId, String name, String description, String thumbnailImage,
            int brandId, String[] color, String[] rom, String[] prices, String[] quantities) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Product product = em.find(Product.class, productId);
            if (product == null || !product.getIsActive()) {
                throw new IllegalArgumentException("Sản phẩm không được tìm thấy hoặc đã bị xóa!");
            }

            //Cập nhật thông tin
            product.setName(name);
            product.setDescription(description);
            product.setThumbnailImage(thumbnailImage);

            //Cập nhật brand
            ProductBrand brand = em.find(ProductBrand.class, brandId);
            if (brand == null) {
                throw new IllegalArgumentException("Không tìm thấy thương hiệu với ID: " + brandId);
            }
            product.setBrand(brand);

            em.merge(product);

            //Lấy các variant hiện có
            List<ProductVariant> existingVariants = em.createQuery(
                    "SELECT pv FROM ProductVariant pv WHERE pv.product.id = :productId AND pv.isActive = true",
                    ProductVariant.class)
                    .setParameter("productId", productId)
                    .getResultList();

            Set<String> updatedKeys = new HashSet<>();

            if (color != null && rom != null && prices != null && quantities != null) {
                int len = Math.min(Math.min(color.length, rom.length), Math.min(prices.length, quantities.length));

                for (int i = 0; i < len; i++) {
                    String colorVal = color[i];
                    int romVal = Integer.parseInt(rom[i]);
                    BigDecimal price = new BigDecimal(prices[i]);
                    int quantity = Integer.parseInt(quantities[i]);

                    String key = colorVal + "-" + romVal;
                    updatedKeys.add(key);

                    ProductVariant variant = existingVariants.stream()
                            .filter(v -> v.getColor().equals(colorVal) && v.getRom().equals(romVal))
                            .findFirst()
                            .orElse(null);

                    if (variant != null) {
                        variant.setPrice(price);
                        em.merge(variant);

                        ProductStock stock = em.createQuery(
                                "SELECT ps FROM ProductStock ps WHERE ps.variant = :variant",
                                ProductStock.class)
                                .setParameter("variant", variant)
                                .getSingleResult();
                        stock.setAmount(quantity);
                        em.merge(stock);
                    } else {
                        ProductVariant newVariant = new ProductVariant();
                        newVariant.setProduct(product);
                        newVariant.setColor(colorVal);
                        newVariant.setRom(romVal);
                        newVariant.setPrice(price);
                        newVariant.setIsActive(true);
                        em.persist(newVariant);

                        ProductStock newStock = new ProductStock();
                        newStock.setVariant(newVariant);
                        newStock.setAmount(quantity);
                        newStock.setInventoryID(em.find(model.Inventory.class, 1)); // Có thể truyền tham số thay vì hard-code
                        em.persist(newStock);
                    }
                }
            }

            //Xóa các variant không còn
            for (ProductVariant variant : existingVariants) {
                String key = variant.getColor() + "-" + variant.getRom();
                if (!updatedKeys.contains(key)) {
                    variant.setIsActive(false);
                    em.merge(variant);
                }
            }

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    public List<Product> searchProductsByNameLike(String keyword) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Product p WHERE p.name LIKE :keyword AND p.isActive = true", Product.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

}

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
import jakarta.persistence.TypedQuery;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    //filter product
    public List<Product> searchAndFilterProducts(String name, String categoryID, String sortType, int page, int pageSize) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder queryStr = new StringBuilder(
                    "SELECT p, "
                    + "COALESCE(MIN(CASE WHEN v.discountPrice IS NOT NULL AND v.discountExpiry >= CURRENT_TIMESTAMP THEN v.discountPrice END), NULL) AS minDiscountPrice, "
                    + "MIN(v.price) AS minOriginalPrice, "
                    + "COALESCE(MIN(v.color), NULL) AS displayStorage "
                    + "FROM Product p "
                    + "LEFT JOIN p.variants v ON v.isActive = true "
                    + "LEFT JOIN p.categories c "
                    + "WHERE p.isActive = true "
            );
            boolean hasCategoryFilter = categoryID != null && !categoryID.trim().isEmpty();
            boolean hasNameFilter = name != null && !name.trim().isEmpty();

            if (hasCategoryFilter) {
                queryStr.append(" AND c.id = :categoryId");
            }
            if (hasNameFilter) {
                queryStr.append(" AND LOWER(p.name) LIKE :name");
            }

            queryStr.append(" GROUP BY p");

            if (sortType != null && !sortType.isEmpty()) {
                switch (sortType) {
                    case "name_asc":
                        queryStr.append(" ORDER BY p.name ASC");
                        break;
                    case "name_desc":
                        queryStr.append(" ORDER BY p.name DESC");
                        break;
                }
            }

            TypedQuery<Object[]> query = em.createQuery(queryStr.toString(), Object[].class);

            if (hasCategoryFilter) {
                query.setParameter("categoryId", Integer.parseInt(categoryID));
            }
            if (hasNameFilter) {
                query.setParameter("name", "%" + name.trim().toLowerCase() + "%");
            }

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Object[]> results = query.getResultList();
            List<Product> products = new ArrayList<>();

            for (Object[] row : results) {
                Product product = (Product) row[0];
                BigDecimal minDiscountPrice = (BigDecimal) row[1];
                BigDecimal minOriginalPrice = (BigDecimal) row[2];
                String displayStorage = (String) row[3];

                product.setOriginalPrice(minOriginalPrice);
                product.setDiscountPrice(minDiscountPrice);
                product.setDisplayStorage(displayStorage);

                if (minDiscountPrice != null) {
                    BigDecimal discount = minOriginalPrice.subtract(minDiscountPrice);
                    BigDecimal percentage = discount.multiply(BigDecimal.valueOf(100))
                            .divide(minOriginalPrice, 0, RoundingMode.HALF_UP);
                    product.setDiscountPercent(percentage.intValue());
                } else {
                    product.setDiscountPercent(0);
                }

                products.add(product);
            }
            // Sắp xếp theo giá nếu cần
            if (sortType != null) {
                switch (sortType) {
                    case "price_asc":
                        products.sort(Comparator.comparing(p -> p.getDiscountPrice() != null ? p.getDiscountPrice() : p.getOriginalPrice()));
                        break;
                    case "price_desc":
                        products.sort((p1, p2) -> {
                            BigDecimal price1 = p1.getDiscountPrice() != null ? p1.getDiscountPrice() : p1.getOriginalPrice();
                            BigDecimal price2 = p2.getDiscountPrice() != null ? p2.getDiscountPrice() : p2.getOriginalPrice();
                            return price2.compareTo(price1);
                        });
                        break;
                }
            }

            return products;

        } finally {
            em.close();
        }
    }

    public List<Map<String, Object>> detailProduct(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT p.id, p.name, p.thumbnailImage, p.description, pv.color, pv.rom, pv.price, pv.discountPrice, ps.amount "
                    + "FROM Product p "
                    + "JOIN ProductVariant pv ON p = pv.product "
                    + "JOIN ProductStock ps ON pv = ps.productVariantID "
                    + "WHERE p.id = :productId AND p.isActive = true AND pv.isActive = true ";

            return em.createQuery(jpql, Object[].class)
                    .setParameter("productId", id)
                    .getResultStream()
                    .map(row -> {
                        Map<String, Object> result = new HashMap<>();
                        result.put("productId", row[0]);
                        result.put("productName", row[1]);
                        result.put("thumbnailImage", row[2]);
                        result.put("description", row[3]);
                        result.put("color", row[4]);
                        result.put("rom", row[5]);
                        result.put("originalPrice", row[6]);
                        result.put("discountPrice", row[7]);
                        result.put("stock", row[8]);
                        return result;
                    })
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
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

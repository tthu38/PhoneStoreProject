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
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import model.Category;

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

    public Product addProduct(Product product) {
        if (!product.getIsActive()) {
            product.setIsActive(true);
        }
        return productDAO.insertAndReturn(product);
    }

    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }

    public void deleteProduct(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Product product = em.find(Product.class, id);

            if (product != null) {
                product.setIsActive(false);
                em.merge(product);
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

    public List<Category> getAllCategories() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        } finally {
            em.close();
        }
    }

    //filter product customer
    public List<Product> searchAndFilterProducts(String name, String categoryID, String brandID, String sortType, int page, int pageSize) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder queryStr = new StringBuilder(
                    "SELECT p, "
                    + "COALESCE(MIN(CASE WHEN v.discountPrice IS NOT NULL AND v.discountExpiry >= CURRENT_TIMESTAMP THEN v.discountPrice END), NULL) AS minDiscountPrice, "
                    + "MIN(v.price) AS minOriginalPrice, "
                    + "COALESCE(MIN(v.rom), NULL) AS displayStorage "
                    + "FROM Product p "
                    + "LEFT JOIN p.variants v ON v.isActive = true "
                    + "LEFT JOIN p.category c "
                    + "LEFT JOIN p.brand b "
                    + "WHERE p.isActive = true "
            );
            boolean hasCategoryFilter = categoryID != null && !categoryID.trim().isEmpty();
            boolean hasNameFilter = name != null && !name.trim().isEmpty();
            boolean hasBrandFilter = brandID != null && !brandID.trim().isEmpty();

            if (hasCategoryFilter) {
                queryStr.append(" AND c.id = :categoryId");
            }
            if (hasNameFilter) {
                queryStr.append(" AND LOWER(p.name) LIKE :name");
            }
            if (hasBrandFilter) {
                queryStr.append(" AND p.brand.id = :brandId");
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
            if (hasBrandFilter) {
                query.setParameter("brandId", Integer.parseInt(brandID));
            }

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Object[]> results = query.getResultList();
            List<Product> products = new ArrayList<>();

            for (Object[] row : results) {
                Product product = (Product) row[0];
                BigDecimal minDiscountPrice = (BigDecimal) row[1];
                BigDecimal minOriginalPrice = (BigDecimal) row[2];
                Integer displayStorage = (Integer) row[3];

                product.setOriginalPrice(minOriginalPrice);
                product.setDisplayStorage(displayStorage);

// Nếu còn hạn giảm giá thì set discount
                if (minDiscountPrice != null) {
                    product.setDiscountPrice(minDiscountPrice);

                    BigDecimal discount = minOriginalPrice.subtract(minDiscountPrice);
                    BigDecimal percentage = discount.multiply(BigDecimal.valueOf(100))
                            .divide(minOriginalPrice, 0, RoundingMode.HALF_UP);
                    product.setDiscountPercent(percentage.intValue());
                } else {
                    // Nếu hết hạn thì reset discount
                    product.setDiscountPrice(null);
                    product.setDiscountPercent(0);
                }

                products.add(product);
            }

            // Sắp xếp theo giá với xử lý null
            if (sortType != null) {
                switch (sortType) {
                    case "price_asc":
                        products.sort(Comparator.comparing(
                                p -> p.getDiscountPrice() != null ? p.getDiscountPrice()
                                : (p.getOriginalPrice() != null ? p.getOriginalPrice() : BigDecimal.ZERO)
                        ));
                        break;
                    case "price_desc":
                        products.sort((p1, p2) -> {
                            BigDecimal price1 = p1.getDiscountPrice() != null ? p1.getDiscountPrice()
                                    : (p1.getOriginalPrice() != null ? p1.getOriginalPrice() : BigDecimal.ZERO);
                            BigDecimal price2 = p2.getDiscountPrice() != null ? p2.getDiscountPrice()
                                    : (p2.getOriginalPrice() != null ? p2.getOriginalPrice() : BigDecimal.ZERO);
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
            String jpql = "SELECT p.id, p.name, p.thumbnailImage, p.description, "
                    + "pv.color, pv.rom, pv.price, pv.discountPrice, pv.discountExpiry, ps.amount "
                    + "FROM Product p "
                    + "JOIN ProductVariant pv ON p = pv.product "
                    + "JOIN ProductStock ps ON pv = ps.variant "
                    + "WHERE p.id = :productId AND p.isActive = true AND pv.isActive = true";

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

                        BigDecimal originalPrice = (BigDecimal) row[6];
                        BigDecimal discountPrice = (BigDecimal) row[7];
                        LocalDateTime discountExpiry = (LocalDateTime) row[8];
                        int stock = (int) row[9];

                        boolean discountValid = discountPrice != null && discountExpiry != null
                                && discountExpiry.isAfter(LocalDateTime.now());

                        result.put("originalPrice", originalPrice);
                        result.put("discountPrice", discountValid ? discountPrice : null);
                        result.put("discountValid", discountValid);
                        result.put("discountExpiry", discountExpiry);
                        result.put("stock", stock);
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

    //Apply discount to products
    public void applyDiscount(String name, String categoryId, int discountPercent, LocalDate expireDate) {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount percentage must be between 0 and 100");
        }
        if (expireDate == null) {
            throw new IllegalArgumentException("Expiration date must be provided");
        }

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            StringBuilder queryStr = new StringBuilder("SELECT DISTINCT p FROM Product p WHERE p.isActive = true");

            if (name != null && !name.trim().isEmpty()) {
                queryStr.append(" AND LOWER(p.name) LIKE :name");
            }

            Integer parsedCategoryId = null;
            if (categoryId != null && !categoryId.trim().isEmpty()) {
                try {
                    parsedCategoryId = Integer.parseInt(categoryId.trim());
                    queryStr.append(" AND p.category.id = :categoryId");
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Category ID không hợp lệ.");
                }
            }

            TypedQuery<Product> query = em.createQuery(queryStr.toString(), Product.class);

            if (name != null && !name.trim().isEmpty()) {
                query.setParameter("name", "%" + name.toLowerCase().trim() + "%");
            }

            if (parsedCategoryId != null) {
                query.setParameter("categoryId", parsedCategoryId);
            }

            List<Product> products = query.getResultList();

            if (products.isEmpty()) {
                throw new IllegalArgumentException("Không tìm thấy sản phẩm phù hợp.");
            }

            Instant expireDateInstant = expireDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

            int discountCount = 0;

            for (Product product : products) {
                List<ProductVariant> variants = em.createQuery(
                        "SELECT pv FROM ProductVariant pv WHERE pv.product = :product AND pv.isActive = true",
                        ProductVariant.class)
                        .setParameter("product", product)
                        .getResultList();

                for (ProductVariant variant : variants) {
                    BigDecimal discount = variant.getPrice().multiply(BigDecimal.valueOf(discountPercent))
                            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
                    variant.setDiscountPrice(variant.getPrice().subtract(discount));
                    variant.setDiscountExpiry(LocalDateTime.ofInstant(expireDateInstant, ZoneId.systemDefault()));
                    em.merge(variant);
                    discountCount++;
                }
            }

            tx.commit();
            System.out.println("✅ Discount applied to " + discountCount + " variants.");

        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new RuntimeException("Lỗi áp dụng khuyến mãi: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    public void removeDiscount(String name, String categoryId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Log input
            System.out.println("removeDiscount - name: " + name + ", categoryId: " + categoryId);

            // Validate categoryId
            boolean hasCategoryFilter = categoryId != null && !categoryId.isEmpty() && categoryId.matches("\\d+");
            boolean hasNameFilter = name != null && !name.isEmpty();

            // Build query for products
            StringBuilder queryStr = new StringBuilder("SELECT p FROM Product p WHERE p.isActive = true");
            if (hasCategoryFilter) {
                queryStr.append(" AND p.category.id = :categoryId");
            }
            if (hasNameFilter) {
                queryStr.append(" AND LOWER(p.name) LIKE :name");
            }

            TypedQuery<Product> query = em.createQuery(queryStr.toString(), Product.class);

            if (hasCategoryFilter) {
                try {
                    query.setParameter("categoryId", Integer.parseInt(categoryId));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid categoryId: " + categoryId);
                }
            }
            if (hasNameFilter) {
                query.setParameter("name", "%" + name.toLowerCase() + "%");
            }

            List<Product> products = query.getResultList();
            System.out.println("Found " + products.size() + " products");

            // Remove discount from variants
            int updatedVariants = 0;
            for (Product product : products) {
                List<ProductVariant> variants = em.createQuery(
                        "SELECT pv FROM ProductVariant pv WHERE pv.product = :product AND pv.isActive = true",
                        ProductVariant.class)
                        .setParameter("product", product)
                        .getResultList();
                System.out.println("Product ID: " + product.getId() + ", Found " + variants.size() + " variants");

                for (ProductVariant variant : variants) {
                    if (variant.getDiscountPrice() != null || variant.getDiscountExpiry() != null) {
                        variant.setDiscountPrice(null);
                        variant.setDiscountExpiry(null);
                        em.merge(variant);
                        updatedVariants++;
                    }
                }
            }

            System.out.println("Updated " + updatedVariants + " variants");
            tx.commit();

            // Log success
            if (updatedVariants == 0) {
                System.out.println("No variants updated - no discounts found or no matching products/variants");
            }
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Error in removeDiscount: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    // Get discounted products
    public List<Product> getDiscountedProducts(int offset, int limit) {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("Fetching discounted products...");

            String jpql = "SELECT DISTINCT p FROM Product p "
                    + "WHERE p.isActive = true AND EXISTS ("
                    + "    SELECT 1 FROM ProductVariant v "
                    + "    WHERE v.product = p "
                    + "    AND v.isActive = true "
                    + "    AND v.discountPrice IS NOT NULL "
                    + "    AND v.discountExpiry >= CURRENT_TIMESTAMP"
                    + ") "
                    + "ORDER BY p.id";

            List<Product> products = em.createQuery(jpql, Product.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();

            System.out.println("Found " + products.size() + " products with discounts");

            for (Product product : products) {

                // Ưu tiên tìm variant có ROM = 64
                ProductVariant displayVariant = findDiscountedVariant(em, product, 64);

                if (displayVariant == null) {
                    // Nếu không có ROM = 64, chọn variant đầu tiên có discount
                    displayVariant = em.createQuery(
                            "SELECT v FROM ProductVariant v "
                            + "WHERE v.product = :product "
                            + "AND v.isActive = true "
                            + "AND v.discountPrice IS NOT NULL "
                            + "AND v.discountExpiry >= CURRENT_TIMESTAMP "
                            + "ORDER BY v.rom",
                            ProductVariant.class)
                            .setParameter("product", product)
                            .setMaxResults(1)
                            .getResultStream()
                            .findFirst()
                            .orElse(null);
                }

                if (displayVariant != null) {
                    product.setOriginalPrice(displayVariant.getPrice());
                    product.setDiscountPrice(displayVariant.getDiscountPrice());

                    if (displayVariant.getPrice().compareTo(BigDecimal.ZERO) > 0) {
                        BigDecimal discount = displayVariant.getPrice().subtract(displayVariant.getDiscountPrice());
                        BigDecimal percentage = discount.multiply(new BigDecimal(100))
                                .divide(displayVariant.getPrice(), 0, RoundingMode.HALF_UP);
                        product.setDiscountPercent(percentage.intValue());
                    }

                    product.setDisplayStorage(displayVariant.getRom());
                    product.setDisplayColor(displayVariant.getColor()); // <-- Thêm dòng này
                }
            }

            return products;
        } finally {
            em.close();
        }
    }

    private ProductVariant findDiscountedVariant(EntityManager em, Product product, int rom) {
        return em.createQuery(
                "SELECT v FROM ProductVariant v "
                + "WHERE v.product = :product AND v.rom = :rom "
                + "AND v.isActive = true "
                + "AND v.discountPrice IS NOT NULL "
                + "AND v.discountExpiry >= CURRENT_TIMESTAMP",
                ProductVariant.class)
                .setParameter("product", product)
                .setParameter("rom", rom)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public int countFilteredProducts(String name, String categoryID, String brandID) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder queryStr = new StringBuilder(
                    "SELECT COUNT(DISTINCT p) "
                    + "FROM Product p "
                    + "LEFT JOIN p.variants v "
                    + "LEFT JOIN p.category c "
                    + "LEFT JOIN p.brand b "
                    + "WHERE p.isActive = true AND v.isActive = true"
            );

            boolean hasCategoryFilter = categoryID != null && !categoryID.isEmpty();
            boolean hasNameFilter = name != null && !name.isEmpty();
            boolean hasBrandFilter = brandID != null && !brandID.isEmpty();

            if (hasCategoryFilter) {
                queryStr.append(" AND c.id = :categoryId");
            }
            if (hasNameFilter) {
                queryStr.append(" AND LOWER(p.name) LIKE :name");
            }
            if (hasBrandFilter) {
                queryStr.append(" AND b.id = :brandId");
            }

            TypedQuery<Long> query = em.createQuery(queryStr.toString(), Long.class);

            if (hasCategoryFilter) {
                query.setParameter("categoryId", Integer.parseInt(categoryID));
            }
            if (hasNameFilter) {
                query.setParameter("name", "%" + name.toLowerCase() + "%");
            }
            if (hasBrandFilter) {
                query.setParameter("brandId", Integer.parseInt(brandID));
            }

            return query.getSingleResult().intValue();
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

    public List<Map<String, Object>> getMostOrderedProducts(Integer limit) {
        EntityManager em = productDAO.emf.createEntityManager();
        try {
            String jpql = "SELECT p.id, p.name, p.description, p.thumbnailImage, "
                    + "MIN(v.price), "
                    + "MIN(CASE WHEN v.discountPrice IS NOT NULL AND v.discountExpiry >= CURRENT_TIMESTAMP "
                    + "THEN v.discountPrice ELSE NULL END), "
                    + "SUM(od.quantity) "
                    + "FROM Product p "
                    + "JOIN p.variants v "
                    + "JOIN OrderDetails od ON v = od.productVariant "
                    + "WHERE p.isActive = true AND v.isActive = true "
                    + "GROUP BY p.id, p.name, p.description, p.thumbnailImage "
                    + "ORDER BY SUM(od.quantity) DESC";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);

            if (limit != null && limit > 0) {
                query.setMaxResults(limit);
            }

            return query.getResultStream().map(row -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", row[0]);
                map.put("name", row[1]);
                map.put("description", row[2]);
                map.put("thumbnailImage", row[3]);

                BigDecimal originalPrice = (BigDecimal) row[4];
                BigDecimal discountPrice = (BigDecimal) row[5];
                Long totalOrdered = (Long) row[6];

                map.put("originalPrice", originalPrice);
                map.put("discountPrice", discountPrice);
                map.put("totalOrdered", totalOrdered);

                BigDecimal discountPercent = BigDecimal.ZERO;
                if (originalPrice != null && discountPrice != null && originalPrice.compareTo(BigDecimal.ZERO) > 0) {
                    discountPercent = originalPrice.subtract(discountPrice)
                            .divide(originalPrice, 2, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .setScale(0, RoundingMode.HALF_UP); // Làm tròn 0 chữ số sau dấu phẩy
                }
                map.put("discountPercent", discountPercent);

                return map;
            }).collect(Collectors.toList());

        } finally {
            em.close();
        }
    }

    // Count total discounted products for pagination
    public int getNumberOfDiscountedProducts() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT COUNT(DISTINCT pv.product) FROM ProductVariant pv "
                    + "WHERE pv.product.isActive = true AND pv.isActive = true "
                    + "AND pv.discountPrice IS NOT NULL "
                    + "AND pv.discountExpiry >= CURRENT_TIMESTAMP";

            return em.createQuery(jpql, Long.class)
                    .getSingleResult()
                    .intValue();
        } finally {
            em.close();
        }
    }

    //filter product admin
    public List<Product> searchAndFilterProductsAdmin(String name, String categoryID, String brandID,
            String status, String sortType,
            int page, int pageSize) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder queryStr = new StringBuilder(
                    "SELECT p, "
                    + "COALESCE(MIN(CASE WHEN v.discountPrice IS NOT NULL AND v.discountExpiry >= CURRENT_TIMESTAMP THEN v.discountPrice END), NULL) AS minDiscountPrice, "
                    + "MIN(v.price) AS minOriginalPrice, "
                    + "COALESCE(MIN(v.rom), NULL) AS displayStorage "
                    + "FROM Product p "
                    + "LEFT JOIN p.variants v ON v.isActive = true "
                    + "LEFT JOIN p.category c "
                    + "LEFT JOIN p.brand b "
                    + "WHERE 1=1 "
            );

            // Kiểm tra từng điều kiện lọc
            boolean hasCategory = categoryID != null && !categoryID.trim().isEmpty();
            boolean hasBrand = brandID != null && !brandID.trim().isEmpty();
            boolean hasName = name != null && !name.trim().isEmpty();
            boolean hasStatus = status != null && !status.trim().isEmpty();

            if (hasCategory) {
                queryStr.append(" AND c.id = :categoryId");
            }

            if (hasBrand) {
                queryStr.append(" AND b.id = :brandId");
            }

            if (hasName) {
                queryStr.append(" AND LOWER(p.name) LIKE :name");
            }

            if (hasStatus) {
                if ("active".equalsIgnoreCase(status)) {
                    queryStr.append(" AND p.isActive = true");
                } else if ("inactive".equalsIgnoreCase(status)) {
                    queryStr.append(" AND p.isActive = false");
                }
            }

            queryStr.append(" GROUP BY p");

            // Sắp xếp theo tên
            if (sortType != null && !sortType.trim().isEmpty()) {
                switch (sortType) {
                    case "name_asc":
                        queryStr.append(" ORDER BY p.name ASC");
                        break;
                    case "name_desc":
                        queryStr.append(" ORDER BY p.name DESC");
                        break;
                    default:
                        break;
                }
            }

            // Tạo truy vấn
            TypedQuery<Object[]> query = em.createQuery(queryStr.toString(), Object[].class);

            if (hasCategory) {
                query.setParameter("categoryId", Integer.parseInt(categoryID));
            }

            if (hasBrand) {
                query.setParameter("brandId", Integer.parseInt(brandID));
            }

            if (hasName) {
                query.setParameter("name", "%" + name.toLowerCase().trim() + "%");
            }

            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            List<Object[]> results = query.getResultList();
            List<Product> products = new ArrayList<>();

            for (Object[] row : results) {
                Product product = (Product) row[0];
                BigDecimal minDiscountPrice = (BigDecimal) row[1];
                BigDecimal minOriginalPrice = (BigDecimal) row[2];
                Integer displayStorage = (Integer) row[3];

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

            // Sắp xếp theo giá
            if (sortType != null) {
                switch (sortType) {
                    case "price_asc":
                        products.sort(Comparator.comparing(
                                p -> {
                                    BigDecimal price = p.getDiscountPrice() != null ? p.getDiscountPrice() : p.getOriginalPrice();
                                    return price != null ? price : BigDecimal.ZERO; // tránh null
                                }
                        ));
                        break;

                    case "price_desc":
                        products.sort((p1, p2) -> {
                            BigDecimal price1 = p1.getDiscountPrice() != null ? p1.getDiscountPrice() : p1.getOriginalPrice();
                            BigDecimal price2 = p2.getDiscountPrice() != null ? p2.getDiscountPrice() : p2.getOriginalPrice();

                            if (price1 == null && price2 == null) {
                                return 0;
                            }
                            if (price1 == null) {
                                return 1;  // null đưa về cuối
                            }
                            if (price2 == null) {
                                return -1;
                            }

                            return price2.compareTo(price1);
                        });
                        break;

                    default:
                        break;
                }
            }

            return products;
        } finally {
            em.close();
        }
    }

    public int countFilteredProductsAdmin(String name, String categoryID, String brandID,
            String status) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder queryStr = new StringBuilder(
                    "SELECT COUNT(DISTINCT p) "
                    + "FROM Product p "
                    + "LEFT JOIN p.variants v "
                    + "LEFT JOIN p.category c "
                    + "LEFT JOIN p.brand b "
                    + "WHERE 1=1"
            );

            boolean hasCategory = categoryID != null && !categoryID.isEmpty();
            boolean hasBrand = brandID != null && !brandID.isEmpty();
            boolean hasName = name != null && !name.isEmpty();
            boolean hasStatus = status != null && !status.isEmpty();

            if (hasCategory) {
                queryStr.append(" AND c.id = :categoryId");
            }

            if (hasBrand) {
                queryStr.append(" AND b.id = :brandId");
            }

            if (hasName) {
                queryStr.append(" AND LOWER(p.name) LIKE :name");
            }

            if (hasStatus) {
                if ("active".equalsIgnoreCase(status)) {
                    queryStr.append(" AND p.isActive = true");
                } else if ("inactive".equalsIgnoreCase(status)) {
                    queryStr.append(" AND p.isActive = false");
                }
            }

            TypedQuery<Long> query = em.createQuery(queryStr.toString(), Long.class);

            if (hasCategory) {
                query.setParameter("categoryId", Integer.parseInt(categoryID));
            }

            if (hasBrand) {
                query.setParameter("brandId", Integer.parseInt(brandID));
            }

            if (hasName) {
                query.setParameter("name", "%" + name.toLowerCase() + "%");
            }

            return query.getSingleResult().intValue();

        } finally {
            em.close();
        }
    }

    public List<Map<String, Object>> getMostDiscountedProducts(Integer limit) {
        EntityManager em = productDAO.emf.createEntityManager();
        try {
            String jpql = "SELECT p.id, p.name, p.description, p.thumbnailImage, "
                    + "MIN(v.price), MIN(v.discountPrice), "
                    + "ROUND((1.0 * (MIN(v.price) - MIN(v.discountPrice)) / MIN(v.price)) * 100) "
                    + "FROM Product p "
                    + "JOIN p.variants v "
                    + "WHERE p.isActive = true AND v.isActive = true "
                    + "AND v.discountPrice IS NOT NULL "
                    + "AND v.discountExpiry >= CURRENT_TIMESTAMP "
                    + "GROUP BY p.id, p.name, p.description, p.thumbnailImage "
                    + "ORDER BY p.id";

            TypedQuery<Object[]> query = em.createQuery(jpql, Object[].class);

            if (limit != null && limit > 0) {
                query.setMaxResults(limit);
            }

            return query.getResultStream().map(row -> {
                Map<String, Object> map = new HashMap<>();
                map.put("id", row[0]);
                map.put("name", row[1]);
                map.put("description", row[2]);
                map.put("thumbnailImage", row[3]);
                map.put("originalPrice", row[4]);
                map.put("discountPrice", row[5]);
                map.put("discountPercent", row[6]);
                return map;
            }).collect(Collectors.toList());

        } finally {
            em.close();
        }
    }
}

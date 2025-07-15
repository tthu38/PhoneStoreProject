package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import java.util.List;
import model.ProductVariant;

public class ProductVariantService {

    private final GenericDAO<ProductVariant> productVariantDao = new GenericDAO<>(ProductVariant.class);
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");

    public ProductVariant getProductVariantById(int id) {
        return productVariantDao.findById(id);
    }

    // Lấy tất cả variant của 1 product
    public List<ProductVariant> getAllProductVariants(int productID) {
    return productVariantDao.findByNamedQuery(
        "ProductVariant.findByProductID", "productBaseID", productID
    );
}


    public void addProductVariant(ProductVariant productVariant) {
        productVariantDao.insert(productVariant);
    }

    public void updateVariant(ProductVariant variant) {
        productVariantDao.update(variant);
    }

    // Soft delete
    public void deleteProductVariant(Integer variantId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            ProductVariant productVariant = em.find(ProductVariant.class, variantId);
            if (productVariant != null) {
                Boolean current = productVariant.getIsActive();
                productVariant.setIsActive(current == null ? false : !current);

                em.merge(productVariant);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    public List<ProductVariant> getVariantsByProductId(int productId) {
    EntityManager em = emf.createEntityManager();
    try {
        return em.createQuery(
            "SELECT pv FROM ProductVariant pv WHERE pv.product.id = :productId AND pv.isActive = true",
            ProductVariant.class)
            .setParameter("productId", productId)
            .getResultList();
    } finally {
        em.close();
    }
}


    // Lấy theo productId và color
    public ProductVariant getVariantByProductAndColor(int productId, String color) {
        EntityManager em = emf.createEntityManager();
        ProductVariant result = null;
        try {
            TypedQuery<ProductVariant> query = em.createQuery(
                    "SELECT pv FROM ProductVariant pv JOIN FETCH pv.product "
                    + "WHERE pv.product.id = :productId AND pv.color = :color AND pv.isActive = true",
                    ProductVariant.class
            );
            query.setParameter("productId", productId);
            query.setParameter("color", color);

            List<ProductVariant> variants = query.getResultList();
            if (!variants.isEmpty()) {
                result = variants.get(0);
            }
        } finally {
            em.close();
        }
        return result;
    }

    // Lấy theo productId và rom
    public ProductVariant getVariantByProductAndRom(int productId, int rom) {
        EntityManager em = emf.createEntityManager();
        ProductVariant result = null;
        try {
            TypedQuery<ProductVariant> query = em.createQuery(
                    "SELECT pv FROM ProductVariant pv JOIN FETCH pv.product "
                    + "WHERE pv.product.id = :productId AND pv.rom = :rom AND pv.isActive = true",
                    ProductVariant.class
            );
            query.setParameter("productId", productId);
            query.setParameter("rom", rom);

            List<ProductVariant> variants = query.getResultList();
            if (!variants.isEmpty()) {
                result = variants.get(0);
            }
        } finally {
            em.close();
        }
        return result;
    }
    public ProductVariant findByProductRomColor(int productId, int rom, String color) {
    EntityManager em = emf.createEntityManager();
    try {
        return em.createQuery("SELECT v FROM ProductVariant v WHERE v.product.id = :productId AND v.rom = :rom AND v.color = :color", ProductVariant.class)
                .setParameter("productId", productId)
                .setParameter("rom", rom)
                .setParameter("color", color)
                .getResultStream()
                .findFirst()
                .orElse(null);
    } finally {
        em.close();
    }
}

}

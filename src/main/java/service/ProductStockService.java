package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.ProductStock;
import static service.ProductService.emf;

public class ProductStockService {
    private static final GenericDAO<ProductStock> productStockDAO = new GenericDAO<>(ProductStock.class);
    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");

    public ProductStock getProductStock(Integer productVariantId) {
        return productStockDAO.findByAttribute("productVariantID", productVariantId).get(0);
    }

    public void updateProductStock(ProductStock productStock) {
        productStockDAO.update(productStock);
    }

    public void addProductStock(ProductStock productStock) {
        productStockDAO.insert(productStock);
    }

    public void deleteByVariantId(Integer productVariantId) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.createQuery("DELETE FROM ProductStock ps WHERE ps.productVariantID.id = :variantId")
                    .setParameter("variantId", productVariantId)
                    .executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
    
    public Map<Integer, Integer> getProductStockQuantities() {
        EntityManager em = emf.createEntityManager();
        try {
            List<ProductStock> stockList = em.createQuery(
                    "SELECT ps FROM ProductStock ps JOIN FETCH ps.variant v JOIN FETCH v.product", ProductStock.class)
                    .getResultList();

            Map<Integer, Integer> stockMap = new HashMap<>();

            for (ProductStock ps : stockList) {
                int productId = ps.getVariant().getProduct().getId();
                int quantity = ps.getAmount(); // Lấy từ bảng ProductStock

                stockMap.put(productId, stockMap.getOrDefault(productId, 0) + quantity);
            }

            return stockMap;
        } finally {
            em.close();
        }
    }
}

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
import model.ProductVariant;
import static service.ProductService.emf;

public class ProductStockService {

    private static final GenericDAO<ProductStock> productStockDAO = new GenericDAO<>(ProductStock.class);
    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");

    public ProductStock getProductStock(Integer productVariantId) {
        List<ProductStock> list = productStockDAO.findByAttribute("variant", productVariantId);

        // Nếu không có bản ghi, trả về ProductStock mặc định (số lượng 0)
        if (list == null || list.isEmpty()) {
            ProductStock emptyStock = new ProductStock();
            emptyStock.setAmount(0);
            return emptyStock;
        }

        // Nếu có thì lấy bản ghi đầu tiên
        return list.get(0);
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

    public List<Map<String, Object>> getVariantDetailsWithLowStock(int threshold) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT v, SUM(ps.amount) FROM ProductStock ps "
                    + "JOIN ps.variant v "
                    + "WHERE v.isActive = true AND v.product.isActive = true "
                    + "GROUP BY v "
                    + "HAVING SUM(ps.amount) < :threshold";
            List<Object[]> results = em.createQuery(jpql, Object[].class)
                    .setParameter("threshold", threshold)
                    .getResultList();
            List<Map<String, Object>> list = new java.util.ArrayList<>();
            for (Object[] row : results) {
                ProductVariant variant = (ProductVariant) row[0];
                Integer stock = ((Number) row[1]).intValue();
                Map<String, Object> map = new java.util.HashMap<>();
                map.put("variant", variant);
                map.put("stock", stock);
                list.add(map);
            }
            return list;
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

    public List<ProductStock> getStockByProductId(int productId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT ps FROM ProductStock ps WHERE ps.variant.product.id = :productId", ProductStock.class)
                    .setParameter("productId", productId)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public ProductStock getStockByVariantId(Integer variantId) {
        EntityManager em = emf.createEntityManager();
        try {
            List<ProductStock> results = em.createQuery(
                    "SELECT ps FROM ProductStock ps WHERE ps.variant.id = :variantId", ProductStock.class)
                    .setParameter("variantId", variantId)
                    .getResultList();

            if (results.isEmpty()) {
                ProductStock empty = new ProductStock();
                empty.setAmount(0);
                return empty;
            }
            return results.get(0);
        } finally {
            em.close();
        }
    }
    

    // Cập nhật số lượng trong kho sau khi thanh toán thành công
    public boolean updateStockAfterPayment(Integer variantId, int quantityToReduce) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Tìm stock record cho variant
            List<ProductStock> results = em.createQuery(
                    "SELECT ps FROM ProductStock ps WHERE ps.variant.id = :variantId", ProductStock.class)
                    .setParameter("variantId", variantId)
                    .getResultList();

            if (results.isEmpty()) {
                System.out.println("[ProductStockService] Không tìm thấy stock cho variant ID: " + variantId);
                transaction.rollback();
                return false;
            }

            ProductStock stock = results.get(0);
            int currentAmount = stock.getAmount();

            // Kiểm tra xem có đủ số lượng để trừ không
            if (currentAmount < quantityToReduce) {
                System.out.println("[ProductStockService] Không đủ số lượng trong kho. Hiện có: " + currentAmount + ", cần trừ: " + quantityToReduce);
                transaction.rollback();
                return false;
            }

            // Trừ số lượng
            int newAmount = currentAmount - quantityToReduce;
            stock.setAmount(newAmount);
            em.merge(stock);

            System.out.println("[ProductStockService] Cập nhật stock thành công. Variant ID: " + variantId
                    + ", Số lượng cũ: " + currentAmount + ", Số lượng mới: " + newAmount
                    + ", Đã trừ: " + quantityToReduce);

            transaction.commit();
            return true;

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.out.println("[ProductStockService] Lỗi khi cập nhật stock: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

}

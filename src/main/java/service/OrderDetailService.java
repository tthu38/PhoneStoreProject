package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import model.OrderDetails;

public class OrderDetailService {

    private final GenericDAO<OrderDetails> orderDetailDAO = new GenericDAO<>(OrderDetails.class);
    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");

    // Lấy tất cả chi tiết đơn hàng
    public List<OrderDetails> getAllOrderDetails() {
        return orderDetailDAO.getAll();
    }

    // Lấy 1 chi tiết theo ID
    public OrderDetails getOrderDetailById(int id) {
        return orderDetailDAO.findById(id);
    }

    // Thêm chi tiết đơn hàng
    public boolean addOrderDetail(OrderDetails orderDetail) {
        // Log dữ liệu đầu vào
        System.out.println("Insert OrderDetail: "
            + "OrderID=" + (orderDetail.getOrder() != null ? orderDetail.getOrder().getId() : "null")
            + ", VariantID=" + (orderDetail.getProductVariant() != null ? orderDetail.getProductVariant().getId() : "null")
            + ", Quantity=" + orderDetail.getQuantity()
            + ", UnitPrice=" + orderDetail.getUnitPrice()
            + ", DiscountPrice=" + orderDetail.getDiscountPrice()
        );
        boolean result = orderDetailDAO.insert(orderDetail);
        if (!result) {
            System.out.println("Insert OrderDetail FAILED: " + orderDetail);
        }
        return result;
    }

    // Cập nhật
    public boolean updateOrderDetail(OrderDetails orderDetail) {
        return orderDetailDAO.update(orderDetail);
    }

    // Xóa (cứng)
    public boolean deleteOrderDetail(int id) {
        return orderDetailDAO.delete(id);
    }

    // Phân trang: còn trang tiếp theo không?
    public boolean hasNextPage(int page, int pageSize) {
        return orderDetailDAO.hasNextPage(page, pageSize);
    }

    // Lấy danh sách theo OrderID
    public List<OrderDetails> getOrderDetailsByOrderId(int orderId) {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("[OrderDetailService] Getting order details for order ID: " + orderId);
            List<OrderDetails> details = em.createQuery(
                "SELECT od FROM OrderDetails od " +
                "LEFT JOIN FETCH od.productVariant pv " +
                "LEFT JOIN FETCH pv.product " +
                "WHERE od.order.id = :orderID", OrderDetails.class)
                .setParameter("orderID", orderId)
                .getResultList();
            System.out.println("[OrderDetailService] Found " + details.size() + " order details for order ID: " + orderId);
            return details;
        } catch (Exception e) {
            System.out.println("[OrderDetailService] Error getting order details for order ID " + orderId + ": " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
    }

    // Lấy theo VariantID (sản phẩm cụ thể)
    public List<OrderDetails> getOrderDetailsByProductVariantId(int variantId) {
        return orderDetailDAO.findByNamedQuery("OrderDetails.findByProductVariantID", "productVariantID", variantId);
    }
}

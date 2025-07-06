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
        return orderDetailDAO.insert(orderDetail);
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
        return orderDetailDAO.findByNamedQuery("OrderDetails.findByOrderDetailID", "orderDetailID", orderId);
    }

    // Lấy theo VariantID (sản phẩm cụ thể)
    public List<OrderDetails> getOrderDetailsByProductVariantId(int variantId) {
        return orderDetailDAO.findByNamedQuery("OrderDetails.findByProductVariantID", "productVariantID", variantId);
    }
}

package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import model.Order;

public class OrderService {

    private final GenericDAO<Order> orderDAO = new GenericDAO<>(Order.class);
    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");

    // Lấy tất cả đơn hàng
    public List<Order> getAllOrders() {
        return orderDAO.getAll();
    }

    // Phân trang: kiểm tra còn trang tiếp theo không
    public boolean hasNextPage(int page, int pageSize) {
        return orderDAO.hasNextPage(page, pageSize);
    }

    // Lấy đơn hàng theo ID
    public Order getOrderById(int id) {
        return orderDAO.findById(id);
    }

    // Thêm đơn hàng mới
    public boolean addOrder(Order order) {
        return orderDAO.insert(order);
    }

    // Cập nhật đơn hàng
    public boolean updateOrder(Order order) {
        return orderDAO.update(order);
    }

    // Soft delete đơn hàng: đánh dấu trạng thái là Cancelled
    public boolean cancelOrder(int orderId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Order order = em.find(Order.class, orderId);
            if (order != null) {
                order.setOrderStatus("Cancelled");
                em.merge(order);
                em.getTransaction().commit();
                return true;
            }

            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Tìm đơn theo tên trạng thái (Pending, Paid, etc.)
    public List<Order> getOrdersByStatus(String status) {
        return orderDAO.findByNamedQuery("Order.findByOrderStatus", "orderStatus", status);
    }

    // Tìm đơn theo User ID
    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.findByNamedQuery("Order.findByUserID", "userID", userId);
    }
}


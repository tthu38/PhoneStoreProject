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

    // Lấy tất cả đơn hàng với User info
    public List<Order> getAllOrders() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT o FROM Order o LEFT JOIN FETCH o.user", Order.class)
                    .getResultList();
        } finally {
            em.close();
        }
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

    // Cập nhật trạng thái đơn hàng thành Paid
    public boolean updateOrderToPaid(int orderId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Order order = em.find(Order.class, orderId);
            if (order != null) {
                order.setOrderStatus("Paid");
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

    // Cập nhật trạng thái đơn hàng thành Cancelled
    public boolean updateOrderToCancelled(int orderId) {
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

    // Cập nhật trạng thái đơn hàng (generic method)
    public boolean updateOrderStatus(int orderId, String status) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Order order = em.find(Order.class, orderId);
            if (order != null) {
                order.setOrderStatus(status);
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

    // Soft delete đơn hàng: đánh dấu trạng thái là Cancelled
    public boolean cancelOrder(int orderId) {
        return updateOrderToCancelled(orderId);
    }

    // Lấy đơn hàng theo status với User info
    public List<Order> getOrdersByStatus(String status) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.status = :status", Order.class)
                    .setParameter("status", status)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // Tìm đơn theo User ID
    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.findByNamedQuery("Order.findByUserID", "userID", userId);
    }

    // Lấy đơn hàng đã thanh toán
    public List<Order> getPaidOrders() {
        return getOrdersByStatus("Paid");
    }

    // Lấy đơn hàng đã hủy
    public List<Order> getCancelledOrders() {
        return getOrdersByStatus("Cancelled");
    }

    // Lấy đơn hàng đang chờ
    public List<Order> getPendingOrders() {
        return getOrdersByStatus("Pending");
    }

    // Test method để kiểm tra database connection
    public static void testDatabaseConnection() {
        System.out.println("=== Testing OrderService Database Connection ===");
        
        try {
            OrderService orderService = new OrderService();
            
            // Test 1: Lấy tất cả đơn hàng
            System.out.println("Test 1: Getting all orders...");
            List<Order> allOrders = orderService.getAllOrders();
            System.out.println("Total orders found: " + allOrders.size());
            
            // Test 2: Lấy đơn hàng theo status
            System.out.println("\nTest 2: Getting orders by status...");
            List<Order> pendingOrders = orderService.getOrdersByStatus("Pending");
            System.out.println("Pending orders: " + pendingOrders.size());
            
            List<Order> paidOrders = orderService.getOrdersByStatus("Paid");
            System.out.println("Paid orders: " + paidOrders.size());
            
            List<Order> cancelledOrders = orderService.getOrdersByStatus("Cancelled");
            System.out.println("Cancelled orders: " + cancelledOrders.size());
            
            // Test 3: Lấy đơn hàng theo ID
            if (!allOrders.isEmpty()) {
                System.out.println("\nTest 3: Getting order by ID...");
                Order firstOrder = allOrders.get(0);
                Order foundOrder = orderService.getOrderById(firstOrder.getId());
                if (foundOrder != null) {
                    System.out.println("Found order ID: " + foundOrder.getId());
                    System.out.println("Order status: " + foundOrder.getStatus());
                    System.out.println("Order amount: " + foundOrder.getTotalAmount());
                } else {
                    System.out.println("Order not found by ID");
                }
            }
            
            // Test 4: Test EntityManager
            System.out.println("\nTest 4: Testing EntityManager...");
            EntityManager em = emf.createEntityManager();
            try {
                em.getTransaction().begin();
                long count = (Long) em.createQuery("SELECT COUNT(o) FROM Order o").getSingleResult();
                System.out.println("Total orders in database: " + count);
                em.getTransaction().commit();
            } catch (Exception e) {
                System.out.println("Error counting orders: " + e.getMessage());
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
            } finally {
                em.close();
            }
            
            System.out.println("=== Database Connection Test Completed Successfully ===\n");
            
        } catch (Exception e) {
            System.out.println("=== Database Connection Test Failed ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== End of Error Report ===\n");
        }
    }

    public Order getOrderWithUserById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.id = :id", Order.class)
                .setParameter("id", id)
                .getSingleResult();
        } finally {
            em.close();
        }
    }
}


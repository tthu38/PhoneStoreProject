package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import model.Order;
import jakarta.persistence.TypedQuery;

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
    public List<Order> getOrdersByUserId(Integer userId) {
        EntityManager em = emf.createEntityManager();
        try {
            System.out.println("[OrderService] Getting orders for user ID: " + userId);
            TypedQuery<Order> query = em.createQuery(
                "SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.user.userID = :userId ORDER BY o.orderDate DESC", 
                Order.class);
            query.setParameter("userId", userId);
            List<Order> orders = query.getResultList();
            System.out.println("[OrderService] Found " + orders.size() + " orders for user ID: " + userId);
            return orders;
        } catch (Exception e) {
            System.out.println("[OrderService] Error getting orders for user ID " + userId + ": " + e.getMessage());
            e.printStackTrace();
            return new java.util.ArrayList<>();
        } finally {
            em.close();
        }
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

    // Lấy doanh thu theo tháng (key: yyyy-MM, value: tổng doanh thu)
    public Map<String, Double> getMonthlyRevenue() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Object[]> results = em.createNativeQuery(
                "SELECT FORMAT(OrderDate, 'yyyy-MM') AS Month, SUM(TotalAmount) " +
                "FROM Orders WHERE Status = 'Paid' " +
                "GROUP BY FORMAT(OrderDate, 'yyyy-MM')"
            ).getResultList();

            // Sắp xếp theo tháng tăng dần
            Map<String, Double> revenueMap = new java.util.TreeMap<>();
            for (Object[] row : results) {
                String month = (String) row[0];
                Double total = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
                revenueMap.put(month, total);
            }
            return revenueMap;
        } finally {
            em.close();
        }
    }

    // Lấy doanh thu theo brand (key: brand name, value: tổng doanh thu)
    public Map<String, Double> getBrandSalesData() {
        EntityManager em = emf.createEntityManager();
        try {
            List<Object[]> results = em.createNativeQuery(
                "SELECT b.BrandName, SUM(od.UnitPrice * od.Quantity) as TotalSales " +
                "FROM Orders o " +
                "JOIN OrderDetails od ON o.OrderID = od.OrderID " +
                "JOIN ProductVariants pv ON od.VariantID = pv.VariantID " +
                "JOIN ProductBase p ON pv.ProductBaseID = p.ProductBaseID " +
                "JOIN Brands b ON p.BrandID = b.BrandID " +
                "WHERE o.Status = 'Paid' " +
                "GROUP BY b.BrandID, b.BrandName " +
                "ORDER BY TotalSales DESC"
            ).getResultList();

            Map<String, Double> brandSalesMap = new java.util.LinkedHashMap<>();
            for (Object[] row : results) {
                String brandName = (String) row[0];
                Double totalSales = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;
                brandSalesMap.put(brandName, totalSales);
            }
            return brandSalesMap;
        } finally {
            em.close();
        }
    }
}


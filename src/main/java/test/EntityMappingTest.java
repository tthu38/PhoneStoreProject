package test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.metamodel.EntityType;
import model.Order;

public class EntityMappingTest {
    
    public static void main(String[] args) {
        System.out.println("=== Testing Entity Mapping ===");
        
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");
            EntityManager em = emf.createEntityManager();
            
            // Get entity metadata
            EntityType<Order> orderType = em.getMetamodel().entity(Order.class);
            System.out.println("Order entity attributes:");
            orderType.getDeclaredAttributes().forEach(attr -> {
                System.out.println("  - " + attr.getName() + " -> " + attr.getJavaType().getSimpleName());
            });
            
            // Test simple query
            System.out.println("\nTesting simple query...");
            long count = (Long) em.createQuery("SELECT COUNT(o) FROM Order o").getSingleResult();
            System.out.println("Total orders: " + count);
            
            // Test named query
            System.out.println("\nTesting named query...");
            var orders = em.createNamedQuery("Order.findAll", Order.class).getResultList();
            System.out.println("Orders from named query: " + orders.size());
            
            em.close();
            emf.close();
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
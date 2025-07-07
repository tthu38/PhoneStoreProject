package test;

import service.OrderService;

public class OrderServiceTestRunner {
    
    public static void main(String[] args) {
        System.out.println("Starting OrderService Database Test...");
        System.out.println("=====================================");
        
        try {
            OrderService.testDatabaseConnection();
            System.out.println("Test completed successfully!");
        } catch (Exception e) {
            System.out.println("Test failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
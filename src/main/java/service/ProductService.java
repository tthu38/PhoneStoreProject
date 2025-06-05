/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import model.Product;

/**
 *
 * @author ThienThu
 */
public class ProductService {
    private final GenericDAO<Product> productDAO = new GenericDAO<>(Product.class);
    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");
    
    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }
    public boolean hasNextPage(int page, int pageSize) {
        return productDAO.hasNextPage(page, pageSize);
    }
    public Product getProductById(int id) {
        return productDAO.findById(id);
    }
    public void addProduct(Product product) {
        if (!product.getIsDeleted()) {
            product.setIsDeleted(false);
        }
        productDAO.insert(product);
    }
    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }

    public void deleteProduct(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Product product = productDAO.findById(id);
            if (product != null) {
                product.setIsDeleted(!product.getIsDeleted());
                em.merge(product);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}

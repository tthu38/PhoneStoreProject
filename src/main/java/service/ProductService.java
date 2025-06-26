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
        if (!product.getIsActive()) {
            product.setIsActive(true);
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
            Product product = em.find(Product.class, id);
            if (product != null) {
                product.setIsActive(false);
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

    public List<Product> getProductsByName(String name) {
        return productDAO.findByName(name);
    }

    public List<Product> searchProductsByNameLike(String keyword) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                    "SELECT p FROM Product p WHERE p.name LIKE :keyword AND p.isActive = true", Product.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
        } finally {
            em.close();
        }
    }

   

}

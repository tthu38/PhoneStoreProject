/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

import model.ProductBrand;

/**
 *
 * @author ThienThu
 */
public class BrandService {
    private final GenericDAO<ProductBrand> productBrandDAO = new GenericDAO<>(ProductBrand.class);
    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");
    
    public List<ProductBrand> getAllBrands() {
        return productBrandDAO.getAll();
    }
}

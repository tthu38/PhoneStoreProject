package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Inventory;

public class InventoryService {
    private final GenericDAO<Inventory> inventoryIntegerGenericDAO = new GenericDAO<>(Inventory.class);
    static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");

    public Inventory findById(int id) {
        return inventoryIntegerGenericDAO.findById(id);
    }
}

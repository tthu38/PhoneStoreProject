/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public abstract class BaseDAO<T> {
    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");
    
    public abstract List<T> getAll();
    public abstract T findById(int id);
    public abstract boolean insert(T t);
    public abstract boolean update(T t);
    public abstract boolean delete(int id);
}

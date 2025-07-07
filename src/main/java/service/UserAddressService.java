/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import model.UserAddress;

import java.util.List;
                
public class UserAddressService {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");

    public List<UserAddress> getAllAddressesByUserId(Integer userId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<UserAddress> query = em.createNamedQuery("UserAddress.findByUserId", UserAddress.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean addAddress(UserAddress address) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(address);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean updateAddress(UserAddress address) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(address);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean deleteAddress(Integer addressId) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            UserAddress address = em.find(UserAddress.class, addressId);
            if (address != null) {
                em.remove(address);
                em.getTransaction().commit();
                return true;
            }
            em.getTransaction().rollback();
            return false;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }

    public UserAddress getAddressById(Integer addressId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(UserAddress.class, addressId);
        } finally {
            em.close();
        }
    }
}

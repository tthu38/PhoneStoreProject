/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author ThienThu
 */
public class GenericDAO<T> extends BaseDAO<T>{
    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public List<T> getAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery(entityClass.getSimpleName() + ".findAll", entityClass)
                    .getResultList();
        } finally {
            em.close();
        }
    }


    @Override
    public boolean insert(T t) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(t);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback(); // Rollback nếu có  lỗi
            System.out.println("LỖI INSERT: " + e.getMessage()); // In lỗi ra console
            e.printStackTrace(); // In toàn bộ lỗi
            return false;
        } finally {
            em.close();
        }
    }



    @Override
    public boolean update(T t) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(t); // Hibernate sẽ kiểm tra ID, nếu có thì cập nhật, không có thì thêm mới
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace(); // In lỗi ra console để debug
            return false;
        } finally {
            em.close();
        }
    }


    @Override
    public boolean delete(int id)  {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
                em.getTransaction().commit();
                return true; // Xóa thành công
            }
            em.getTransaction().rollback();
            return false; // Không tìm thấy entity để xóa
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e; // Nếu có lỗi, ném exception để xử lý
        } finally {
            em.close();
        }
    }

    @Override
    public T findById(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }
    public List<T> findByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery(entityClass.getSimpleName() + ".findByName", entityClass)
                    .setParameter("name", "%" + name + "%") // Tìm kiếm chuỗi chứa 'name'
                    .getResultList();
        } finally {
            em.close();
        }
    }
    
        public boolean hasNextPage(int page, int pageSize) {
        EntityManager em = emf.createEntityManager();
        try {
            List<T> result = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                    .setFirstResult(page * pageSize) // Lấy từ offset của trang tiếp theo
                    .setMaxResults(1) // Chỉ cần lấy 1 bản ghi để kiểm tra
                    .getResultList();

            return !result.isEmpty(); // Nếu có ít nhất 1 bản ghi, có trang tiếp theo
        } finally {
            em.close();
        }
    }

    
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

import java.util.Collections;
import java.util.List;
import utils.DaoUtils;

/**
 *
 * @author ThienThu
 * @param <T>
 */
public class GenericDAO<T> extends BaseDAO<T> {

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public List<T> getAll() {
        EntityManager em = emf.createEntityManager();  // tạo EntityManager để kết nối DB
        try {
            return em.createNamedQuery(entityClass.getSimpleName() + ".findAll", entityClass)
                    .getResultList();   // gọi NamedQuery động
        } finally {
            em.close(); // luôn đóng EntityManager sau khi dùng
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
            em.getTransaction().rollback(); // Rollback nếu có  lỗi, phục hồi lại trạng thái 
            System.out.println("LỖI INSERT: " + e.getMessage()); 
            e.printStackTrace(); 
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
            em.merge(t); 
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace(); 
            return false;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean delete(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            T entity = em.find(entityClass, id);
            if (entity != null) {
                em.remove(entity);
                em.getTransaction().commit();
                return true; 
            }
            em.getTransaction().rollback();
            return false; 
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e; 
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

    public boolean hasNextPage(int currentPage, int pageSize) {
    EntityManager em = emf.createEntityManager();
    try {
        // Trang tiếp theo sẽ bắt đầu từ offset = currentPage * pageSize
        List<T> result = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .setFirstResult(currentPage * pageSize) // offset trang kế tiếp
                .setMaxResults(1) // Chỉ cần 1 bản ghi để kiểm tra
                .getResultList();

        return !result.isEmpty(); // Có bản ghi => có trang tiếp theo
    } finally {
        em.close();
    }
}


    //tìm kiếm các bản ghi (entities) trong cơ sở dữ liệu theo 
    //một thuộc tính bất kỳ bằng Named Query trong JPA.
    public List<T> findByAttribute(String attributeName, Object value) {
        EntityManager em = emf.createEntityManager();
        List<T> resultList = Collections.emptyList(); // Tránh trả về null
        try {
            // Tạo tên NamedQuery dựa trên entity
            String queryName = entityClass.getSimpleName() + ".findBy" + DaoUtils.capitalizeFirstLetter(attributeName);
            System.out.println("Executing NamedQuery: " + queryName + " with value: " + value);

            // Thực thi NamedQuery
            resultList = em.createNamedQuery(queryName, entityClass)
                    .setParameter(attributeName, value)
                    .getResultList();
        } catch (IllegalArgumentException e) {
            System.err.println(" ERROR: NamedQuery '" + attributeName + "' không tồn tại hoặc tham số không hợp lệ: " + e.getMessage());
        } catch (PersistenceException e) {
            System.err.println("Database Error khi thực thi query: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(" Unexpected Error trong findByAttribute: " + e.getMessage());
        } finally {
            em.close();
        }
        return resultList;
    }
public List<T> findByNamedQuery(String queryName, String paramName, Object value) {
    EntityManager em = emf.createEntityManager();
    try {
        return em.createNamedQuery(queryName, entityClass)
                 .setParameter(paramName, value)
                 .getResultList();
    } finally {
        em.close();
    }
}


}

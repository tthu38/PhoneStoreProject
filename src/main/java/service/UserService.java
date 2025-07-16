package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import model.User;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import model.UserAddress;

public class UserService {

    private final GenericDAO<User> userDao;
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");
    @PersistenceContext
    private EntityManager entityManager;

    public UserService() {
        this.userDao = new GenericDAO<>(User.class);
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public Optional<User> getUserById(int id) {
        return Optional.ofNullable(userDao.findById(id));
    }

    public boolean register(User user) {
        if (getUserByEmail(user.getEmail()).isPresent()) {
            return false;
        }
        user.setCreatedAt(Instant.now());
        user.setIsActive(true);
        user.setRoleID(2);
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            String email = user.getEmail();
            String tempName = email.contains("@") ? email.substring(0, email.indexOf("@")) : email;
            user.setFullName(tempName);
        }
        return addUser(user);
    }

    public boolean addUser(User user) {
        user.setCreatedAt(Instant.now());
        user.setIsActive(true);
        return userDao.insert(user);
    }

    public boolean updateUser(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }

    public boolean deactivateUser(int id) {
        return getUserById(id).map(user -> {
            user.setIsActive(false);
            return userDao.update(user);
        }).orElse(false);
    }

    public boolean restoreUser(int id) {
        return getUserById(id).map(user -> {
            user.setIsActive(true);
            return userDao.update(user);
        }).orElse(false);
    }

    public boolean deleteUser(int id) {
        return userDao.delete(id);
    }

    public List<User> searchUsersByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findByName", User.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<User> getUserByEmail(String email) {
        List<User> users = userDao.findByAttribute("email", email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }

    public boolean changePassword(User user, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return false;
        }
        user.setPassword(newPassword); // Thay bằng hash nếu có
        return updateUser(user);
    }

    public void saveRememberToken(int userId, String token) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setRememberToken(token);
                em.merge(user);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public boolean isValidToken(int userId, String token) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.id = :userId AND u.rememberToken = :token AND u.isActive = true", User.class);
            query.setParameter("userId", userId);
            query.setParameter("token", token);
            return !query.getResultList().isEmpty();
        } finally {
            em.close();
        }
    }

    public void clearRememberToken(int userId) {
        saveRememberToken(userId, null);
    }

    public User findOrCreateGoogleUser(String email, String fullName, String picture, String provider, boolean isActive) {
        Optional<User> existingUser = getUserByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            boolean changed = false;

            if (user.getOauthProvider() == null || !user.getOauthProvider().equals(provider)) {
                user.setOauthProvider(provider);
                changed = true;
            }

            if (user.getFullName() == null || !user.getFullName().equals(fullName)) {
                user.setFullName(fullName);
                changed = true;
            }

            if (user.getPicture() == null || !user.getPicture().equals(picture)) {
                user.setPicture(picture);
                changed = true;
            }

            if (!Boolean.TRUE.equals(user.getIsOauthUser())) {
                user.setIsOauthUser(true);
                changed = true;
            }

            if (user.getPassword() == null || !user.getPassword().equals("GOOGLE_USER")) {
                user.setPassword("GOOGLE_USER");
                changed = true;
            }

            if (changed) {
                updateUser(user);
            }

            return user;
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setPicture(picture);
        newUser.setOauthProvider(provider);
        newUser.setIsOauthUser(true);
        newUser.setIsActive(isActive);
        newUser.setRoleID(2);
        newUser.setPassword("GOOGLE_USER");
        addUser(newUser);

        return getUserByEmail(email).orElse(newUser);
    }

    public User login(String phoneNumber, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findByPhone", User.class);
            query.setParameter("phoneNumber", phoneNumber);
            List<User> users = query.getResultList();
            if (!users.isEmpty()) {
                User user = users.get(0);
                if (user.getPassword().equals(password) && Boolean.TRUE.equals(user.getIsActive())) {
                    return user;
                }
            }
            return null;
        } finally {
            em.close();
        }
    }

    public Optional<User> checkRememberToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        String token = null;
        for (Cookie cookie : cookies) {
            if ("remember_token".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        if (token == null) {
            return Optional.empty();
        }
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findByRememberToken", User.class);
            query.setParameter("token", token);
            List<User> users = query.getResultList();
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } finally {
            em.close();
        }
    }

    public boolean isTokenValid(String token) {
        EntityManager em = emf.createEntityManager();
        try {
            User user = em.createNamedQuery("User.findByRememberToken", User.class)
                    .setParameter("token", token)
                    .getSingleResult();
            return user != null && user.getIsActive();
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

    public void invalidateToken(String token) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.createNamedQuery("User.updateRememberToken")
                    .setParameter("token", token)
                    .executeUpdate();
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public User getUserByToken(String token) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("User.findByRememberToken", User.class)
                    .setParameter("token", token)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<UserAddress> getUserAddressesByUserId(int userId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<UserAddress> query = em.createNamedQuery("UserAddress.findByUserId", UserAddress.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public boolean hasAddress(int userId) {
        List<UserAddress> addresses = getUserAddressesByUserId(userId);
        return addresses != null && !addresses.isEmpty();
    }

    public User findOrCreateOauthUser(String email, String fullName, String picture, String oauthId, String provider, boolean isActive) {
        // Tìm user theo OauthID trước
        List<User> usersByOauthId = userDao.findByAttribute("oauthId", oauthId);
        if (!usersByOauthId.isEmpty()) {
            return usersByOauthId.get(0);
        }
        // Nếu chưa có, kiểm tra theo email (tránh trùng)
        Optional<User> existingUser = getUserByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            boolean changed = false;

            if (user.getOauthId() == null || !user.getOauthId().equals(oauthId)) {
                user.setOauthId(oauthId);
                changed = true;
            }
            if (user.getOauthProvider() == null || !user.getOauthProvider().equals(provider)) {
                user.setOauthProvider(provider);
                changed = true;
            }
            if (user.getFullName() == null || !user.getFullName().equals(fullName)) {
                user.setFullName(fullName);
                changed = true;
            }
            if (user.getPicture() == null || !user.getPicture().equals(picture)) {
                user.setPicture(picture);
                changed = true;
            }
            if (!Boolean.TRUE.equals(user.getIsOauthUser())) {
                user.setIsOauthUser(true);
                changed = true;
            }
            if (user.getPassword() == null || !user.getPassword().equals(provider.toUpperCase() + "_USER")) {
                user.setPassword(provider.toUpperCase() + "_USER");
                changed = true;
            }
            if (changed) {
                updateUser(user);
            }
            return user;
        }
        // Nếu chưa có user, tạo mới
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setPicture(picture);
        newUser.setOauthId(oauthId);
        newUser.setOauthProvider(provider);
        newUser.setIsOauthUser(true);
        newUser.setIsActive(isActive);
        newUser.setRoleID(2); // Customer
        newUser.setPassword(provider.toUpperCase() + "_USER");
        addUser(newUser);

        return getUserByEmail(email).orElse(newUser);
    }

    public void updateUserInfoFromFacebook(User user) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(user); // Cập nhật thông tin user
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

    public Optional<User> getUserByOauthId(String oauthId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createNamedQuery("User.findByOauthId", User.class)
                    .setParameter("oauthId", oauthId)
                    .getResultList()
                    .stream().findFirst();
        } finally {
            em.close();
        }
    }

    public boolean updatePasswordByEmail(String email, String newPassword) {
        Optional<User> userOpt = getUserByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(newPassword); // Nên hash mật khẩu ở đây nếu có
            return updateUser(user);
        }
        return false;
    }

    public User findByEmailOrPhoneAndPassword(String login, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE (u.email = :login OR u.phoneNumber = :login) AND u.isActive = true",
                    User.class
            );
            query.setParameter("login", login);
            List<User> users = query.getResultList();

            for (User u : users) {
                if (u.getPassword() != null && u.getPassword().equals(password)) {
                    return u;
                }
            }
            return null;
        } finally {
            em.close();
        }
    }

}

package service;

import dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import org.json.JSONObject;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public class UserService {
    private final GenericDAO<User> userDao;
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("PhoneStorePU");

    public UserService() {
        this.userDao = new GenericDAO<>(User.class);
    }

    public List<User> getAllUsers() {
        return userDao.getAll();
    }

    public Optional<User> getUserById(Long id) {
        return Optional.ofNullable(userDao.findById(id.intValue()));
    }

    public boolean addUser(User user) {
        user.setCreatedAt(Instant.now());
        user.setIsActive(true);
        return userDao.insert(user);
    }

    public boolean updateUser(User user) {
        return userDao.update(user);
    }

    public boolean deactivateUser(Long id) {
        return getUserById(id).map(user -> {
            user.setIsActive(false);
            return userDao.update(user);
        }).orElse(false);
    }

    public boolean restoreUser(Long id) {
        return getUserById(id).map(user -> {
            user.setIsActive(true);
            return userDao.update(user);
        }).orElse(false);
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
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findByEmail", User.class);
            query.setParameter("email", email);
            List<User> users = query.getResultList();
            return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
        } finally {
            em.close();
        }
    }

    public boolean changePassword(User user, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) return false;
        user.setPassword(newPassword); // Thay bằng hash nếu có
        return updateUser(user);
    }

    public void saveRememberToken(Long userId, String token) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            if (user != null) {
                user.setRememberToken(token);
                em.merge(user);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }

    public boolean isValidToken(Long userId, String token) {
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

    public void clearRememberToken(Long userId) {
        saveRememberToken(userId, null);
    }

    public User findOrCreateGoogleUser(String email, String fullName, String picture, String oauthProvider, boolean isOauthUser) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<User> query = em.createNamedQuery("User.findByEmail", User.class);
            query.setParameter("email", email);
            List<User> users = query.getResultList();
            User user;
            if (users.isEmpty()) {
                user = new User();
                user.setEmail(email);
                user.setFullName(fullName);
                user.setPicture(picture);
                user.setIsOauthUser(isOauthUser);
                user.setOauthProvider(oauthProvider);
                user.setIsActive(true);
                user.setRole("CUSTOMER");
                user.setCreatedAt(Instant.now());
                user.setPassword("GOOGLE_OAUTH");
                em.persist(user);
            } else {
                user = users.get(0);
                user.setFullName(fullName);
                user.setPicture(picture);
                user.setIsOauthUser(isOauthUser);
                user.setOauthProvider(oauthProvider);
                user.setIsActive(true);
                em.merge(user);
            }
            em.getTransaction().commit();
            return user;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return null;
        } finally {
            em.close();
        }
    }

    public void setupUserSession(HttpServletRequest request, HttpServletResponse response, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("userFullName", user.getFullName());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("loggedIn", true);
        if (user.getRememberToken() != null) {
            Cookie rememberCookie = new Cookie("remember_token", user.getRememberToken());
            rememberCookie.setMaxAge(60 * 60 * 24 * 30);
            rememberCookie.setPath("/");
            rememberCookie.setHttpOnly(true);
            response.addCookie(rememberCookie);
        }
    }

    public Optional<User> checkRememberToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return Optional.empty();
        String token = null;
        for (Cookie cookie : cookies) {
            if ("remember_token".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }
        if (token == null) return Optional.empty();
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

    public User login(String email, String password) {
        Optional<User> userOpt = getUserByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password) && Boolean.TRUE.equals(user.getIsActive())) {
                return user;
            }
        }
        return null;
    }

    public boolean register(User user) {
        if (getUserByEmail(user.getEmail()).isPresent()) {
            return false;
        }
        user.setCreatedAt(Instant.now());
        user.setIsActive(true);
        user.setRole("CUSTOMER");
        return addUser(user);
    }
} 
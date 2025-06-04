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
        user.setCreateDate(Instant.now());
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

    public User processGoogleUserInfo(String userInfoJson) {
        try {
            JSONObject userInfo = new JSONObject(userInfoJson);
            String email = userInfo.optString("email");
            String fullname = userInfo.optString("name");
            if (email.isEmpty() || fullname.isEmpty()) return null;

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
                    user.setFullname(fullname);
                    user.setIsActive(true);
                    user.setRoleId(2); 
                    user.setCreateDate(Instant.now());
                    user.setPassword("GOOGLE_OAUTH");
                    em.persist(user);
                } else {
                    user = users.get(0);
                    user.setFullname(fullname);
                    em.merge(user);
                }
                user.setRememberToken("token"); 
                em.merge(user);
                em.getTransaction().commit();
                return user;
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                return null;
            } finally {
                em.close();
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void setupUserSession(HttpServletRequest request, HttpServletResponse response, User user) {
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("userFullname", user.getFullname());
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("userRole", user.getRoleId());
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
        user.setCreateDate(Instant.now());
        user.setIsActive(true);
        user.setRoleId(2);
        return addUser(user);
    }

    public User getUserInfoFromGoogle(String accessToken) throws Exception {
        String link = utils.GoogleUtils.getConfig("google.userinfo.url") + accessToken;
        String response = org.apache.http.client.fluent.Request.Get(link).execute().returnContent().asString();
        com.google.gson.JsonObject googleUser = new com.google.gson.Gson().fromJson(response, com.google.gson.JsonObject.class);
        User user = new User();
        user.setEmail(googleUser.get("email").getAsString());
        user.setVerifiedEmail(googleUser.get("verified_email").getAsBoolean());
        user.setFullname(googleUser.get("name").getAsString());
        user.setPicture(googleUser.get("picture").getAsString());
        user.setGoogleId(googleUser.get("id").getAsString());
        return user;
    }
} 
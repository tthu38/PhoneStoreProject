package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import model.User;

import java.io.IOException;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private static final Set<String> ADMIN_ONLY_PAGES = new HashSet<>(Arrays.asList(
            "/layout/admin-header.jsp",
            "/layout/sidebar.jsp",
            "/product/DiscountManagement.jsp",
            "/product/DiscountProduct.jsp",
            "/product/UpdateProduct.jsp",
            "/product/ProductCreate.jsp",
            "/user/adduser.jsp",
            "/user/edituser.jsp",
            "/user/listuser.jsp",
            "/paypal/cancel.jsp",
            "/admin/dashboard/dashboard.jsp"
    ));

    private static final Set<String> USER_ONLY_PAGES = new HashSet<>(Arrays.asList(
            "/orders/detail_fragment.jsp",
            "/orders/list.jsp",
            "/orders/view.jsp",
            "/cart/cart.jsp",
            "/cart/confirm.jsp",
            "/order/success.jsp",
            "/paypal/PayPalResult.jsp",
            "/paypal/cart-payment.jsp",
            "/paypal/success.jsp",
            "/user/editprofile.jsp",
            "/user/profile.jsp",
            "/user/order-history.jsp",
            "/user/userupdate.jsp",
            "/user/registerfb.jsp"
    ));

    private static final Set<String> PUBLIC_PAGES = new HashSet<>(Arrays.asList(
            "/indexFirst.jsp",
            "/user/login.jsp",
            "/user/register.jsp",
            "/user/forgotpassword.jsp",
            "/user/registerfb.jsp",
            "/templates/header.jsp",
            "/templates/footer.jsp",
            "/templates/googletop.jsp",
            "/product/ProductList.jsp",
            "/product/ProductDetail.jsp",
            "/product/BestSeller.jsp",
            "/login",
            "/products"
    ));

    private static final Set<String> STATIC_RESOURCES = new HashSet<>(Arrays.asList(
            ".css", ".js", ".png", ".jpg", ".jpeg", ".gif", ".ico", ".woff", ".woff2", ".ttf"
    ));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        // Nếu là tài nguyên tĩnh hoặc trang public thì cho qua
        if (isStaticResource(path) || isPublicPage(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            res.sendRedirect(contextPath + "/user/login.jsp");
            return;
        }

        String role = (user.getRoleID() == 1) ? "admin" : "user";

        if (!hasPermission(path, role)) {
            if ("admin".equals(role)) {
                res.sendRedirect(contextPath + "/admin/dashboard/dashboard.jsp");
            } else {
                res.sendRedirect(contextPath + "/indexFirst.jsp");
            }
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isStaticResource(String path) {
        return STATIC_RESOURCES.stream().anyMatch(path::endsWith);
    }

    private boolean isPublicPage(String path) {
        return PUBLIC_PAGES.contains(path) || path.equals("/");
    }

    private boolean hasPermission(String path, String role) {
        if ("admin".equals(role)) {
            return !USER_ONLY_PAGES.contains(path); // admin KHÔNG vào được trang user
        }

        if ("user".equals(role)) {
            return !ADMIN_ONLY_PAGES.contains(path); // user KHÔNG vào được trang admin
        }

        return false;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}

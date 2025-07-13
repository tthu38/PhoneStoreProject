package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import model.Category;
import model.Product;
import model.ProductBrand;
import model.ProductStock;
import model.ProductVariant;
import org.json.JSONArray;
import org.json.JSONObject;
import service.BrandService;
import service.InventoryService;
import service.ProductService;
import service.ProductStockService;
import service.ProductVariantService;
import utils.ProductUtils;

@WebServlet(name = "ProductServlet", urlPatterns = { "/products" })
@MultipartConfig
public class ProductServlet extends HttpServlet {

    private ProductService productService;
    private ProductVariantService productVariantService;
    private ProductStockService productStockService;
    private BrandService brandService;
    private static final String INTERACTION_API_URL = "http://localhost:5555/api/interaction";
    private static final String SUGGESTION_API_URL = "http://localhost:5555/api";

    @Override
    public void init() {
        productService = new ProductService();
        productVariantService = new ProductVariantService();
        productStockService = new ProductStockService();
        brandService = new BrandService();
    }

    // Gửi tương tác đến Flask API
    private void logInteraction(int userId, int productId, String interactionType) {
        try {
            String jsonPayload = String.format("{\"user_id\":%d,\"product_id\":%d,\"interaction_type\":\"%s\"}",
                    userId, productId, interactionType);
            URL url = new URL(INTERACTION_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(jsonPayload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String errorResponse = br.lines().collect(Collectors.joining());
                    System.out
                            .println("Lỗi API /api/interaction: Code=" + responseCode + ", Response=" + errorResponse);
                }
            } else {
                System.out.println(
                        "Tương tác " + interactionType + " đã lưu: UserID=" + userId + ", ProductID=" + productId);
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println("Lỗi gọi API /api/interaction: UserID=" + userId + ", ProductID=" + productId +
                    ", Type=" + interactionType + ", Error=" + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        switch (action) {
            case "create":
                List<Product> existingProducts = productService.getAllProducts();
                request.setAttribute("productList", existingProducts);
                request.getRequestDispatcher("/product/ProductCreate.jsp").forward(request, response);
                break;
            case "delete":
                deleteProduct(request, response);
                break;
            case "update":
                sendToUpdateProduct(request, response);
                break;
            case "find":
                searchProduct(request, response);
                break;
            case "findAdmin":
                searchProductAdmin(request, response);
                break;
            case "productDetail":
                detailProduct(request, response);
                break;
            case "productBestSeller":
                getProductBestSeller(request, response);
                break;
            case "manageDiscounts":
                showDiscountManagement(request, response);
                break;
            case "showDiscountedProducts":
                showDiscountedProducts(request, response);
                break;
            case "removeDiscount":
                removeDiscount(request, response);
                break;
            case "search":
                searchProductsKeyWord(request, response);
                break;

            default:
                listProducts(request, response);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int page = 1;
        int pageSize = 10;

        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");

        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }
        if (pageSizeStr != null && !pageSizeStr.isEmpty()) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        List<Product> allProducts = productService.getAllProducts();

        int totalProducts = allProducts.size();
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        int fromIndex = (page - 1) * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalProducts);
        List<Product> pagedProducts = allProducts.subList(fromIndex, toIndex);

        Map<Integer, Integer> stockMap = productStockService.getProductStockQuantities();

        request.setAttribute("products", pagedProducts);
        request.setAttribute("productStockQuantity", stockMap);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);

        request.getRequestDispatcher("product/ProductList.jsp").forward(request, response);
    }

    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            productService.deleteProduct(id);
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ");
        }
    }

    private void sendToUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("productId"));

            Product product = productService.getProductById(productId);
            request.setAttribute("product", product);

            String formattedCreateAt = ProductUtils.formatInstantForDateTimeLocal(product.getCreateAt());
            request.setAttribute("formattedCreateAt", formattedCreateAt);

            List<ProductVariant> productVariants = productVariantService.getAllProductVariants(productId);
            HashMap<ProductVariant, Integer> productVariantQuantity = new HashMap<>();
            for (ProductVariant productVariant : productVariants) {
                ProductStock stock = productStockService.getStockByVariantId(productVariant.getId());
                int amount = (stock == null) ? 0 : stock.getAmount();
                productVariantQuantity.put(productVariant, amount);
            }

            List<Map.Entry<ProductVariant, Integer>> entryList = new ArrayList<>(productVariantQuantity.entrySet());
            request.setAttribute("productVariantEntries", entryList);

            List<ProductBrand> brands = brandService.getAllBrands();
            request.setAttribute("brands", brands);

            request.getRequestDispatcher("/product/UpdateProduct.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            throw new NumberFormatException("ID phải là integer");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "create":
                createProduct(request, response);
                break;
            case "update":
                updateProduct(request, response);
                break;
            case "applyDiscount":
                applyDiscount(request, response);
                break;
            case "removeDiscount":
                removeDiscount(request, response);
                break;
        }
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            // Nhận dữ liệu từ form
            String selectedProductId = request.getParameter("productId");
            String productName = request.getParameter("productName");
            String description = request.getParameter("description");
            String color = request.getParameter("color");
            String romStr = request.getParameter("rom");
            String brandIDStr = request.getParameter("brandID");
            String priceStr = request.getParameter("price");
            String stockStr = request.getParameter("stock");

            if ((selectedProductId == null || selectedProductId.isEmpty())
                    && (productName == null || productName.trim().isEmpty())) {
                throw new IllegalArgumentException("Vui lòng chọn sản phẩm có sẵn hoặc nhập tên sản phẩm mới.");
            }

            int rom = Integer.parseInt(romStr);
            int brandID = Integer.parseInt(brandIDStr);
            BigDecimal price = new BigDecimal(priceStr);
            int stock = Integer.parseInt(stockStr);

            Part filePart = request.getPart("thumbnailImage");
            String relativePath = ProductUtils.saveImage(filePart, getServletContext(), "default.png");

            Product product;
            boolean isNewProduct = false;

            if (selectedProductId != null && !selectedProductId.isEmpty()) {
                int productId = Integer.parseInt(selectedProductId);
                product = productService.getProductById(productId);
                if (product == null) {
                    throw new IllegalArgumentException("Không tìm thấy sản phẩm có ID: " + productId);
                }
            } else {

                product = new Product();
                product.setName(productName);
                product.setDescription(description);
                product.setThumbnailImage(relativePath);
                product.setIsActive(true);
                product.setCreateAt(Instant.now());

                Category defaultCategory = new Category();
                defaultCategory.setId(1);
                product.setCategory(defaultCategory);

                ProductBrand brand = new ProductBrand();
                brand.setId(brandID);
                product.setBrand(brand);

                productService.addProduct(product);
                isNewProduct = true;
            }

            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setRom(rom);
            variant.setColor(color);
            variant.setPrice(price);
            variant.setIsActive(true);
            variant.setImageURLs(relativePath);

            // Lưu variant
            productVariantService.addProductVariant(variant);

            // Tạo Stock cho variant
            ProductStock productStock = new ProductStock();
            productStock.setVariant(variant);
            productStock.setAmount(stock);
            productStock.setInventoryID(new InventoryService().findById(1)); // giả định kho mặc định
            new ProductStockService().addProductStock(productStock);

            response.sendRedirect("products");

        } catch (Exception e) {
            e.printStackTrace();

            request.setAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());

            List<Product> existingProducts = productService.getAllProducts();
            request.setAttribute("productList", existingProducts);

            request.getRequestDispatcher("/product/ProductCreate.jsp").forward(request, response);
        }
    }

    private void searchProductsKeyWord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("term"); // 'term' là mặc định của Select2
        List<Product> products;

        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.searchProductsByNameLike(keyword); // cần hàm LIKE trong ProductService
        } else {
            products = productService.getAllProducts();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        out.print("[");
        for (int i = 0; i < products.size(); i++) {
            Product p = products.get(i);

            out.print(String.format("{\"id\": %d, \"text\": \"%s\"}", p.getId(), p.getName()));
            if (i < products.size() - 1) {
                out.print(",");
            }
        }
        out.print("]");
        out.flush();
    }

    @Override
    public String getServletInfo() {
        return "Servlet xử lý thêm sản phẩm và biến thể";
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            int productId = Integer.parseInt(request.getParameter("productId"));
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            int brandId = Integer.parseInt(request.getParameter("brandId"));

            String[] colors = request.getParameterValues("color[]");
            String[] roms = request.getParameterValues("rom[]");
            String[] prices = request.getParameterValues("prices[]");
            String[] quantities = request.getParameterValues("quantities[]");

            Part filePart = request.getPart("thumbnailImage");
            String thumbnailPath = null;
            if (filePart != null && filePart.getSize() > 0) {
                thumbnailPath = ProductUtils.saveImage(filePart, getServletContext(), "default.png");
            } else {
                Product existingProduct = productService.getProductById(productId);
                thumbnailPath = existingProduct.getThumbnailImage();
            }

            if (colors == null || roms == null || prices == null || quantities == null
                    || colors.length != roms.length || colors.length != prices.length
                    || colors.length != quantities.length) {
                throw new IllegalArgumentException("Dữ liệu variant không hợp lệ hoặc không đồng bộ");
            }

            productService.updateProductDetails(productId, name, description, thumbnailPath, brandId, colors, roms,
                    prices, quantities);

            response.sendRedirect(request.getContextPath() + "/products");

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Dữ liệu không hợp lệ: " + e.getMessage());
            request.getRequestDispatcher("/product/UpdateProduct.jsp").forward(request, response); // Sửa forward
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/product/UpdateProduct.jsp").forward(request, response); // Sửa forward
        } catch (Exception e) {
            request.setAttribute("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            request.getRequestDispatcher("/product/UpdateProduct.jsp").forward(request, response); // Sửa forward
        }
    }

    private void searchProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        int pageSize = 10;

        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");

        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }

        if (pageSizeStr != null) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        String categoryID = request.getParameter("categoryId");
        String brandID = request.getParameter("brandId"); // Lọc theo brand
        String sort = request.getParameter("sort");
        String searchName = request.getParameter("searchName");

        List<Product> products = productService.searchAndFilterProducts(searchName, categoryID, brandID, sort, page,
                pageSize);

        int totalProducts = productService.countFilteredProducts(searchName, categoryID, brandID);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Category> categories = productService.getAllCategories();
        List<ProductBrand> brands = new BrandService().getAllBrands();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("brands", brands);
        request.setAttribute("selectedCategory", categoryID);
        request.setAttribute("selectedBrand", brandID);
        request.setAttribute("selectedSort", sort);
        request.setAttribute("searchName", searchName);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/product/productListCart.jsp").forward(request, response);
    }

    private void detailProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("productId"));
            int userId = Optional.ofNullable(request.getSession(false))
                    .map(s -> (Integer) s.getAttribute("userId"))
                    .orElse(1); // Giá trị mặc định để test

            // Ghi tương tác view và click
            logInteraction(userId, id, "purchase");
            logInteraction(userId, id, "click");
            logInteraction(userId, id, "cart");

            // Lấy chi tiết sản phẩm
            List<Map<String, Object>> productDetails = productService.detailProduct(id);
            if (productDetails.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Sản phẩm không tồn tại");
                return;
            }

            // Lấy stock và variants
            Map<Integer, Integer> stockMap = productStockService.getStockByProductId(id).stream()
                    .collect(HashMap::new, (m, ps) -> m.put(ps.getVariant().getId(), ps.getAmount()), HashMap::putAll);
            List<ProductVariant> productVariants = productVariantService.getVariantsByProductId(id);

            // Gọi API gợi ý (chỉ content-based)
            List<Map<String, String>> suggestedProducts = new ArrayList<>();
            try {
                String apiUrl = SUGGESTION_API_URL + "?id=" + id + "&user_id=" + userId + "&method=content";
                URL url = new URL(apiUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String json = in.lines().collect(Collectors.joining());
                    JSONArray arr = new JSONObject(json).getJSONArray("san pham goi y");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        Map<String, String> item = new HashMap<>();
                        item.put("id", String.valueOf(obj.getInt("id")));
                        item.put("name", obj.getString("name"));
                        item.put("price", obj.getString("price"));
                        item.put("image", obj.optString("image", "default.jpg"));
                        suggestedProducts.add(item);
                        // Ghi tương tác view cho sản phẩm gợi ý
                        logInteraction(userId, obj.getInt("id"), "view");
                    }
                }
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("Lỗi gọi API gợi ý: " + e.getMessage());

            }

            // Gửi dữ liệu sang JSP
            request.setAttribute("productDetails", productDetails);
            request.setAttribute("productVariants", productVariants);
            request.setAttribute("suggestedProducts", suggestedProducts);
            request.setAttribute("stockMap", stockMap);
            request.getRequestDispatcher("/product/ProductDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID phải là số nguyên");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi hệ thống: " + e.getMessage());
        }
    }

    private void getProductBestSeller(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Map<String, Object>> products = productService.getMostOrderedProducts(20);
        request.setAttribute("products", products);
        request.getRequestDispatcher("product/BestSeller.jsp").forward(request, response);
    }

    private void showDiscountManagement(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Category> categories = productService.getAllCategories();
        List<Product> discountedProducts = productService.getDiscountedProducts(0, 10); // Lấy sản phẩm có discount
        request.setAttribute("categories", categories);
        request.setAttribute("discountedProducts", discountedProducts);
        request.getRequestDispatcher("/product/DiscountManagement.jsp").forward(request, response);
    }

    private void showDiscountedProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        int recordsPerPage = 10;

        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }

        List<Product> discountedProducts = productService.getDiscountedProducts((page - 1) * recordsPerPage,
                recordsPerPage);
        int noOfRecords = productService.getNumberOfDiscountedProducts();
        int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);

        request.setAttribute("discountedProducts", discountedProducts);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", noOfPages);

        request.getRequestDispatcher("/product/DiscountProduct.jsp").forward(request, response);
    }

    private void removeDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("searchName");
            String categoryId = request.getParameter("categoryId");

            // Log input
            System.out.println("Removing discount - searchName: " + name + ", categoryId: " + categoryId);

            // Validate input
            if ((name == null || name.trim().isEmpty()) && (categoryId == null || categoryId.trim().isEmpty())) {
                throw new IllegalArgumentException("Please provide a product name or select a category.");
            }

            // Call service
            productService.removeDiscount(name, categoryId);
            request.setAttribute("successMessage", "Discounts have been successfully removed!");

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Failed to remove discounts: " + e.getMessage());
        }

        showDiscountManagement(request, response);
    }

   

    private void searchProductAdmin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int page = 1;
        int pageSize = 10;

        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");

        if (pageStr != null && !pageStr.trim().isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        if (pageSizeStr != null && !pageSizeStr.trim().isEmpty()) {
            pageSize = Integer.parseInt(pageSizeStr);
        }

        String categoryID = request.getParameter("categoryId");
        String brandID = request.getParameter("brandId");
        String sort = request.getParameter("sort");
        String searchName = request.getParameter("searchName");
        String status = request.getParameter("status");

        // Gọi service
        List<Product> products = productService.searchAndFilterProductsAdmin(
                searchName, categoryID, brandID, status, sort, page, pageSize);

        int totalProducts = productService.countFilteredProductsAdmin(searchName, categoryID, brandID, status);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Category> categories = productService.getAllCategories();
        List<ProductBrand> brands = brandService.getAllBrands();
        Map<Integer, Integer> stockMap = productStockService.getProductStockQuantities();
        request.setAttribute("productStockQuantity", stockMap);

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("brands", brands);
        request.setAttribute("selectedCategory", categoryID);
        request.setAttribute("selectedBrand", brandID);
        request.setAttribute("selectedSort", sort);
        request.setAttribute("searchName", searchName);
        request.setAttribute("status", status);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize); // Thêm dòng này

        request.getRequestDispatcher("/product/ProductList.jsp").forward(request, response);
    }

   private void applyDiscount(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    try {
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("searchName"); // tìm theo tên sản phẩm
        String categoryId = request.getParameter("categoryId");
        int discountPercent = Integer.parseInt(request.getParameter("discountPercent"));
        String expireDateStr = request.getParameter("expireDate");

        if ((name == null || name.trim().isEmpty()) && (categoryId == null || categoryId.trim().isEmpty())) {
            throw new IllegalArgumentException("Vui lòng nhập tên sản phẩm hoặc chọn danh mục để áp dụng giảm giá.");
        }

        LocalDate expireDate = LocalDate.parse(expireDateStr);

        // Gọi service xử lý theo name + categoryId
        productService.applyDiscount(name, categoryId, discountPercent, expireDate);

        request.setAttribute("successMessage", "✅ Giảm giá đã được áp dụng thành công!");

    } catch (IllegalArgumentException e) {
        request.setAttribute("errorMessage", "⚠ " + e.getMessage());
    } catch (Exception e) {
        e.printStackTrace();
        request.setAttribute("errorMessage", "❌ Áp dụng khuyến mãi thất bại: " + e.getMessage());
    }

    // Reload lại trang quản lý
    showDiscountManagement(request, response);
}


}

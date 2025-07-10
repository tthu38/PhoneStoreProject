package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    public void init() {
        productService = new ProductService();
        productVariantService = new ProductVariantService();
        productStockService = new ProductStockService();
        brandService = new BrandService();
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
            List<Map<String, Object>> productDetails = productService.detailProduct(id);
            List<ProductStock> productStocks = productStockService.getStockByProductId(id);
            Map<Integer, Integer> stockMap = new HashMap<>();

            for (ProductStock ps : productStocks) {
                stockMap.put(ps.getVariant().getId(), ps.getAmount());
            }

            if (productDetails.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product không được tìm thấy");
                return;
            }

            List<ProductVariant> productVariants = productVariantService.getVariantsByProductId(id);

            // --- Gọi Flask API ---
            List<Map<String, String>> suggestedProducts = new ArrayList<>();
            try {
                String apiURL = "http://localhost:5555/api?id=" + id;
                URL url = new URL(apiURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                in.close();
                conn.disconnect();

                JSONObject json = new JSONObject(jsonBuilder.toString());
                JSONArray arr = json.getJSONArray("san pham goi y");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Map<String, String> item = new HashMap<>();
                    item.put("name", obj.getString("name"));
                    item.put("price", obj.getString("price"));
                    item.put("image", obj.optString("image", "default.jpg"));
                    suggestedProducts.add(item);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            // Gửi dữ liệu sang JSP
            request.setAttribute("productDetails", productDetails);
            request.setAttribute("productVariants", productVariants);
            request.setAttribute("suggestedProducts", suggestedProducts);
            request.setAttribute("stockMap", stockMap);
            request.getRequestDispatcher("/product/ProductDetail.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID must be an integer");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred");
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
        request.setAttribute("categories", categories);
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

            productService.removeDiscount(name, categoryId);

            request.setAttribute("successMessage", "Discounts have been successfully removed!");

            showDiscountManagement(request, response);
        } catch (Exception e) {

            request.setAttribute("errorMessage", "Failed to remove discounts: " + e.getMessage());
            showDiscountManagement(request, response);
        }
    }

    private void applyDiscount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String name = request.getParameter("searchName");
            String categoryId = request.getParameter("categoryId");
            int discountPercent = Integer.parseInt(request.getParameter("discountPercent"));
            String expireDateStr = request.getParameter("expireDate");

            LocalDate expireDate = LocalDate.parse(expireDateStr);

            productService.applyDiscount(name, categoryId, discountPercent, expireDate);

            request.setAttribute("successMessage", "Discounts have been successfully applied!");

            showDiscountManagement(request, response);
        } catch (Exception e) {

            request.setAttribute("errorMessage", "Failed to apply discounts: " + e.getMessage());
            showDiscountManagement(request, response);
        }
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

}

package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Category;
import model.Product;
import model.ProductBrand;
import model.ProductStock;
import model.ProductVariant;
import service.BrandService;
import service.InventoryService;
import service.ProductService;
import service.ProductStockService;
import service.ProductVariantService;
import utils.ProductUtils;

@WebServlet(name = "ProductServlet", urlPatterns = {"/products"})
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
            case "search":
                searchProductsKeyWord(request, response);
                break;

            default:
                listProducts(request, response);
        }
    }

    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> products = productService.getAllProducts();
        request.setAttribute("products", products);
        ProductStockService stockService = new ProductStockService();
        var stockMap = stockService.getProductStockQuantities();

        request.setAttribute("productStockQuantity", stockMap);

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

        // Lấy product
        Product product = productService.getProductById(productId);
        request.setAttribute("product", product);
        
        String formattedCreateAt = ProductUtils.formatInstantForDateTimeLocal(product.getCreateAt());
        request.setAttribute("formattedCreateAt", formattedCreateAt);

        // Lấy danh sách ProductVariant và số lượng
        List<ProductVariant> productVariants = productVariantService.getAllProductVariants(productId);
        HashMap<ProductVariant, Integer> productVariantQuantity = new HashMap<>();
        for (ProductVariant productVariant : productVariants) {
            ProductStock stock = productStockService.getStockByVariantId(productVariant.getId());
            int amount = (stock == null) ? 0 : stock.getAmount();
            productVariantQuantity.put(productVariant, amount);
        }


        // CHUYỂN SANG ENTRY LIST CHO JSP
        List<Map.Entry<ProductVariant, Integer>> entryList = new ArrayList<>(productVariantQuantity.entrySet());
        request.setAttribute("productVariantEntries", entryList);

        // Lấy danh sách brand
        List<ProductBrand> brands = brandService.getAllBrands(); 
        request.setAttribute("brands", brands);

        // Forward tới trang JSP
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
        }
    }

    private void createProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            String selectedProductId = request.getParameter("productId");
            String productName = request.getParameter("productName");
            String description = request.getParameter("description");
            String color = request.getParameter("color");
            String romStr = request.getParameter("rom");
            String brandIDStr = request.getParameter("brandID");
            Integer stock = Integer.parseInt(request.getParameter("stock"));
            String priceStr = request.getParameter("price");

            if ((selectedProductId == null || selectedProductId.isEmpty())
                    && (productName == null || productName.trim().isEmpty())) {
                throw new IllegalArgumentException("Vui lòng chọn sản phẩm có sẵn hoặc nhập tên sản phẩm mới.");
            }

            int rom = Integer.parseInt(romStr);
            int brandID = Integer.parseInt(brandIDStr);
            BigDecimal price = new BigDecimal(priceStr);

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
                // Tạo mới sản phẩm
                product = new Product();
                product.setName(productName);
                product.setDescription(description);
                product.setThumbnailImage(relativePath); // <-- Chỉ set khi là sản phẩm mới
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

// Tạo variant mới
            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setColor(color);
            variant.setRom(rom);
            variant.setPrice(price);
            variant.setIsActive(true);

// Lưu ảnh variant luôn trong cả 2 trường hợp
            variant.setImageURLs(relativePath);

            productVariantService.addProductVariant(variant);

            ProductStock productStock = new ProductStock();
            productStock.setVariant(variant);
            productStock.setAmount(stock);
            productStock.setInventoryID(new InventoryService().findById(1));
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
            // Nếu muốn hiện thêm brand hay description thì chỉnh ở đây
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
        request.setCharacterEncoding("UTF-8"); // Đảm bảo mã hóa UTF-8

        // Lấy các tham số
        int productId = Integer.parseInt(request.getParameter("productId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int brandId = Integer.parseInt(request.getParameter("brandId"));

        // Lấy mảng variant
        String[] colors = request.getParameterValues("color[]");
        String[] roms = request.getParameterValues("rom[]");
        String[] prices = request.getParameterValues("prices[]");
        String[] quantities = request.getParameterValues("quantities[]");

        // Xử lý ảnh thumbnail
        Part filePart = request.getPart("thumbnailImage");
        String thumbnailPath = null;
        if (filePart != null && filePart.getSize() > 0) {
            thumbnailPath = ProductUtils.saveImage(filePart, getServletContext(), "default.png");
        } else {
            Product existingProduct = productService.getProductById(productId);
            thumbnailPath = existingProduct.getThumbnailImage();
        }

        // Kiểm tra mảng variant
        if (colors == null || roms == null || prices == null || quantities == null || 
            colors.length != roms.length || colors.length != prices.length || colors.length != quantities.length) {
            throw new IllegalArgumentException("Dữ liệu variant không hợp lệ hoặc không đồng bộ");
        }

        // Cập nhật sản phẩm
        productService.updateProductDetails(productId, name, description, thumbnailPath, brandId, colors, roms, prices, quantities);

        // Chuyển hướng về ProductList.jsp
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
             throws ServletException, IOException{
        int page = 1;
        int pageSize = 8;
        
        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");
        if(pageStr != null){
                page = Integer.parseInt(pageStr);
            }

            if(pageSizeStr != null){
                pageSize = Integer.parseInt(pageSizeStr);
            }
        String categoryID = request.getParameter("categoryId");
        String sort = request.getParameter("sort"); //giá
        String searchName = request.getParameter("searchName");
        
        
    }

}

package controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import model.Category;
import model.Product;
import model.ProductBrand;
import model.ProductStock;
import model.ProductVariant;
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

    @Override
    public void init() {
        productService = new ProductService();
        productVariantService = new ProductVariantService();
        productStockService = new ProductStockService();
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
            int id = Integer.parseInt(request.getParameter("productId"));
            productService.deleteProduct(id);
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID sản phẩm không hợp lệ");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("create".equals(action)) {
            createProduct(request, response);
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

            // Kiểm tra bắt buộc
            if ((selectedProductId == null || selectedProductId.isEmpty())
                    && (productName == null || productName.trim().isEmpty())) {
                throw new IllegalArgumentException("Vui lòng chọn sản phẩm có sẵn hoặc nhập tên sản phẩm mới.");
            }

            int rom = Integer.parseInt(romStr);
            int brandID = Integer.parseInt(brandIDStr);

            BigDecimal price = new BigDecimal(priceStr);

            Part filePart = request.getPart("thumbnailImage");
            String relativePath = null;
            if (filePart != null && filePart.getSize() > 0) {
                String contentType = filePart.getContentType();
                if (!contentType.startsWith("image/")) {
                    throw new IllegalArgumentException("Chỉ được tải lên tệp hình ảnh.");
                }
                relativePath = ProductUtils.saveImage(filePart, getServletContext(), "default.png");
            }

            Product product;

            if (selectedProductId != null && !selectedProductId.isEmpty()) {
                // Thêm biến thể vào sản phẩm có sẵn
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
                product.setThumbnailImage((relativePath != null) ? relativePath : "default.png");
                product.setIsActive(true);
                product.setCreateAt(Instant.now());

                Category defaultCategory = new Category();
                defaultCategory.setId(1); // Giả định có sẵn category ID = 1
                product.setCategory(defaultCategory);

                ProductBrand brand = new ProductBrand();
                brand.setId(brandID);
                product.setBrand(brand);

                productService.addProduct(product);
            }

            // Tạo biến thể
            ProductVariant variant = new ProductVariant();
            variant.setProduct(product);
            variant.setColor(color);
            variant.setRom(rom);
            variant.setPrice(price);
            variant.setIsActive(true);

            productVariantService.addProductVariant(variant);
            // Tạo bản ghi trong ProductStock
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

}

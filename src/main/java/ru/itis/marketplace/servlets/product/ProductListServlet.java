package ru.itis.marketplace.servlets.product;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.services.ProductService;
import ru.itis.marketplace.utils.ServletFileUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/catalog")
public class ProductListServlet extends HttpServlet {

    private static final int MAX_PRODUCTS_AT_PAGE = 10;
    private ProductService productService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productService = (ProductService) config.getServletContext().getAttribute("productService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long page;
        try {
            page = Long.parseLong(req.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1L;
        }
        List<Product> productList = productService.getAllActive(MAX_PRODUCTS_AT_PAGE, (page - 1) * MAX_PRODUCTS_AT_PAGE);
        Long totalPages = productService.getActiveProductsTotalPages(MAX_PRODUCTS_AT_PAGE);
        Map<Long, String> productMainImages = new HashMap<>();
        for (Product product : productList) {
            String mainImage = ServletFileUtil.getEntityMainImage(Product.class, product.getId(), req);
            productMainImages.put(product.getId(), mainImage);
        }
        req.setAttribute("products", productList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("productMainImages", productMainImages);
        req.getRequestDispatcher("/WEB-INF/views/product/product-list.jsp").forward(req, resp);
    }
}

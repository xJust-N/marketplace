package ru.itis.marketplace.servlets.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.utils.ServletFileUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/catalog/edit/*")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10,
        fileSizeThreshold = 1024 * 1024
)
public class EditProductServlet extends BaseProductServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = getProductFromRequest(req, resp);
        if (product == null)
            return;

        if (!isProductOwner(req, product)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        List<String> imagePaths = ServletFileUtil.getEntityImages(Product.class, product.getId(), req);
        req.setAttribute("product", product);
        req.setAttribute("imagePaths", imagePaths);
        req.getRequestDispatcher("/WEB-INF/views/product/product-edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = getProductFromRequest(req, resp);
        if (product == null)
            return;

        if (!isProductOwner(req, product)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String name = req.getParameter("name");
        String price = req.getParameter("price");
        String description = req.getParameter("description");
        String stockQuantity = req.getParameter("stockQuantity");
        String active = req.getParameter("active");
        try {
            productService.updateProduct(product, name.trim(), price, description, stockQuantity, active);
            ServletFileUtil.saveEntityImages(Product.class, product.getId(), req);
            resp.sendRedirect(req.getContextPath() + "/catalog/" + product.getId());
        } catch (ValidationException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (IOException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
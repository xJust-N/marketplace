package ru.itis.marketplace.servlets.product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.models.User;
import ru.itis.marketplace.utils.ServletFileUtil;

import java.io.IOException;

@WebServlet("/catalog/create")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10,
        fileSizeThreshold = 1024 * 1024
)
public class CreateProductServlet extends BaseProductServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if (isIncorrectPermissions(req, resp, currentUser)) {
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/product/create-product.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if (isIncorrectPermissions(req, resp, currentUser)) {
            return;
        }

        String name = req.getParameter("name");
        String price = req.getParameter("price");
        String description = req.getParameter("description");
        Long productId;
        try {
            productId = productService.createProduct(currentUser.getShop(), name.trim(), price, description);
            ServletFileUtil.saveEntityImages(Product.class, productId, req);
            resp.sendRedirect(req.getContextPath() + "/catalog/" + productId);
        } catch(ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

        } catch (IOException e){
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private boolean isIncorrectPermissions(HttpServletRequest req, HttpServletResponse resp, User currentUser) throws IOException {
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return true;
        }
        if (currentUser.getShop() == null) {
            resp.sendRedirect(req.getContextPath() + "/shops/create");
            return true;
        }
        return false;
    }
}
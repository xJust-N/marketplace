package ru.itis.marketplace.servlets.product;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.services.ProductService;
import ru.itis.marketplace.utils.ServletUtils;

import java.io.IOException;
import java.util.Optional;

import static ru.itis.marketplace.utils.ServletUtils.isOwner;

public abstract class BaseProductServlet extends HttpServlet {

    protected ProductService productService;
    @Override
    public void init(ServletConfig sc) throws ServletException {
        super.init(sc);
        this.productService = (ProductService) sc.getServletContext().getAttribute("productService");
    }

    protected boolean isProductOwner(HttpServletRequest req, Product product) {
        return isOwner(req, productService.getProductOwnerId(product));
    }

    protected Product getProductFromRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = ServletUtils.extractIdFromPath(req);
        if(id == null){
            resp.sendRedirect(req.getContextPath() + "/catalog");
            return null;
        }
        Optional<Product> productOptional = productService.findById(id);
        if(productOptional.isEmpty()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return productOptional.get();
    }
}

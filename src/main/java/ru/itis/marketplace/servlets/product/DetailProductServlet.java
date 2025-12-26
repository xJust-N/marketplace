package ru.itis.marketplace.servlets.product;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.Product;
import ru.itis.marketplace.models.User;
import ru.itis.marketplace.services.OrderService;
import ru.itis.marketplace.services.ReviewService;
import ru.itis.marketplace.utils.ServletFileUtil;
import ru.itis.marketplace.utils.ServletUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/catalog/*")
public class DetailProductServlet extends BaseProductServlet{
    private OrderService orderService;
    private ReviewService reviewService;
    @Override
    public void init(ServletConfig sc) throws ServletException {
        super.init(sc);
        this.orderService = (OrderService) sc.getServletContext().getAttribute("orderService");
        this.reviewService = (ReviewService) sc.getServletContext().getAttribute("reviewService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product = getProductFromRequest(req, resp);
        User currentUser = (User) req.getAttribute("currentUser");
        if (product == null) {
            return;
        }
        List<String> imagePaths = ServletFileUtil.getEntityImages(Product.class, product.getId(), req);
        req.setAttribute("product", product);
        req.setAttribute("imagePaths", imagePaths);
        boolean isOwner = isProductOwner(req, product);
        req.setAttribute("isOwner", isOwner);
        boolean canCreateReview = currentUser != null
                && !isOwner
                && !reviewService.isReviewExists(product.getId(), currentUser.getId());
        req.setAttribute("canCreateReview", canCreateReview);
        req.getRequestDispatcher("/WEB-INF/views/product/product-detail.jsp").forward(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if(currentUser == null){
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Product product = getProductFromRequest(req, resp);
        if (product == null)
            return;
        if (isProductOwner(req, product)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You cannot add your own product to cart");
            return;
        }
        String quantityStr = req.getParameter("quantity");
        try{
            orderService.addToCart(currentUser.getId(), product, quantityStr);
        } catch (ValidationException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/catalog/" + product.getId());
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = ServletUtils.extractIdFromPath(req);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid post ID");
            return;
        }
        if(!isProductOwner(req, productService.findById(id).orElse(null))){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        productService.deleteById(id);
        ServletFileUtil.deleteEntityImages(Product.class, id);
        resp.sendRedirect(req.getContextPath() + "/catalog");
    }
}

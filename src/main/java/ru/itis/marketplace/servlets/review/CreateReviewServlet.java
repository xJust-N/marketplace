package ru.itis.marketplace.servlets.review;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.User;

import java.io.IOException;

@WebServlet("/reviews/create")
public class CreateReviewServlet extends BaseReviewServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String productId = req.getParameter("productId");
        if (productId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Product ID is required");
            return;
        }
        req.setAttribute("productId", productId);
        req.getRequestDispatcher("/WEB-INF/views/review/create-review.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String productId = req.getParameter("productId");
        String value = req.getParameter("value");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        try{
            Long reviewId = reviewService.createReview(currentUser.getId(), productId, value, title.trim(), content);
            resp.sendRedirect("%s/reviews/%s".formatted(req.getContextPath(), reviewId));
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }

    }
}
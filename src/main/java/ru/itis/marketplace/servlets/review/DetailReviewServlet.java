package ru.itis.marketplace.servlets.review;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.Review;

import java.io.IOException;

import static ru.itis.marketplace.utils.ServletUtils.isOwner;

@WebServlet("/reviews/*")
public class DetailReviewServlet extends BaseReviewServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Review review = getReviewFromRequest(req, resp);
        if(review == null)
            return;
        req.setAttribute("review", review);
        req.setAttribute("isOwner", isOwner(req, review.getReviewerId()));
        req.getRequestDispatcher("/WEB-INF/views/review/review-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Review review = getReviewFromRequest(req, resp);
        if (review == null) {
            return;
        }
        if(!isOwner(req, review.getReviewerId())){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        Long productId = review.getProductId();
        reviewService.deleteById(review.getId());
        resp.sendRedirect("%s/catalog/%s".formatted(req.getContextPath(), productId));
    }
}
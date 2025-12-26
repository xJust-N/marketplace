package ru.itis.marketplace.servlets.review;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.Review;

import java.io.IOException;

import static ru.itis.marketplace.utils.ServletUtils.isOwner;

@WebServlet("/reviews/edit/*")
public class EditReviewServlet extends BaseReviewServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Review review = getReviewFromRequest(req, resp);
        if (review == null) return;

        if (!isOwner(req, review.getReviewerId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        req.setAttribute("review", review);
        req.getRequestDispatcher("/WEB-INF/views/review/review-edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Review review = getReviewFromRequest(req, resp);
        if (review == null) return;

        if (!isOwner(req, review.getReviewerId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String value = req.getParameter("value");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        try {
            reviewService.updateReview(review, value, title.trim(), content);
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/reviews/" + review.getId());
    }
}
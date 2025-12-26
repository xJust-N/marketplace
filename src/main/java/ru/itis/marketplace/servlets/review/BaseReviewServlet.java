package ru.itis.marketplace.servlets.review;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.Review;
import ru.itis.marketplace.services.ReviewService;
import ru.itis.marketplace.utils.ServletUtils;

import java.io.IOException;
import java.util.Optional;

public abstract class BaseReviewServlet extends HttpServlet {
    protected ReviewService reviewService;

    @Override
    public void init(ServletConfig sc) throws ServletException {
        super.init(sc);
        this.reviewService = (ReviewService) sc.getServletContext().getAttribute("reviewService");
    }

    protected Review getReviewFromRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = ServletUtils.extractIdFromPath(req);
        if(id == null){
            resp.sendRedirect(req.getContextPath() + "/reviews");
            return null;
        }
        Optional<Review> reviewOptional = reviewService.findById(id);
        if(reviewOptional.isEmpty()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return reviewOptional.get();
    }
}
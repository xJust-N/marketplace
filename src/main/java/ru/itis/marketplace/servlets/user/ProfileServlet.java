package ru.itis.marketplace.servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.utils.ServletFileUtil;
import ru.itis.marketplace.models.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/*")
public class ProfileServlet extends BaseUserServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getUserFromRequest(req, resp);
        if (user == null)
            return;
        User currentUser = (User) req.getAttribute("currentUser");
        boolean isCurrentUser = false;
        if(currentUser != null)
            isCurrentUser = currentUser.getId().equals(user.getId());

        req.setAttribute("user", user);
        req.setAttribute("isOwner", isCurrentUser);
        String imageWebPath = ServletFileUtil.getEntityMainImage(user.getClass(), user.getId(), req);
        if(imageWebPath != null)
            req.setAttribute("imagePaths", List.of(imageWebPath));
        else
            req.setAttribute("imagePaths", List.of());
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }
}

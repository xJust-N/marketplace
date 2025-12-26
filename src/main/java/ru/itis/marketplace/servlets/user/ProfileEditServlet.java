package ru.itis.marketplace.servlets.user;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.User;
import ru.itis.marketplace.utils.ServletFileUtil;

import java.io.IOException;
import java.util.List;

@WebServlet("/profile/edit/*")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10,
        fileSizeThreshold = 1024 * 1024
)
public class ProfileEditServlet extends BaseUserServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getUserFromRequest(req,resp);
        if(user == null)
            return;
        if(isIncorrectPermissions(user, req, resp))
            return;
        List<String> imagePaths = ServletFileUtil.getEntityImages(User.class, user.getId(), req);
        req.setAttribute("user", user);
        req.setAttribute("imagePaths", imagePaths);
        req.getRequestDispatcher("/WEB-INF/views/user/profile-edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getUserFromRequest(req,resp);
        if(user == null)
            return;
        if(isIncorrectPermissions(user, req, resp))
            return;
        try {
            ServletFileUtil.saveEntityImages(user.getClass(), user.getId(), req);
            resp.sendRedirect(req.getContextPath() + "/profile/" + user.getId());
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    private boolean isIncorrectPermissions(User user, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if(currentUser == null){
            resp.sendRedirect(req.getContextPath() + "/login");
            return true;
        }
        if(currentUser.getRole().haveEditRights())
            return false;

        if(!user.getId().equals(currentUser.getId())){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You cant edit this profile");
            return true;
        }
        return false;
    }
}

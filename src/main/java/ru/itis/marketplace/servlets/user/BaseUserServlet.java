package ru.itis.marketplace.servlets.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.User;
import ru.itis.marketplace.services.SecurityService;
import ru.itis.marketplace.utils.ServletUtils;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public abstract class BaseUserServlet extends HttpServlet {
    protected SecurityService<UUID> securityService;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.securityService = (SecurityService<UUID>) config.getServletContext().getAttribute("securityService");
    }

    protected User getUserFromRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = ServletUtils.extractIdFromPath(req);
        if (id == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "User ID is required");
            return null;
        }
        Optional<User> userOptional = securityService.findUserById(id);
        if (userOptional.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "No user found with id " + id);
            return null;
        }
        return userOptional.get();
    }
}

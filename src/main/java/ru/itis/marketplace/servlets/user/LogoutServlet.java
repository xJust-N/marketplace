package ru.itis.marketplace.servlets.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.services.SecurityService;
import ru.itis.marketplace.utils.ServletUtils;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    private SecurityService<UUID> securityService;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.securityService = (SecurityService<UUID>) config.getServletContext().getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doLogout(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doLogout(request, response);
    }

    private void doLogout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        UUID sessionId = ServletUtils.getSessionId(req);
        if (sessionId != null)
            securityService.deleteSessionById(sessionId);
        ServletUtils.cleanCookies(resp);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
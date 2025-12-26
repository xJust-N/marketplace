package ru.itis.marketplace.servlets.user;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.config.AppProperties;
import ru.itis.marketplace.exceptions.AuthenticationException;
import ru.itis.marketplace.services.SecurityService;

import java.io.IOException;
import java.util.UUID;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    private static final int COOKIE_MAX_AGE =
            Integer.parseInt(AppProperties.getProperty("security.cookie-max-age", "3600"));

    private SecurityService<UUID> securityService;

    @Override
    @SuppressWarnings("unchecked")
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        this.securityService = (SecurityService<UUID>) config.getServletContext().getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/user/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("login");
        String password = req.getParameter("password");
        String passwordRepeat = req.getParameter("passwordRepeat");
        UUID sessionId;
        try {
            sessionId = securityService.registerUser(email, password, passwordRepeat);
        } catch (AuthenticationException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/user/registration.jsp").forward(req, resp);
            return;
        }
        Cookie cookie = new Cookie("sessionId", sessionId.toString());
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath() + "/profile");
    }
}

package ru.itis.marketplace.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.itis.marketplace.exceptions.AuthenticationException;
import ru.itis.marketplace.models.User;
import ru.itis.marketplace.services.SecurityService;
import ru.itis.marketplace.utils.ServletUtils;

import java.io.IOException;
import java.util.UUID;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private SecurityService<UUID> securityService;

    @Override
    @SuppressWarnings("unchecked")
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        this.securityService = (SecurityService<UUID>) filterConfig.getServletContext().getAttribute("securityService");
        logger.info("AuthenticationFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (shouldSkipFilter(req)) {
            chain.doFilter(request, response);
            return;
        }
        UUID sessionID = ServletUtils.getSessionId(req);
        if (sessionID != null) {
            logger.debug("Processing request with session: {}", sessionID);
            try {
                User currentUser = securityService.findUserBySessionId(sessionID).orElse(null);
                if (currentUser != null) {
                    req.setAttribute("currentUser", currentUser);
                    req.setAttribute("currentUserId", currentUser.getId());
                    logger.debug("User authenticated: {} (ID: {})", currentUser.getLogin(), currentUser.getId());
                } else {
                    logger.debug("No user found for session: {}", sessionID);
                }
            } catch (AuthenticationException e) {
                ServletUtils.cleanCookies((HttpServletResponse) response);
                logger.warn("Cant authenticate user by session {}", sessionID, e);
            }
        } else {
            logger.debug("No session ID found in request");
        }

        chain.doFilter(request, response);
    }

    private boolean shouldSkipFilter(HttpServletRequest req) {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        return (path.startsWith("/uploads") ||
                path.startsWith("/login") ||
                path.startsWith("/logout") ||
                path.startsWith("/register")) ||
                path.endsWith(".css") ||
                path.endsWith(".js") ||
                path.endsWith(".png") ||
                path.endsWith(".jpg") ||
                path.endsWith(".ico");
    }
}
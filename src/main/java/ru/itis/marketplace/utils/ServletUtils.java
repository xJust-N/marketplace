package ru.itis.marketplace.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.User;

import java.util.UUID;

public class ServletUtils {
    public static void cleanCookies(HttpServletResponse resp) {
        Cookie cookie = new Cookie("sessionId", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    public static Long extractIdFromPath(HttpServletRequest req) {
        String pathInfo = req.getPathInfo();
        if (pathInfo != null && pathInfo.length() > 1) {
            try {
                String longStr = pathInfo.substring(1);
                return Validator.validateLong(longStr);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    public static boolean isOwner(HttpServletRequest req, Long realOwnerId) {
        User currentUser = (User) req.getAttribute("currentUser");
        return currentUser != null &&(
                currentUser.getId().equals(realOwnerId)
                || currentUser.getRole().haveEditRights()
        );
    }

    public static UUID getSessionId(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("sessionId".equals(c.getName())) {
                    try {
                        return UUID.fromString(c.getValue());
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
}

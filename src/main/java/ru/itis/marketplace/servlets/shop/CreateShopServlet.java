package ru.itis.marketplace.servlets.shop;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.User;

import java.io.IOException;

@WebServlet("/shops/create")
public class CreateShopServlet extends BaseShopServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if(currentUser == null){
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/shop/create-shop.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if(currentUser == null){
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        try {
            Long shopId = shopService.createShop(currentUser, name, description);
            resp.sendRedirect("%s/shops/%s".formatted(req.getContextPath(), shopId));
        } catch(ValidationException e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
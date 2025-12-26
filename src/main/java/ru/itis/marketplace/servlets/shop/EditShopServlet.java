package ru.itis.marketplace.servlets.shop;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.exceptions.ValidationException;
import ru.itis.marketplace.models.Shop;

import java.io.IOException;

import static ru.itis.marketplace.utils.ServletUtils.isOwner;

@WebServlet("/shops/edit/*")
public class EditShopServlet extends BaseShopServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Shop shop = getShopFromRequest(req, resp);
        if (shop == null) {
            return;
        }
        if(!isOwner(req, shop.getUserId())){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        req.setAttribute("shop", shop);
        req.getRequestDispatcher("/WEB-INF/views/shop/shop-edit.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Shop shop = getShopFromRequest(req, resp);
        if (shop == null) {
            return;
        }
        if(!isOwner(req, shop.getUserId())){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        try {
            shopService.update(shop, name, description);
        } catch (ValidationException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        resp.sendRedirect("%s/shops/%s".formatted(req.getContextPath(), shop.getId()));
    }
}
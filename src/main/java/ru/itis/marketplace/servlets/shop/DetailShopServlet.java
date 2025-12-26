package ru.itis.marketplace.servlets.shop;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.Shop;

import java.io.IOException;

import static ru.itis.marketplace.utils.ServletUtils.isOwner;

@WebServlet("/shops/*")
public class DetailShopServlet extends BaseShopServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Shop shop = getShopFromRequest(req, resp);
        if(shop == null) return;
        req.setAttribute("shop", shop);
        req.setAttribute("isOwner", isOwner(req, shop.getUserId()));
        req.getRequestDispatcher("/WEB-INF/views/shop/shop-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Shop shop = getShopFromRequest(req, resp);
        if (shop == null) return;

        if(!isOwner(req, shop.getUserId())){
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        shopService.deleteById(shop.getId());
        resp.sendRedirect(req.getContextPath() + "/shops");
    }
}
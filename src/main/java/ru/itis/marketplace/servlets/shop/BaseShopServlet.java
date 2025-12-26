package ru.itis.marketplace.servlets.shop;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.Shop;
import ru.itis.marketplace.services.ShopService;
import ru.itis.marketplace.utils.ServletUtils;

import java.io.IOException;
import java.util.Optional;

public abstract class BaseShopServlet extends HttpServlet {

    protected ShopService shopService;

    @Override
    public void init(ServletConfig sc) throws ServletException {
        super.init(sc);
        this.shopService = (ShopService) sc.getServletContext().getAttribute("shopService");
    }

    protected Shop getShopFromRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = ServletUtils.extractIdFromPath(req);
        if(id == null){
            resp.sendRedirect(req.getContextPath() + "/shops");
            return null;
        }
        Optional<Shop> shopOptional = shopService.findById(id);
        if(shopOptional.isEmpty()){
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return shopOptional.get();
    }
}
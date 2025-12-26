package ru.itis.marketplace.servlets.shop;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.Shop;
import ru.itis.marketplace.services.ShopService;

import java.io.IOException;
import java.util.List;

@WebServlet("/shops")
public class ShopListServlet extends HttpServlet {

    private static final int MAX_SHOPS_AT_PAGE = 10;
    private ShopService shopService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        shopService = (ShopService) config.getServletContext().getAttribute("shopService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long page;
        try {
            page = Long.parseLong(req.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1L;
        }

        List<Shop> shopList = shopService.getAll(MAX_SHOPS_AT_PAGE, (page - 1) * MAX_SHOPS_AT_PAGE);
        Long totalPages = shopService.getTotalPages(MAX_SHOPS_AT_PAGE);
        req.setAttribute("shops", shopList);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/WEB-INF/views/shop/shop-list.jsp").forward(req, resp);
    }
}
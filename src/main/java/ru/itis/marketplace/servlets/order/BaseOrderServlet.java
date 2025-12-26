package ru.itis.marketplace.servlets.order;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import ru.itis.marketplace.services.OrderService;

public class BaseOrderServlet extends HttpServlet {
    protected OrderService orderService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderService = (OrderService) config.getServletContext().getAttribute("orderService");
    }
}

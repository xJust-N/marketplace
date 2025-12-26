package ru.itis.marketplace.servlets.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.Order;
import ru.itis.marketplace.models.User;

import java.io.IOException;

@WebServlet("/cart")
public class CartServlet extends BaseOrderServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if(currentUser == null){
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        Order order = orderService.getCartByUserId(currentUser.getId());
        req.setAttribute("order", order);
        req.getRequestDispatcher("/WEB-INF/views/order/cart.jsp").forward(req, resp);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if(currentUser == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You are not logged in");
            return;
        }
        Long orderId = orderService.createOrderFromCartByUserId(currentUser.getId());
        resp.sendRedirect(req.getContextPath() + "/order" + orderId);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if(currentUser == null){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "You are not logged in");
            return;
        }
        orderService.deleteCartByUserId(currentUser.getId());
        resp.sendRedirect(req.getContextPath() + "/catalog");
    }
}

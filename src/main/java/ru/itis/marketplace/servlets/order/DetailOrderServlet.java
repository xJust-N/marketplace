package ru.itis.marketplace.servlets.order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.itis.marketplace.models.Order;
import ru.itis.marketplace.models.User;
import ru.itis.marketplace.utils.ServletUtils;

import java.io.IOException;
import java.util.Optional;

import static ru.itis.marketplace.utils.ServletUtils.isOwner;

@WebServlet("/orders/*")
public class DetailOrderServlet extends BaseOrderServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        Order order = getOrderFromRequest(req, resp);
        if (order == null) {
            return;
        }
        if (!isOwner(req, order.getUserId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to view this order");
            return;
        }

        req.setAttribute("order", order);
        req.setAttribute("canCancel", order.getStatus().canBeCancelled());
        req.getRequestDispatcher("/WEB-INF/views/order/order-detail.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User currentUser = (User) req.getAttribute("currentUser");
        if (currentUser == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You are not logged in");
            return;
        }

        Order order = getOrderFromRequest(req, resp);
        if (order == null) {
            return;
        }

        if (!isOwner(req, order.getUserId())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "You don't have permission to modify this order");
            return;
        }

        String action = req.getParameter("action");
        if ("cancel".equals(action)) {

            if (!order.getStatus().canBeCancelled()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "This order cannot be cancelled");
                return;
            }
            orderService.cancelOrder(order.getId());
            resp.sendRedirect(req.getContextPath() + "/orders/" + order.getId());

        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private Order getOrderFromRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long orderId = ServletUtils.extractIdFromPath(req);
        if (orderId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
            return null;
        }

        Optional<Order> orderOptional = orderService.findById(orderId);
        if (orderOptional.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
            return null;
        }

        return orderOptional.get();
    }
}
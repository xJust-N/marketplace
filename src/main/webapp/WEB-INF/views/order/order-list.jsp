<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="orders" scope="request" type="java.util.List<ru.itis.marketplace.models.Order>"/>
<t:styles/>
<t:navbar/>
<t:layout title="Мои заказы">
    <h2>Мои заказы</h2>

    <c:if test="${empty orders}">
        <div class="empty-cart">
            <p>У вас пока нет заказов</p>
        </div>
    </c:if>

    <c:if test="${not empty orders}">
        <div class="orders-list">
            <c:forEach var="order" items="${orders}">
                <div class="order-card">
                    <div class="order-header">
                        <h3>Заказ #${order.id}</h3>
                        <p class="order-date">${order.createdAt}</p>
                    </div>

                    <div class="order-status">
                        Статус: ${order.status}
                    </div>

                    <div class="order-items-preview">
                        <c:forEach var="item" items="${order.orderItems}" end="2">
                            <span>${item.productName} (${item.count} шт.)</span>
                            <c:if test="${not empty order.orderItems && order.orderItems.size() > 3}">
                                <span>и еще ${order.orderItems.size() - 3} товаров</span>
                            </c:if>
                        </c:forEach>
                    </div>
                    <button class="button"
                            onclick="goToOrderDetail('${pageContext.request.contextPath}', ${order.id})">
                        Подробнее
                    </button>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <div class="actions">
        <button class="button" onclick="goToMainPage('${pageContext.request.contextPath}')">
            Вернуться в каталог
        </button>
    </div>

<t:script/>
</t:layout>
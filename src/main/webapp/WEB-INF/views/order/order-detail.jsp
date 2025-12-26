<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="order" scope="request" type="ru.itis.marketplace.models.Order"/>
<jsp:useBean id="canCancel" scope="request" type="java.lang.Boolean"/>
<t:styles/>
<t:navbar/>
<t:layout title="Заказ #${order.id.toString()}">
    <h2>Заказ #${order.id}</h2>

    <div class="order-info">
        <p><strong>Дата создания:</strong> ${order.createdAt}</p>
        <p><strong>Статус:</strong> ${order.status}</p>
    </div>

    <div class="order-items">
        <h3>Товары в заказе:</h3>
        <c:forEach var="item" items="${order.orderItems}">
            <div class="order-item-detail">
                <h4>${item.productName}</h4>
                <p>Цена за шт: ${item.price} ₽</p>
                <p>Количество: ${item.count}</p>
                <p>Сумма: ${item.price * item.count} ₽</p>
                <button class="button"
                        onclick="goToViewProduct('${pageContext.request.contextPath}', ${item.productId})">
                    Посмотреть товар
                </button>
            </div>
        </c:forEach>
    </div>

    <div class="order-summary">
        <c:set var="total" value="0"/>
        <c:forEach var="item" items="${order.orderItems}">
            <c:set var="total" value="${total + (item.price * item.count)}"/>
        </c:forEach>
        <h3>Общая сумма: ${order.total} ₽</h3>
    </div>

    <div class="actions">
        <c:if test="${canCancel}">
            <form action="${pageContext.request.contextPath}/orders/${order.id}" method="post">
                <input type="hidden" name="action" value="cancel">
                <button type="submit" class="cancel"
                        onclick="return confirm('Вы уверены, что хотите отменить заказ?')">
                    Отменить заказ
                </button>
            </form>
        </c:if>
        <button class="button" onclick="goToOrders('${pageContext.request.contextPath}')">
            К списку заказов
        </button>
        <button class="button" onclick="goToMainPage('${pageContext.request.contextPath}')">
            Вернуться в каталог
        </button>
    </div>

<t:script/>
</t:layout>
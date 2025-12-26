<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="order" scope="request" type="ru.itis.marketplace.models.Order"/>
<t:styles/>
<t:navbar/>
<t:layout title="Корзина">
    <h2>Корзина</h2>

    <c:if test="${empty order.orderItems}">
        <div class="empty-cart">
            <p>Ваша корзина пуста</p>
        </div>
    </c:if>

    <c:if test="${not empty order.orderItems}">
        <div class="order-items">
            <c:forEach var="item" items="${order.orderItems}">
                <div class="order-item">
                    <h3>${item.productName}</h3>
                    <p>Цена: ${item.price} ₽</p>
                    <p>Количество: ${item.count}</p>
                    <p>Сумма: ${item.price * item.count} ₽</p>
                    <button class="button" onclick="goToViewProduct('${pageContext.request.contextPath}', ${item.productId})">
                        Посмотреть товар
                    </button>
                </div>
            </c:forEach>
        </div>

        <div class="order-total">
            <h3>Итого: ${order.total} ₽</h3>
        </div>

        <div class="actions">
            <form action="${pageContext.request.contextPath}/cart" method="post">
                <button type="submit">Оформить заказ</button>
            </form>
            <form action="${pageContext.request.contextPath}/cart" method="post"
                  onsubmit="return confirm('Вы уверены, что хотите очистить корзину?')">
                <input type="hidden" name="_method" value="DELETE">
                <button type="submit" class="cancel">Очистить корзину</button>
            </form>
            <button class="button" onclick="goToMainPage('${pageContext.request.contextPath}')">
                Продолжить покупки
            </button>
        </div>
    </c:if>

<t:script/>
</t:layout>
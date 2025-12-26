<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="product" scope="request" type="ru.itis.marketplace.models.Product"/>
<jsp:useBean id="imagePaths" scope="request" type="java.util.List"/>
<jsp:useBean id="isOwner" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="canCreateReview" scope="request" type="java.lang.Boolean"/>
<t:styles/>
<t:navbar/>
<t:layout title="${product.name}">
    <t:product product="${product}" imagePaths="${imagePaths}"/>

    <div class="actions">
        <c:choose>
            <c:when test="${isOwner}">
                <button class="button" onclick="goToEditProduct('${pageContext.request.contextPath}', ${product.id})">
                    Редактировать
                </button>
                <button class="button" onclick="goToDeleteProduct('${pageContext.request.contextPath}', ${product.id})">
                    Удалить
                </button>
            </c:when>
        </c:choose>
        <c:if test="${canCreateReview}">
            <button class="button" onclick="goToCreateReview('${pageContext.request.contextPath}', ${product.id})">
                Оставить отзыв
            </button>
            <c:choose>
                <c:when test="${product.stockQuantity > 0}">
                    <form action="${pageContext.request.contextPath}/catalog/${product.id}"
                          method="post"
                          class="add-to-cart-form">
                        <div class="quantity-selector">
                            <label for="quantity">Количество:</label>
                            <input type="number"
                                   id="quantity"
                                   name="quantity"
                                   value="1"
                                   min="1"
                                   max="${product.stockQuantity}"
                                   class="quantity-input">
                        </div>
                        <button type="submit" class="add-to-cart-btn">
                            Добавить в корзину
                        </button>
                    </form>
                </c:when>
                <c:otherwise>
                    <button class="button" disabled class="out-of-stock-btn">
                        Нет в наличии
                    </button>
                </c:otherwise>
            </c:choose>
        </c:if>
        <button class="button" onclick="goToMainPage('${pageContext.request.contextPath}')">
            Вернуться в каталог
        </button>
    </div>

<t:script/>
</t:layout>
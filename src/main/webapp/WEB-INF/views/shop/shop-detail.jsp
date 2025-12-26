<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="shop" scope="request" type="ru.itis.marketplace.models.Shop"/>
<jsp:useBean id="isOwner" scope="request" type="java.lang.Boolean"/>
<t:styles/>
<t:navbar/>
<t:layout title="Детали магазина">
    <t:shop shop="${shop}"/>

    <div class="actions">
        <c:choose>
            <c:when test="${isOwner}">
                <button class="button" onclick="goToEditShop('${pageContext.request.contextPath}', ${shop.id})">
                    Редактировать
                </button>
                <button class="button" onclick="goToDeleteShop('${pageContext.request.contextPath}', ${shop.id})">
                    Удалить
                </button>
                <button class="button" onclick="goToCreateProduct('${pageContext.request.contextPath}')">
                    Добавить товар
                </button>
            </c:when>
        </c:choose>
        <button class="button" onclick="goToShopsPage('${pageContext.request.contextPath}')">
            К списку магазинов
        </button>
    </div>

<t:script/>
</t:layout>
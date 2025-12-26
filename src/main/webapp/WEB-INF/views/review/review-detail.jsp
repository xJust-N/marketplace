<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="review" scope="request" type="ru.itis.marketplace.models.Review"/>
<jsp:useBean id="isOwner" scope="request" type="java.lang.Boolean"/>
<t:styles/>
<t:navbar/>
<t:layout title="Детали отзыва">
    <div class="review-detail-container">
        <t:review review="${review}"/>
    </div>

    <div class="actions">
        <c:choose>
            <c:when test="${isOwner}">
                <button class="button" onclick="goToEditReview('${pageContext.request.contextPath}', ${review.id})">
                    Редактировать
                </button>
                <button class="button" onclick="goToDeleteReview('${pageContext.request.contextPath}', ${review.id})">
                    Удалить
                </button>
            </c:when>
        </c:choose>
        <button class="button" onclick="goToProductPage('${pageContext.request.contextPath}', ${review.productId})">
            Вернуться к товару
        </button>
    </div>

<t:script/>
</t:layout>
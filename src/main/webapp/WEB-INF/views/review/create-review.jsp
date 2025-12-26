<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:styles/>
<t:layout title="Создание отзыва">
    <t:navbar/>
    <h2>Создать отзыв</h2>
    <form action="${pageContext.request.contextPath}/reviews/create" method="post">
        <input type="hidden" name="productId" value="${param.productId}">
        <div>
            <label for="value">Оценка (1-5):</label>
            <input type="number" id="value" name="value" min="1" max="5" required>
        </div>

        <div>
            <label for="title">Заголовок:</label>
            <input type="text" id="title" name="title" required maxlength="255">
        </div>

        <div>
            <label for="content">Содержание:</label>
            <textarea id="content" name="content" rows="10" required></textarea>
        </div>

        <div class="form-actions">
            <button type="submit">
                Создать отзыв
            </button>
            <button class="button" onclick="goToProductPage('${pageContext.request.contextPath}', ${param.productId})">
                Вернуться к товару
            </button>
        </div>
    </form>

<t:script/>
</t:layout>
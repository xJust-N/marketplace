<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="review" scope="request" type="ru.itis.marketplace.models.Review"/>
<t:navbar/>
<t:layout title="Редактирование отзыва">
    <h2>Редактировать отзыв</h2>
    <form action="${pageContext.request.contextPath}/reviews/edit/${review.id}" method="post">
        <div>
            <label for="value">Оценка (1-5):</label>
            <input type="number" id="value" name="value" min="1" max="5" value="${review.value}" required>
        </div>

        <div>
            <label for="title">Заголовок:</label>
            <input type="text" id="title" name="title" value="${review.title}" required>
        </div>

        <div>
            <label for="content">Содержание:</label>
            <textarea id="content" name="content" rows="10" required>${review.content}</textarea>
        </div>

        <div class="form-actions">
            <button type="submit">Сохранить изменения</button>
            <button class="button" onclick="goToViewReview('${pageContext.request.contextPath}', ${review.id})">Отмена</button>
        </div>
    </form>

<t:script/>
</t:layout>
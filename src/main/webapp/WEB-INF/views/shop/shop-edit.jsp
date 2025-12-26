<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<jsp:useBean id="shop" scope="request" type="ru.itis.marketplace.models.Shop"/>
<t:navbar/>
<t:layout title="Редактирование магазина">
    <h2>Редактировать магазин</h2>
    <form action="${pageContext.request.contextPath}/shops/edit/${shop.id}" method="post">
        <div>
            <label for="name">Название магазина:</label>
            <input type="text" id="name" name="name" value="${shop.name}" required>
        </div>

        <div>
            <label for="description">Описание:</label>
            <textarea id="description" name="description" rows="10">${shop.description}</textarea>
        </div>

        <div class="form-actions">
            <button type="submit">Сохранить изменения</button>
            <button class="button" onclick="goToViewShop('${pageContext.request.contextPath}', ${shop.id})">Отмена</button>
        </div>
    </form>

<t:script/>
</t:layout>
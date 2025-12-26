<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:styles/>
<t:layout title="Создание магазина">
    <t:navbar/>
    <h2>Создать магазин</h2>
    <form action="${pageContext.request.contextPath}/shops/create" method="post">
        <div>
            <label for="name">Название магазина:</label>
            <input type="text" id="name" name="name" required maxlength="255">
        </div>

        <div>
            <label for="description">Описание:</label>
            <textarea id="description" name="description" rows="10"></textarea>
        </div>

        <div class="form-actions">
            <button type="submit">
                Создать магазин
            </button>
            <button class="button" onclick="goToMainPage('${pageContext.request.contextPath}')">
                Вернуться
            </button>
        </div>
    </form>

<t:script/>
</t:layout>
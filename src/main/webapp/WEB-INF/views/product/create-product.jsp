<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<t:styles/>
<t:layout title="Создание товара">
    <t:navbar/>
    <h2>Создать новый товар</h2>
    <form action="${pageContext.request.contextPath}/catalog/create" method="post" enctype="multipart/form-data">
        <div>
            <label for="name">Название:</label>
            <input type="text" id="name" name="name" required maxlength="255">
        </div>

        <div>
            <label for="price">Цена:</label>
            <input type="number" id="price" name="price" step="0.01" required>
        </div>

        <div>
            <label for="description">Описание:</label>
            <textarea id="description" name="description" rows="10"></textarea>
        </div>

        <div>
            <label for="images">Изображения товара:</label>
            <input type="file" id="images" name="images" accept="image/*" multiple>
            <small>Можно выбрать несколько файлов (JPEG, PNG, GIF)</small>
        </div>

        <div class="form-actions">
            <button type="submit">
                Создать товар
            </button>
            <button class="button" onclick="goToMainPage('${pageContext.request.contextPath}')">
                Вернуться
            </button>
        </div>
    </form>

<t:script/>
</t:layout>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="product" scope="request" type="ru.itis.marketplace.models.Product"/>
<jsp:useBean id="imagePaths" scope="request" type="java.util.List"/>
<t:styles/>
<t:navbar/>
<t:layout title="Редактирование товара">
    <h2>Редактировать товар</h2>
    <c:if test="${not empty imagePaths}">
        <div class="current-images">
            <h3>Текущие изображения:</h3>
            <t:images imagePaths="${imagePaths}"/>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/catalog/edit/${product.id}"
          method="post" enctype="multipart/form-data">
        <div>
            <label for="name">Название:</label>
            <input type="text" id="name" name="name" value="${product.name}" required>
        </div>

        <div>
            <label for="price">Цена:</label>
            <input type="number" id="price" name="price" step="0.01" value="${product.price}" required>
        </div>

        <div>
            <label for="description">Описание:</label>
            <textarea id="description" name="description" rows="10">${product.description}</textarea>
        </div>

        <div>
            <label for="stockQuantity">Количество на складе:</label>
            <input type="number" id="stockQuantity" name="stockQuantity" value="${product.stockQuantity}" required>
        </div>

        <div>
            <label for="active">Активен:</label>
            <input type="checkbox" id="active" name="active" ${product.active ? 'checked' : ''}>
        </div>

        <div>
            <label for="images">Добавить изображения:</label>
            <input type="file" id="images" name="images" accept="image/*" multiple>
            <small>Можно выбрать несколько файлов (JPEG, PNG, GIF, WebP)</small>
        </div>

        <div class="form-actions">
            <button type="submit">Сохранить изменения</button>
            <button class="button" onclick="goToViewProduct('${pageContext.request.contextPath}', ${product.id})">
                Отмена
            </button>
        </div>
    </form>

<t:script/>
</t:layout>
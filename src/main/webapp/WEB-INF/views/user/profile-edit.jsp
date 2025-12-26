<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" scope="request" type="ru.itis.marketplace.models.User"/>
<jsp:useBean id="imagePaths" scope="request" type="java.util.List"/>
<t:styles/>
<t:navbar/>
<t:layout title="Редактирование товара">
    <h2>Редактировать товар</h2>

    <c:if test="${not empty imagePaths}">
        <div class="icon">
            <t:images imagePaths="${imagePaths}"/>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/profile/edit/${user.id}"
          method="post" enctype="multipart/form-data">
        <div>
            <label for="images">Добавить аватарку:</label>
            <input type="file" id="images" name="images" accept="image/*" multiple>
        </div>

        <div class="form-actions">
            <button type="submit">Сохранить изменения</button>
            <button class="button" onclick="goToProfile('${pageContext.request.contextPath}')">
                Отмена
            </button>
        </div>
    </form>

    <t:script/>
</t:layout>
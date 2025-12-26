<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="user" scope="request" type="ru.itis.marketplace.models.User"/>
<jsp:useBean id="imagePaths" scope="request" type="java.util.List"/>
<jsp:useBean id="isOwner" scope="request" type="java.lang.Boolean"/>
<t:navbar/>
<t:layout title="Профиль - ${user.login}">

    <div class="profile-container">
        <div class="profile-header">
            <h1>Профиль пользователя</h1>
        </div>
        <t:images imagePaths="${imagePaths}"/>

        <div class="profile-info">
            <div class="info-card">
                <h3>Основная информация</h3>
                <div class="info-item">
                    <strong>Логин:</strong>
                    <span>${user.login}</span>
                </div>
                <div class="info-item">
                    <strong>ID пользователя:</strong>
                    <span>${user.id}</span>
                </div>
                <div class="info-item">
                    <strong>Роль на сайте</strong>
                    <span>${user.role.name()}</span>
                </div>
            </div>
        </div>
    </div>
    <div class="actions">
        <c:choose>
            <c:when test="${isOwner}">
                <button class="button" onclick="goToEditProfile('${pageContext.request.contextPath}', ${user.id})">
                    Редактировать
                </button>
            </c:when>
        </c:choose>
    </div>
</t:layout>
<t:script/>
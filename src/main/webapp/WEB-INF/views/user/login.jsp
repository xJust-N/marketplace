<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<t:navbar/>
<t:layout title="Login">
    <form method="post">
        <label for="login">Login:</label>
        <input id="login" name="login">

        <label for="password">Password:</label>
        <input type="password" id="password" name="password">

        <div class="form-actions">
            <input type="submit" value="Log in">
        </div>
    </form>
    <div class="text-center">
        <a href="${pageContext.servletContext.contextPath}/registration">I dont have account</a>
        <c:if test="${not empty error}">
            <p>Ошибка: ${error}</p>
        </c:if>
    </div>
</t:layout>
<t:script/>
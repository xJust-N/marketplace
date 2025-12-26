<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<t:navbar/>
<t:layout title="Registration">
    <form method="post">
        <label for="login">Login:</label>
        <input id="login" name="login">

        <label for="password">Password:</label>
        <input type="password" id="password" name="password">

        <label for="passwordRepeat">Password repeat:</label>
        <input type="password" id="passwordRepeat" name="passwordRepeat">

        <div class="form-actions">
            <input type="submit" value="Registration">
        </div>
    </form>
    <div class="text-center">
        <a href="${pageContext.servletContext.contextPath}/login">Already have account</a>
        <c:if test="${not empty error}">
            <p>Ошибка: ${error}</p>
        </c:if>
    </div>
</t:layout>
<t:script/>
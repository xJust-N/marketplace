<%@tag description="Navigation bar" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<nav>
    <button type="button" onclick="goToMainPage('${pageContext.servletContext.contextPath}')" class="nav-button">
        Каталог
    </button>
    <button type="button" onclick="goToShopsPage('${pageContext.servletContext.contextPath}')" class="nav-button">
        Магазины
    </button>
    <c:choose>
        <c:when test="${not empty requestScope.currentUser}">
            <c:if test="${requestScope.currentUser.role.isCustomerOnly()}">
                <button type="button" onclick="goToCreateShop('${pageContext.servletContext.contextPath}')"
                        class="nav-button">Создать магазин
                </button>
            </c:if>
            <c:if test="${not empty requestScope.currentUser.shop}">
                <button type="button"
                        onclick="goTo('${pageContext.servletContext.contextPath}' + '/shops/' + ${requestScope.currentUser.shop.id})"
                        class="nav-button">
                    Мой магазин
                </button>
                <button type="button" onclick="goToCreateProduct('${pageContext.servletContext.contextPath}')"
                        class="nav-button">
                    Добавить товар
                </button>
            </c:if>
            <button type="button" onclick="goToOrders('${pageContext.servletContext.contextPath}')" class="nav-button">
                Мои заказы
            </button>
            <button type="button" onclick="goToCart('${pageContext.servletContext.contextPath}')" class="nav-button">
                Корзина
            </button>
            <span class="user-info">Привет, ${requestScope.currentUser.login}</span>
            <button type="button" onclick="goToProfile('${pageContext.request.contextPath}')"
                    class="nav-button profile">
                Профиль
            </button>
            <button type="button" onclick="goToLogout('${pageContext.request.contextPath}')"
                    class="nav-button logout">
                Выйти
            </button>
        </c:when>
        <c:otherwise>
            <button type="button" onclick="goToLogin('${pageContext.servletContext.contextPath}')" class="nav-button">
                Войти
            </button>
            <button type="button" onclick="goToRegister('${pageContext.servletContext.contextPath}')"
                    class="nav-button">
                Регистрация
            </button>
        </c:otherwise>
    </c:choose>
</nav>

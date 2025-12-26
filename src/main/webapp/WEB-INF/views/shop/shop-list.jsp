<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:useBean id="shops" scope="request" type="java.util.List<ru.itis.marketplace.models.Shop>"/>
<jsp:useBean id="currentPage" scope="request" type="java.lang.Long"/>
<jsp:useBean id="totalPages" scope="request" type="java.lang.Long"/>
<t:navbar/>
<t:layout title="Список магазинов">
    <c:if test="${empty shops}">
        <h1>Магазины не найдены</h1>
    </c:if>
    <c:if test="${not empty shops}">
        <c:forEach var="shop" items="${shops}">
            <div class="shop shop-list-item">
                <h3 class="shop-name">${shop.name}</h3>
                <p class="shop-description">${shop.description}</p>
            </div>
            <div class="actions">
                <button type="button" class="button" onclick="goToViewShop('${pageContext.request.contextPath}', ${shop.id})">
                    Посмотреть магазин
                </button>
            </div>
        </c:forEach>
    </c:if>

    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/shops?page=${currentPage - 1}">
                    ←
                </a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="pageNum">
                <c:choose>
                    <c:when test="${pageNum == currentPage}">
                        <span class="current-page">${pageNum}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/shops?page=${pageNum}">
                                ${pageNum}
                        </a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/shops?page=${currentPage + 1}">
                    →
                </a>
            </c:if>
        </div>
    </c:if>

<t:script/>
</t:layout>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:useBean id="products" scope="request" type="java.util.List<ru.itis.marketplace.models.Product>"/>
<jsp:useBean id="productMainImages" scope="request" type="java.util.Map<java.lang.Long, java.lang.String>"/>
<jsp:useBean id="currentPage" scope="request" type="java.lang.Long"/>
<jsp:useBean id="totalPages" scope="request" type="java.lang.Long"/>
<t:navbar/>
<t:layout title="Каталог товаров">
    <c:if test="${empty products}">
        <h1>Товары не найдены</h1>
    </c:if>
    <c:if test="${not empty products}">
        <div class="products-grid">
            <c:forEach var="product" items="${products}">
                <div class="product-card">
                    <c:set var="mainImage" value="${productMainImages[product.id]}"/>
                    <div class="product-image">
                        <c:choose>
                            <c:when test="${not empty mainImage}">
                                <img src="${mainImage}" alt="${product.name}"
                                     class="product-thumb">
                            </c:when>
                            <c:otherwise>
<%--                                <img src="${pageContext.request.contextPath}/images/default-product.png"--%>
<%--                                     alt="Нет изображения" class="product-thumb">--%>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="product-info">
                        <h3>${product.name}</h3>
                        <p class="price">${product.price} ₽</p>
                        <p class="description">${product.description}</p>
                    </div>

                    <button type="button" onclick="goToViewProduct('${pageContext.request.contextPath}', ${product.id})"
                            class="view-btn">
                        Подробнее
                    </button>
                </div>
            </c:forEach>
        </div>
    </c:if>

    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/catalog?page=${currentPage - 1}">
                    ←
                </a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="pageNum">
                <c:choose>
                    <c:when test="${pageNum == currentPage}">
                        <span class="current-page">${pageNum}</span>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/catalog?page=${pageNum}">
                                ${pageNum}
                        </a>
                    </c:otherwise>
                </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/catalog?page=${currentPage + 1}">
                    →
                </a>
            </c:if>
        </div>
    </c:if>

<t:script/>
</t:layout>
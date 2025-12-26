<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@tag description="Shop display" pageEncoding="UTF-8" %>
<%@attribute name="shop" required="true" type="ru.itis.marketplace.models.Shop" %>
<div class="shop">
    <h3 class="shop-name">${shop.name}</h3>
    <p class="shop-description">${shop.description}</p>
    <c:if test="${not empty shop.productList}">
        <div class="products">
            <h4>Товары магазина:</h4>
            <c:if test="${not empty shop.productList}">
                <c:forEach var="product" items="${shop.productList}">
                    <div class="product-preview">
                        <h5>${product.name}</h5>
                        <p>Цена: ${product.price} ₽</p>
                        <button class="button"
                                onclick="goToViewProduct('${pageContext.request.contextPath}', ${product.id})">
                            Посмотреть товар
                        </button>
                    </div>
                </c:forEach>
            </c:if>
        </div>
    </c:if>
</div>
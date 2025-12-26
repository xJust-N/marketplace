<%@tag description="Product display" pageEncoding="UTF-8"%>
<%@attribute name="product" required="true" type="ru.itis.marketplace.models.Product" %>
<%@attribute name="imagePaths" required="false" type="java.util.List" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="product">
    <t:images imagePaths="${imagePaths}"/>
    <div class="product-info">
        <h3>${product.name}</h3>
        <p class="price">Цена: ${product.price} ₽</p>
        <p class="description">${product.description}</p>
        <p class="stock">В наличии: ${product.stockQuantity} шт.</p>
        <button type="button" class="button" onclick="goToViewShop('${pageContext.servletContext.contextPath}', ${product.shop.id})">
            Магазин: ${product.shop.name}
        </button>
    </div>

    <c:if test="${not empty product.reviews}">
        <div class="reviews-section">
            <h4 class="reviews-title">Отзывы (${product.reviews.size()})</h4>
            <div class="reviews-list">
                <c:forEach var="review" items="${product.reviews}">
                    <div class="review-wrapper">
                        <t:review review="${review}"/>
                        <div class="review-actions">
                            <button class="button" onclick="goToViewReview('${pageContext.servletContext.contextPath}', ${review.id})">
                                Посмотреть отзыв
                            </button>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:if>
</div>
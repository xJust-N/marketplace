<%@tag description="Review display" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="review" required="true" type="ru.itis.marketplace.models.Review" %>
<div class="review-card">
    <div class="review-header">
        <div class="review-title-rating">
            <h4 class="review-title">${review.title}</h4>
            <div class="review-rating">
                <c:forEach begin="1" end="5" var="star">
                    <span class="star ${star <= review.value ? 'filled' : ''}">★</span>
                </c:forEach>
                <span class="rating-value">${review.value}/5</span>
            </div>
        </div>
    </div>
    <div class="review-content">
        <p>${review.content}</p>
    </div>
</div>
<%@tag description="Universal images display" pageEncoding="UTF-8"%>
<%@attribute name="imagePaths" required="true" type="java.util.List" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="images-container">
    <c:choose>
        <c:when test="${not empty imagePaths}">
            <div class="main-image">
                <img src="${imagePaths[0]}" alt="Нет изображения" class="main-image-img">
            </div>
            <c:if test="${imagePaths.size() > 1}">
                <div class="image-gallery">
                    <c:forEach var="imagePath" items="${imagePaths}" varStatus="status">
                        <div class="gallery-item">
                            <img src="${imagePath}" alt="Нет изображения" class="gallery-item-img"
                                 onclick="setAsMainImage(this, '${imagePath}')">
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </c:when>
    </c:choose>
</div>

<script>

</script>
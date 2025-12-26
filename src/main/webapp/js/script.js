function goToViewProduct(contextPath, productId) {
    window.location.href = contextPath + '/catalog/' + productId;
}

function goToEditProduct(contextPath, productId) {
    window.location.href = contextPath + '/catalog/edit/' + productId;
}

function goToDeleteProduct(contextPath, productId) {
    fetch(contextPath + '/catalog/' + productId, {
        method: 'DELETE'
    }).then(() => {
        window.location.href = contextPath + '/catalog';
    });
}

function goToViewShop(contextPath, shopId) {
    window.location.href = contextPath + '/shops/' + shopId;
}

function goToEditShop(contextPath, shopId) {
    window.location.href = contextPath + '/shops/edit/' + shopId;
}

function goToDeleteShop(contextPath, shopId) {
    fetch(contextPath + '/shops/' + shopId, {
        method: 'DELETE'
    }).then(() => {
        window.location.href = contextPath + '/shops';
    });
}

function goToMainPage(contextPath) {
    window.location.href = contextPath + '/catalog';
}

function goToShopsPage(contextPath) {
    window.location.href = contextPath + '/shops';
}

function goToCreateProduct(contextPath) {
    window.location.href = contextPath + '/catalog/create';
}

function goToViewReview(contextPath, reviewId) {
    window.location.href = contextPath + '/reviews/' + reviewId;
}

function goToEditReview(contextPath, reviewId) {
    window.location.href = contextPath + '/reviews/edit/' + reviewId;
}

function goToDeleteReview(contextPath, reviewId) {
    fetch(contextPath + '/reviews/' + reviewId, {
        method: 'DELETE'
    }).then(() => {
        window.location.href = contextPath + '/catalog';
    });
}

function goToProductPage(contextPath, productId) {
    window.location.href = contextPath + '/catalog/' + productId;
}

function goToCreateReview(contextPath, productId) {
    window.location.href = contextPath + '/reviews/create?productId=' + productId;
}

function setAsMainImage(thumbElement, imagePath) {
    const container = thumbElement.closest('.images-container');
    const mainImage = container.querySelector('.main-image img');
    mainImage.src = imagePath;

    container.querySelectorAll('.gallery-item').forEach(item => {
        item.style.borderColor = 'transparent';
    });
    thumbElement.parentElement.style.borderColor = '#007bff';

}
function goToOrderDetail(contextPath, orderId) {
    window.location.href = contextPath + '/orders/' + orderId;
}

function goToOrders(contextPath) {
    window.location.href = contextPath + '/orders';
}
function goToCreateShop(contextPath) {
    window.location.href = contextPath + '/shops/create';
}

function goToCart(contextPath) {
    window.location.href = contextPath + '/cart';
}
function goToLogin(contextPath) {
    window.location.href = contextPath + '/login';
}

function goToRegister(contextPath) {
    window.location.href = contextPath + '/registration';
}
function goToLogout(contextPath) {
    window.location.href = contextPath + '/logout';
}
function goTo(path) {
    window.location.href = path
}
function goToProfile(contextPath) {
    window.location.href = contextPath + '/profile';
}
function goToEditProfile(contextPath, userId) {
    window.location.href = contextPath + '/profile/edit/' + userId;
}
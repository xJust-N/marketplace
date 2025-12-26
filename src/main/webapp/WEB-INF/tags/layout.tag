<%@tag description="Layout template" pageEncoding="UTF-8"%>
<%@attribute name="title" required="true" type="java.lang.String" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/css/style.css">
</head>
<body>
<jsp:doBody/>
</body>
</html>
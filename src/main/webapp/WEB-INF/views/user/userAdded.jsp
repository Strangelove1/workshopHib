<%--
  Created by IntelliJ IDEA.
  User: riain
  Date: 12.12.2023
  Time: 14:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Udało ci się zarejestrować, ${user.name} ${user.lastName}!</h1>
<a href="<c:url value="/login/login"/>"> Przejdź do logowania </a>

</body>
</html>

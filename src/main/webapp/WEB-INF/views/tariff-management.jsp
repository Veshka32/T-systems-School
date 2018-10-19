<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="services.TariffServiceI" %>
<html>
<head>
    <title>Tariffs</title>
</head>

<body>
<h2>All tariffs</h2>
<c:forEach items="${allTariffs}" var="tariff">
    ${tariff.toString()}</br>
</c:forEach>

<a href="createTariff">Create new tariff</a>
</body>
</html>

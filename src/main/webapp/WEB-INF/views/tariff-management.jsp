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
<h2>Create new tariff</h2>
<form method="post" action="createTariff">
    ${createTariffError}<br/>
    name: <input type="text" name="name"><br />
    <button type="submit">Create</button><br/>
</form>
</body>
</html>

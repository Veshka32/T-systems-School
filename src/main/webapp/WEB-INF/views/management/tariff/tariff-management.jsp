<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tariffs</title>
</head>

<body>
<h2>All tariffs</h2>
<table>
    <c:forEach items="${allTariffs}" var="tariff">
        <tr>
            <td>${tariff.name}</td>
            <td>
                <form action="editTariff" method="get">
                <input type="hidden" name="id" value=${tariff.id}>
                <input type="submit" value="Edit"></form>
            </td>
            <td>
            <form action="showTariff" method="get">
                <input type="hidden" name="id" value=${tariff.id}>
                <input type="submit" value="Show details"></form>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="createTariff">Create new tariff</a>
<a href="cabinet">Back to cabinet</a>

</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Clients</title>
</head>

<body>
<h2>Clients</h2>
All clients:<br>
<c:forEach items="${clients}" var="client">
    <tr>
        <td>${client.toString()}</td>
        <td>
            <form action="editClient" method="get">
                <input type="hidden" name="clientId" value=${client.id}>
                <input type="submit" value="Edit"></form>
        </td>
    </tr>
</c:forEach>

<a href="createClient">Create new client</a>
</body>
</html>

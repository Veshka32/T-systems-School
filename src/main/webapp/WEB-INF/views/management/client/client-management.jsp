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
Find client by phone:
<form:form method="POST" action="findClientByPhone" modelAttribute="phone">
    <form:input path="phoneNumber"/><input type="submit" value="Find"/><br>
    <form:errors path="phoneNumber"/>
</form:form>


<form action="showAllClients" method="post">
    <input type="submit" value="Show all clients">
</form>

<form action="showAllContracts" method="post">
    <input type="submit" value="Show all contracts">
</form>

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
<a href="cabinet">Back to cabinet</a>

</body>
</html>

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>Client</title>
</head>

<body>
<h2>Client ${editedClient.name}</h2>

<form:form method="POST" modelAttribute="editedClient">

</form:form>

<form action="createContract" method="get">
    <input type="hidden" name="clientId" value=${editedClient.id}>
    <input type="submit" value="Create contract">
</form>

Contracts:<br>
<c:forEach items="${contracts}" var="contract">
    <tr>
        <td>${contract.toString()}</td>
        <td>
            <form action="editContract" method="get">
                <input type="hidden" name="id" value=${contract.id}>
                <input type="submit" value="Edit"></form>
        </td>
    </tr>
</c:forEach>

<a href="clients">Back to clients</a>
</body>
</html>

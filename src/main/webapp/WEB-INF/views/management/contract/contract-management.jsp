<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Contracts</title>
</head>

<body>
<h2>Contracts</h2>
Find contract by phone:
<form:form method="POST" action="findContract" modelAttribute="phone">
    <form:input path="phoneNumber"/><input type="submit" value="Find"/><br>
    <form:errors path="phoneNumber"/>
</form:form>

<form action="showAllContracts" method="post">
    <input type="submit" value="Show all contracts">
</form>

<c:forEach items="${contracts}" var="contract">
    <tr>
        <td>${contract.toString()}</td>
        <td>
            <form action="editContract" method="get">
                <input type="hidden" name="contractId" value=${contract.id}>
                <input type="submit" value="Edit"></form>
        </td>
    </tr>
</c:forEach>

<a href="createClient">Create new client</a>
<a href="cabinet">Back to cabinet</a>

</body>
</html>

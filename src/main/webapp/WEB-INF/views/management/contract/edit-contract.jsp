<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>Edit Contract</title>
</head>

<body>
<h2>Phone number ${phone}</h2>

<form:form method="POST" modelAttribute="contract">
    <table>
        <tr>
            <td><form:hidden path="number" value="${phone}"/></td>
        </tr>

        <tr>
            <td>Tariff:</td>
            <td><form:select items="${allTariffs}" path="tariff" /></td>
            <td><form:errors path="tariff" /></td>
        </tr>

        <tr>
            <td>Add options:</td>
            <td><form:select items="${allOptions}" path="options" multiple="true"/></td>
            <td><form:errors path="options" /></td>
        </tr>

        <tr>
            <td>Blocked by client:</td>
            <td><form:checkbox path="blocked"/>Yes</td>
        </tr>

        <tr>
            <td>Blocked by admin:</td>
            <td><form:checkbox path="blockedByAdmin"/>Yes</td>
        </tr>

        <tr>
            <input type="hidden" name="clientId" value=${clientId}>
            <input type="hidden" name="phone" value="${phone}">
            <td colspan="3"><input type="submit" value="Save"/></td>
        </tr>
    </table>
</form:form>

Options:
<table>
    <c:forEach items="${contract.options}" var="option">
        <tr>
            <td>${option.toString()}</td>
            <td>
                <form action="contract/deleteOption" method="get">
                    <input type="hidden" name="id" value=${contract.id}>
                    <input type="hidden" name="option_id" value=${option.id}>
                    <input type="submit" value="Delete"></form>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="contracts">Back to search</a>
</body>
</html>
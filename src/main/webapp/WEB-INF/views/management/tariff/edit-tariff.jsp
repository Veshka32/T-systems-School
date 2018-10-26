<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>Tariffs</title>
</head>

<body>
<h2>Edit tariff ${editedTariff.name}</h2>
<span>${updated}</span><br><br>


<form:form method="POST" modelAttribute="editedTariff">
    <table>
        <tr>
            <td>Name:</td>
            <td><form:input path="name" value="${editedTariff.name}"/></td>
            <td><form:errors path="name" /></td>
        </tr>
        <tr>
            <td>Price:</td>
            <td><form:input path="price" value="${editedTariff.price}" /></td>
            <td><form:errors path="price" /></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><form:input value="${editedTariff.description}" path="description" /></td>
        </tr>

        <tr>
            <td>Archived:</td>
            <td><form:checkbox path="archived"/>Yes</td>
        </tr>
        <tr>
            <input type="hidden" name="id" value=${editedTariff.id}>
            <td colspan="3"><input type="submit" value="Save"/></td>
        </tr>
    </table>
</form:form>

Options:
<table>
    <c:forEach items="${currentOption}" var="option">
        <tr>
            <td>${option.name}</td>
            <td>
                <form action="tariff/deleteOption" method="get">
                    <input type="hidden" name="id" value=${editedTariff.id}>
                    <input type="hidden" name="option_id" value=${option.id}>
                    <input type="submit" value="Delete"></form>
            </td>
        </tr>
    </c:forEach>
</table>

Add options:
<table>
    <c:forEach items="${newOptions}" var="option">
        <tr>
            <td>${option.name}</td>
            <td>
                <form action="tariff/addOption" method="get">
                    <input type="hidden" name="id" value=${editedTariff.id}>
                    <input type="hidden" name="option_id" value=${option.id}>
                    <input type="submit" value="Add"></form>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="tariffs">Back to tariffs</a>
</body>
</html>

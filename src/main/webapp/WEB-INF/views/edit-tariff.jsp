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

<form:form method="POST" modelAttribute="editedTariff">
    <table>
        <tr>
            <td>Tariff name:</td>
            <td><form:input path="name" value="${editedTariff.name}"/></td>
        </tr>
        <tr>
            <td>Price:</td>
            <td><form:input path="price" value="${editedTariff.price}" /></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><form:input value="${editedTariff.description}" path="description" /></td>
        </tr>
        <tr>
            <td>Choose options:</td>
            <td><form:checkboxes items="${allOptions}" path="options" /></td>
        </tr>

        <tr>
            <td>Choose incompatible options:</td>
            <td><form:checkboxes items="${allOptions}" path="incompatibleOptions" /></td>
        </tr>

        <tr>
            <td>Choose incompatible tariffs:</td>
            <td><form:checkboxes items="${allTariffs}" path="incompatibleTariffs" /></td>
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
    <c:forEach items="${editedTariff.options}" var="option">
        <tr>
            <td>${option.toString()}</td>
            <td>
                <form action="deleteOption" method="get">
                    <input type="hidden" name="id" value=${editedTariff.id}>
                    <input type="hidden" name="option_id" value=${option.id}>
                    <input type="submit" value="Delete"></form>
            </td>
        </tr>
    </c:forEach>
</table>

Incompatible options:
<table>
    <c:forEach items="${editedTariff.incompatibleOptions}" var="option">
        <tr>
            <td>${option.toString()}</td>
            <td>
                <form action="deleteIncompatibleOption" method="get">
                    <input type="hidden" name="id" value=${editedTariff.id}>
                    <input type="hidden" name="option_id" value=${option.id}>
                    <input type="submit" value="Delete"></form>
            </td>
        </tr>
    </c:forEach>
</table>

Incompatible tariffs:
<table>
    <c:forEach items="${editedTariff.incompatibleTariffs}" var="tariff">
        <tr>
            <td>${option.toString()}</td>
            <td>
                <form action="deleteIncompatibleTariff" method="get">
                    <input type="hidden" name="id" value=${editedTariff.id}>
                    <input type="hidden" name="tariff_id" value=${tariff.id}>
                    <input type="submit" value="Delete"></form>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="tariffs">Back to tariffs</a>
</body>
</html>

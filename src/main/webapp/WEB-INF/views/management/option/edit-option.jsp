<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>Edit Option</title>
</head>


<h2>Edit option ${editedOption.name}</h2><br>
<span>${updated}</span><br><br>
<span>${error}</span><br><br>

<form:form method="POST" modelAttribute="editedOption">
    <table>
        <tr>
            <td>Option name:</td>
            <td><form:input path="name" value="${editedOption.name}"/></td>
            <td><form:errors path="name" /></td>
        </tr>
        <tr>
            <td>Price:</td>
            <td><form:input path="price" value="${editedOption.price}" /></td
            <td><form:errors path="price" /></td>
        </tr>

        <tr>
            <td>Subscribe cost:</td>
            <td><form:input value="${editedOption.subscribeCost}" path="subscribeCost" /></td>
            <td><form:errors path="subscribeCost" /></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><form:input value="${editedOption.description}" path="description" /></td>
        </tr>

        <tr>
            <td>Archived:</td>
            <td><form:checkbox path="archived"/>Yes</td>
        </tr>

        <tr>
            <input type="hidden" name="id" value=${editedOption.id}>
            <td colspan="3"><input type="submit" value="Save"/></td>
        </tr>
    </table>
</form:form>

Incompatible options:
<table>
    <c:forEach items="${currentIncompatible}" var="option">
        <tr>
            <td>${option.name}</td>
            <td>
                <form action="option/deleteIncompatibleOption" method="get">
                    <input type="hidden" name="id" value=${editedOption.id}>
                    <input type="hidden" name="option_id" value=${option.id}>
                    <input type="submit" value="Delete"></form>
            </td>
        </tr>
    </c:forEach>
</table>

Mandatory options:
<table>
    <c:forEach items="${currentMandatory}" var="option">
        <tr>
            <td>${option.name}</td>
            <td>
                <form action="option/deleteMandatoryOption" method="get">
                    <input type="hidden" name="id" value=${editedOption.id}>
                    <input type="hidden" name="option_id" value=${option.id}>
                    <input type="submit" value="Delete"></form>
            </td>
        </tr>
    </c:forEach>
</table>

Add incompatible options:
<table>
    <c:forEach items="${all}" var="option">
        <tr>
            <td>${option}</td>
            <td>
                <form action="option/addIncompatibleOption" method="get">
                    <input type="hidden" name="id" value=${editedOption.id}>
                    <input type="hidden" name="option_name" value=${option}>
                    <input type="submit" value="Add"></form>
            </td>
        </tr>
    </c:forEach>
</table>
Add mandatory options:
<table>
    <c:forEach items="${all}" var="option">
        <tr>
            <td>${option}</td>
            <td>
                <form action="option/addMandatoryOption" method="get">
                    <input type="hidden" name="id" value=${editedOption.id}>
                    <input type="hidden" name="option_id" value=${option}>
                    <input type="submit" value="Add"></form>
            </td>
        </tr>
    </c:forEach>
</table>

<br>
<form action="deleteOption" method="get">
    <input type="hidden" name="id" value=${editedOption.id}>
    <input type="submit" value="Delete option"></form>

<a href="options">Back to options</a>
</body>
</html>

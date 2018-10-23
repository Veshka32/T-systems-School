<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>Edit Option</title>
</head>

<body>
<h2>Edit option ${editedOption.name}</h2>

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
            <td><form:errors path="price" /></td>
        </tr>
        <tr>
            <td>Description:</td>
            <td><form:input value="${editedOption.description}" path="description" /></td>
        </tr>

        <tr>
            <td>Add incompatible options:</td>
            <td><form:select items="${allOptions}" path="incompatibleOptions" multiple="true"/></td>
            <td><form:errors path="incompatibleOptions" /></td>
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
    <c:forEach items="${editedOption.incompatibleOptions}" var="option">
        <tr>
            <td>${option.toString()}</td>
            <td>
                <form action="option/deleteIncompatibleOption" method="get">
                    <input type="hidden" name="id" value=${editedOption.id}>
                    <input type="hidden" name="option_id" value=${option.id}>
                    <input type="submit" value="Delete"></form>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="options">Back to options</a>
</body>
</html>

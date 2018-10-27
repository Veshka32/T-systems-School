<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>Edit Option</title>
</head>


<h2>Edit option ${editedOption.name}</h2><br>
<span>${message}</span><br><br>

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
            <td>Incompatible options:</td>
            <td><form:select multiple="true" path="incompatible" items="${all}" /></td>
        </tr>

        <tr>
            <td>Mandatory options:</td>
            <td><form:select multiple="true" path="mandatory" items="${all}"/></td>
        </tr>

        <tr>
            <input type="hidden" name="id" value=${editedOption.id}>
            <td colspan="3"><input type="submit" value="Save"/></td>
        </tr>
    </table>
</form:form>



<br>
<form action="deleteOption" method="get">
    <input type="hidden" name="id" value=${editedOption.id}>
    <input type="submit" value="Delete option"></form>

<a href="options">Back to options</a>
</body>
</html>

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>New option</title>
</head>

<body>
<h2>Create option</h2>

<form:form method="POST" modelAttribute="option">
    <table>
        <tr>
            <td>Option name:</td>
            <td><form:input path="name" /></td>
            <td><form:errors path="name" /></td>
        </tr>
        <tr>
            <td>Price:</td>
            <td><form:input path="price" /></td>
            <td><form:errors path="price" /></td>
        </tr>

        <tr>
            <td>Subscribe cost:</td>
            <td><form:input path="subscribeCost" /></td>
            <td><form:errors path="subscribeCost" /></td>
        </tr>

        <tr>
            <td>Description:</td>
            <td><form:input path="description" /></td>
            <td><form:errors path="description" /></td>
        </tr>

        <tr>
            <td>Set incompatible options :</td>
            <td><form:checkboxes items="${allOptions}" path="incompatibleOptions" /></td>
            <td><form:errors path="incompatibleOptions" /></td>
        </tr>

        <tr>
            <td colspan="3"><input type="submit" value="Save"/></td>
        </tr>
    </table>
</form:form><br>
<a href="options">Back to options</a>

</body>
</html>

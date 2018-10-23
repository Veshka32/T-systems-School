<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>New Contract</title>
</head>

<body>
<h2>Create contract</h2>

<form:form method="POST" modelAttribute="contract">
    <table>
        <tr>
            <td>Phone number: ${contract.number}</td>
        </tr>
        <tr>
            <td>Set tariff:</td>
            <td><form:select path="tariff" /></td>
            <td><form:errors path="tariff" /></td>
        </tr>

        <tr>
            <td>Add options:</td>
            <td><form:select path="options" multiple="true"/></td>
            <td><form:errors path="options" /></td>
        </tr>
        <tr>
            <td colspan="3"><input type="submit" value="Save"/></td>
        </tr>
    </table>
</form:form>

</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Tariffs</title>
</head>

<body>
<h2>Create tariff</h2>

<form:form method="POST" modelAttribute="tariff">
    <table>
        <tr>
            <td>Tariff name:</td>
            <td><form:input path="name" /></td>
        </tr>
        <tr>
            <td>Price:</td>
            <td><form:input path="price" /></td>
        </tr>
        <tr>
            <td>Base options :</td>
            <td><form:checkboxes items="${allOptions}" path="baseOptions" /></td>
        </tr>
        <tr>
            <td colspan="3"><input type="submit" /></td>
        </tr>
    </table>
</form:form>

</body>
</html>

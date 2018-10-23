<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>clients</title>
</head>

<body>
<h2>Create client</h2>

<form:form method="POST" modelAttribute="client">
    <table>
        <tr>
            <td>Name:</td>
            <td><form:input path="name" /></td>
            <td><form:errors path="name" /></td>
        </tr>
        <tr>
            <td>Surname:</td>
            <td><form:input path="surname" /></td>
            <td><form:errors path="surname" /></td>
        </tr>

        <tr>
            <td>Passport:</td>
            <td><form:input path="passportId" /></td>
            <td><form:errors path="passportId" /></td>
        </tr>
        <tr>
            <td>Birthday :</td>
            <td><form:input type="date" path="birthday" /></td>
            <td><form:errors path="birthday" /></td>
        </tr>
        <tr>
            <td>Email :</td>
            <td><form:input path="email" /></td>
            <td><form:errors path="email" /></td>
        </tr>

        <tr>
            <td>Address :</td>
            <td><form:input path="address" /></td>
            <td><form:errors path="address" /></td>
        </tr>

        <tr>
            <td colspan="3"><input type="submit" value="Save"/></td>
        </tr>
    </table>
</form:form>

</body>
</html>

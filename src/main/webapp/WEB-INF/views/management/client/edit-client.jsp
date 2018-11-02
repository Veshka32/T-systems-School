<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Edit client info</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<%@ include file="/resources/navbar.html" %>

<c:url value="/logout" var="logoutUrl"/>
<form id="logout" action="${logoutUrl}" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<div class="container">
    <span class="pull-right"><a href="clients" class="btn btn-info" role="button">Back to clients</a></span>
    <h3>Edit client info</h3>
    <p class="bg-danger">${message}</p>
    <form:form method="POST" modelAttribute="client">

    <div class="form-group">
        <label for="name">Name:</label>
        <form:input path="name" value="${client.name}" class="form-control" id="name"/>
        <p class="bg-danger"><form:errors path="name" /></p>
    </div>
    <div class="form-group">
        <label for="price">Surname:</label>
        <form:input path="surname" value="${client.surname}" class="form-control" id="price"/>
        <p class="bg-danger"><form:errors path="surname" /></p>
    </div>
    <div class="form-group">
        <label for="cost">Passport:</label>
        <form:input value="${client.passportId}" path="passportId" class="form-control" id="cost"/>
        <p class="bg-danger"><form:errors path="passportId" /></p>    </div>
    <div class="form-group">
        <label for="desc">Email:</label>
        <form:input value="${client.email}" path="email" class="form-control" id="desc"/>
        <p class="bg-danger"><form:errors path="email" /></p>
    </div>
    <div class="form-group">
        <label for="desc">Birthday:</label>
        <form:input type="date" value="${client.birthday}" path="birthday" class="form-control" id="desc"/>
        <p class="bg-danger"><form:errors path="birthday" /></p>
    </div>
    <div class="form-group">
        <label for="arch">Address:</label>
        <form:input path="address" class="form-control" id="arch"/>
    </div>
    <input type="hidden" name="id" value=${client.id}>
            <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>
    <br>

    <div class="pull-right"><form action="deleteClient" method="get">
        <input type="hidden" name="id" value=${client.id}>
        <input type="submit" value="Delete client" class="btn btn-danger"></form></div>

</body>
</html>

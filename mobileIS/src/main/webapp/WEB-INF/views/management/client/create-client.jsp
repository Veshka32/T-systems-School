<%--suppress ALL --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Client details</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<%@include file="/resources/navbar.jsp" %>

<div class="container">
    <span class="pull-right"><a href="clients" class="btn btn-info" role="button">Back to clients</a></span>
    <h3>Create client</h3>
    <p class="bg-danger">${message}</p>
    <form:form method="POST" modelAttribute="client">

    <div class="form-group">
        <label for="name">Name:</label>
        <form:input path="name" value="${client.name}" class="form-control" id="name"/>
        <p class="bg-danger"><form:errors path="name"/></p>
    </div>

    <div class="form-group">
        <label for="surname">Surname:</label>
        <form:input path="surname" value="${client.surname}" class="form-control" id="surname"/>
        <p class="bg-danger"><form:errors path="surname"/></p>
    </div>

    <div class="row">
        <div class="form-group col-xs-4">
            <label for="pass">Passport:</label>
            <form:input value="${client.passportId}" path="passportId" class="form-control" id="pass" type="number"/>
            <p class="bg-danger"><form:errors path="passportId"/></p>
        </div>

        <div class="form-group col-xs-4">
            <label for="desc">Email:</label>
            <form:input value="${client.email}" path="email" class="form-control" id="desc"/>
            <p class="bg-danger"><form:errors path="email"/></p>
        </div>

        <div class="form-group col-xs-2">
            <label for="desc">Birthday:</label>
            <form:input type="date" value="${client.birthday}" path="birthday" class="form-control" id="desc"/>
            <p class="bg-danger"><form:errors path="birthday"/></p>
        </div>
    </div>

    <div class="form-group">
        <label for="arch">Address:</label>
        <form:input path="address" class="form-control" id="arch"/>
    </div>
        <form:hidden path="id" value="${client.id}"/>
    <input type="submit" value="Save" class="btn btn-success"/>
    <c:if test="${not empty client.id}">
    <form action="deleteClient" method="post">
        <input type="hidden" name="id" value=${client.id}>
        <input type="submit" value="Delete client" class="btn btn-danger pull-right">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </c:if>

    </form:form>
    <br>

</body>
</html>

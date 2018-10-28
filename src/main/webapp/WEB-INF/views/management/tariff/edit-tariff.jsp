<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Edit tariff</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">Space mobile</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"><a href="cabinet">Cabinet</a></li>
            <li class="active"><a href="clients">Clients</a></li>
            <li class="active"><a href="contracts">Contracts</a></li>
            <li class="active"><a href="tariffs">Tariffs</a></li>
            <li class="active"><a href="options">Options</a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><c:if test="${pageContext.request.userPrincipal.name != null}">
                <a href="javascript:document.getElementById('logout').submit()"><span
                        class="glyphicon glyphicon-log-out"></span>LOG OUT</a>
            </c:if></li>
        </ul>

    </div>
</nav>

<c:url value="/logout" var="logoutUrl"/>
<form id="logout" action="${logoutUrl}" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<div class="container">
    <span class="pull-right"><a href="tariffs" class="btn btn-info" role="button">Back to tariffs</a></span>
    <h3>Edit tariff</h3>
    <p class="bg-danger">${message}</p>
<form:form method="POST" modelAttribute="editedTariff">

    <div class="form-group">
        <label for="name">Name:</label>
        <form:input path="name" value="${editedTariff.name}" class="form-control" id="name"/>
        <p class="bg-danger"><form:errors path="name" /></p>
    </div>
    <div class="form-row">
        <div class="form-group col-md-6">
            <label for="price">Price:</label>
            <form:input path="price" value="${editedTariff.price}" class="form-control" id="price"/>
            <p class="bg-danger"><form:errors path="price" /></p>
        </div>
    </div>
    <div class="form-group">
        <label for="desc">Description:</label>
        <form:input value="${editedTariff.description}" path="description" class="form-control" id="desc"/>
    </div>

    <div class="form-group">
        <div class="form-check">
            <label class="form-check-label" for="arch">Archived:</label><form:checkbox path="archived" class="checkbox" id="arch"/>
        </div>
    </div>
    <div class="form-check">
    </div>
    <div class="form-group">
        <label for="inc">Options:</label>
        <form:select multiple="true" path="options" items="${all}" class="form-control" id="inc"/>
    </div>
            <input type="hidden" name="id" value=${editedTariff.id}>
            <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>

    <span class="pull-right"><form action="deleteTariff" method="get">
        <input type="hidden" name="id" value=${editedTariff.id}>
        <input type="submit" value="Delete tariff" class="btn btn-danger"></form></span>


</body>
</html>

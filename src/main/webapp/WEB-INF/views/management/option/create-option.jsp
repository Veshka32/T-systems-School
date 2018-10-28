<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create option</title>
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
    <span class="pull-right"><a href="options" class="btn btn-info" role="button">Back to options</a></span>
    <h3>Create option</h3>
    <p class="bg-danger">${message}</p>
<form:form method="POST" modelAttribute="option">

    <div class="form-group">
        <label for="name">Name:</label>
        <form:input path="name" value="${option.name}" class="form-control" id="name"/>
        <p class="bg-danger"><form:errors path="name" /></p>
    </div>
    <div class="form-group">
        <label for="price">Price:</label>
        <form:input path="price" value="${option.price}" class="form-control" id="price"/>
        <form:errors path="price" />
    </div>
    <div class="form-group">
        <label for="cost">Subscribe cost:</label>
            <form:input value="${option.subscribeCost}" path="subscribeCost" class="form-control" id="cost"/>
        <form:errors path="subscribeCost" />
    </div>
    <div class="form-group">
        <label for="desc">Description:</label>
        <form:input value="${option.description}" path="description" class="form-control" id="desc"/>
    </div>
    <div class="form-group">
        <label for="arch">Archived:</label>
        <form:checkbox path="archived" class="checkbox" id="arch"/>
    </div>
    <div class="form-group">
        <label for="inc">Incompatible options:</label>
        <form:select multiple="true" path="incompatible" items="${all}" class="form-control" id="inc"/>
    </div>
    <div class="form-group">
        <label for="mand">Mandatory options:</label>
        <form:select multiple="true" path="mandatory" items="${all}" class="form-control" id="mand"/>
    </div>
            <input type="hidden" name="id" value=${option.id}>
            <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>
    <br>

</body>
</html>

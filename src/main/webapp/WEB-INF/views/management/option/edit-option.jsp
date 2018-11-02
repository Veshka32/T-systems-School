<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Edit option</title>
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
    <span class="pull-right"><a href="options" class="btn btn-info" role="button">Back to options</a></span>
    <h3>Edit option</h3>
    <p class="bg-danger">${message}</p>
    <form:form method="POST" modelAttribute="editedOption">

    <div class="form-group row">

    <div class="form-group col-xs-4">
        <label for="name">Name:</label>
        <form:input path="name" value="${editedOption.name}" class="form-control" id="name"/>
        <p class="bg-danger"><form:errors path="name"/></p>
    </div>

        <div class="form-group col-xs-2">
            <label for="price">Price:</label>
            <form:input path="price" value="${editedOption.price}" class="form-control" id="price"/>
            <p class="bg-danger"><form:errors path="price"/></p>
        </div>

        <div class="form-group col-xs-2">
            <label for="cost">Subscribe cost:</label>
            <form:input value="${editedOption.subscribeCost}" path="subscribeCost" class="form-control" id="cost"/>
            <p class="bg-danger"><form:errors path="subscribeCost"/></p>
        </div>
    </div>

    <div class="form-group">
        <label for="desc">Description:</label>
        <form:input value="${editedOption.description}" path="description" class="form-control" id="desc"/>
    </div>

    <label for="inc">Set incompatible options:</label>
    <div class="form-check form-check-inline">
        <c:forEach items="${all}" var="item">
            <form:checkbox path="incompatible" value="${item}" class="form-check" id="inc"/>
            <label class="form-check-label" for=inc>${item}</label>
        </c:forEach></div>
    <br>
    <label for="inc">Set mandatory options:</label>
    <div class="form-check form-check-inline"><c:forEach items="${all}" var="item">
        <form:checkbox path="mandatory" value="${item}" class="form-check" id="inc"/>
        <label class="form-check-label" for=inc>${item}</label>
    </c:forEach></div>

    <input type="hidden" name="id" value=${editedOption.id}>
    <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>
    <br>

    <div class="pull-right">
        <form action="deleteOption" method="get">
            <input type="hidden" name="id" value=${editedOption.id}>
            <input type="submit" value="Delete option" class="btn btn-danger"></form>
    </div>
</body>
</html>

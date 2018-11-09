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

<%@ include file="/resources/navbar.html" %>

<div class="container">
    <span class="pull-right"><a href="tariffs" class="btn btn-info" role="button">Back to tariffs</a></span>
    <h3>Edit tariff</h3>
    <p class="bg-danger">${message}</p>
    <form:form method="POST" modelAttribute="tariff">

    <div class="form-group">
        <label for="name">Name:</label>
        <form:input path="name" value="${tariff.name}" class="form-control" id="name"/>
        <p class="bg-danger"><form:errors path="name"/></p>
    </div>

    <div class="form-group">
        <label for="price">Price:</label>
        <form:input path="price" value="${tariff.price}" class="form-control" id="price"/>
        <p class="bg-danger"><form:errors path="price"/></p>
    </div>

    <div class="form-group">
        <label for="desc">Description:</label>
        <form:input value="${tariff.description}" path="description" class="form-control" id="desc"/>
    </div>

    <div class="form-check form-check-inline">
        <label for="inc">Set options:</label>
        <br>
        <c:forEach items="${all}" var="item">
            <form:checkbox path="options" value="${item}" class="form-check" id="inc"/>
            <label class="form-check-label" for=inc>${item}</label>
            <br>
        </c:forEach>
    </div>
    <br>

    <input type="hidden" name="id" value=${tariff.id}>
    <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>
    <br>


    <div class="pull-right">
        <form action="deleteTariff" method="post">
        <input type="hidden" name="id" value=${tariff.id}>
        <input type="submit" value="Delete tariff" class="btn btn-danger"></form></div>
</body>
</html>

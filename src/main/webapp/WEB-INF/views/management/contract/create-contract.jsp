<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create contract</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<%@ include file="/resources/navbar.html" %>

<div class="container">
    <h3>Create contract</h3>
    <p class="bg-danger">${message}</p>
<form:form method="POST" modelAttribute="contract">

    <div class="form-group">
        <label for="tr">Choose tariff:</label>
        <form:select path="tariffName" items="${allTariffs}" class="form-control" id="tr"/>
    </div>

    <div class="form-check form-check-inline" id="opt">
        <label for="opt">Set options:</label><br>
        <c:forEach items="${allOptions}" var="item">
        <form:checkbox path="optionsNames" value="${item}" class="form-check" id="inc"/>
        <label class="form-check-label" for=inc>${item}</label>
            <br>
    </c:forEach></div>
    <br>

        <form:hidden path="ownerId" value="${contract.ownerId}"/>

    <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>
    <br>

</body>
</html>

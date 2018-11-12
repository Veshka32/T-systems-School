<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Edit contract</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<%@ include file="/resources/navbar.html" %>

<div class="container">
    <span class="pull-right"><a href="contracts" class="btn btn-info" role="button">Back to contracts</a></span>
    <h3>Edit contract</h3>
    <p class="bg-danger">${message}</p>
<form:form method="POST" modelAttribute="editedContract">
    <ul class="list-group">
        <li class="list-group-item"> Phone number: ${editedContract.number}</li>
        <li class="list-group-item">Current tariff: ${editedContract.tariffName}</li>
        <li class="list-group-item">Options: ${editedContract.optionsNames}</li>
    </ul>

    <div class="checkbox">
        <label><form:checkbox path="blocked"/>Client blocking</label>
    </div>

    <div class="checkbox">
        <label><form:checkbox path="blockedByAdmin"/>Admin blocking</label>
    </div>
    <br>

    <div class="form-group">
        <label for="tr">Choose tariff:</label>
        <form:select path="tariffName" items="${allTariffsNames}" class="form-control" id="tr"/>
    </div>


    <div class="form-check form-check-inline">
        <label for="opt">Set options:</label><br>
        <c:forEach items="${allOptionsNames}" var="item">
            <form:checkbox path="optionsNames" value="${item}" class="form-check" id="opt"/>
            <label class="form-check-label" for=opt>${item}</label>
            <br>
    </c:forEach></div>
    <br>

        <form:hidden path="ownerId" value="${editedContract.ownerId}"/>
        <form:hidden path="id" value="${editedContract.id}"/>

    <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>

    <div class="pull-right">
        <form action="deleteContract" method="post">
        <input type="hidden" name="id" value=${editedContract.id}>
        <input type="hidden" name="clientId" value=${editedContract.ownerId}>
            <input type="submit" value="Delete contract" class="btn btn-danger">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>

</body>
</html>

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
    <h3>Edit contract</h3>
    <p class="bg-danger">${message}</p>
<form:form method="POST" modelAttribute="editedContract">

    Phone number: ${editedContract.number}<br>
    Current tariff: ${editedContract.tariffName}<br>
    Options: ${editedContract.optionsNames}<br>

    <div class="form-check">
        <label for="bl">Client blocking:</label>
        <form:checkbox path="blocked" class="form-control" id="bl"/>
    </div>

    <div class="form-check">
        <label for="bl1">Admin blocking:</label>
        <form:checkbox path="blockedByAdmin" class="form-control" id="bl1"/>
    </div>

    <div class="form-group">
        <label for="tr">Choose tariff:</label>
        <form:select path="tariffName" items="${allTariffs}" class="form-control" id="tr"/>
    </div>

    <label for="opt">Set options:</label>
    <div class="form-check form-check-inline" id="opt">
        <c:forEach items="${allOptions}" var="item">
        <form:checkbox path="optionsNames" value="${item}" class="form-check" id="inc"/>
        <label class="form-check-label" for=inc>${item}</label>
    </c:forEach></div>

        <form:hidden path="ownerId" value="${editedContract.ownerId}"/>
        <form:hidden path="id" value="${editedContract.id}"/>

    <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>

    <div class="pull-right"><form action="deleteContract" method="get">
        <input type="hidden" name="id" value=${editedContract.id}>
        <input type="hidden" name="clientId" value=${editedContract.ownerId}>
        <input type="submit" value="Delete contract" class="btn btn-danger"></form></div>

</body>
</html>

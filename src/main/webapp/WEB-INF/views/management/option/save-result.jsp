<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Option</title>
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
    <h3>Option details</h3>
    <table class="table table-striped">
        <thead>
        <th style="width:20%"></th>
        <th style="width:80%"></th>
        </thead>
        <tbody>
        <tr>
            <td >Id:</td>
            <td>${newOption.id} </td>
        </tr>
        <tr>
            <td>Name:</td>
            <td>${newOption.name} </td>
        </tr>
        <tr>
            <td>Price:</td>
            <td>${newOption.price} </td>
        </tr>
        <tr>
            <td>Subscribe cost:</td>
            <td>${newOption.subscribeCost} </td>
        </tr>
        <tr>
            <td>Description:</td>
            <td>${newOption.description} </td>
        </tr>

        <tr>
            <td>Is archived:</td>
            <td>${newOption.archived} </td>
        </tr>

        <tr>
            <td>Mandatory options:</td>
            <td>${mandatoryOptions}</td>
        </tr>

        <tr>
            <td>Incompatible options:</td>
            <td>${badOptions}</td>
        </tr>
        </tbody>
    </table>

    <form action="editOption" method="get">
        <input type="hidden" name="id" value=${newOption.id}>
        <input type="submit" value="Edit option" class="btn btn-warning"></form>
    <br>

    <a href="options" class="btn btn-info" role="button">Back to options</a>


</body>
</html>

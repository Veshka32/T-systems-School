<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Tariffs</title>
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
                <a href="javascript:document.getElementById('logout').submit()"><span class="glyphicon glyphicon-log-out"></span>LOG OUT</a>
            </c:if></li>
        </ul>

    </div>
</nav>

<c:url value="/logout" var="logoutUrl" />
<form id="logout" action="${logoutUrl}" method="post" >
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<div class="container">
    <h3>All tariffs</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>id</th>
            <th>Name</th>
            <th>Price</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${allTariffs}" var="tariff">
            <tr>
                <td>${tariff.id} </td>
                <td>${tariff.name}</td>
                <td>${tariff.price}</td>
                <td>${tariff.description}</td>
                <td>
                    <form action="editTariff" method="get">
                        <input type="hidden" name="id" value=${tariff.id}>
                        <input type="submit" value="Edit"  class="btn btn-warning"></form>
                </td>
                <td>
                    <form action="showTariff" method="get">
                        <input type="hidden" name="id" value=${tariff.id} >
                        <input type="submit" value="Show details" class="btn"></form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <a href="createTariff" role="button" class="btn btn-success">Create new tariff</a><br></div>

</body>
</html>

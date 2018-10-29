<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Contracts</title>
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
    <h3>Find contract by phone:</h3>
    <p class="bg-danger">${message}</p>
    <form:form method="POST" action="findContractByPhone" modelAttribute="phone">
        <form:input path="phoneNumber"/><input type="submit" value="Find"/><br>
        <p class="bg-danger"><form:errors path="phoneNumber"/></p>
    </form:form>
    <br>

    <h3>All contracts</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>id</th>
            <th>Phone</th>
            <th>Tariff</th>
            <th>Owner</th>
            <th>Is blocked</th>
            <th>Blocked by admin</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${allContracts}" var="contract">
            <tr>
                <td>${contract.id} </td>
                <td>${contract.number}</td>
                <td>${contract.tariff.toString()}</td>
                <td>${contract.owner.toString()}</td>
                <td>${contarct.isBlocked}</td
                <td>${contarct.isBlockedByAdmin}</td
                <td>
                    <form action="editContract" method="get">
                        <input type="hidden" name="id" value=${contract.id}>
                        <input type="submit" value="Edit"  class="btn btn-warning"></form>
                </td>
                <td>
                    <form action="showContract" method="get">
                        <input type="hidden" name="id" value=${contract.id} >
                        <input type="submit" value="Show details" class="btn"></form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>

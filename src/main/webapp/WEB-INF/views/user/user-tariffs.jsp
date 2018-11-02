<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>User cabinet</title>
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
            <a class="navbar-brand" href="index">Space mobile</a>
        </div>
        <ul class="nav navbar-nav">
            <li class="active"><a href="cabinet">Cabinet</a></li>
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
    <%--Tariffs--%>
    <h4>Tariffs:</h4>
        <span class="pull-right"><a href="cabinet" class="btn btn-info" role="button">Back to cabinet</a></span>
    <div class="row" id="options">
        <c:forEach items="${allTariffs}" var="tariff">
            <div class="col-sm-4">
                <div class="panel panel-info">
                    <div class="panel-heading">${tariff.name}</div>
                    <div class="panel-body">
                            ${tariff.description}<br>
                            ${tariff.price} per month
                    </div>
                    <div class="panel-footer">
                        <a href="getTariff/${tariff.id}" role="button" class="btn btn-info">Get tariff</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>

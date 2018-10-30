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
    <div class="row">
        <div class="col-sm-4">
            <div class="panel panel-info">
                <div class="panel-heading">Phone number: ${contract.number}
                    <c:if test="${contract.blocked}">
                        <span class="label label-danger right-pill">Blocked</span>
                    </c:if>
                    <c:if test="${contract.blockedByAdmin}">
                        <span class="label label-danger right-pill">Blocked</span>
                    </c:if>
                </div>
                <div class="panel-body">
                    Tariff:${contract.tariff.name}<br>
                    ${contract.tariff.description}<br>
                    ${contract.tariff.price} per month
                </div>
                <div class="panel-footer">
                    <c:choose>
                        <c:when test="${contract.blockedByAdmin}">""</c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${contract.blocked}"><a href="unblock" role="button"
                                                                      class="btn btn-success">Unblock
                                    number</a></c:when>
                                <c:otherwise><a href="block" role="button" class="btn btn-danger">Block number</a>
                                    <a href="showTariffs" role="button" class="btn btn-info">Change tariff</a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </div>

    </div>
    <%--Options--%>
    <h4>Active options:</h4>
    <div class="row">
        <c:forEach items="${tariffOptions}" var="option">
            <div class="col-sm-4">
                <div class="panel panel-info">
                    <div class="panel-heading">${option.name}</div>
                    <div class="panel-body">
                            ${option.description}<br>
                            ${option.price} per month
                    </div>
                    <div class="panel-footer">Can't be deactivated</div>
                </div>
            </div>
        </c:forEach>
        <c:forEach items="${contractOptions}" var="option">
            <div class="col-sm-4">
                <div class="panel panel-info">
                    <div class="panel-heading">${option.name}</div>
                    <div class="panel-body">
                            ${option.description}<br>
                            ${option.price} per month
                    </div>
                    <div class="panel-footer">
                        <a href="deleteOption/${option.id}" role="button" class="btn btn-info">Delete option</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <%--Available options--%>
    <h4>Available options:</h4>
    <div class="row" id="options">
        <c:forEach items="${availableOptions}" var="option">
            <div class="col-sm-4">
                <div class="panel panel-success">
                    <div class="panel-heading">${option.name}</div>
                    <div class="panel-body">
                            ${option.description}<br>
                            ${option.price} per month<br>
                            ${option.subscribeCost} for subscribe
                    </div>
                    <div class="panel-footer">
                        <a href="getOption/${option.id}" role="button" class="btn btn-info">Get option</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>


</div>
</body>
</html>

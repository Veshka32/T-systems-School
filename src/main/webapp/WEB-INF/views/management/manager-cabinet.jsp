<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Manager cabinet</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<style>
    .jumbotron {
        background-color: #082759; /* dark blue */
        color: #ffffff;
    }
    .logo-small {
        color: #072740;
        font-size: 70px;
    }
    .navbar {
        margin-bottom: 0;
        background-color: #072740;
        z-index: 9999;
        border: 0;
        font-size: 12px !important;
        line-height: 1.42857143 !important;
        letter-spacing: 4px;
        border-radius: 0;
    }

    .navbar li a, .navbar .navbar-brand {
        color: #fff !important;
    }

    .navbar-nav li a:hover, .navbar-nav li.active a {
        color: #072740 !important;
        background-color: #fff !important;
    }

    .navbar-default .navbar-toggle {
        border-color: transparent;
        color: #fff !important;
    }

</style>



<div class="jumbotron text-center">
    <h1>Space Mobile</h1>
    <p>Calls through space and time</p>
</div>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">Logo</a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
                <li><c:if test="${pageContext.request.userPrincipal.name != null}">
                    <a href="javascript:document.getElementById('logout').submit()">LOG OUT</a>
                </c:if></li>
            </ul>
        </div>
    </div>
</nav>

<c:url value="/logout" var="logoutUrl" />
<form id="logout" action="${logoutUrl}" method="post" >
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>




<div class="container-fluid text-center">
    <h2>MANAGER CABINET</h2>
    <h4>Be careful with delete button</h4>
    <br>
    <div class="row">
        <div class="col-sm-6" class="jumbotron">
            <a href="clients"><span class="glyphicon glyphicon-user logo-small"></span></a>
            <h4>Clients</h4>
            <p>all about our clients</p>
        </div>
        <div class="col-sm-6">
            <a href="contracts"><span class="glyphicon glyphicon-earphone logo-small"></span></a>
            <h4>Contracts</h4>
            <p>All contracts in one place</p>
        </div>
    </div>
    <br><br>
    <div class="row">
        <div class="col-sm-6">
            <a href="tariffs"><span class="glyphicon glyphicon-wrench logo-small"></span></a>
            <h4>Tariffs</h4>
            <p>tariff management</p>
        </div>
        <div class="col-sm-6">
            <a href="options"><span class="glyphicon glyphicon-list logo-small"></span></a>
            <h4>Tariff options</h4>
            <p>Set up options</p>
        </div>
    </div>
</div>

<c:url value="/logout" var="logoutUrl" />
<form id="logout" action="${logoutUrl}" method="post" >
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>
<c:if test="${pageContext.request.userPrincipal.name != null}">
    <a href="javascript:document.getElementById('logout').submit()">Logout</a>
</c:if>
</body>
</html>
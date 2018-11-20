<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Lato" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/style.css"/>">
    <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>

    <title>Sign up</title>
</head>

<body data-spy="scroll" data-target=".navbar" data-offset="60">
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
                <sec:authorize access="hasRole('MANAGER')">
                    <li><a href="management/cabinet">MANAGEMENT</a></li>
                </sec:authorize>
                <sec:authorize access="hasRole('CLIENT')">
                    <li><a href="user/cabinet">Cabinet</a></li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                           data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                            Cart<span class="glyphicon glyphicon-shopping-cart"></span>
                        </a>
                    </li>

                </sec:authorize>
                <sec:authorize access="isAnonymous()">
                    <li><a href="login">SIGN IN<span class="glyphicon glyphicon-log-in"></span></a></li>
                </sec:authorize>

                <sec:authorize access="isAnonymous()">
                    <li><a href="register">SIGN UP<span class="glyphicon glyphicon-user"></span></a></li>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <li>
                        <a href="javascript:document.getElementById('logout').submit()">LOG OUT <span
                                class="glyphicon glyphicon-log-out"></span></a>
                        <form id="logout" action="logout" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    </li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>
<div class="jumbotron text-center">
    <h1><img src="<c:url value="/resources/spacelogo.jpg"/>" class="img-valign" width="150" height="120">Multiverse
        mobile</h1>
    <p>Feel free to communicate through space and time</p>
</div>

<div class="container">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Enter phone number and password</h3>
                </div>
                <div class="panel-body">

                    <c:if test="${not empty message1}">
                        <div class="bg-danger">${message1}</div>
                    </c:if>
                    <form:form  method='POST' modelAttribute="user">
                        <fieldset>
                            <div class="form-group">
                                <form:input type="number" path="login" class="form-control" placeholder="9062107057"/>
                                <p class="bg-danger"><form:errors path="login"/></p>
                            </div>
                            <div class="form-group">
                                <form:input type="password" path="password" class="form-control" placeholder="password" />
                                <p class="bg-danger"><form:errors path="password"/></p>
                            </div>
                            <input class="btn btn-lg btn-success btn-block" type="submit"
                                   value="Sign up"/>
                        </fieldset>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>


</html>

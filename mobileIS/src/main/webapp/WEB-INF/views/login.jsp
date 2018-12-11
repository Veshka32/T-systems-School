<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/style.css"/>">

    <title>Sign in</title>
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

<div style="position: absolute; width: 100%">
    <div id="wrapper" style="position:relative;">
        <div class="jumbotron text-center">
            <h1><img src="<c:url value="/resources/spacelogo.jpg"/>" class="img-valign" width="150" height="120">Multiverse
                mobile</h1>
            <p>Feel free to communicate through space and time</p>
        </div>
        <div class="row">
            <div class="col-md-4 col-md-offset-4">
                <div class="login-panel panel">
                    <div class="panel-body">
                        <h3 class="panel-title">Please sign in</h3><br>

                        <c:if test="${not empty message}">
                            <div id="error">${message}</div>
                        </c:if>

                        <form role="form" action="<c:url value='login' />" method='POST'>
                            <fieldset>
                                <div class="form-group">
                                    <input id="login" class="form-control" placeholder="9062107057" name="username"
                                           type="text"
                                           autofocus>
                                </div>
                                <div class="form-group">
                                    <input id="password" class="form-control" placeholder="password" name="password"
                                           type="password"
                                           value="">
                                </div>
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                                <input class="btn btn-lg btn-success btn-block" name="submit" type="submit"
                                       value="Sign in"/>
                            </fieldset>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<div id="particles-js"></div>
<script src="<c:url value="/resources/particles-js/particles.js"/>"></script>
<script src="<c:url value="/resources/particles-js/app.js"/>"></script>
</body>


</html>

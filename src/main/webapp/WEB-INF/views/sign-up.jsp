<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

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
    <script src="https://code.jquery.com/jquery-1.11.1.min.js"></script>

    <title>Sign up</title>
</head>

<body>

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
                    </c:if><br>
                    <form:form  method='POST' modelAttribute="user">
                        <fieldset>
                            <div class="form-group">
                                <form:input type="text" path="login" class="form-control" placeholder="9062107057"  />
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
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Please Sign In</h3>
                </div>
                <div class="panel-body">

                    <c:if test="${not empty message}">
                        <div>${message}</div>
                    </c:if>

                    <form role="form" action="<c:url value='login' />" method='POST'>
                        <fieldset>
                            <div class="form-group">
                                <input class="form-control" placeholder="9062107057" name="username" type="text"
                                       autofocus>
                            </div>
                            <div class="form-group">
                                <input class="form-control" placeholder="password" name="password" type="password"
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
</body>


</html>

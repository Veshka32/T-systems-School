<%@ page contentType="text/html;charset=UTF-8" %>
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
<%@ include file="/resources/navbar.html" %>

<c:url value="/logout" var="logoutUrl" />
<form id="logout" action="${logoutUrl}" method="post" >
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
</form>

<div class="container">
    <h3>Manager cabinet</h3>
    <ul class="list-group">
        <li class="list-group-item">To create Client, go Clients - > Add Client</li>
        <li class="list-group-item">To create contract, go Clients - > Find Client by number or passport id - > Add
            contract
            or Clients - > All
            clients - > Show details - > Add contract
        </li>
        <li class="list-group-item">You can find client by phone number or by passport id</li>
        <li class="list-group-item">You can find contract by number or find client first</li>
        <li class="list-group-item list-group-item-danger">Be extremely careful with Delete button!</li>

    </ul>


</div>

</body>
</html>

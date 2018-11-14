<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Edit contract</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.2/css/bootstrap-select.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.2/js/bootstrap-select.min.js"></script>
</head>
<body>

<%@ include file="/resources/navbar.html" %>

<div class="container">
    <span class="pull-right"><a href="contracts" class="btn btn-info" role="button">Back to contracts</a></span>
    <h3>Edit contract</h3>
    <p class="bg-danger">${message}</p>
<form:form method="POST" modelAttribute="editedContract">
    <ul class="list-group">
        <li class="list-group-item"> Phone number: ${editedContract.number}</li>
        <li class="list-group-item"> Owner: ${editedContract.ownerName}</li>
        <li class="list-group-item">Current tariff: ${editedContract.tariffName}</li>
        <li class="list-group-item">Options: ${editedContract.optionNames}</li>
    </ul>

    <div class="checkbox">
        <label><form:checkbox path="blocked"/>Client blocking</label>
    </div>

    <div class="checkbox">
        <label><form:checkbox path="blockedByAdmin"/>Admin blocking</label>
    </div>
    <br>

    <div class="form-group">
        <label for="tr">Choose tariff:</label>
        <form:select path="tariffId" id="tr" class="selectpicker"
                     data-live-search="true" data-size="5" data-actions-box="true" data-width="75%">
            <c:forEach items="${allTariffs}" var="item">
                <form:option label="${item.key}" value="${item.value}"/>
            </c:forEach>
        </form:select>
    </div>

    <div class="form-group">
        <label for="inc">Set options:</label>
        <form:select path="optionsIds" multiple="multiple" id="inc" class="selectpicker"
                     data-live-search="true" data-size="5" data-actions-box="true" data-width="75%">
            <c:forEach items="${allOptions}" var="item">
                <form:option label="${item.key}" value="${item.value}"/>
            </c:forEach>
        </form:select>
    </div>

        <form:hidden path="ownerId" value="${contract.ownerId}"/>
        <form:hidden path="id" value="${contract.id}"/>

    <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>

    <div class="pull-right">
        <form action="deleteContract" method="post">
            <input type="hidden" name="id" value=${contract.id}>
            <input type="hidden" name="clientId" value=${contract.ownerId}>
            <input type="submit" value="Delete contract" class="btn btn-danger">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
    </div>

</body>
</html>

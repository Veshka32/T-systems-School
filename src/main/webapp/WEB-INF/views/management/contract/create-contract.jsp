<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create contract</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.2/css/bootstrap-select.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.13.2/js/bootstrap-select.min.js"></script>
<body>

<%@ include file="/resources/navbar.html" %>

<div class="container">
    <h3>Create contract</h3>
    <p class="bg-danger">${message}</p>
<form:form method="POST" modelAttribute="contract">

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
            <c:forEach items="${allOption}" var="item">
                <form:option label="${item.key}" value="${item.value}"/>
            </c:forEach>
        </form:select>
    </div>
        <form:hidden path="ownerId" value="${contract.ownerId}"/>

    <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>
    <br>

</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Tariff editor</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <script src="<c:url value="/resources/jquery.bootstrap-duallistbox.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bootstrap-duallistbox.css"/>">
</head>
<body>
<%@include file="/resources/navbar.jsp" %>

<div class="container">
    <span class="pull-right"><a href="tariffs" class="btn btn-info" role="button">Back to tariffs</a></span>
    <h3>Tariff details</h3>
    <c:if test="${not empty message}"><p class="bg-danger">${message}</p></c:if>
<form:form method="POST" modelAttribute="tariff">

    <div class="form-group">
        <label for="name">Name:</label>
        <form:input path="name" value="${tariff.name}" class="form-control" id="name"/>
        <p class="bg-danger"><form:errors path="name"/></p>
    </div>
    <div class="row">
        <div class="form-group col-xs-4">
            <label for="price">Price:</label>
            <form:input path="price" value="${tariff.price}" class="form-control" id="price" type="number" step="0.01"/>
            <p class="bg-danger"><form:errors path="price"/></p>
        </div>
    </div>

    <div class="form-group">
        <label for="desc">Description:</label>
        <form:input value="${tariff.description}" path="description" class="form-control" id="desc"/>
    </div>

    <div class="form-group">
        <label for="opt">Set options:</label>
        <form:select path="options" multiple="multiple" id="opt">
            <c:forEach items="${all}" var="item">
                <form:option label="${item.key}" value="${item.value}"/>
            </c:forEach>
        </form:select>
    </div>

    <c:if test="${not empty tariff.id}">
        <form:hidden path="id" value="${tariff.id}"/>
    </c:if>
    <input type="submit" value="Save" class="btn btn-success"/>
    </form:form>

    <c:if test="${not empty tariff.id}">
    <form action="deleteTariff" method="post">
        <input type="hidden" name="id" value=${tariff.id}>
        <input type="submit" value="Delete tariff" class="btn btn-danger pull-right">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>
    </c:if>

    <script>
        $("#opt").bootstrapDualListbox();
    </script>

</body>
</html>

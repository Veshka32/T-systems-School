<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Create option</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <script src="<c:url value="/resources/jquery.bootstrap-duallistbox.js"/>"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/bootstrap-duallistbox.css"/>">
<body>

<%@include file="/resources/navbar.jsp" %>

<div class="container">
    <span class="pull-right"><a href="options" class="btn btn-info" role="button">Back to options</a></span>
    <h3>Option details</h3>
    <c:if test="${not empty message}"><p class="bg-danger">${message}</p></c:if>
    <form:form method="POST" modelAttribute="option">

        <div class="form-group row">
            <div class="form-group col-xs-4">
                <label for="name">Name:</label>
                <form:input path="name" value="${option.name}" class="form-control" id="name"/>
                <p class="bg-danger"><form:errors path="name"/></p>
            </div>

            <div class="form-group col-xs-2">
                <label for="price">Price:</label>
                <form:input path="price" value="${option.price}" class="form-control" id="price"/>
                <p class="bg-danger"><form:errors path="price"/></p>
            </div>
            <div class="form-group col-xs-2">
                <label for="cost">Subscribe cost:</label>
                <form:input value="${option.subscribeCost}" path="subscribeCost" class="form-control" id="cost"/>
                <p class="bg-danger"><form:errors path="subscribeCost"/></p>
            </div>
        </div>

        <div class="form-group">
            <label for="desc">Description:</label>
            <form:input value="${option.description}" path="description" class="form-control" id="desc"/>
        </div>
        <br>
        <div class="form-group">
            <label for="mand">Set mandatory options:</label>
            <form:select path="mandatory" multiple="multiple" id="mand">
                <c:forEach items="${all}" var="item">
                    <form:option label="${item.key}" value="${item.value}"/>
                </c:forEach>
            </form:select>
        </div>
        <br>
        <div class="form-group">
            <label for="inc">Set incompatible options:</label>
            <form:select path="incompatible" multiple="multiple" id="inc">
                <c:forEach items="${all}" var="item">
                    <form:option label="${item.key}" value="${item.value}"/>
                </c:forEach>
            </form:select>
        </div>

        <c:if test="${not empty option.id}"><form:hidden path="id" value="${option.id}"/></c:if>
        <input type="submit" value="Save" class="btn btn-success"/>

        <c:if test="${not empty option.id}">
            <form action="deleteOption" method="post">
                <input type="hidden" name="id" value=${option.id}>
                <input type="submit" value="Delete option" class="btn btn-danger pull-right">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
            </form>
        </c:if>
    </form:form>
    <script>
        $("#mand").bootstrapDualListbox();
        $("#inc").bootstrapDualListbox();
    </script>
</div>
</body>
</html>

<%--suppress ALL --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Tariff</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<%@include file="/resources/navbar.jsp" %>

<div class="container">
    <h3>Tariff details</h3>
    <table class="table table-striped">
        <thead>
        <th style="width:20%"></th>
        <th style="width:80%"></th>
        </thead>
        <tbody>
        <tr>
            <td>Id:</td>
            <td>${tariff.id} </td>
        </tr>
        <tr>
            <td>Name:</td>
            <td>${tariff.name} </td>
        </tr>
        <tr>
            <td>Price:</td>
            <td>${tariff.price} </td>
        </tr>
        <tr>
            <td>Description:</td>
            <td>${tariff.description} </td>
        </tr>

        <tr>
            <td>Options:</td>
            <td>${tariff.optionsNames}</td>
        </tr>
        </tbody>
    </table>

    <form action="editTariff" method="get">
        <input type="hidden" name="id" value=${tariff.id}>
        <input type="submit" value="Edit tariff" class="btn btn-warning"></form>
    <br>

    <a href="tariffs" class="btn btn-info" role="button">Back to tariffs</a>


</div>
</body>
</html>

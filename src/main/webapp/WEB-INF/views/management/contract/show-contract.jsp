<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Contract</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<%@ include file="/resources/navbar.html" %>


<div class="container">
    <span class="pull-right"><form action="showClient" method="get">
        <input type="hidden" name="id" value=${contract.ownerId}>
        <input type="submit" value="Back to client" class="btn btn-info"></form></span>
    <h3>Contract details</h3>
    <table class="table table-striped">
        <thead>
        <th style="width:20%"></th>
        <th style="width:80%"></th>
        </thead>
        <tbody>
        <tr>
            <td >Id:</td>
            <td>${contract.id} </td>
        </tr>
        <tr>
            <td>Phone:</td>
            <td>${contract.number} </td>
        </tr>
        <tr>
            <td>Tariff:</td>
            <td>${contract.tariffName} </td>
        </tr>
        <tr>
            <td>Options:</td>
            <td>${contract.optionsNames} </td>
        </tr>
        <tr>
            <td>Is blocked:</td>
            <td>${contract.blocked} </td>
        </tr>
        <tr>
            <td>Is blocked by admin:</td>
            <td>${contract.blockedByAdmin} </td>
        </tr>
        <tr>
            <td>Owner:</td>
            <td>${contract.ownerName}</td>
        </tr>
        </tbody>
    </table>

    <form action="editContract" method="get">
        <input type="hidden" name="id" value=${contract.id}>
        <input type="submit" value="Edit" class="btn btn-warning"></form>
</div>
</body>
</html>

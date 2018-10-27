<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>Tariff</title>
</head>

<body>
Name: ${newTariff.name}<br>
Price: ${newTariff.price}<br>
Archived: ${newTariff.archived}<br>
Description: ${newTariff.description}<br>
Options:${tariffOptions}<br>
<form action="editTariff" method="get">
    <input type="hidden" name="id" value=${newTariff.id}>
    <input type="submit" value="Edit"></form>

<a href="tariffs">Back to options</a>

</body>
</html>

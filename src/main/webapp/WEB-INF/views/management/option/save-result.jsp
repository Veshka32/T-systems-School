<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>Option</title>
</head>

<body>
Name: ${newOption.name}<br>
Price: ${newOption.price}<br>
SubscribeCost: ${newOption.subscribeCost}<br>
Archived: ${newOption.archived}<br>
Description: ${newOption.description}<br>
Incompatible options: ${badOptions}<br>
<form action="editOption" method="get">
    <input type="hidden" name="id" value=${newOption.id}>
    <input type="submit" value="Edit"></form>

<a href="options">Back to options</a>

</body>
</html>

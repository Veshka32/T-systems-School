<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>


<html>
<head>
    <title>New option</title>
</head>

<body>
<h2>Option saved</h2>
Name: "${newOption.name}"<<br>
Price: "${newOption.price}"<<br>
SubscribeCost: "${newOption.subscribeCost}"<<br>
Archived: "${newOption.archived}"<br>
Description: "${newOption.description}"<<br>
IncompatibleOptions: "${badOptions}"<br>
<form action="/management/options" method="get">
    <input value="Back to options" type="submit"/>
</form>

</body>
</html>

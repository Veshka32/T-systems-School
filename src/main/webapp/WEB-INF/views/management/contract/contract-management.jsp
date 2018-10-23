<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Contract</title>
</head>

<body>
<h2>Find contract</h2>
<form action="findContract">
    Phone number:
    <input type="text" name="number">
    <br>
    <input type="submit" value="Find">
</form>


<a href="createContract">Create new contract</a>
</body>
</html>

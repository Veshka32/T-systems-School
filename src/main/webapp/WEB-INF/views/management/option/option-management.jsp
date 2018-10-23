<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Options</title>
</head>

<body>
<h2>All options</h2>
<table>
    <c:forEach items="${allOptions}" var="option">
        <tr>
            <td>${option.toString()}</td>
            <td>
                <form action="editOption" method="get">
                <input type="hidden" name="id" value=${option.id}>
                <input type="submit" value="Edit option"></form>
            </td>
            <td>
            <form action="deleteOption" method="get">
                <input type="hidden" name="id" value=${option.id}>
                <input type="submit" value="Delete option"></form>
            </td>
        </tr>
    </c:forEach>
</table>

<a href="createOption">Create new option</a>
</body>
</html>

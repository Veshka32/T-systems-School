<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Options</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<%@ include file="/resources/navbar.html" %>

<c:url value="/logout" var="logoutUrl"/>
<form id="logout" action="${logoutUrl}" method="post">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
</form>

<div class="container">
    <h3>All options</h3><br>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>id</th>
            <th>Name</th>
            <th>Price</th>
            <th>Subscribe cost</th>
            <th>Description</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${allInPage}" var="option">
            <tr>
                <td>${option.id} </td>
                <td>${option.name}</td>
                <td>${option.price}</td>
                <td>${option.subscribeCost}</td>
                <td>${option.description}</td>
                <td>
                    <form action="editOption" method="get">
                        <input type="hidden" name="id" value=${option.id}>
                        <input type="submit" value="Edit" class="btn btn-warning"></form>
                </td>
                <td>
                    <form action="showOption" method="get">
                        <input type="hidden" name="id" value=${option.id}>
                        <input type="submit" value="Show details" class="btn btn-info"></form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    <ul class="pagination">
        <c:choose>
            <c:when test="${currentPage eq 1}">
                <li class="page-item disabled"><a href="">Previous</a></li>
            </c:when>
            <c:otherwise>
                <li class="page-item"><a href="?page=${currentPage-1}">Previous</a></li>
            </c:otherwise>
        </c:choose>

        <c:forEach begin="${1}" end="${pageTotal}" var="page">
            <c:choose>
                <c:when test="${page eq currentPage}">
                    <li class="page-item active"><a href="?page=${page}">${page}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="?page=${page}">${page}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:choose>
            <c:when test="${currentPage eq pageTotal}">
                <li class="page-item disabled"><a href="">Next</a></li>
            </c:when>
            <c:otherwise>
                <li class="page-item"><a href="?page=${currentPage+1}">Next</a></li>
            </c:otherwise>
        </c:choose>
    </ul>
    <p><a href="createOption" role="button" class="btn btn-success">Create new option</a></p></div>


</body>
</html>

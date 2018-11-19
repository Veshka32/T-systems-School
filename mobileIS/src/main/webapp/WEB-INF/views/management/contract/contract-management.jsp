<%--suppress ALL --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>

<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Contracts</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>

<%@include file="/resources/navbar.jsp" %>

<div class="container">
    <h3>Find contract by phone:</h3>
    <p class="bg-danger">${message}</p>
    <form:form method="POST" action="findContract" modelAttribute="phone" class="form-inline">
        <form:input path="phoneNumber" class="form-control" placeholder="1234567890"/>
    <input class="input-group btn btn-info" type="submit" value="Find"/><br>
    <p class="bg-danger"><form:errors path="phoneNumber"/></p>
    </form:form>
    <br>

    <h3>All contracts</h3>
    <table class="table table-striped">
        <thead>
        <tr>
            <th>id</th>
            <th>Phone</th>
            <th>Tariff</th>
            <th>Owner</th>
            <th>Client block</th>
            <th>Blocked</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${allInPage}" var="contract">
            <tr>
                <td>${contract.id} </td>
                <td>${contract.number}</td>
                <td>${contract.tariff.toString()}</td>
                <td>${contract.owner.toString()}</td>
                <td><c:if test="${contract.blocked}"><span class="label label-danger">Blocked</span></c:if></td>
                <td><c:if test="${contract.blockedByAdmin}"><span class="label label-danger">Blocked</span></c:if></td>
                <td>
                    <form action="editContract" method="get">
                        <input type="hidden" name="id" value=${contract.id}>
                        <input type="submit" value="Edit" class="btn btn-warning"></form>
                </td>
                <td>
                    <form action="showContract" method="get">
                        <input type="hidden" name="id" value=${contract.id} >
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

</body>
</html>

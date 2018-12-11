<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Page not found</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/style.css"/>">
</head>
<body>
<div style="position: absolute; width: 100%">
    <div style="position:relative;">
        <div class="jumbotron text-center">
            <h2>This page does not exist in your version of reality</h2>
            <p><a href="/index" style="color: white">Go to main page</a></p>
        </div>
    </div>
</div>
<div id="particles-js"></div>
<script src="<c:url value="/resources/particles-js/particles.js"/>"></script>
<script src="<c:url value="/resources/particles-js/app.js"/>"></script>
</body>
</html>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Theme Made By www.w3schools.com -->
    <title>Universe mobile</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link href="https://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">
    <link href="https://fonts.googleapis.com/css?family=Lato" rel="stylesheet" type="text/css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link href="resources/style.css" rel="stylesheet" type="text/css">

</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
                <sec:authorize access="hasRole('MANAGER')">
                    <li><a href="management/cabinet">Management</a></li>
                </sec:authorize>
                <sec:authorize access="hasRole('CLIENT')">
                    <li><a href="user/cabinet">Cabinet</a></li>
                </sec:authorize>
                <sec:authorize access="isAnonymous()">
                    <li><a href="login">SIGN IN</a></li>
                </sec:authorize>
                <sec:authorize access="isAnonymous()">
                    <li><a href="register">SIGN UP</a></li>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <li>
                        <form action="logout" method="post">
                            <input type="submit" value="LOG OUT" role="button" class="btn default navbar-btn">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    </li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>

<div class="jumbotron text-center">
    <h1><img src="resources/spacelogo.jpg" class="img-valign" width="150" height="120">Multiverse mobile</h1>
    <p>Feel free to communicate through space and time</p>
</div>

<!-- Container (Services Section) -->
<div id="services" class="container-fluid text-center">
    <h2>We offer</h2>
    <br>
    <div class="row slideanim">
        <div class="col-sm-4">
            <span class="glyphicon glyphicon-map-marker logo-small"></span>
            <h4>Any destination</h4>
            <p>Calls without time-lag to/from all over the existing universe</p>
        </div>
        <div class="col-sm-4">
            <span class="glyphicon glyphicon-lock logo-small"></span>
            <h4>Safety</h4>
            <p>We provide reliable service of time-paradox defence</p>
        </div>
        <div class="col-sm-4">
            <span class="glyphicon glyphicon-time logo-small"></span>
            <h4>No time limitation</h4>
            <p>Call to the past or future with our tachyon-tunnel technology</p>
        </div>
    </div>
</div>

<!-- Options Section -->
<div id="portfolio" class="container-fluid text-center bg-grey">
    <h2>Options</h2>
    <div id="myCarousel" class="carousel slide text-center" data-ride="carousel">
        <%--<!-- Indicators -->--%>
        <%--<ol class="carousel-indicators">--%>
        <%--<li data-target="#myCarousel" data-slide-to="0" class="active"></li>--%>
        <%--<li data-target="#myCarousel" data-slide-to="1"></li>--%>
        <%--<li data-target="#myCarousel" data-slide-to="2"></li>--%>
        <%--</ol>--%>

        <!-- Wrapper for slides -->
        <div class="carousel-inner" role="listbox">
            <c:forEach items="${options}" var="option" varStatus="status">
                <c:choose>
                    <c:when test="${status.first}">
                        <div class="item active">
                            <h4>${option.name}</h4><span>${option.description}</span>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="item">
                            <h4>${option.name}</h4><span>${option.description}</span>
                        </div>
                    </c:otherwise>
                </c:choose>

            </c:forEach>
        </div>

        <!-- Left and right controls -->
        <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
            <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
</div>

<!-- Tariffs Section) -->
<div id="pricing" class="container-fluid">
    <div class="text-center">
        <h2>Tariffs</h2>
    </div>
    <div class="row slideanim">
        <c:forEach items="${tariffs}" var="tariff">
            <div class="col-sm-4">
                <div class="panel panel-default text-center">
                    <div class="panel-heading">
                        <h1>${tariff.name}</h1>
                    </div>
                    <div class="panel-body">
                        <p>${tariff.description}</p>
                    </div>
                    <div class="panel-footer">
                        <h3>$${tariff.price}</h3>
                        <span>per month</span>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<footer class="container-fluid text-center">
    <a href="index" title="To Top">
        <span class="glyphicon glyphicon-chevron-up"></span>
    </a>
    <p>Made by Veshka</p>
</footer>

<script>
    $(document).ready(function () {
        // Add smooth scrolling to all links in navbar + footer link
        $(".navbar a, footer a[href='#myPage']").on('click', function (event) {
            // Make sure this.hash has a value before overriding default behavior
            if (this.hash !== "") {
                // Prevent default anchor click behavior
                event.preventDefault();

                // Store hash
                var hash = this.hash;

                // Using jQuery's animate() method to add smooth page scroll
                // The optional number (900) specifies the number of milliseconds it takes to scroll to the specified area
                $('html, body').animate({
                    scrollTop: $(hash).offset().top
                }, 900, function () {

                    // Add hash (#) to URL when done scrolling (default click behavior)
                    window.location.hash = hash;
                });
            } // End if
        });

        $(window).scroll(function () {
            $(".slideanim").each(function () {
                var pos = $(this).offset().top;

                var winTop = $(window).scrollTop();
                if (pos < winTop + 600) {
                    $(this).addClass("slide");
                }
            });
        });
    })
</script>

</body>
</html>

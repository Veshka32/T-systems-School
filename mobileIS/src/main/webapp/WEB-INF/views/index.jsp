<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <title>Universe mobile</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<c:url value="/resources/style.css"/>">

</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav navbar-right">
                <sec:authorize access="hasRole('MANAGER')">
                    <li><a href="management/cabinet">MANAGEMENT</a></li>
                </sec:authorize>
                <sec:authorize access="hasRole('CLIENT')">
                    <li><a href="user/cabinet">Cabinet</a></li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="user/cabinet">CART
                            <span class="caret"></span></a>
                        <ul class="dropdown-menu">
                            <c:forEach items="${cart.options}" var="opt">
                                <li>${opt.name}: <span class="pull-right">$${opt.subscribeCost}</span></li>
                                <br>
                            </c:forEach>
                            <li class="dropdown-divider"></li>
                            <li>Total: $${cart.totalSum}</li>
                        </ul>
                    </li>

                </sec:authorize>
                <sec:authorize access="isAnonymous()">
                    <li><a href="login">SIGN IN<span class="glyphicon glyphicon-log-in"></span></a></li>
                </sec:authorize>

                <sec:authorize access="isAnonymous()">
                    <li><a href="register">SIGN UP<span class="glyphicon glyphicon-user"></span></a></li>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <li>
                        <a href="javascript:document.getElementById('logout').submit()">LOG OUT <span
                                class="glyphicon glyphicon-log-out"></span></a>
                        <form id="logout" action="logout" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        </form>
                    </li>
                </sec:authorize>
            </ul>
        </div>
    </div>
</nav>

<!-- Jumbotron -->

<div style="position: absolute; width: 100%">
    <div id="wrapper" style="position:relative;">
        <div class="jumbotron text-center">
            <h1>Multiverse mobile</h1>
            <p>Feel free to communicate through space and time</p>
        </div>
    </div>
</div>
<div id="particles-js"></div>
<script src="<c:url value="/resources/particles-js/particles.js"/>"></script>
<script src="<c:url value="/resources/particles-js/app.js"/>"></script>

<!-- Services Section) -->
<div class="container-light">
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
<div class="container-dark">
    <h2>Options</h2>
    <div id="myCarousel" class="carousel slide text-center" data-ride="carousel">
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

<!-- Tariffs Section -->
<div class="container-light">
    <div class="text-center">
        <h2>Tariffs</h2>
    </div>
    <div class="row slideanim">
        <c:forEach items="${tariffs}" var="tariff">
            <div class="col-sm-4">
                <div class="panel">
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

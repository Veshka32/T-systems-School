<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>User cabinet</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootswatch/4.1.3/sketchy/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="<c:url value="/resources/usre-cabinet-scripts.js"/>"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
            integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
    <style>
        .card-body {
            color: black;
            background-color: lightgrey;
        }
    </style>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="collapse navbar-collapse" id="navbarColor02">
        <a class="navbar-left" href="../">
            <img src="<c:url value="/resources/spacelogo.jpg"/>" width="50" height="40" alt="">
        </a>
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active"><a class="nav-link" href="cabinet">Cabinet</a></li>
        </ul>
        <ul class="navbar-nav mr-right">
            <li>
                <a class="nav-link" href="javascript:document.getElementById('logout').submit()"><span
                        class="glyphicon glyphicon-log-out"></span>LOG OUT</a>
                <c:url value="/logout" var="logoutUrl"/>
                <form id="logout" action="${logoutUrl}" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                </form>
            </li>
        </ul>
    </div>
</nav>

<br>
<div class="container">
    <div class="row">
        <!---Tariff info--->
        <div class="col-sm-4">
            <div class="card border-success">
                <div class="card-header">Phone number: ${contract.number}
                    <c:if test="${contract.blocked || contract.blockedByAdmin}">
                        <span class="badge badge-pill badge-danger">Blocked</span>
                    </c:if>
                </div>
                <div class="card-body">
                    <h4 class="card-title" id="tariffInfo" data-tariffId="tariff${contract.tariff.id}">
                        Tariff: ${contract.tariff.name}</h4>
                    <p class="card-text">${contract.tariff.description}</p>
                    <p class="card-text text-secondary">Cost: $${contract.tariff.price} per month</p>
                </div>
                <div class="card-footer">
                    <c:if test="${contract.blocked && !contract.blockedByAdmin}">
                        <a href="unblock" role="button" class="btn btn-success">Unblock number</a>
                    </c:if>

                    <c:if test="${!contract.blocked && !contract.blockedByAdmin}">
                        <a href="block" role="button" class="btn btn-outline-warning">Block number</a>
                    </c:if>
                </div>
            </div>
        </div>
        <!---Cart--->
        <div class="col-sm-4">
            <div class="card border-success bg-dark">
                <div class="card-header">Cart:</div>
                <div class="card-body" id="cart-body">
                    <c:forEach items="${cart.options}" var="opt">
                        <p class="card-text d-flex justify-content-between align-items-center"
                           id="optionInCart${opt.id}">
                                ${opt.name}: $${opt.subscribeCost}
                            <button class="badge-danger badge-pill" onclick="deleteFromCart(${opt.id})"> X</button>
                        </p>
                    </c:forEach>
                </div>
                <div class="card-footer d-flex justify-content-between align-items-center">
                    <span id="cart-sum">Total: $${cart.totalSum}</span>
                    <button id="buy-button" onclick="buy()" class="btn btn-success">Buy</button>
                    <c:if test="${cart.isEmpty() || contract.blockedByAdmin || contract.blocked}">
                        <script>$("#buy-button").attr('disabled', 'disabled');</script>
                    </c:if>
                </div>
            </div>

            <div id="cart-message" class="modal fade" role="dialog">
                <div class="modal-dialog modal-sm">
                    <!-- Modal content-->
                    <div class="modal-content alert alert-dismissible alert-danger">
                        <button type="button" class="close" data-dismiss="modal">&times;</button>
                        <div class="modal-body"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <br>
    <div class="bs-component">
        <ul class="nav nav-tabs">
            <li class="nav-item">
                <a class="nav-link active" data-toggle="tab" href="#active-options">Active options</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" data-toggle="tab" id="tab1" href="#available-options">Available options</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" data-toggle="tab" id="tab2" href="#tariffs">Tariffs</a>
            </li>
        </ul>
        <c:if test="${contract.blockedByAdmin || contract.blocked}">
            <script>
                $("#tab1").addClass('disabled');
                $("#tab2").addClass('disabled');
            </script>
        </c:if>
        <div class="tab-content">
            <div class="tab-pane fade active show" id="active-options">
                <br>
                <div class="card-columns">
                    <c:forEach items="${contract.tariff.options}" var="option">
                        <div class="card border-success" id="tariffOption${option.id}">
                            <div class="card-body">
                                <h4 class="card-title">${option.name}</h4>
                                <p class="card-text"> ${option.description}</p>
                                <p class="text-secondary">$${option.price} per month</p>
                            </div>
                            <div class="card-footer">Can't be deactivated</div>
                        </div>
                    </c:forEach>


                    <c:forEach items="${contract.options}" var="option">
                        <div class="card border-warning" id="option${option.id}">
                            <div class="card-body">
                                <h4 class="card-title">${option.name}</h4>
                                <p class="card-text">${option.description}</p>
                                <p class="text-secondary">$${option.price} per month</p>
                            </div>
                            <div class="card-footer">
                                <c:if test="${!contract.blocked && !contract.blockedByAdmin}">
                                    <form class="delete-option">
                                        <input type="hidden" name="id" value=${option.id}>
                                        <input type="submit" value="Delete" class="btn btn-danger">
                                        <input type="hidden" class="csrf" name="${_csrf.parameterName}"
                                               value="${_csrf.token}"/></form>
                                </c:if>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>

            <div class="tab-pane fade" id="available-options">
                <br>
                <div class="card-columns">
                    <c:forEach items="${availableOptions}" var="option">
                        <div class="card border-warning" id="newOption${option.id}">
                            <div class="card-body">
                                <h4 class="card-title">${option.name}</h4>
                                <p class="card-text">${option.description}</p>
                                <p class="text-secondary">$${option.price} per month</p>
                                <p class="text-secondary">$${option.subscribeCost} for subscribe</p>
                            </div>
                            <div class="card-footer">
                                <button onclick="addToCart(${option.id})" class="btn btn-primary">Get</button>
                            </div>
                            <script>
                                if ($('#optionInCart${option.id}').length > 0 || $('#tariffOption${option.id}').length > 0 || $('#option${option.id}').length > 0) {
                                    $('#newOption${option.id}').find('button').attr('disabled', 'disabled');
                                }
                            </script>
                        </div>
                    </c:forEach>
                </div>
                <
                <div class="text-center">
                    <button id="moreOptions" data-page=2 onclick="getMoreOptions()"
                            class="btn btn-primary btn-xs center-block">Show more &gt;
                    </button>
                </div>
            </div>

            <div class="tab-pane fade" id="tariffs">
                <br>
                <div class="card-columns">
                    <c:forEach items="${allTariffs}" var="tariff">
                        <div class="card border-warning" id="tariff${tariff.id}">
                            <div class="card-body">
                                <h4 class="card-title">${tariff.name}</h4>
                                <p class="card-text">${tariff.description}</p>
                                <p class="text-secondary">$${tariff.price} per month</p>
                            </div>
                            <div class="card-footer">
                                <a href="getTariff/${tariff.id}" role="button" class="btn btn-success">Get
                                    tariff</a>
                            </div>
                        </div>
                        <br>
                    </c:forEach>
                </div>
                <script> filterTariff();</script>
                <div class="text-center">
                    <button id="moreTariffs" data-page=2 onclick="getMoreTariffs()" class="btn btn-primary btn-xs">
                        Show
                        more &gt;
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

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
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootswatch/4.1.3/superhero/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="<c:url value="/resources/usre-cabinet-scripts.js"/>"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
            integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
            crossorigin="anonymous"></script>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="collapse navbar-collapse" id="navbarColor02">
        <a class="navbar-left" href="../">
            <img src="<c:url value="/resources/spacelogo.jpg"/>" width="50" height="40" alt="">
        </a>
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active"><a class="nav-link" href="cabinet">Cabinet</a></li>
            <li class="nav-item"><a class="nav-link" href="showTariffs">Tariffs</a></li>
        </ul>
        <ul class="navbar-nav mr-right">
            <li>
                <a class="nav-link" href="javascript:document.getElementById('logout').submit()"><span
                        class="glyphicon glyphicon-log-out"></span>LOG OUT</a>
                <form id="logout" action="../logout" method="post">
                    <input type="hidden" name="_csrf" value="fcf08ffd-4fc3-41c8-9676-a0423b9dfef4"/>
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
                    <h4 class="card-title">Tariff: ${contract.tariff.name}</h4>
                    <p class="card-text text-success">${contract.tariff.description}</p>
                    <h5 class="card-text font-italic">Cost: $${contract.tariff.price} per month</h5>
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
            <div class="card border-success bg-light">
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
                <div class="card-footer text-left">
                    Total: $<span id="cart-sum">${cart.totalSum}</span>
                    <div class="text-right">
                        <button id="buy-button" onclick="buy()" class="btn btn-success">Buy</button>
                    </div>
                    <c:if test="${cart.isEmpty()}">
                        <script>
                            $("#buy-button").addClass('disabled');
                        </script>
                    </c:if>
                </div>
            </div>

            <div class="modal fade" id="cart-message">
                <div class="modal-dialog">
                    <div class="modal-content alert alert-dismissible alert-danger">
                        <button type="button" class="close" data-dismiss="modal">×</button>

                        <!-- Modal body -->
                        <div class="modal-body">
                        </div>


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
                <a class="nav-link" data-toggle="tab" id="tab1" href="#options">Available options</a>
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
        <div id="myTabContent" class="tab-content">
            <div class="tab-pane fade active show" id="active-options">
                <br>
                <p class="bg-danger"></p>
                <div class="row">
                    <c:forEach items="${contract.tariff.options}" var="option">
                        <div class="col-sm-4">
                            <div class="card border-success">
                                <div class="card-body">
                                    <h4 class="card-title">${option.name}</h4>
                                    <p class="card-text text-success"> ${option.description}</p>
                                    <p>$${option.price} per month</p>
                                </div>
                                <div class="card-footer">Can't be deactivated</div>
                            </div>
                        </div>
                        <br>
                    </c:forEach>

                    <c:forEach items="${contract.options}" var="option">
                        <div class="col-sm-4" id="option${option.id}">
                            <div class="card border-warning">
                                <div class="card-body">
                                    <h4 class="card-title">${option.name}</h4>
                                    <p class="card-text text-warning">${option.description}</p>
                                    <p>$${option.price} per month</p>
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
                        </div>
                        <br>
                    </c:forEach>
                </div>
            </div>

            <div class="tab-pane fade" id="options">
                <br>
                <div id="source-button" class="btn btn-primary btn-xs">More &gt;</div>
                <div class="row">
                    <c:forEach items="${availableOptions}" var="option">
                        <div class="col-sm-4" id="newOption${option.id}">
                            <div class="card border-warning">
                                <div class="card-body">
                                    <h4 class="card-title">${option.name}</h4>
                                    <p class="card-text text-warning">${option.description}</p>
                                    <p>$${option.price} per month</p>
                                    <p>$${option.subscribeCost} for subscribe</p>
                                </div>
                                <div class="card-footer">
                                    <button onclick="addToCart(${option.id})" class="btn btn-primary">Get</button>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>


            <div class="tab-pane fade" id="tariffs">
                <br>
                <p class="bg-danger"></p>
                <div class="row">
                    <c:forEach items="${allTariffs}" var="tariff">
                        <div class="col-sm-4">
                            <div class="card border-warning">
                                <div class="card-body">
                                    <h4 class="card-title">${tariff.name}</h4>
                                    <p class="card-text text-warning">${tariff.description}</p>
                                    <p>$${tariff.price} per month</p>
                                </div>
                                <div class="card-footer">
                                    <a href="getTariff/${tariff.id}" role="button" class="btn btn-success">Get
                                        tariff</a>
                                </div>
                            </div>
                        </div>
                        <br>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>

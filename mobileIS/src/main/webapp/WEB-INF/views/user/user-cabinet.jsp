<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>User cabinet</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <style>
        .logo-small {
            font-size: 15px;
        }

        .panel-body span {
            font-weight: bold;
        }

        .panel-body p {
            font-style: italic;
        }
    </style>
</head>
<body>
<%@ include file="/resources/user-navbar.html" %>

<div class="container">
    <%--Info and cart--%>
    <div class="row">
        <%--Tariff --%>
        <div class="col-sm-4">
            <div class="panel panel-primary">
                <div class="panel-heading">Phone number: ${contract.number}
                    <c:if test="${contract.blocked || contract.blockedByAdmin}">
                        <span class="label label-danger right-pill">Blocked</span>
                    </c:if>
                </div>
                <div class="panel-body">
                    <span>Tariff:</span> ${contract.tariff.name}<br><br>
                    <p>${contract.tariff.description}</p><br>
                    <span>Cost: </span>$${contract.tariff.price} per month
                </div>
                <div class="panel-footer">
                    <c:choose>
                        <c:when test="${contract.blockedByAdmin}"> </c:when>
                        <c:otherwise>
                            <c:choose>
                                <c:when test="${contract.blocked}"><a href="unblock" role="button"
                                                                      class="btn btn-success">Unblock
                                    number</a></c:when>
                                <c:otherwise><a href="block" role="button" class="btn btn-danger">Block number</a>
                                    <a href="showTariffs" role="button" class="btn btn-info">Change tariff</a>
                                </c:otherwise>
                            </c:choose>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </div>
        <%--Cart--%>
        <div class="col-sm-4">
            <div class="panel panel-primary">
                <div class="panel-heading text-left">Cart:<span
                        class="glyphicon glyphicon-shopping-cart pull-right logo-small"></span></div>
                <div class="panel-body text-left">
                    <c:forEach items="${cart.options}" var="opt">
                        ${opt.name}: <span class="pull-right">$${opt.subscribeCost}<a
                            href="deleteFromCart/${opt.id}"><span
                            class="glyphicon glyphicon-remove-circle logo-small"></span></a></span><br>
                    </c:forEach>
                </div>
                <div class="panel-footer text-left">
                    Total: $${cart.totalSum}
                    <c:choose>
                        <c:when test="${cart.isEmpty()}">
                            <div class="text-right">
                                <a href="buy" role="button" class="btn btn-success disabled">Buy</a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="text-right"><a href="buy" role="button" class="btn btn-success">Buy</a></div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <br>
            <p class="bg-danger">${cart.getMessage()}</p>

        </div>
    </div>
    <%--Options--%>
    <h4>Active options:</h4>
    <p class="bg-danger">${message}</p>
    <div class="row">
        <%--Options in tariff--%>
        <c:forEach items="${contract.tariff.options}" var="option">
            <div class="col-sm-4">
                <div class="panel panel-info">
                    <div class="panel-heading">${option.name}</div>
                    <div class="panel-body">
                        <p class="font-italic"> ${option.description}</p>
                        <h5>$${option.price} per month</h5>
                    </div>
                    <div class="panel-footer">Can't be deactivated</div>
                </div>
            </div>
        </c:forEach>
        <%--Options in contract--%>
        <c:forEach items="${contract.options}" var="option">
            <div class="col-sm-4">
                <div class="panel panel-info">
                    <div class="panel-heading">${option.name}</div>
                    <div class="panel-body">
                        <p class="font-italic">${option.description}</p>
                        <h5>$${option.price} per month</h5>
                    </div>
                    <div class="panel-footer">
                        <c:if test="${!contract.blocked && !contract.blockedByAdmin}">
                            <form action="deleteOption" method="post">
                                <input type="hidden" name="id" value=${option.id}>
                                <input type="submit" value="Delete" class="btn btn-danger">
                                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/></form>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
    <%--Available options--%>
    <h4>Available options:</h4>
    <div class="row">
        <c:forEach items="${availableOptions}" var="option">
            <div class="col-sm-4">
                <div class="panel panel-success">
                    <div class="panel-heading">${option.name}</div>
                    <div class="panel-body">
                        <p class="font-italic">${option.description}</p>
                        <h5>$${option.price} per month</h5>
                        <h5>$${option.subscribeCost} for subscribe</h5>
                    </div>
                    <div class="panel-footer">
                        <a href="addOptionToCart/${option.id}" role="button" class="btn btn-info">Get</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
</body>
</html>
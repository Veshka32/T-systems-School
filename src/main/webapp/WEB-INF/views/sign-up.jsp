<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page isELIgnored="false" %>
<html>
<h3>Enter phone number and password</h3>

<c:if test="${not empty message}"><div>${message}</div></c:if>

<form:form  method='POST' modelAttribute="user">
    <table>
        <tr>
            <td>Phone number:</td>
            <form:input type="text" path="login"/>
        </tr>
        <tr>
            <td>Password:</td>
            <form:input type="password" path="password"/>
        </tr>
        <tr>
            <input type="submit" value="Sign up" class="btn btn-success"/>
        </tr>
    </table>

</form:form>
</body>
</html>
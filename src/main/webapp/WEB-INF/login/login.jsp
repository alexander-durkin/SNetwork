<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.login" var="reg"/>
<fmt:message var="title" bundle="${reg}" key="login.title"/>

<jsp:useBean id="validation" class="web.servlet.model.FormValidation" scope="request"/>
<jsp:useBean id="data" type="model.Credentials" scope="request"/>

<tags:loginPage title="${title}">
    <h2>${title}</h2>
    <c:if test="${validation.errors.INVALID_CREDENTIALS}">
        <div class="alert alert-danger">
            <fmt:message bundle="${reg}" key="login.invalid.credentials"/>
        </div>
    </c:if>
    <form class="login" action="login" method="post">
        <div
                <c:choose>
                    <c:when test="${not empty validation.fields.login}">
                        class="form-group has-error"
                    </c:when>
                    <c:otherwise>
                        class="form-group"
                    </c:otherwise>
                </c:choose>
        >
            <label for="name" class="control-label"><fmt:message bundle="${reg}" key="login.username"/></label>
            <input id="name" class="form-control" type="text" name="login" value="${data.login}"/>
            <!-- надо делать через cout -->
            <c:if test="${validation.fields.login.emptyField}">
                <span class="help-block"><fmt:message bundle="${reg}" key="login.empty.username"/></span>
            </c:if>
        </div>
        <div
                <c:choose>
                    <c:when test="${not empty validation.fields.password}">
                        class="form-group has-error"
                    </c:when>
                    <c:otherwise>
                        class="form-group"
                    </c:otherwise>
                </c:choose>
        >
            <label for="pwd" class="control-label"><fmt:message bundle="${reg}" key="login.password"/></label>
            <input id="pwd" class="form-control" type="password" name="password" value="${data.password}"/>
            <!-- лучше пароль сбрасывать -->
            <c:if test="${validation.fields.password.emptyField}">
                <span class="help-block"><fmt:message bundle="${reg}" key="login.empty.password"/></span>
            </c:if>
        </div>
        <button type="submit" class="btn btn-default">
            <fmt:message bundle="${reg}" key="login.log.in"/>
        </button>
        <!--<img src="http://static.lostfilm.tv/Images/276/Posters/image.jpg">-->
    </form>
</tags:loginPage>

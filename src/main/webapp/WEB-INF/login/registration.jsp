<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.reg" var="reg"/>
<fmt:message var="title" bundle="${reg}" key="reg.title"/>

<jsp:useBean id="validation" class="web.servlet.model.FormValidation" scope="request"/>
<jsp:useBean id="data" type="model.Credentials" scope="request"/>

<tags:regPage title="${title}">
    <h2>${title}</h2>
    <c:if test="${validation.errors.USER_ALREADY_EXISTS}">
        <div class="alert alert-danger">
            <fmt:message bundle="${reg}" key="reg.user.exists"/>
        </div>
    </c:if>
    <form class="registration" action="registration" method="post">
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
            <label for="username" class="control-label"><fmt:message bundle="${reg}" key="reg.username"/></label>
            <input id="username" class="form-control" type="text" name="login" value="${data.login}"/>
            <!-- надо делать через cout -->
            <c:if test="${validation.fields.login.emptyField}">
                <span class="help-block"><fmt:message bundle="${reg}" key="reg.empty.username"/></span>
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
            <label for="pwd" class="control-label"><fmt:message bundle="${reg}" key="reg.password"/></label>
            <input id="pwd" class="form-control" type="password" name="password" value="${data.password}"/>
            <!-- лучше пароль сбрасывать -->
            <c:if test="${validation.fields.password.emptyField}">
                <span class="help-block"><fmt:message bundle="${reg}" key="reg.empty.password"/></span>
            </c:if>
        </div>

        <div
                <c:choose>
                    <c:when test="${not empty validation.fields.first_name}">
                        class="form-group has-error"
                    </c:when>
                    <c:otherwise>
                        class="form-group"
                    </c:otherwise>
                </c:choose>
        >
            <label for="first_name" class="control-label"><fmt:message bundle="${reg}" key="reg.first.name"/></label>
            <input id="first_name" class="form-control" type="text" name="first_name" value="${data.first_name}"/>
            <c:if test="${validation.fields.first_name.emptyField}">
                <span class="help-block"><fmt:message bundle="${reg}" key="reg.empty.first.name"/></span>
            </c:if>
        </div>

        <div
                <c:choose>
                    <c:when test="${not empty validation.fields.last_name}">
                        class="form-group has-error"
                    </c:when>
                    <c:otherwise>
                        class="form-group"
                    </c:otherwise>
                </c:choose>
        >
            <label for="last_name" class="control-label"><fmt:message bundle="${reg}" key="reg.last.name"/></label>
            <input id="last_name" class="form-control" type="text" name="last_name" value="${data.last_name}"/>
            <c:if test="${validation.fields.last_name.emptyField}">
                <span class="help-block"><fmt:message bundle="${reg}" key="reg.empty.last.name"/></span>
            </c:if>
        </div>
        <div class="form-group">
            <label for="gender" class="control-label"><fmt:message bundle="${reg}" key="reg.gender"/></label>
            <input id="gender" type="radio" name="gender" value="UNKNOWN" checked><fmt:message bundle="${reg}" key="reg.gender.unknown"/>
            <input id="gender" type="radio" name="gender" value="MALE"><fmt:message bundle="${reg}" key="reg.gender.male"/>
            <input id="gender" type="radio" name="gender" value="FEMALE"><fmt:message bundle="${reg}" key="reg.gender.female"/>
        </div>
        <div
                <c:choose>
                    <c:when test="${not empty validation.fields.birth_date}">
                        class="form-group has-error"
                    </c:when>
                    <c:otherwise>
                        class="form-group"
                    </c:otherwise>
                </c:choose>
        >
            <label for="birth_date" class="control-label"><fmt:message bundle="${reg}" key="reg.birth.date"/></label>
            <input id="birth_date" class="form-control" type="date" name="birth_date" value="${data.birth_date}" max="2003-12-31" min="1900-01-01"/>
            <c:if test="${validation.fields.birth_date.emptyField}">
                <span class="help-block"><fmt:message bundle="${reg}" key="reg.empty.birth.date"/></span>
            </c:if>
        </div>
        <button type="submit" class="btn btn-default">
            <fmt:message bundle="${reg}" key="reg.sign.up"/>
        </button>
    </form>
</tags:regPage>

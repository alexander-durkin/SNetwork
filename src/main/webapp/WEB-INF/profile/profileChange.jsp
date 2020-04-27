<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.reg" var="reg"/>
<fmt:setBundle basename="i18n.profile" var="profile"/>
<fmt:message var="title" bundle="${profile}" key="profile.change"/>

<jsp:useBean id="validation" class="web.servlet.model.FormValidation" scope="request"/>
<jsp:useBean id="data" type="model.Credentials" scope="request"/>
<jsp:useBean id="user" type="model.User" scope="session"/>

<c:set var="userFirstName" value="<%=user.getFirstName()%>"/>
<c:set var="userLastName" value="<%=user.getLastName()%>"/>
<c:set var="userBirthDateTmp" value="<%=user.getBirthDate()%>"/>
<c:set var="userGenderTmp" value="<%=user.getGender()%>"/>
<c:set var="userAddress" value="<%=user.getAddress()%>"/>
<c:set var="userInfo" value="<%=user.getInfo()%>"/>

<c:set var="userName" value="${userFirstName} ${userLastName}"/>
<c:set var="userBirthDate"
       value="${fn:substring(userBirthDateTmp,8,10)}.${fn:substring(userBirthDateTmp,5,7)}.${fn:substring(userBirthDateTmp,0,4)}"/>

<c:if test="${userGenderTmp == 'UNKNOWN'}">
    <fmt:message var="userGender" bundle="${profile}" key="profile.gender.unknown"/>
</c:if>
<c:if test="${userGenderTmp == 'MALE'}">
    <fmt:message var="userGender" bundle="${profile}" key="profile.gender.male"/>
</c:if>
<c:if test="${userGenderTmp == 'FEMALE'}">
    <fmt:message var="userGender" bundle="${profile}" key="profile.gender.female"/>
</c:if>

<tags:page title="${title}">
    <h2>${title}</h2>
    <form class="profileChange" action="profileChange" method="post">
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
            <label for="first_name" class="control-label"><fmt:message bundle="${reg}"
                                                                       key="reg.first.name"/></label>
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
            <input id="gender" type="radio" name="gender" value="UNKNOWN"
            <c:choose>
            <c:when test="${userGenderTmp == 'UNKNOWN'}">
                   checked
            </c:when>
            </c:choose>
            >
            <fmt:message bundle="${reg}" key="reg.gender.unknown"/>

            <input id="gender" type="radio" name="gender" value="MALE"
            <c:choose>
            <c:when test="${userGenderTmp == 'MALE'}">
                   checked
            </c:when>
            </c:choose>
            >
            <fmt:message bundle="${reg}" key="reg.gender.male"/>

            <input id="gender" type="radio" name="gender" value="FEMALE"
            <c:choose>
            <c:when test="${userGenderTmp == 'FEMALE'}">
                   checked
            </c:when>
            </c:choose>
            >
            <fmt:message bundle="${reg}" key="reg.gender.female"/>
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
            <label for="birth_date" class="control-label"><fmt:message bundle="${reg}"
                                                                       key="reg.birth.date"/></label>
            <input id="birth_date" class="form-control" type="date" name="birth_date" value="${data.birth_date}"
                   max="2003-12-31" min="1900-01-01"/>
            <c:if test="${validation.fields.birth_date.emptyField}">
                <span class="help-block"><fmt:message bundle="${reg}" key="reg.empty.birth.date"/></span>
            </c:if>
        </div>

        <div class="form-group">
            <label for="address" class="control-label"><fmt:message bundle="${profile}"
                                                                    key="profile.address"/></label>
            <input id="address" class="form-control" type="text" name="address" value="${data.address}"/>

            <label for="info" class="control-label"><fmt:message bundle="${profile}" key="profile.info"/></label>
            <input id="info" class="form-control" type="text" name="info" value="${data.info}"/>
        </div>
        <button type="submit" class="btn btn-default">
            <fmt:message bundle="${profile}" key="profile.save.changes"/>
        </button>
    </form>
</tags:page>
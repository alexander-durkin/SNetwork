<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.profile" var="profile"/>
<fmt:message var="title" bundle="${profile}" key="profile.title"/>

<jsp:useBean id="user" type="model.User" scope="session"/>
<jsp:useBean id="userProfile" type="model.User" scope="request"/>
<jsp:useBean id="userAction" type="java.lang.String" scope="request"/>

<c:set var="id" value="<%=user.getId()%>"/>
<c:set var="userId" value="<%=userProfile.getId()%>"/>
<c:set var="userFirstName" value="<%=userProfile.getFirstName()%>"/>
<c:set var="userLastName" value="<%=userProfile.getLastName()%>"/>
<c:set var="userBirthDateTmp" value="<%=userProfile.getBirthDate()%>"/>
<c:set var="userGenderTmp" value="<%=userProfile.getGender()%>"/>
<c:set var="userAddress" value="<%=userProfile.getAddress()%>"/>
<c:set var="userInfo" value="<%=userProfile.getInfo()%>"/>

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

<tags:page title="${userName}">
    <form class="profile" action="profile" method="post">
        <h2>${title}</h2>
        <div class="panel panel-default">
            <div class="panel-heading">${userName}</div>
            <div class="panel-body">
                <p><fmt:message bundle="${profile}" key="profile.birth.date"/>: ${userBirthDate}</p>
                <p><fmt:message bundle="${profile}" key="profile.gender"/>: ${userGender}</p>
                <c:if test="${userAddress != null && userAddress != ''}">
                    <p><fmt:message bundle="${profile}" key="profile.address"/>: ${userAddress}</p>
                </c:if>
                <c:if test="${userInfo != null && userInfo != ''}">
                    <p><fmt:message bundle="${profile}" key="profile.info"/>: ${userInfo}</p>
                </c:if>

                <c:if test="${userAction == 'ADD'}">
                    <c:set var="addButton" value="ADD_${userId}"/>
                    <button type="submit" class="btn btn-default" name="action" value="${addButton}">
                        <fmt:message bundle="${profile}" key="profile.add"/>
                    </button>
                </c:if>

                <c:if test="${userAction == 'ACC'}">
                    <c:set var="acceptButton" value="ACC_${userId}"/>
                    <button type="submit" class="btn btn-default" name="action" value="${acceptButton}">
                        <fmt:message bundle="${profile}" key="profile.accept"/>
                    </button>
                    <c:set var="denyButton" value="DEN_${userId}"/>
                    <button type="submit" class="btn btn-default" name="action" value="${denyButton}">
                        <fmt:message bundle="${profile}" key="profile.deny"/>
                    </button>
                </c:if>

                <c:if test="${userAction == 'ACO'}">
                    <c:set var="acceptButton" value="ACC_${userId}"/>
                    <button type="submit" class="btn btn-default" name="action" value="${acceptButton}">
                        <fmt:message bundle="${profile}" key="profile.accept"/>
                    </button>
                </c:if>

                <c:if test="${userAction == 'DEL'}">
                    <c:set var="deleteButton" value="DEL_${userId}"/>
                    <button type="submit" class="btn btn-default" name="action" value="${deleteButton}">
                        <fmt:message bundle="${profile}" key="profile.delete"/>
                    </button>
                </c:if>

                <c:if test="${userAction == 'WAIT'}">
                    <p><fmt:message bundle="${profile}" key="profile.wait"/></p>
                    <c:set var="deleteRequestButton" value="DER_${userId}"/>
                    <button type="submit" class="btn btn-default" name="action" value="${deleteRequestButton}">
                        <fmt:message bundle="${profile}" key="profile.delete.request"/>
                    </button>
                </c:if>

                <c:if test="${userId != id}">
                    <c:set var="messageButton" value="MES_${userId}"/>
                    <button type="submit" class="btn btn-default" name="action" value="${messageButton}">
                        <fmt:message bundle="${profile}" key="profile.write.message"/>
                    </button>
                </c:if>

            </div>
        </div>
    </form>
</tags:page>
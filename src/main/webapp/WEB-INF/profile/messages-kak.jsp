<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.messages" var="messages"/>
<fmt:message var="title" bundle="${messages}" key="messages.title"/>

<jsp:useBean id="user" type="model.User" scope="session"/>
<jsp:useBean id="userProfile" type="model.User" scope="request"/>
<jsp:useBean id="chatList" type="java.util.List" scope="request"/>

<c:set var="userId" value="<%=user.getId()%>"/>
<c:set var="userFirstName" value="<%=user.getFirstName()%>"/>
<c:set var="userLastName" value="<%=user.getLastName()%>"/>

<c:set var="userProfileId" value="<%=userProfile.getId()%>"/>
<c:set var="userProfileFirstName" value="<%=userProfile.getFirstName()%>"/>
<c:set var="userProfileLastName" value="<%=userProfile.getLastName()%>"/>

<c:set var="chatListIsEmpty" value="<%=chatList.isEmpty()%>"/>

<tags:page title="${title}">
    <h2>${title}</h2>
    <c:if test="${chatListIsEmpty == true}">
        <fmt:message bundle="${messages}" key="messages.chats.empty"/>
    </c:if>
    <div class="list-group col-sm-4">
        <c:forEach items="${chatList}" var="item">
            <c:url var="messagesUrl" value="/messages">
                <c:param name="id" value="${item.user.id}"/>
            </c:url>
            <c:set var="userName" value="${item.user.firstName} ${item.user.lastName}"/>
            <a href="${messagesUrl}" class="list-group-item">${userName}
                <c:if test="${item.newMessages > 0}">
                    <span class="badge">${item.newMessages}</span>
                </c:if>
            </a>
        </c:forEach>
    </div>
    <c:if test="${userProfileId != userId}">
        <div class="well-sm">
            <c:url var="messagesUrl" value="/messages"/>
            <a href="${messagesUrl}"><fmt:message bundle="${messages}" key="messages.back"/></a>
        </div>
        <div class="well-sm">
            <c:url var="profileUrl" value="/profile">
                <c:param name="id" value="${userProfileId}"/>
            </c:url>
            <fmt:message bundle="${messages}" key="messages.companion"/>:
            <a href="${profileUrl}">${userProfileFirstName} ${userProfileLastName}</a>
        </div>
        <c:forEach items="${messageList}" var="item">
            <div
                    <c:choose>
                        <c:when test="${item.receiverId == userId}">
                            class="text text-left col-sm-offset-4 col-sm-4"
                        </c:when>
                        <c:otherwise>
                            class="text text-right col-sm-offset-8 col-sm-4"
                        </c:otherwise>
                    </c:choose>
            >
                <c:set var="sendingTime"
                       value="${fn:substring(item.sendingTime,8,10)}.${fn:substring(item.sendingTime,5,7)}.${fn:substring(item.sendingTime,0,4)}
                           ${fn:substring(item.sendingTime,11,19)}"/>
                <p class="text">${item.text}</p>
                <p class="text small">${sendingTime}</p>
            </div>
        </c:forEach>
        <form class="messages" action="messages" method="post">
            <div class="input-group col-sm-offset-4 col-sm-8">
                <fmt:message bundle="${messages}" key="messages.enter" var="enter"/>
                <input id="text" class="form-control" type="text" name="messageText"
                       placeholder="${enter}">
                <span class="input-group-btn">
                    <button type="submit" class="btn btn-default" name="userId" value="${userProfileId}">
                        <fmt:message bundle="${messages}" key="messages.send"/>
                    </button>
                </span>
            </div>
        </form>
    </c:if>
</tags:page>
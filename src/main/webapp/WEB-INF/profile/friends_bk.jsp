<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.friends" var="friends"/>
<fmt:message var="title" bundle="${friends}" key="friends.title"/>

<tags:page title="${title}">
    <form class="friends" action="friends" method="post">

        <div class="col-sm-6">
            <h2>${title}</h2>
            <c:forEach items="${friendList}" var="item">
                <div class="well-sm">
                    <c:url var="profileUrl" value="/profile">
                        <c:param name="id" value="${item.id}"/>
                    </c:url>
                    <c:set var="userName" value="${item.firstName} ${item.lastName}"/>
                    <a href="${profileUrl}">${userName}</a>
                    <c:set var="deleteButton" value="delete_${item.id}"/>
                    <button type="submit" class="btn btn-default" name="delete" value="${deleteButton}">
                        <fmt:message bundle="${friends}" key="friends.delete"/>
                    </button>
                </div>
            </c:forEach>
        </div>

        <div class="col-sm-6">
            <h2><fmt:message bundle="${friends}" key="friends.requests.in"/></h2>
            <c:forEach items="${requestList}" var="item">
                <div class="well-sm">
                    <c:url var="profileUrl" value="/profile">
                        <c:param name="id" value="${item.id}"/>
                    </c:url>
                    <c:set var="userName" value="${item.firstName} ${item.lastName}"/>
                    <a href="${profileUrl}">${userName}</a>
                    <c:set var="acceptButton" value="acc_${item.id}"/>
                    <button type="submit" class="btn btn-default" name="accept" value="${acceptButton}">
                        <fmt:message bundle="${friends}" key="friends.accept"/>
                    </button>
                    <c:set var="denyButton" value="den_${item.id}"/>
                    <button type="submit" class="btn btn-default" name="deny" value="${denyButton}">
                        <fmt:message bundle="${friends}" key="friends.deny"/>
                    </button>
                </div>
            </c:forEach>

            <h2><fmt:message bundle="${friends}" key="friends.requests.out"/></h2>
            <c:forEach items="${requestOutList}" var="item">
                <div class="well-sm">
                    <c:url var="profileUrl" value="/profile">
                        <c:param name="id" value="${item.id}"/>
                    </c:url>
                    <c:set var="userName" value="${item.firstName} ${item.lastName}"/>
                    <a href="${profileUrl}">${userName}</a>
                    <c:set var="deleteButton" value="del_${item.id}"/>
                    <button type="submit" class="btn btn-default" name="deleteRequest" value="${denyButton}">
                        <fmt:message bundle="${friends}" key="friends.delete.request"/>
                    </button>
                </div>
            </c:forEach>
        </div>

    </form>
</tags:page>
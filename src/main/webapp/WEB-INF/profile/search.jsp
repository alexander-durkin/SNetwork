<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.search" var="search"/>
<fmt:message var="title" bundle="${search}" key="search.title"/>

<jsp:useBean id="user" type="model.User" scope="session"/>
<jsp:useBean id="data" type="model.Credentials" scope="request"/>
<jsp:useBean id="searchList" type="java.util.List" scope="request"/>
<jsp:useBean id="currentPage" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="pages" type="java.lang.Integer" scope="request"/>

<c:set var="id" value="<%=user.getId()%>"/>

<tags:page title="${title}">
    <h2>${title}</h2>
    <form class="form-inline" action="search" method="get">
        <div class="form-group">
            <fmt:message bundle="${search}" key="search.username" var="byUsername"/>
            <input id="username" class="form-control" type="text" name="login" value="${data.login}"
                   placeholder="${byUsername}"/>

            <fmt:message bundle="${search}" key="search.first.name" var="byFirstame"/>
            <input id="firstname" class="form-control" type="text" name="first_name" value="${data.first_name}"
                   placeholder="${byFirstame}"/>

            <fmt:message bundle="${search}" key="search.last.name" var="byLastname"/>
            <input id="lastname" class="form-control" type="text" name="last_name" value="${data.last_name}"
                   placeholder="${byLastname}"/>
        </div>
        <button type="submit" class="btn btn-default">
            <fmt:message bundle="${search}" key="search.find"/>
        </button>
    </form>

    <h2><fmt:message bundle="${search}" key="search.results"/></h2>
    <c:if test="${pages == 0}">
        <fmt:message bundle="${search}" key="search.nothing"/>
    </c:if>

    <c:if test="${pages > 1}">
        <nav aria-label="Page Navigation">
            <ul class="pager">
                <c:url var="firstPage" value="">
                    <c:param name="login" value="${data.login}"/>
                    <c:param name="first_name" value="${data.first_name}"/>
                    <c:param name="last_name" value="${data.last_name}"/>
                    <c:param name="page" value="1"/>
                </c:url>
                <li class="previous"><a href="${firstPage}"><span aria-hidden="true">&laquo;</span></a></li>

                <c:if test="${pages > 2}">
                    <c:url var="prevPage" value="">
                        <c:param name="login" value="${data.login}"/>
                        <c:param name="first_name" value="${data.first_name}"/>
                        <c:param name="last_name" value="${data.last_name}"/>
                        <c:if test="${currentPage == 1}">
                            <c:param name="page" value="1"/>
                        </c:if>
                        <c:if test="${currentPage > 1}">
                            <c:param name="page" value="${currentPage - 1}"/>
                        </c:if>
                    </c:url>
                    <li class="previous"><a href="${prevPage}"><span aria-hidden="true">&lsaquo;</span></a></li>

                    <c:url var="nextPage" value="">
                        <c:param name="login" value="${data.login}"/>
                        <c:param name="first_name" value="${data.first_name}"/>
                        <c:param name="last_name" value="${data.last_name}"/>
                        <c:if test="${currentPage == pages}">
                            <c:param name="page" value="${pages}"/>
                        </c:if>
                        <c:if test="${currentPage < pages}">
                            <c:param name="page" value="${currentPage + 1}"/>
                        </c:if>
                    </c:url>
                    <li class="previous"><a href="${nextPage}"><span aria-hidden="true">&rsaquo;</span></a></li>
                </c:if>

                <c:url var="lastPage" value="">
                    <c:param name="login" value="${data.login}"/>
                    <c:param name="first_name" value="${data.first_name}"/>
                    <c:param name="last_name" value="${data.last_name}"/>
                    <c:param name="page" value="${pages}"/>
                </c:url>
                <li class="previous"><a href="${lastPage}"><span aria-hidden="true">&raquo;</span></a></li>
            </ul>
        </nav>
    </c:if>

    <c:forEach items="${searchList}" var="item">
        <div class="well-sm">
            <c:url var="profileUrl" value="/profile">
                <c:if test="${item.id != id}">
                    <c:param name="id" value="${item.id}"/>
                </c:if>
            </c:url>
            <c:set var="userName" value="${item.firstName} ${item.lastName}"/>
            <a href="${profileUrl}">${userName}</a>
        </div>
    </c:forEach>
</tags:page>
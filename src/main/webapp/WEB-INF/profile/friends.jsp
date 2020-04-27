<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.friends" var="friends"/>
<fmt:setBundle basename="i18n.search" var="search"/>
<fmt:message var="title" bundle="${friends}" key="friends.title"/>

<jsp:useBean id="list" type="java.util.List" scope="request"/>
<jsp:useBean id="data" type="model.Credentials" scope="request"/>
<jsp:useBean id="currentPage" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="pages" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="all_num" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="in_num" type="java.lang.Integer" scope="request"/>
<jsp:useBean id="out_num" type="java.lang.Integer" scope="request"/>

<c:set var="section" value="${param.section}"/>
<c:if test="${section == null}">
    <c:set var="section" value="all"/>
</c:if>

<tags:page title="${title}">

    <ul class="nav nav-pills nav-justified">
        <li role="presentation"
                <c:choose>
                    <c:when test="${section == 'all'}">
                        class="active"
                    </c:when>
                </c:choose>
        ><a href="?section=all">${title}
            <span class="badge">${all_num}</span>
        </a></li>

        <li role="presentation"
                <c:choose>
                    <c:when test="${section == 'in'}">
                        class="active"
                    </c:when>
                </c:choose>
        ><a href="?section=in"><fmt:message bundle="${friends}" key="friends.requests.in"/>
            <span class="badge">${in_num}</span>
        </a></li>

        <li role="presentation"
                <c:choose>
                    <c:when test="${section == 'out'}">
                        class="active"
                    </c:when>
                </c:choose>
        ><a href="?section=out"><fmt:message bundle="${friends}" key="friends.requests.out"/>
            <span class="badge">${out_num}</span>
        </a></li>
    </ul>

    <h2></h2>

    <form class="form-inline" action="friends" method="get">
        <div class="form-group">
            <input id="section" class="hidden" type="hidden" name="section" value="${section}">

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

    <form class="friends" action="friends" method="post">
        <input id="section" class="hidden" type="hidden" name="section" value="${section}">
        <input id="username" class="hidden" type="hidden" name="login" value="${data.login}">
        <input id="firstname" class="hidden" type="hidden" name="first_name" value="${data.first_name}">
        <input id="lastname" class="hidden" type="hidden" name="last_name" value="${data.last_name}">
        <input id="page" class="hidden" type="hidden" name="page" value="${currentPage}">

        <c:if test="${section == 'all'}">
            <c:if test="${pages == 0}">
                <fmt:message bundle="${friends}" key="friends.nothing"/>
            </c:if>
            <c:forEach items="${list}" var="item">
                <div class="well-sm">
                    <c:url var="profileUrl" value="/profile">
                        <c:param name="id" value="${item.id}"/>
                    </c:url>
                    <c:set var="userName" value="${item.firstName} ${item.lastName}"/>
                    <a href="${profileUrl}">${userName}</a>
                    <button type="submit" class="btn btn-default" name="delete" value="${item.id}">
                        <fmt:message bundle="${friends}" key="friends.delete"/>
                    </button>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${section == 'in'}">
            <!--<h2><fmt:message bundle="${friends}" key="friends.requests.in"/></h2>-->
            <c:if test="${pages == 0}">
                <fmt:message bundle="${friends}" key="friends.requests.in.nothing"/>
            </c:if>
            <c:forEach items="${list}" var="item">
                <div class="well-sm">
                    <c:url var="profileUrl" value="/profile">
                        <c:param name="id" value="${item.id}"/>
                    </c:url>
                    <c:set var="userName" value="${item.firstName} ${item.lastName}"/>
                    <a href="${profileUrl}">${userName}</a>
                    <button type="submit" class="btn btn-default" name="accept" value="${item.id}">
                        <fmt:message bundle="${friends}" key="friends.accept"/>
                    </button>
                    <button type="submit" class="btn btn-default" name="deny" value="${item.id}">
                        <fmt:message bundle="${friends}" key="friends.deny"/>
                    </button>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${section == 'out'}">
            <!--<h2><fmt:message bundle="${friends}" key="friends.requests.out"/></h2>-->
            <c:if test="${pages == 0}">
                <fmt:message bundle="${friends}" key="friends.requests.out.nothing"/>
            </c:if>
            <c:forEach items="${list}" var="item">
                <div class="well-sm">
                    <c:url var="profileUrl" value="/profile">
                        <c:param name="id" value="${item.id}"/>
                    </c:url>
                    <c:set var="userName" value="${item.firstName} ${item.lastName}"/>
                    <a href="${profileUrl}">${userName}</a>
                    <button type="submit" class="btn btn-default" name="deleteRequest" value="${item.id}">
                        <fmt:message bundle="${friends}" key="friends.delete.request"/>
                    </button>
                </div>
            </c:forEach>
        </c:if>

        <c:if test="${pages > 1}">
            <nav aria-label="Page Navigation">
                <ul class="pager">
                    <c:url var="firstPage" value="">
                        <c:param name="section" value="${section}"/>
                        <c:param name="login" value="${data.login}"/>
                        <c:param name="first_name" value="${data.first_name}"/>
                        <c:param name="last_name" value="${data.last_name}"/>
                        <c:param name="page" value="1"/>
                    </c:url>
                    <li class="previous"><a href="${firstPage}"><span aria-hidden="true">&laquo;</span></a></li>

                    <c:if test="${pages > 2}">
                        <c:url var="prevPage" value="">
                            <c:param name="section" value="${section}"/>
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
                            <c:param name="section" value="${section}"/>
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
                        <c:param name="section" value="${section}"/>
                        <c:param name="login" value="${data.login}"/>
                        <c:param name="first_name" value="${data.first_name}"/>
                        <c:param name="last_name" value="${data.last_name}"/>
                        <c:param name="page" value="${pages}"/>
                    </c:url>
                    <li class="previous"><a href="${lastPage}"><span aria-hidden="true">&raquo;</span></a></li>
                </ul>
            </nav>
        </c:if>
    </form>
</tags:page>

<%@ attribute name="title" required="true" rtexprvalue="true" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="i18n.menu" var="menu"/>

<jsp:useBean id="user" type="model.User" scope="session"/>
<jsp:useBean id="newMessages" type="java.lang.Integer" scope="session"/>
<jsp:useBean id="newRequests" type="java.lang.Integer" scope="session"/>

<html>
<head>
    <c:url var="bsMain" value="/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${bsMain}"/>
    <c:url var="bsTheme" value="/webjars/bootstrap/3.3.7-1/css/bootstrap-theme.min.css"/>
    <link rel="stylesheet" href="${bsTheme}"/>
    <c:url var="flagCss" value="/webjars/flag-icon-css/2.4.0/css/flag-icon.min.css"/>
    <link rel="stylesheet" href="${flagCss}"/>
    <c:url var="bsJs" value="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js"/>
    <script src="${bsJs}"></script>
    <title>${title}</title>
</head>
<body>

<nav class="navbar navbar-default">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse"
                    data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                <span class="sr-only"><fmt:message bundle="${menu}" key="menu.toggle.navigation"/></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#"><fmt:message bundle="${menu}" key="menu.social.network"/></a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <c:url var="profileUrl" value="/profile">
                    <!-- c:param name="id" value="<--%=user.getId()%>"/-->
                </c:url>
                <c:set var="userFirstName" value="<%=user.getFirstName()%>"/>
                <c:set var="userLastName" value="<%=user.getLastName()%>"/>
                <c:set var="userName" value="${userFirstName} ${userLastName}"/>
                <li><a href="${profileUrl}">${userName}</a></li>

                <c:url var="friendsUrl" value="/friends"/>
                <li><a href="${friendsUrl}">
                    <fmt:message bundle="${menu}" key="menu.friends"/>
                    <c:if test="${newRequests > 0}">
                        <span class="badge">${newRequests}</span>
                    </c:if>
                </a></li>

                <c:url var="messagesUrl" value="/messages"/>
                <li><a href="${messagesUrl}">
                    <fmt:message bundle="${menu}" key="menu.messages"/>
                    <c:if test="${newMessages > 0}">
                        <span class="badge">${newMessages}</span>
                    </c:if>
                </a></li>

                <c:url var="searchUrl" value="/search"/>
                <li><a href="${searchUrl}"><fmt:message bundle="${menu}" key="menu.search"/></a></li>
            </ul>

            <c:set var="currentUrl"
                   value="${requestScope['javax.servlet.forward.request_uri']}?${pageContext.request.queryString}"/>
            <c:url var="localeUrl" value="/locale"/>
            <form class="navbar-form navbar-right" action="${localeUrl}" method="post">
                <input type="hidden" name="redirect_to" value="${currentUrl}"/>
                <input type="hidden" name="locale" value="en"/>
                <div class="form-group">
                    <fmt:message var="enTitle" bundle="${menu}" key="locale.select.en"/>
                    <button type="submit" class="btn flag-icon flag-icon-gb" title="${enTitle}"></button>
                </div>
            </form>
            <form class="navbar-form navbar-right" action="${localeUrl}" method="post">
                <input type="hidden" name="redirect_to" value="${currentUrl}"/>
                <input type="hidden" name="locale" value="ru"/>
                <div class="form-group">
                    <fmt:message var="ruTitle" bundle="${menu}" key="locale.select.ru"/>
                    <button type="submit" class="btn flag-icon flag-icon-ru" title="${ruTitle}"></button>
                </div>
            </form>

            <ul class="nav navbar-nav navbar-right">
                <c:url var="profileChangeUrl" value="/profileChange"/>
                <li><a href="${profileChangeUrl}"><fmt:message bundle="${menu}" key="menu.change.profile"/></a></li>
                <c:url var="logOutUrl" value="/logout"/>
                <li><a href="${logOutUrl}"><fmt:message bundle="${menu}" key="menu.logout"/></a></li>
            </ul>

        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav>

<div class="container">
    <jsp:doBody/>
</div>
</body>
</html>
package web.filter;

import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class LoggedInFilter implements Filter {

    private FilterConfig config = null;

    private String sessionAttributeName = null;
    private String SESSION_ATTR_DEFAULT = "default_session_attribute";

    private String[] allowedUnauth = null;
    private String[] forbiddenUnauth = null;
    private String redirectUnauth = null;

    private String[] allowedAuth = null;
    private String[] forbiddenAuth = null;
    private String redirectAuth = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        config = filterConfig;

        sessionAttributeName = config.getInitParameter("sessionAttributeName");
        if (sessionAttributeName == null) {
            sessionAttributeName = SESSION_ATTR_DEFAULT;
        }

        String allowedUnauthSplit = config.getInitParameter("allowedUnauth");
        if (allowedUnauthSplit != null) {
            allowedUnauth = allowedUnauthSplit.split(",");
        }
        String forbiddenUnauthSplit = config.getInitParameter("forbiddenUnauth");
        if (forbiddenUnauthSplit != null) {
            forbiddenUnauth = forbiddenUnauthSplit.split(",");
        }
        redirectUnauth = config.getInitParameter("redirectUnauth");

        String allowedAuthSplit = config.getInitParameter("allowedAuth");
        if (allowedAuthSplit != null) {
            allowedAuth = allowedAuthSplit.split(",");
        }
        String forbiddenAuthSplit = config.getInitParameter("forbiddenAuth");
        if (forbiddenAuthSplit != null) {
            forbiddenAuth = forbiddenAuthSplit.split(",");
        }
        redirectAuth = config.getInitParameter("redirectAuth");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) request;
        final HttpServletResponse resp = (HttpServletResponse) response;
        final HttpSession session = req.getSession(false);
        final String path = req.getRequestURI();

        boolean isLoggedIn = session != null && session.getAttribute(sessionAttributeName) != null;
        final String redirectUnauthPage = req.getContextPath() + redirectUnauth;
        final String redirectAuthPage = req.getContextPath() + redirectAuth;

        if (!isLoggedIn && redirect(path, allowedUnauth, forbiddenUnauth)) {
            resp.sendRedirect(redirectUnauthPage);
            return;
        }

        if (isLoggedIn && redirect(path, allowedAuth, forbiddenAuth)) {
            resp.sendRedirect(redirectAuthPage);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private boolean redirect(String path, String[] allowed, String[] forbidden) {
        boolean onAllowedPage = false;

        if (allowed != null) {
            for (String s : allowed) {
                if (path.endsWith(s)) {
                    onAllowedPage = true;
                }
            }
            if (!onAllowedPage) {
                return true;
            }
        }

        if (forbidden != null) {
            for (String s : forbidden) {
                if (path.endsWith(s)) {
                    return true;
                }
            }
        }

        return false;
    }
}

package web.filter;

import web.filter.wrapper.RequestWrapper;

import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Singleton
public class RequestEscapingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest req = new RequestWrapper ((HttpServletRequest) request);
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {

    }
}

package web.filter;

import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ResponseAddHeadersFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletResponse resp = (HttpServletResponse) response;

        resp.addHeader("X-Frame-Options", "DENY");
        resp.addHeader("X-Content-Type-Options", "nosniff");
        resp.addHeader("X-XSS-Protection", "1; mode=block");
        resp.addHeader("Content-Security-Policy", "default-src 'self'");

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

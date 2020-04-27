package web.filter.wrapper;

import web.filter.escaping.Escaper;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class RequestWrapper extends HttpServletRequestWrapper {
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request
     * @throws IllegalArgumentException if the request is null
     */
    public RequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getRequestURI() {
        String value = super.getRequestURI();
        if (value == null) {
            return null;
        }
        return Escaper.escapeHeader(value);
    }

    @Override
    public StringBuffer getRequestURL() {
        StringBuffer value = super.getRequestURL();
        if (value == null) {
            return null;
        }
        return Escaper.escape(value);
    }

    @Override
    public String[] getParameterValues(String parameter) {

        String[] values = super.getParameterValues(parameter);
        if (values == null)  {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = Escaper.escape(values[i]);
        }
        return encodedValues;
    }

    @Override
    public Cookie[] getCookies() {
        Cookie[] cookies = super.getCookies();
        if (cookies == null)  {
            return null;
        }
        int count = cookies.length;
        String encodedValue = null;
        for (int i = 0; i < count; i++) {
            encodedValue = Escaper.escape(cookies[i].getValue());
            cookies[i].setValue(encodedValue);
        }
        return cookies;
    }

    @Override
    public String getQueryString() {
        String value = super.getQueryString();
        if (value == null) {
            return null;
        }
        return Escaper.escapeQuery(value);
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        if (parameter.equals("redirect_to")) {
            return Escaper.escapeHeader(value);
        }
        return Escaper.escape(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null) {
            return null;
        }
        return Escaper.escapeHeader(value);
    }
}

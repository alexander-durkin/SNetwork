package web.servlet;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class LocaleServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession(true);
        session.setAttribute("locale", req.getParameter("locale"));

        final String redirectTo = req.getParameter("redirect_to");

        if (redirectTo != null && !redirectTo.isEmpty()) {
            resp.sendRedirect(redirectTo);
        } else {
            resp.sendRedirect(req.getContextPath());
        }
    }
}

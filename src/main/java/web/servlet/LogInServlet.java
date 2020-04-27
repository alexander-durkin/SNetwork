package web.servlet;

import model.Credentials;
import model.User;
import service.FriendService;
import service.MessageService;
import service.UserService;
import web.servlet.model.FieldValidation;
import web.servlet.model.FormValidation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

import static constants.Constants.LOGIN;
import static constants.Constants.PASSWORD;

@Singleton
public class LogInServlet extends HttpServlet {

    private final UserService userService;
    private final FriendService friendService;
    private final MessageService messageService;

    @Inject
    public LogInServlet(UserService userService, FriendService friendService, MessageService messageService) {
        this.userService = userService;
        this.friendService = friendService;
        this.messageService = messageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("data", Credentials.builder().build());
        req.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Credentials credentials = Credentials.builder()
                .login(req.getParameter(LOGIN))
                .password(req.getParameter(PASSWORD))
                .build();

        final FormValidation validation = validate(credentials);

        if (validation.isValid()) {
            final Optional<User> user = userService.getByCredentials(credentials);

            if (!user.isPresent()) {
                validation.getErrors().put("INVALID_CREDENTIALS", true);
            } else {
                final HttpSession session = req.getSession(true);
                session.setAttribute("user", user.get());
                session.setAttribute("newRequests", friendService.inRequests(user.get().getId()));
                session.setAttribute("newMessages", messageService.newMessages(user.get().getId()));
            }
        }

        if (!validation.isValid()) {
            req.setAttribute("validation", validation);
            req.setAttribute("data", credentials);
            req.getRequestDispatcher("/WEB-INF/login/login.jsp").forward(req, resp);
        }

        final String profilePage = req.getContextPath() + "/profile";
        resp.sendRedirect(profilePage);
    }

    static FormValidation validate(Credentials credentials) {
        final FormValidation validation = new FormValidation();

        if (credentials.getLogin() == null || credentials.getLogin().isEmpty()) {
            validation.getFields().put(LOGIN,
                    FieldValidation.builder().isEmptyField(true).build());
        }

        if (credentials.getPassword() == null || credentials.getPassword().isEmpty()) {
            validation.getFields().put(PASSWORD,
                    FieldValidation.builder().isEmptyField(true).build());
        }

        return validation;
    }
}


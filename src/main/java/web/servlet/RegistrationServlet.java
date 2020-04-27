package web.servlet;

import model.Credentials;
import model.Gender;
import model.User;
import service.SecurityService;
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
import java.sql.Timestamp;
import java.util.Optional;

import static constants.Constants.*;

@Singleton
public class RegistrationServlet extends HttpServlet {

    private final UserService userService;
    private final SecurityService securityService;

    @Inject
    public RegistrationServlet(UserService userService, SecurityService securityService) {
        this.userService = userService;
        this.securityService = securityService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("data", Credentials.builder().build());
        req.getRequestDispatcher("/WEB-INF/login/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        final Credentials credentials = Credentials.builder()
                .login(req.getParameter(LOGIN))
                .password(req.getParameter(PASSWORD))
                .first_name(req.getParameter(FIRST_NAME))
                .last_name(req.getParameter(LAST_NAME))
                .gender(req.getParameter(GENDER))
                .birth_date(req.getParameter(BIRTH_DATE))
                .build();

        final FormValidation validation = validate(credentials);

        if (validation.isValid()) {
            final Optional<User> userOptional = userService.getNameByUsername(credentials.getLogin());

            if (userOptional.isPresent()) {
                validation.getErrors().put("USER_ALREADY_EXISTS", true);
            } else {
                //create user
                Gender gender = Gender.UNKNOWN;
                if (credentials.getGender().equals("MALE")) {
                    gender = Gender.MALE;
                }
                if (credentials.getGender().equals("FEMALE")) {
                    gender = Gender.FEMALE;
                }

                String date = credentials.getBirth_date() + " 00:00:00";
                User user = User.builder()
                        .id(credentials.getLogin())
                        .passwordHash(credentials.getPassword())//securityService.encript(credentials.getPassword()))
                        .firstName(credentials.getFirst_name())
                        .lastName(credentials.getLast_name())
                        .gender(gender)
                        .birthDate(Timestamp.valueOf(date))
                        .build();

                userService.addUser(user);

                final HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
            }
        }

        if (!validation.isValid()) {
            req.setAttribute("validation", validation);
            req.setAttribute("data", credentials);
            req.getRequestDispatcher("/WEB-INF/login/registration.jsp").forward(req, resp);
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

        if (credentials.getFirst_name() == null || credentials.getFirst_name().isEmpty()) {
            validation.getFields().put(FIRST_NAME,
                    FieldValidation.builder().isEmptyField(true).build());
        }

        if (credentials.getLast_name() == null || credentials.getLast_name().isEmpty()) {
            validation.getFields().put(LAST_NAME,
                    FieldValidation.builder().isEmptyField(true).build());
        }

        if (credentials.getBirth_date() == null || credentials.getBirth_date().isEmpty()) {
            validation.getFields().put(BIRTH_DATE,
                    FieldValidation.builder().isEmptyField(true).build());
        }

        return validation;
    }
}

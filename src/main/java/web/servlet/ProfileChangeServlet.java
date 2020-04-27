package web.servlet;

import model.Credentials;
import model.Gender;
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
import java.sql.Timestamp;

import static constants.Constants.*;

@Singleton
public class ProfileChangeServlet extends HttpServlet {

    private final UserService userService;
    private final FriendService friendService;
    private final MessageService messageService;

    @Inject
    public ProfileChangeServlet(UserService userService, FriendService friendService, MessageService messageService) {
        this.userService = userService;
        this.friendService = friendService;
        this.messageService = messageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final HttpSession session = req.getSession(false);
        final User user = (User) session.getAttribute("user");
        Credentials credentials = Credentials.builder()
                .first_name(user.getFirstName())
                .last_name(user.getLastName())
                .gender(String.valueOf(user.getGender()))
                .birth_date(String.valueOf(user.getBirthDate()).substring(0, 10))
                .address(user.getAddress())
                .info(user.getInfo())
                .build();
        req.setAttribute("data", credentials);

        session.setAttribute("newRequests", friendService.inRequests(user.getId()));
        session.setAttribute("newMessages", messageService.newMessages(user.getId()));

        req.getRequestDispatcher("/WEB-INF/profile/profileChange.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        final Credentials credentials = Credentials.builder()
                .first_name(req.getParameter(FIRST_NAME))
                .last_name(req.getParameter(LAST_NAME))
                .gender(req.getParameter(GENDER))
                .birth_date(req.getParameter(BIRTH_DATE))
                .address(req.getParameter(ADDRESS))
                .info(req.getParameter(INFO))
                .build();

        final FormValidation validation = validate(credentials);

        if (validation.isValid()) {

            final HttpSession session = req.getSession(false);

            final User user = (User) session.getAttribute("user");

            Gender gender = Gender.UNKNOWN;
            if (credentials.getGender().equals("MALE")) {
                gender = Gender.MALE;
            }
            if (credentials.getGender().equals("FEMALE")) {
                gender = Gender.FEMALE;
            }

            System.out.println("doPost{");
            System.out.println("getAddress[" + req.getParameter(ADDRESS) + "]");
            System.out.println("getInfo[" + credentials.getInfo() + "]");
            System.out.println("}");

            String date = credentials.getBirth_date() + " 00:00:00";
            User userUpdate = User.builder()
                    .id(user.getId())
                    .passwordHash(user.getPasswordHash())
                    .firstName(credentials.getFirst_name())
                    .lastName(credentials.getLast_name())
                    .gender(gender)
                    .birthDate(Timestamp.valueOf(date))
                    .address(credentials.getAddress())
                    .info(credentials.getInfo())
                    .build();

            userService.updateUser(userUpdate);
            session.setAttribute("user", userUpdate);
        }

        if (!validation.isValid()) {
            req.setAttribute("validation", validation);
            req.setAttribute("data", credentials);
            req.getRequestDispatcher("/WEB-INF/profile/profileChange.jsp").forward(req, resp);
            return;
        }

        final String profilePage = req.getContextPath() + "/profile";
        resp.sendRedirect(profilePage);
    }

    static FormValidation validate(Credentials credentials) {
        final FormValidation validation = new FormValidation();

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

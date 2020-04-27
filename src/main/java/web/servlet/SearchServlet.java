package web.servlet;

import model.Credentials;
import model.User;
import service.FriendService;
import service.MessageService;
import service.UserService;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static constants.Constants.*;

@Singleton
public class SearchServlet extends HttpServlet {

    private final UserService userService;
    private final FriendService friendService;
    private final MessageService messageService;

    @Inject
    public SearchServlet(UserService userService, FriendService friendService, MessageService messageService) {
        this.userService = userService;
        this.friendService = friendService;
        this.messageService = messageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        final HttpSession session = req.getSession(false);
        final User user = (User) session.getAttribute("user");

        final Credentials credentials = Credentials.builder()
                .login(req.getParameter(LOGIN))
                .first_name(req.getParameter(FIRST_NAME))
                .last_name(req.getParameter(LAST_NAME))
                .build();

//        System.out.println(credentials);

        final String currentPageTmp = req.getParameter(PAGE);
        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(currentPageTmp);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }

        List<User> searchList = new ArrayList<>();
        if (!credentials.isEmpty()) {
            searchList = userService.getNameBySearchFields(credentials.getLogin(), credentials.getFirst_name(), credentials.getLast_name());
        }

        int pages = (int) Math.ceil((double) searchList.size()/ITEMS_ON_PAGE);
        if (currentPage < 1 || currentPage > pages) {
            currentPage = 1;
        }

//        System.out.println("currPage: " + currentPage);
//        System.out.println("size: " + searchList.size());
//        System.out.println("pages: " + (int) Math.ceil((double) searchList.size()/ITEMS_ON_PAGE));
//        System.out.println((currentPage-1)*ITEMS_ON_PAGE);
//        System.out.println(Math.min(currentPage*ITEMS_ON_PAGE, searchList.size()));
//        System.out.println(searchList);

        if (!searchList.isEmpty()) {
            searchList = searchList.subList((currentPage-1)*ITEMS_ON_PAGE, Math.min(currentPage*ITEMS_ON_PAGE, searchList.size()));
        }
//        System.out.println(searchList);

        req.setAttribute("searchList", searchList);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("pages", pages);
        req.setAttribute("data", credentials);

        session.setAttribute("newMessages", messageService.newMessages(user.getId()));
        session.setAttribute("newRequests", friendService.inRequests(user.getId()));

        req.getRequestDispatcher("/WEB-INF/profile/search.jsp").forward(req, resp);
    }
}

package web.servlet;

import model.Credentials;
import model.FriendRequest;
import model.RequestStatus;
import model.User;
import service.FriendService;
import service.MessageService;

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
import static constants.Constants.LAST_NAME;

@Singleton
public class FriendsServlet extends HttpServlet {

    private final FriendService friendService;
    private final MessageService messageService;

    @Inject
    public FriendsServlet(FriendService friendService, MessageService messageService) {
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

        final String currentPageTmp = req.getParameter(PAGE);
        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(currentPageTmp);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }

        String section = req.getParameter("section");

        List<User> list = new ArrayList<>();

        if (section == null) {
            if (!credentials.isEmpty()) {
                list = friendService.getFriends(user.getId(), credentials.getLogin(),
                        credentials.getFirst_name(), credentials.getLast_name());
            } else {
                list = friendService.getFriends(user.getId(), "", "", "");
            }
        } else {
            switch (section) {
                case "in":
                    if (!credentials.isEmpty()) {
                        list = friendService.getInRequests(user.getId(), credentials.getLogin(),
                                credentials.getFirst_name(), credentials.getLast_name());
                    } else {
                        list = friendService.getInRequests(user.getId(), "", "", "");
                    }
                    break;
                case "out":
                    if (!credentials.isEmpty()) {
                        list = friendService.getOutRequests(user.getId(), credentials.getLogin(),
                                credentials.getFirst_name(), credentials.getLast_name());
                    } else {
                        list = friendService.getOutRequests(user.getId(), "", "", "");
                    }
                    break;
                default:
                    if (!credentials.isEmpty()) {
                        list = friendService.getFriends(user.getId(), credentials.getLogin(),
                                credentials.getFirst_name(), credentials.getLast_name());
                    } else {
                        list = friendService.getFriends(user.getId(), "", "", "");
                    }
                    break;
            }
        }

        int pages = (int) Math.ceil((double) list.size() / ITEMS_ON_PAGE);
        if (currentPage < 1 || currentPage > pages) {
            currentPage = 1;
        }

        if (!list.isEmpty()) {
            list = list.subList((currentPage - 1) * ITEMS_ON_PAGE, Math.min(currentPage * ITEMS_ON_PAGE, list.size()));
        }

        int all_num = friendService.allFriends(user.getId());
        int in_num = friendService.inRequests(user.getId());
        int out_num = friendService.outRequests(user.getId());

        req.setAttribute("list", list);
        req.setAttribute("data", credentials);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("pages", pages);
        req.setAttribute("all_num", all_num);
        req.setAttribute("in_num", in_num);
        req.setAttribute("out_num", out_num);

        session.setAttribute("newRequests", in_num);
        session.setAttribute("newMessages", messageService.newMessages(user.getId()));

        req.getRequestDispatcher("/WEB-INF/profile/friends.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        final HttpSession session = req.getSession(false);
        final User user = (User) session.getAttribute("user");

        final Credentials credentials = Credentials.builder()
                .login(req.getParameter(LOGIN))
                .first_name(req.getParameter(FIRST_NAME))
                .last_name(req.getParameter(LAST_NAME))
                .build();

        String section = req.getParameter("section");
        if (section == null || (!section.equals("in") && !section.equals("out") && !section.equals("all"))) {
            section = "all";
        }

        final String currentPageTmp = req.getParameter(PAGE);
        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(currentPageTmp);
        } catch (NumberFormatException e) {
            System.err.println(e);
        }

        String accept = req.getParameter("accept");
        String deny = req.getParameter("deny");
        String delete = req.getParameter("delete");
        String deleteRequest = req.getParameter("deleteRequest");

        if (accept != null) {
            //String acceptUser = accept.substring(4);
            //System.out.println("acceptUser[" + acceptUser + "]");
            System.out.println("accept[" + accept + "]");
            final FriendRequest request = FriendRequest.builder()
                    .senderId(accept)
                    .receiverId(user.getId())
                    .status(RequestStatus.ACCEPTED)
                    .build();
            friendService.updateRequest(request);
        }

        if (deny != null) {
            //String denyUser = deny.substring(4);
            //System.out.println("denyUser[" + denyUser + "]");
            System.out.println("deny[" + deny + "]");
            final FriendRequest request = FriendRequest.builder()
                    .senderId(deny)
                    .receiverId(user.getId())
                    .status(RequestStatus.DENIED)
                    .build();
            friendService.updateRequest(request);
        }

        if (delete != null) {
            //String deleteUser = delete.substring(4);
            //System.out.println("deleteUser[" + deleteUser + "]");
            System.out.println("delete[" + delete + "]");
            final FriendRequest request = FriendRequest.builder()
                    .senderId(delete)
                    .receiverId(user.getId())
                    .status(RequestStatus.DENIED)
                    .build();
            if (friendService.updateRequest(request) < 1) {
                System.out.println("SWAP");
                friendService.updateAndSwapRequest(request);
            }
        }

        if (deleteRequest != null) {
            System.out.println("deleteRequest[" + deleteRequest + "]");
            friendService.deleteRequest(FriendRequest.builder()
                    .senderId(user.getId())
                    .receiverId(deleteRequest)
                    .build()
            );
        }

        req.setAttribute("list", new ArrayList<>());
        req.setAttribute("data", credentials);
        req.setAttribute("currentPage", currentPage);
        req.setAttribute("pages", 0);
        req.setAttribute("all_num", 0);
        req.setAttribute("in_num", 0);
        req.setAttribute("out_num", 0);

        session.setAttribute("newRequests", friendService.inRequests(user.getId()));
        session.setAttribute("newMessages", messageService.newMessages(user.getId()));

        String nextPage = req.getContextPath() + "/friends?section=" + section;

        if (!credentials.isEmpty()) {
            nextPage = nextPage + "&login=" + credentials.getLogin() + "&first_name=" + credentials.getFirst_name()
                    + "&last_name=" + credentials.getLast_name();
        }
        if (currentPage > 1) {
            nextPage = nextPage + "&page=" + currentPage;
        }

        resp.sendRedirect(nextPage);
    }
}

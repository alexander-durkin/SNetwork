package web.servlet;

import model.FriendRequest;
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
import java.util.Optional;

import static constants.Constants.*;
import static model.RequestStatus.ACCEPTED;
import static model.RequestStatus.DENIED;

@Singleton
public class ProfileServlet extends HttpServlet {

    private final UserService userService;
    private final FriendService friendService;
    private final MessageService messageService;

    @Inject
    public ProfileServlet(UserService userService, FriendService friendService, MessageService messageService) {
        this.userService = userService;
        this.friendService = friendService;
        this.messageService = messageService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final HttpSession session = req.getSession(false);
        final User user = (User) session.getAttribute("user");

        session.setAttribute("newRequests", friendService.inRequests(user.getId()));
        session.setAttribute("newMessages", messageService.newMessages(user.getId()));

        req.setAttribute("userAction", "");

        Optional<User> userOptional = userService.getProfileByUsername(req.getParameter("id"));
        if (userOptional.isPresent()) {

            final User userProfile = userOptional.get();
            req.setAttribute("userProfile", userProfile);

            if (!userProfile.getId().equals(user.getId())) {
                final FriendRequest request = FriendRequest.builder()
                        .senderId(user.getId())
                        .receiverId(userProfile.getId())
                        .build();
                FriendRequest friendRequest = friendService.getRequest(request);
                String status = "";
                try {
                    status = friendRequest.getStatus().name();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                switch (status) {
                    case "ACCEPTED":
                        req.setAttribute("userAction", DELETE);
                        break;

                    case "DENIED":
                        if (friendRequest.getSenderId().equals(user.getId())) {
                            req.setAttribute("userAction", WAIT);
                        } else {
                            req.setAttribute("userAction", ACCEPT_ONLY);
                        }
                        break;

                    case "WAITING":
                        if (friendRequest.getSenderId().equals(user.getId())) {
                            req.setAttribute("userAction", WAIT);
                        } else {
                            req.setAttribute("userAction", ACCEPT);
                        }
                        break;

                    default:
                        req.setAttribute("userAction", ADD);
                        break;
                }
            }
        } else {
            req.setAttribute("userProfile", user);
        }
        req.getRequestDispatcher("/WEB-INF/profile/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final HttpSession session = req.getSession(false);
        final User user = (User) session.getAttribute("user");

        String userAction = req.getParameter("action");

        String nextPage = req.getContextPath() + "/profile";

        if (userAction != null) {

            String action = userAction.substring(0, 3);
            String userId = userAction.substring(4);

            final User userProfile = userService.getProfileByUsername(userId).get();
            req.setAttribute("userProfile", userProfile);
            nextPage = req.getContextPath() + "/profile?id=" + userProfile.getId();

            switch (action) {
                case ADD:
                    friendService.addRequest(FriendRequest.builder()
                            .senderId(user.getId())
                            .receiverId(userProfile.getId())
                            .build()
                    );
                    break;

                case ACCEPT:
                    friendService.updateRequest(FriendRequest.builder()
                            .senderId(userProfile.getId())
                            .receiverId(user.getId())
                            .status(ACCEPTED)
                            .build()
                    );
                    break;

                case DELETE:
                    final FriendRequest request = FriendRequest.builder()
                            .senderId(userProfile.getId())
                            .receiverId(user.getId())
                            .status(DENIED)
                            .build();
                    if (friendService.updateRequest(request) < 1) {
                        System.out.println("SWAP");
                        friendService.updateAndSwapRequest(request);
                    }
                    break;

                case DENY:
                    friendService.updateRequest(FriendRequest.builder()
                            .senderId(userProfile.getId())
                            .receiverId(user.getId())
                            .status(DENIED)
                            .build()
                    );
                    break;

                case DELETE_REQUEST:
                    friendService.deleteRequest(FriendRequest.builder()
                            .senderId(user.getId())
                            .receiverId(userProfile.getId())
                            .build()
                    );
                    break;

                case MESSAGE:
                    nextPage = req.getContextPath() + "/messages?id=" + userProfile.getId();
                    break;

                default:
                    break;
            }

        }
        req.setAttribute("userAction", "");
        resp.sendRedirect(nextPage);
    }
}

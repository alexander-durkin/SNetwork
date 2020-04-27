package web.servlet;

import model.Chat;
import model.Message;
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
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Singleton
public class MessagesServlet extends HttpServlet {

    private final MessageService messageService;
    private final FriendService friendService;
    private final UserService userService;

    @Inject
    public MessagesServlet(MessageService messageService, FriendService friendService, UserService userService) {
        this.messageService = messageService;
        this.friendService = friendService;
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final HttpSession session = req.getSession(false);
        final User user = (User) session.getAttribute("user");
        List<Chat> chats = messageService.findChatsByUserId(user.getId());
        req.setAttribute("chatList", chats);

        Optional<User> userOptional = userService.getNameByUsername(req.getParameter("id"));
        if (userOptional.isPresent()) {
            final User userProfile = userOptional.get();
            List<Message> messages = messageService.findByUserId(user.getId(), userProfile.getId());

            Iterator<Message> iterator = messages.iterator();
            while (iterator.hasNext()) {
                messageService.readMessage(iterator.next(), user.getId());
            }
            req.setAttribute("messageList", messages);
            req.setAttribute("userProfile", userProfile);
        } else {
            req.setAttribute("userProfile", user);
        }

        session.setAttribute("newMessages", messageService.newMessages(user.getId()));
        session.setAttribute("newRequests", friendService.inRequests(user.getId()));

        req.getRequestDispatcher("/WEB-INF/profile/messages.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        final HttpSession session = req.getSession(false);
        final User user = (User) session.getAttribute("user");

        String text = req.getParameter("messageText");
        String userId = req.getParameter("userId");
        String messagePage = req.getContextPath() + "/messages?id=" + userId;

        if (text!=null && !text.equals("") && userId!=null) {
            messageService.addMessage(Message.builder()
                    .senderId(user.getId())
                    .receiverId(userId)
                    .text(text)
                    .build()
            );
        }

        resp.sendRedirect(messagePage);
    }
}

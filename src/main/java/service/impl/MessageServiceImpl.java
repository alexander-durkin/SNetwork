package service.impl;

import dao.MessageDao;
import dao.UserDao;
import model.Chat;
import model.Message;
import model.MessageStatus;
import service.MessageService;

import javax.inject.Inject;
import java.util.*;

public class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;
    private final UserDao userDao;

    @Inject
    public MessageServiceImpl(MessageDao messageDao, UserDao userDao) {
        this.messageDao = messageDao;
        this.userDao = userDao;
    }

    @Override
    public int newMessages(String id) {
        return messageDao.newMessages(id);
    }

    @Override
    public List<Message> findByUserId(String id, String userId) {
        List<Message> messages = new ArrayList<>(messageDao.findByUserId(id, userId));
        return messages;
    }

    @Override
    public List<Chat> findChatsByUserId(String id) {
        List<Chat> chats = new ArrayList<>();
        Set<String> users = new LinkedHashSet<>(messageDao.findChatsByUserId(id));

        Iterator<String> iterator = users.iterator();
        while (iterator.hasNext()) {
            String userId = iterator.next();
            chats.add(Chat.builder()
                    .user(userDao.getNameById(userId).get())
                    .newMessages(messageDao.newMessagesFromUser(id, userId))
                    .build()
            );
        }
        return chats;
    }

    @Override
    public void addMessage(Message message) {
        messageDao.createMessage(message);
        System.out.println("Message is added into DB");
    }

    @Override
    public void readMessage(Message message, String receiverId) {
        if (message.getStatus().equals(MessageStatus.SENT) && message.getReceiverId().equals(receiverId)) {
            messageDao.readMessage(message);
            System.out.println("readMessage[" + message.getId() + "]");
        }
    }
}

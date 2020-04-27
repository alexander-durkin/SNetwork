package service;

import model.Chat;
import model.Message;

import java.util.List;

public interface MessageService {

    int newMessages(String id);

    List<Message> findByUserId(String id, String userId);
    List<Chat> findChatsByUserId(String id);

    void addMessage(Message message);
    void readMessage(Message message, String receiverId);

}

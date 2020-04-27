package dao;

import model.Message;

import java.util.List;

public interface MessageDao {

    int newMessages(String id);
    int newMessagesFromUser(String id, String userId);

    List<Message> findByUserId(String id, String userId);
    List<String> findChatsByUserId(String id);

    int createMessage(Message message);
    void readMessage(Message message);
}

package dao.impl;

import dao.MessageDao;
import model.Message;
import model.MessageStatus;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

public class PgMessageDao implements MessageDao {

    private final DataSource dataSource;

    @Inject
    public PgMessageDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Returns number of new messages from different users */
    @Override
    public int newMessages(String id) {

        int newMessages = 0;

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT count(DISTINCT sender_id) " +
                            "FROM itnet.messages " +
                            "WHERE receiver_id = ? " +
                            "      AND status = 'SENT'"
            );
            select.setString(1, id);

            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                newMessages = resultSet.getInt(1);
            }

            select.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return newMessages;
    }

    /** Returns number of new messages from specific user */
    @Override
    public int newMessagesFromUser(String id, String userId) {
        int newMessagesFromUser = 0;

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT count(*) " +
                            "FROM itnet.messages " +
                            "WHERE sender_id = ? AND receiver_id = ? AND messages.status = 'SENT'"
            );
            select.setString(1, userId);
            select.setString(2, id);

            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                newMessagesFromUser = resultSet.getInt(1);
            }

            select.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return newMessagesFromUser;
    }

    /** Returns list of messages from chat with specific user */
    @Override
    public List<Message> findByUserId(String id, String userId) {

        List<Message> messages = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * " +
                            "FROM itnet.messages " +
                            "WHERE (sender_id = ? AND receiver_id = ?) " +
                            "OR (sender_id = ? AND receiver_id = ?) " +
                            "ORDER BY sending_time"
            );
            select.setString(1, id);
            select.setString(2, userId);
            select.setString(3, userId);
            select.setString(4, id);

            final ResultSet resultSet = select.executeQuery();
            while (resultSet.next()) {
                messages.add(extractMessage(resultSet));
                System.out.println(messages);
            }

            select.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return messages;
    }

    /** Returns list of opened chats */
    @Override
    public List<String> findChatsByUserId(String id) {

        List<String> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT receiver_id " +
                            "FROM ( " +
                            "       SELECT " +
                            "         receiver_id, " +
                            "         text, " +
                            "         sending_time " +
                            "       FROM itnet.messages " +
                            "       WHERE sender_id = ? " +
                            "       GROUP BY receiver_id, text, sending_time " +
                            "       UNION " +
                            "       SELECT " +
                            "         sender_id, " +
                            "         text, " +
                            "         sending_time " +
                            "       FROM itnet.messages " +
                            "       WHERE receiver_id = ? " +
                            "       GROUP BY sender_id, text, sending_time " +
                            "       ORDER BY sending_time DESC) AS p"
            );
            select.setString(1, id);
            select.setString(2, id);

            final ResultSet resultSet = select.executeQuery();
            while (resultSet.next()) {
                users.add(resultSet.getString(1));
            }

            select.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return users;
    }

    /** Creates message in database */
    @Override
    public int createMessage(Message message) {
        int code = 0;
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO itnet.messages" +
                            "(sender_id, receiver_id, text) " +
                            "VALUES (?, ?, ?)"
            );
            insert.setString(1, message.getSenderId());
            insert.setString(2, message.getReceiverId());
            insert.setString(3, message.getText());

            code = insert.executeUpdate();
            System.out.println("createMessage[" + message.getText() + "], code: " + code);
            insert.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("failed to create message[" + message.getText() + "], code: " + code);
            throw new RuntimeException();
        } finally {
            return code;
        }
    }

    /** Marks message as read */
    @Override
    public void readMessage(Message message) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement update = connection.prepareStatement(
                    "UPDATE itnet.messages SET " +
                            "status = 'READ' " +
                            "WHERE id = ?"
            );
            update.setInt(1, (int) message.getId());

            System.out.println("updateMessage[" + message.getId() + "]");
            update.executeUpdate();

            update.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /** Extracts message from database */
    private Message extractMessage(ResultSet resultSet) throws SQLException {
        return Message.builder()
                .id(resultSet.getLong("id"))
                .senderId(resultSet.getString("sender_id"))
                .receiverId(resultSet.getString("receiver_id"))
                .text(resultSet.getString("text"))
                .sendingTime(resultSet.getTimestamp("sending_time"))
                .status(MessageStatus.valueOf(resultSet.getString("status")))
                .build();
    }
}

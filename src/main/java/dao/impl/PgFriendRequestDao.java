package dao.impl;

import dao.FriendRequestDao;
import model.FriendRequest;
import model.RequestStatus;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PgFriendRequestDao implements FriendRequestDao {

    private final DataSource dataSource;

    @Inject
    public PgFriendRequestDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int allFriends(String id) {

        int allFriends = 0;

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT count(*) FROM (" +
                            "       SELECT sender_id " +
                            "       FROM itnet.friends " +
                            "       WHERE receiver_id = ? AND status = 'ACCEPTED' " +
                            "       UNION " +
                            "       SELECT receiver_id " +
                            "       FROM itnet.friends " +
                            "       WHERE sender_id = ? AND status = 'ACCEPTED') AS X"
            );
            select.setString(1, id);
            select.setString(2, id);

            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                allFriends = resultSet.getInt(1);
            }
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return allFriends;
    }

    /**
     * Returns number of incoming friend requests
     */
    @Override
    public int inRequests(String id) {

        int inRequests = 0;

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT count(*) " +
                            "FROM itnet.friends " +
                            "WHERE receiver_id = ? " +
                            "   AND status = 'WAITING'"
            );
            select.setString(1, id);

            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                inRequests = resultSet.getInt(1);
            }
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return inRequests;
    }

    @Override
    public int outRequests(String id) {

        int outRequests = 0;

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT count(*) " +
                            "FROM itnet.friends " +
                            "WHERE sender_id = ? " +
                            "   AND (status = 'WAITING' OR status = 'DENIED')"
            );
            select.setString(1, id);

            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                outRequests = resultSet.getInt(1);
            }
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return outRequests;
    }

    /**
     * Returns friend request by request fields (senderId and receiverId) from database
     */
    @Override
    public FriendRequest getRequest(FriendRequest request) {

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * " +
                            "FROM itnet.friends " +
                            "WHERE sender_id = ? " +
                            "  AND receiver_id = ? " +
                            "UNION " +
                            "SELECT * " +
                            "FROM itnet.friends " +
                            "WHERE sender_id = ? " +
                            "  AND receiver_id = ?"
            );
            select.setString(1, request.getSenderId());
            select.setString(2, request.getReceiverId());
            select.setString(3, request.getReceiverId());
            select.setString(4, request.getSenderId());

            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                FriendRequest friendRequest = extractRequest(resultSet);
                select.close();
                return friendRequest;
            }
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return null;
    }

    /**
     * Returns list of friends
     */
    @Override
    public List<String> getFriends(String id, String userName, String firstName, String lastName) {
        List<String> friends = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * FROM (" +
                            "       SELECT sender_id " +
                            "       FROM itnet.friends " +
                            "       WHERE receiver_id = ? AND status = 'ACCEPTED' " +
                            "       UNION " +
                            "       SELECT receiver_id " +
                            "       FROM itnet.friends " +
                            "       WHERE sender_id = ? AND status = 'ACCEPTED') AS X " +
                            "WHERE sender_id IN (SELECT id FROM itnet.user " +
                            "                   WHERE id ILIKE ? AND first_name ILIKE ? AND last_name ILIKE ?)"
            );

            String userNameTmp = userName + '%';
            String firstNameTmp = firstName + '%';
            String lastNameTmp = lastName + '%';

            select.setString(1, id);
            select.setString(2, id);
            select.setString(3, userNameTmp);
            select.setString(4, firstNameTmp);
            select.setString(5, lastNameTmp);

            final ResultSet resultSet = select.executeQuery();
            while (resultSet.next()) {
                friends.add(resultSet.getString(1));
            }
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return friends;
    }

    /**
     * Returns list of incoming requests
     */
    @Override
    public List<String> getInRequests(String id, String userName, String firstName, String lastName) {
        List<String> inRequests = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT sender_id " +
                            "FROM itnet.friends " +
                            "WHERE (receiver_id = ? AND status = 'WAITING') " +
                            "   AND sender_id IN (SELECT id FROM itnet.user " +
                            "                       WHERE id ILIKE ? AND first_name ILIKE ? AND last_name ILIKE ?)"
            );

            String userNameTmp = userName + '%';
            String firstNameTmp = firstName + '%';
            String lastNameTmp = lastName + '%';

            select.setString(1, id);
            select.setString(2, userNameTmp);
            select.setString(3, firstNameTmp);
            select.setString(4, lastNameTmp);

            final ResultSet resultSet = select.executeQuery();
            while (resultSet.next()) {
                inRequests.add(resultSet.getString(1));
            }
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return inRequests;
    }

    /**
     * Returns list of sent requests
     */
    @Override
    public List<String> getOutRequests(String id, String userName, String firstName, String lastName) {
        List<String> outRequests = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT receiver_id " +
                            "FROM itnet.friends " +
                            "WHERE (sender_id = ? AND (status = 'WAITING' OR status = 'DENIED')) " +
                            "   AND receiver_id IN (SELECT id FROM itnet.user " +
                            "                       WHERE id ILIKE ? AND first_name ILIKE ? AND last_name ILIKE ?)"
            );

            String userNameTmp = userName + '%';
            String firstNameTmp = firstName + '%';
            String lastNameTmp = lastName + '%';

            select.setString(1, id);
            select.setString(2, userNameTmp);
            select.setString(3, firstNameTmp);
            select.setString(4, lastNameTmp);

            final ResultSet resultSet = select.executeQuery();
            while (resultSet.next()) {
                outRequests.add(resultSet.getString(1));
            }
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return outRequests;
    }

    /**
     * Creates friend request in database
     */
    @Override
    public void createRequest(FriendRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO itnet.friends " +
                            "(sender_id, receiver_id) " +
                            "VALUES (?, ?)"
            );
            insert.setString(1, request.getSenderId());
            insert.setString(2, request.getReceiverId());

            insert.executeUpdate();
            insert.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Deletes friend request from database
     */
    @Override
    public void deleteRequest(FriendRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement delete = connection.prepareStatement(
                    "DELETE FROM itnet.friends " +
                            "WHERE sender_id = ? AND receiver_id = ?"
            );
            delete.setString(1, request.getSenderId());
            delete.setString(2, request.getReceiverId());

            delete.executeUpdate();
            delete.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Updates status of friend request in database
     */
    @Override
    public int updateRequest(FriendRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement update = connection.prepareStatement(
                    "UPDATE itnet.friends SET " +
                            "status = ? " +
                            "WHERE sender_id = ? AND receiver_id = ?"
            );

            update.setString(1, request.getStatus().name());
            update.setString(2, request.getSenderId());
            update.setString(3, request.getReceiverId());

            int res = update.executeUpdate();
            update.close();

            return res;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Updates status of friend request and swap senderId and receiverId in database
     */
    @Override
    public int updateAndSwapRequest(FriendRequest request) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement update = connection.prepareStatement(
                    "UPDATE itnet.friends SET " +
                            "sender_id = ?, receiver_id = ?, status = ? " +
                            "WHERE sender_id = ? AND receiver_id = ?"
            );

            update.setString(1, request.getSenderId());
            update.setString(2, request.getReceiverId());
            update.setString(3, request.getStatus().name());
            update.setString(4, request.getReceiverId());
            update.setString(5, request.getSenderId());

            int res = update.executeUpdate();
            update.close();

            return res;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    /**
     * Extracts friend request from database
     */
    private FriendRequest extractRequest(ResultSet resultSet) throws SQLException {
        return FriendRequest.builder()
                .senderId(resultSet.getString("sender_id"))
                .receiverId(resultSet.getString("receiver_id"))
                .status(RequestStatus.valueOf(resultSet.getString("status")))
                .build();
    }
}

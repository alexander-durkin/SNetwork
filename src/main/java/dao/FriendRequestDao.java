package dao;

import model.FriendRequest;

import java.util.List;

public interface FriendRequestDao {

    int allFriends(String id);
    int inRequests(String id);
    int outRequests(String id);

    FriendRequest getRequest(FriendRequest request);

    List<String> getFriends(String id, String userName, String firstName, String lastName);
    List<String> getInRequests(String id, String userName, String firstName, String lastName);
    List<String> getOutRequests(String id, String userName, String firstName, String lastName);

    void createRequest(FriendRequest request);
    void deleteRequest(FriendRequest request);
    int updateRequest(FriendRequest request);
    int updateAndSwapRequest(FriendRequest request);
}

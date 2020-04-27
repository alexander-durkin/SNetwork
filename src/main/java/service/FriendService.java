package service;

import model.FriendRequest;
import model.User;

import java.util.List;

public interface FriendService {

    int allFriends(String id);
    int inRequests(String id);
    int outRequests(String id);

    FriendRequest getRequest(FriendRequest request);

    List<User> getFriends(String id, String userName, String firstName, String lastName);
    List<User> getInRequests(String id, String userName, String firstName, String lastName);
    List<User> getOutRequests(String id, String userName, String firstName, String lastName);

    void addRequest(FriendRequest request);
    void deleteRequest(FriendRequest request);
    int updateRequest(FriendRequest request);
    int updateAndSwapRequest(FriendRequest request);
}

package service.impl;

import dao.FriendRequestDao;
import dao.UserDao;
import model.FriendRequest;
import model.User;
import service.FriendService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class FriendServiceImpl implements FriendService {

    private final FriendRequestDao friendRequestDao;
    private final UserDao userDao;

    @Inject
    public FriendServiceImpl(FriendRequestDao friendRequestDao, UserDao userDao) {
        this.friendRequestDao = friendRequestDao;
        this.userDao = userDao;
    }

    @Override
    public int allFriends(String id) {
        return friendRequestDao.allFriends(id);
    }

    @Override
    public int inRequests(String id) {
        return friendRequestDao.inRequests(id);
    }

    @Override
    public int outRequests(String id) {
        return friendRequestDao.outRequests(id);
    }

    @Override
    public FriendRequest getRequest(FriendRequest request) {
        return friendRequestDao.getRequest(request);
    }

    @Override
    public List<User> getFriends(String id, String userName, String firstName, String lastName) {
        List<String> friendsId = new ArrayList<>(friendRequestDao.getFriends(id, userName, firstName, lastName));
        List<User> friends = new ArrayList<>();

        for (String string: friendsId) {
            friends.add(userDao.getNameById(string).get());
        }

        return friends;
    }

    @Override
    public List<User> getInRequests(String id, String userName, String firstName, String lastName) {
        List<String> friendsInRequests = new ArrayList<>(friendRequestDao.getInRequests(id, userName, firstName, lastName));
        List<User> inRequests = new ArrayList<>();

        for (String string: friendsInRequests) {
            inRequests.add(userDao.getNameById(string).get());
        }

        return inRequests;
    }

    @Override
    public List<User> getOutRequests(String id, String userName, String firstName, String lastName) {
        List<String> friendsOutRequests = new ArrayList<>(friendRequestDao.getOutRequests(id, userName, firstName, lastName));
        List<User> outRequests = new ArrayList<>();

        for (String string: friendsOutRequests) {
            outRequests.add(userDao.getNameById(string).get());
        }

        return outRequests;
    }

    @Override
    public void addRequest(FriendRequest request) {
        friendRequestDao.createRequest(request);
    }

    @Override
    public void deleteRequest(FriendRequest request) {
        friendRequestDao.deleteRequest(request);
    }

    @Override
    public int updateRequest(FriendRequest request) {
        return friendRequestDao.updateRequest(request);
    }

    @Override
    public int updateAndSwapRequest(FriendRequest request) {
        return friendRequestDao.updateAndSwapRequest(request);
    }
}

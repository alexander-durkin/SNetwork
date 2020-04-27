package service.impl;

import dao.UserDao;
import model.Credentials;
import model.User;
import service.SecurityService;
import service.UserService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final SecurityService securityService;

    @Inject
    public UserServiceImpl(UserDao userDao, SecurityService securityService) {
        this.userDao = userDao;
        this.securityService = securityService;
    }

    @Override
    public Optional<User> getByCredentials(Credentials credentials) {
        final Optional<User> userOptional = userDao.getById(credentials.getLogin());

        if (!userOptional.isPresent()) {
            System.out.println("User doesn't found");
            return Optional.empty();
        }

        final User user = userOptional.get();
        if (!securityService.validate(credentials.getPassword(), user.getPasswordHash())) {
            System.out.println("Wrong login/password");
            return Optional.empty();
        }

        System.out.println("User found");
        return Optional.of(user);
    }

    @Override
    public Optional<User> getNameByUsername(String username) {
        final Optional<User> userOptional = userDao.getNameById(username);

        if (!userOptional.isPresent()) {

            return Optional.empty();
        }

        final User user = userOptional.get();
        return Optional.of(user);
    }

    @Override
    public Optional<User> getProfileByUsername(String username) {
        final Optional<User> userOptional = userDao.getProfileById(username);

        if (!userOptional.isPresent()) {
            return Optional.empty();
        }

        final User user = userOptional.get();
        return Optional.of(user);
    }

    @Override
    public List<User> getNameBySearchFields(String userName, String firstName, String lastName) {
        List<User> users = new ArrayList<>(userDao.getBySearchFields(userName, firstName, lastName));
        return users;
    }

    @Override
    public int addUser(User user) {
        int result = userDao.createUser(user);
        if (result == 0) {
            System.out.println("Failed to add user to DB");
        } else {
            System.out.println("User " + user.getId() + " is added into DB");
        }
        return result;
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
        System.out.println("User " + user.getId() + " is updated");
    }
}

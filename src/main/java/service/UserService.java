package service;

import model.Credentials;
import model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getByCredentials(Credentials credentials);
    Optional<User> getNameByUsername(String username);
    Optional<User> getProfileByUsername(String username);
    List<User> getNameBySearchFields(String userName, String firstName, String lastName);

    int addUser(User user);

    void updateUser(User user);
}

package dao;

import model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> getById(String id);
    Optional<User> getProfileById(String id);
    Optional<User> getNameById(String id);
    List<User> getBySearchFields(String userName, String firstName, String lastName);

    int createUser(User user);

    int updateUser(User user);
}

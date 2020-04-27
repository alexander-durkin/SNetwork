package dao.impl;

import dao.UserDao;
import model.Gender;
import model.User;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PgUserDao implements UserDao {

    //внутри дао не стоит делать что-то с паролем
    private final DataSource dataSource;

    @Inject
    public PgUserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /** Gets all user info by username */
    @Override
    public Optional<User> getById(String id) {

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * " +
                            "FROM itnet.user " +
                            "WHERE id = ?"
            );
            select.setString(1, id);
            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                Optional<User> optionalUser = Optional.of(extractUser(resultSet));
                select.close();
                System.out.println("User is found");
                return optionalUser;
            } else {
                select.close();
                System.out.println("User doesn't exist");
                return Optional.empty();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /** Gets all user info except password by username */
    @Override
    public Optional<User> getProfileById(String id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT * " +
                            "FROM itnet.user " +
                            "WHERE id = ?"
            );
            select.setString(1, id);
            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                Optional<User> optionalUser = Optional.of(extractUserProfile(resultSet));
                select.close();
                System.out.println("User is found");
                return optionalUser;
            } else {
                select.close();
                System.out.println("User doesn't exist");
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /** Gets user name fields by username */
    @Override
    public Optional<User> getNameById(String id) {
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT id, first_name, last_name " +
                            "FROM itnet.user " +
                            "WHERE id = ?"
            );
            select.setString(1, id);
            final ResultSet resultSet = select.executeQuery();
            if (resultSet.next()) {
                Optional<User> optionalUser = Optional.of(extractUserName(resultSet));
                select.close();
                System.out.println("User is found");
                return optionalUser;
            } else {
                select.close();
                System.out.println("User doesn't exist");
                return Optional.empty();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /** Gets user name fields by search fields */
    @Override
    public List<User> getBySearchFields(String userName, String firstName, String lastName) {
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement select = connection.prepareStatement(
                    "SELECT id, first_name, last_name " +
                            "FROM itnet.user " +
                            "WHERE id ILIKE ? AND first_name ILIKE ? AND last_name ILIKE ?"
            );

            String userNameTmp = userName + '%';
            String firstNameTmp = firstName + '%';
            String lastNameTmp = lastName + '%';

            select.setString(1, userNameTmp);
            select.setString(2, firstNameTmp);
            select.setString(3, lastNameTmp);

            final ResultSet resultSet = select.executeQuery();
            while (resultSet.next()) {
                users.add(extractUserName(resultSet));
            }
            System.out.println("Found users: " + users.size());
            select.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return users;
    }

    /** Creates user in database */
    @Override
    public int createUser(User user) {
        int code = 0;
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement insert = connection.prepareStatement(
                    "INSERT INTO itnet.user" +
                            "(id, password, first_name, last_name, gender, birth_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?)"
            );

            insert.setString(1, user.getId());
            insert.setString(2, user.getPasswordHash());
            insert.setString(3, user.getFirstName());
            insert.setString(4, user.getLastName());
            insert.setString(5, user.getGender().name());
            insert.setTimestamp(6, user.getBirthDate());

            code = insert.executeUpdate();
            System.out.println("createUser succeeded, code: " + code);
            insert.close();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("createUser failed, code: " + code);
            throw new RuntimeException();
        } finally {
            return code;
        }
    }

    /** Updates user fields in database */
    @Override
    public int updateUser(User user) {
        int code = 0;
        try (Connection connection = dataSource.getConnection()) {
            final PreparedStatement update = connection.prepareStatement(
                    "UPDATE itnet.user SET " +
                            "first_name = ?, last_name = ?, gender = ?, birth_date = ?, address = ?, info = ? " +
                            "WHERE id = ?"
            );

            update.setString(1, user.getFirstName());
            update.setString(2, user.getLastName());
            update.setString(3, user.getGender().name());
            update.setTimestamp(4, user.getBirthDate());
            update.setString(5, user.getAddress());
            update.setString(6, user.getInfo());
            update.setString(7, user.getId());

            System.out.println("updateUser{");
            System.out.println("getAddress[" + user.getAddress() + "]");
            System.out.println("getInfo[" + user.getInfo() + "]");
            System.out.println("}");

            code = update.executeUpdate();

            update.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            return code;
        }
    }

    /** Extracts all user info from database */
    private User extractUser(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getString("id"))
                .passwordHash(resultSet.getString("password"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .gender(Gender.valueOf(resultSet.getString("gender")))
                .birthDate(resultSet.getTimestamp("birth_date"))
                .address(resultSet.getString("address"))
                .info(resultSet.getString("info"))
                .build();
    }

    /** Extracts all user info except password from database */
    private User extractUserProfile(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getString("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .gender(Gender.valueOf(resultSet.getString("gender")))
                .birthDate(resultSet.getTimestamp("birth_date"))
                .address(resultSet.getString("address"))
                .info(resultSet.getString("info"))
                .build();
    }

    /** Extracts username, first name and last name from database */
    private User extractUserName(ResultSet resultSet) throws SQLException {
        return User.builder()
                .id(resultSet.getString("id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .build();
    }
}

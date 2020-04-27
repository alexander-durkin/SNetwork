package services;

import dao.UserDao;
import dao.impl.PgUserDao;
import db.PgConfig;
import db.PgConfigProvider;
import db.PgDataSourceProvider;
import model.Credentials;
import model.Gender;
import model.User;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import service.SecurityService;
import service.UserService;
import service.impl.SecurityServiceImpl;
import service.impl.UserServiceImpl;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)   //Определяет порядок запуска тестов по их названию (по возрастанию)
public class UserServiceImplTest {

    final PgConfig config = new PgConfigProvider().get();
    final DataSource dataSource = new PgDataSourceProvider(config).get();
    final UserDao userDao = new PgUserDao(dataSource);
    final SecurityService securityService = new SecurityServiceImpl();
    final UserService userService = new UserServiceImpl(userDao, securityService);  //Объект тестируемого класса
    final User user = User.builder()    //Полный объект класса User
            .id("serviceTest")
            .firstName("Service")
            .lastName("Impl")
            .passwordHash("testpwd")
            .gender(Gender.FEMALE)
            .birthDate(Timestamp.valueOf("2000-06-11 00:00:00"))
            .address("")
            .info("")
            .build();
    final User userProfile = User.builder() //Объект класса User без пароля
            .id("serviceTest")
            .firstName("Service")
            .lastName("Impl")
            .gender(Gender.FEMALE)
            .birthDate(Timestamp.valueOf("2000-06-11 00:00:00"))
            .address("")
            .info("")
            .build();
    final User userName = User.builder()    //Объект класса User, содержащий только id, firstName, lastName
            .id("serviceTest")
            .firstName("Service")
            .lastName("Impl")
            .build();

    @Test
    public void test01_addUser() {
        int actual = userService.addUser(user);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void test02_addUser_ExistingUser() {
        int actual = userService.addUser(user);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void test03_getByCredentials() {
        final Optional<User> userOptional = userService.getByCredentials(Credentials.builder()
                .login("serviceTest")
                .password("testpwd")
                .build());
        Assert.assertEquals(user, userOptional.get());
    }

    @Test
    public void test04_getByCredentials_WrongLogin() {
        final Optional<User> userOptional = userService.getByCredentials(Credentials.builder()
                .login("serviceTestes")
                .password("testpwd")
                .build());
        Assert.assertEquals(Optional.empty(), userOptional);
    }

    @Test
    public void test05_getByCredentials_WrongPassword() {
        final Optional<User> userOptional = userService.getByCredentials(Credentials.builder()
                .login("serviceTest")
                .password("testpwdqwer")
                .build());
        Assert.assertEquals(Optional.empty(), userOptional);
    }

    @Test
    public void test06_getNameByUsername() {
        final Optional<User> userOptional = userService.getNameByUsername("serviceTest");
        Assert.assertEquals(userName, userOptional.get());
    }

    @Test
    public void test07_getNameByUsername_WrongUsername() {
        final Optional<User> userOptional = userService.getNameByUsername("serviceTestTestTest");
        Assert.assertEquals(Optional.empty(), userOptional);
    }

    @Test
    public void test08_getProfileByUsername() {
        final Optional<User> userOptional = userService.getProfileByUsername("serviceTest");
        Assert.assertEquals(userProfile, userOptional.get());
    }

    @Test
    public void test09_getProfileByUsername_WrongUsername() {
        final Optional<User> userOptional = userService.getProfileByUsername("serviceTestTestTest");
        Assert.assertEquals(Optional.empty(), userOptional);
    }

    @Test
    public void test10_getBySearchFields() {
        List<User> userList = userService.getNameBySearchFields("user2", "", "");
        Assert.assertEquals(2, userList.size());
    }

    @Test
    public void test11_getBySearchFields_NullFields() {
        List<User> userList = userService.getNameBySearchFields("user2", null, null);
        Assert.assertEquals(0, userList.size());
    }

    @Test
    public void test12_getBySearchFields_WrongUserName() {
        List<User> userList = userService.getNameBySearchFields("wrong", "Petr", "Petrov");
        Assert.assertEquals(0, userList.size());
    }

    @Test
    public void test13_getBySearchFields_EmptyUserName() {
        List<User> userList = userService.getNameBySearchFields("", "С", "С");
        Assert.assertEquals(2, userList.size());
    }
}

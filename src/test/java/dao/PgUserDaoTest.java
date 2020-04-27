package dao;

import dao.impl.PgUserDao;
import db.PgConfig;
import db.PgConfigProvider;
import db.PgDataSourceProvider;
import model.Gender;
import model.User;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)   //Определяет порядок запуска тестов по их названию (по возрастанию)
public class PgUserDaoTest {

    final PgConfig config = new PgConfigProvider().get();
    final DataSource dataSource = new PgDataSourceProvider(config).get();

    final UserDao userDao = new PgUserDao(dataSource);  //Объект тестируемого класса
    final User user = User.builder()    //Полный объект класса User
            .id("daoTest")
            .firstName("Pg")
            .lastName("User")
            .passwordHash("testpwd")
            .gender(Gender.MALE)
            .birthDate(Timestamp.valueOf("2000-05-10 00:00:00"))
            .address("")
            .info("")
            .build();
    final User userProfile = User.builder() //Объект класса User без пароля
            .id("daoTest")
            .firstName("Pg")
            .lastName("User")
            .gender(Gender.MALE)
            .birthDate(Timestamp.valueOf("2000-05-10 00:00:00"))
            .address("")
            .info("")
            .build();
    final User userName = User.builder()    //Объект класса User, содержащий только id, firstName, lastName
            .id("daoTest")
            .firstName("Pg")
            .lastName("User")
            .build();

    @Test
    public void test01_createUser() {
        int actual = userDao.createUser(user);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void test02_createUser_ExistingUser() {
        int actual = userDao.createUser(user);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void test03_getById() {
        Optional<User> userOptional = userDao.getById("daoTest");
        Assert.assertEquals(user, userOptional.get());
    }

    @Test
    public void test04_getById_WrongId() {
        Optional<User> userOptional = userDao.getById("testinno");
        Assert.assertEquals(Optional.empty(), userOptional);
    }

    @Test
    public void test05_getProfileById() {
        Optional<User> userOptional = userDao.getProfileById("daoTest");
        Assert.assertEquals(userProfile, userOptional.get());
    }

    @Test
    public void test06_getProfileById_WrongId() {
        Optional<User> userOptional = userDao.getProfileById("testinno");
        Assert.assertEquals(Optional.empty(), userOptional);
    }

    @Test
    public void test07_getNameById() {
        Optional<User> userOptional = userDao.getNameById("daoTest");
        Assert.assertEquals(userName, userOptional.get());
    }

    @Test
    public void test08_getNameById_WrongId() {
        Optional<User> userOptional = userDao.getNameById("testinno");
        Assert.assertEquals(Optional.empty(), userOptional);
    }

    @Test
    public void test09_updateUser() {
        int actual = userDao.updateUser(userProfile);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void test10_updateUser_NullFields() {
        int actual = userDao.updateUser(userName);
        Assert.assertEquals(0, actual);
    }

    @Test
    public void test11_getBySearchFields() {
        List<User> userList = userDao.getBySearchFields("user2", "", "");
        Assert.assertEquals(2, userList.size());
    }

    @Test
    public void test12_getBySearchFields_NullFields() {
        List<User> userList = userDao.getBySearchFields("user2", null, null);
        Assert.assertEquals(0, userList.size());
    }

    @Test
    public void test13_getBySearchFields_WrongUserName() {
        List<User> userList = userDao.getBySearchFields("wrong", "Petr", "Petrov");
        Assert.assertEquals(0, userList.size());
    }

    @Test
    public void test14_getBySearchFields_EmptyUserName() {
        List<User> userList = userDao.getBySearchFields("", "С", "С");
        Assert.assertEquals(2, userList.size());
    }
}

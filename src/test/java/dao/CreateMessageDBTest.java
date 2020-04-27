package dao;

import dao.impl.PgMessageDao;
import db.PgConfig;
import db.PgConfigProvider;
import db.PgDataSourceProvider;
import model.Message;
import org.junit.Assert;
import org.junit.Test;

import javax.sql.DataSource;

public class CreateMessageDBTest {

    final PgConfig config = new PgConfigProvider().get();
    final DataSource dataSource = new PgDataSourceProvider(config).get();
    final MessageDao messageDao = new PgMessageDao(dataSource);

    final Message message = Message.builder()
            .senderId("user1")
            .receiverId("user2")
            .text("testText")
            .build();

    @Test
    public void createMessage() {
        int actual = messageDao.createMessage(message);
        Assert.assertEquals(1, actual);
    }

    @Test
    public void createMessageFromNonExistingUser() {
        int actual = messageDao.createMessage(Message.builder()
                .senderId("use")
                .receiverId("user2")
                .text("testText")
                .build());
        Assert.assertEquals(0, actual);
    }

    @Test
    public void createMessageToNonExistingUser() {
        int actual = messageDao.createMessage(Message.builder()
                .senderId("user1")
                .receiverId("use")
                .text("testText")
                .build());
        Assert.assertEquals(0, actual);
    }
}

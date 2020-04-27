package services;

import dao.MessageDao;
import dao.UserDao;
import dao.impl.PgMessageDao;
import dao.impl.PgUserDao;
import db.PgConfig;
import db.PgConfigProvider;
import db.PgDataSourceProvider;
import model.Message;
import org.junit.Test;
import service.MessageService;
import service.impl.MessageServiceImpl;

import javax.sql.DataSource;

public class CreateMessageServiceTest {

    final PgConfig config = new PgConfigProvider().get();
    final DataSource dataSource = new PgDataSourceProvider(config).get();
    final MessageDao messageDao = new PgMessageDao(dataSource);
    final UserDao userDao = new PgUserDao(dataSource);
    final MessageService messageService = new MessageServiceImpl(messageDao, userDao);
    final Message message = Message.builder()
            .senderId("user1")
            .receiverId("user2")
            .text("testText")
            .build();

    @Test
    public void createMessage() {
        messageService.addMessage(message);
    }
}

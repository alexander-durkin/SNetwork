package model;

import lombok.Builder;
import lombok.Value;

import java.sql.Timestamp;

@Value
@Builder
public class Message {

    long id;

    String senderId;
    String receiverId;

    String text;

    Timestamp sendingTime;

    MessageStatus status;
}

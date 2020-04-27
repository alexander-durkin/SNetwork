package model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Chat {
    User user;
    int newMessages;
}

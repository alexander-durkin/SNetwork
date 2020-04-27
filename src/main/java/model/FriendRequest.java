package model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FriendRequest {
    String senderId;
    String receiverId;
    RequestStatus status;
}

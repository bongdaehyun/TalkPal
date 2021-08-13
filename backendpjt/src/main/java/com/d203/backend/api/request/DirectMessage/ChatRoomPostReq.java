package com.d203.backend.api.request.DirectMessage;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel("ChatRoomPostReq")
public class ChatRoomPostReq {
    private Long fromUserId;
    private Long toUserId;
}

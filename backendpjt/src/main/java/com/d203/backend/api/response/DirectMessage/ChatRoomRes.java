package com.d203.backend.api.response.DirectMessage;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("chatRoom Response")
public class ChatRoomRes {
    private String nickName;

    private String imgPath;

    private Long chatRoomId;
}

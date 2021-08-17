package com.d203.backend.api.request.DirectMessage;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel("DirectMessagePostReq")
public class DirectMessagePostReq {
    private String message;
    private Long userId;
    private Long chatRoomId;
}

package com.d203.backend.api.response.DirectMessage;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("chatMessage Response")
public class ChatMessageRes {
    private String message;

    private Long userId;

    private Long chatRoomId;

    private LocalDateTime time;
}

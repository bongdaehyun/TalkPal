package com.d203.backend.api.response.DirectMessage;

import com.d203.backend.db.entity.ChatMessage;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("chatMessageList Response")
public class ChatMessageListRes {
    List<ChatMessageRes> chatMessageList;


    public static ChatMessageListRes of(List<ChatMessage> chatMessageList) {
        ChatMessageListRes chatMessageListRes = new ChatMessageListRes();

        List<ChatMessageRes> chatMessageResList = new ArrayList<>();
        for (int i = 0; i < chatMessageList.size(); i++) {
            ChatMessageRes res = new ChatMessageRes();
            res.setUserId(chatMessageList.get(i).getUserId().getId());
            res.setChatRoomId(chatMessageList.get(i).getChatRoom().getId());
            res.setMessage(chatMessageList.get(i).getMessage());
            res.setTime(chatMessageList.get(i).getCreateDate());

            chatMessageResList.add(res);
        }

        chatMessageListRes.setChatMessageList(chatMessageResList);

        return chatMessageListRes;
    }
}

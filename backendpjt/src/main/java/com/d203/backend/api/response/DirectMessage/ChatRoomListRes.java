package com.d203.backend.api.response.DirectMessage;

import com.d203.backend.db.entity.ChatJoinInfo;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("chatRoomList Response")
public class ChatRoomListRes {
    List<ChatRoomRes> chatRoomList;

    public static ChatRoomListRes of(List<ChatJoinInfo> chatJoininfoList) {
        ChatRoomListRes chatRoomListRes = new ChatRoomListRes();

        List<ChatRoomRes> chatRoomResList = new ArrayList<>();
        for (int i = 0; i < chatJoininfoList.size(); i++) {
            ChatRoomRes res = new ChatRoomRes();
            res.setChatRoomId(chatJoininfoList.get(i).getChatRoom().getId());
            res.setNickName(chatJoininfoList.get(i).getUserId().getNickname());
            res.setImgPath(chatJoininfoList.get(i).getUserId().getImgpath());

            chatRoomResList.add(res);
        }

        chatRoomListRes.setChatRoomList(chatRoomResList);

        return chatRoomListRes;
    }
}

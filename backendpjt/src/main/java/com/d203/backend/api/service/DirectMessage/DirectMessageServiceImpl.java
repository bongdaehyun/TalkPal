package com.d203.backend.api.service.DirectMessage;

import com.d203.backend.api.request.DirectMessage.ChatRoomPostReq;
import com.d203.backend.api.request.DirectMessage.DirectMessagePostReq;
import com.d203.backend.db.entity.ChatJoinInfo;
import com.d203.backend.db.entity.ChatMessage;
import com.d203.backend.db.entity.ChatRoom;
import com.d203.backend.db.entity.User;
import com.d203.backend.db.repository.ChatJoinInfoRepository;
import com.d203.backend.db.repository.ChatMessageRepository;
import com.d203.backend.db.repository.ChatRoomRepository;
import com.d203.backend.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("DirectMessageService")
public class DirectMessageServiceImpl implements DirectMessageService {

    @Autowired
    ChatJoinInfoRepository chatJoinInfoRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Long createChatRoom(ChatRoomPostReq chatRoomPostReq) {
        ChatJoinInfo tmp = chatJoinInfoRepository.isExist(chatRoomPostReq.getFromUserId(), chatRoomPostReq.getToUserId());
        System.out.println(tmp);
        if (tmp != null) {
            return null;
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setMsgCnt(0l);
        chatRoomRepository.save(chatRoom);

        ChatJoinInfo chatJoinInfo = new ChatJoinInfo();
        User fromUser = userRepository.findById(chatRoomPostReq.getFromUserId()).get();

        chatJoinInfo.setChatRoom(chatRoom);
        chatJoinInfo.setUserId(fromUser);
        chatJoinInfoRepository.save(chatJoinInfo);

        chatJoinInfo = new ChatJoinInfo();
        User toUser = userRepository.findById(chatRoomPostReq.getToUserId()).get();

        chatJoinInfo.setChatRoom(chatRoom);
        chatJoinInfo.setUserId(toUser);
        chatJoinInfoRepository.save(chatJoinInfo);

        return chatRoom.getId();
    }

    @Override
    public boolean sendDirectMessage(DirectMessagePostReq directMessagePostReq) {
        ChatMessage chatMessage = new ChatMessage();

        ChatRoom chatRoom = chatRoomRepository.findById(directMessagePostReq.getChatRoomId()).get();
        User user = userRepository.findById(directMessagePostReq.getUserId()).get();

        chatMessage.setChatRoom(chatRoom);
        chatMessage.setUserId(user);
        chatMessage.setMessage(directMessagePostReq.getMessage());

        chatRoom.setMsgCnt(chatRoom.getMsgCnt() + 1);

        chatMessageRepository.save(chatMessage);
        chatRoomRepository.save(chatRoom);

        return true;
    }

    @Override
    public List<ChatJoinInfo> getChatRoomList(Long userId) {
        List<ChatJoinInfo> chatRoomList = chatJoinInfoRepository.getChatRoomListByUserId(userId);
        return chatRoomList;
    }

    @Override
    public List<ChatMessage> getChatMessageList(Long chatRoomId) {
        List<ChatMessage> chatMessageList = chatMessageRepository.findAllByChatRoomIdOrderByCreateDate(chatRoomId);
        return chatMessageList;
    }

    @Override
    public Long getOpponentId(Long userId, Long chatRoomId) {
        Long opponentId = chatJoinInfoRepository.getOpponentId(userId, chatRoomId);
        return opponentId;
    }

    @Override
    public boolean deleteChatRoom(Long chatRoomId) {
        ChatRoom delChatRoom = chatRoomRepository.getOne(chatRoomId);
        System.out.println(delChatRoom.toString());
        chatRoomRepository.delete(delChatRoom);
        return true;
    }
}

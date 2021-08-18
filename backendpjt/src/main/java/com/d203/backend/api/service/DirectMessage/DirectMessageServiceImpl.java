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
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionManager;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;


@Service("DirectMessageService")
public class DirectMessageServiceImpl implements DirectMessageService {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ChatJoinInfoRepository chatJoinInfoRepository;

    @Autowired
    ChatMessageRepository chatMessageRepository;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public Long createChatRoom(ChatRoomPostReq chatRoomPostReq) {

        ChatJoinInfo tmp = chatJoinInfoRepository.isExist(chatRoomPostReq.getFromUserId(), chatRoomPostReq.getToUserId());
        if (tmp != null) {
            return null;
        }

        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setMsgCnt(0l);


        ChatJoinInfo chatJoinInfo = new ChatJoinInfo();
        User fromUser = userRepository.findById(chatRoomPostReq.getFromUserId()).get();
        chatJoinInfo.setUserId(fromUser);

        chatRoom.addJoinInfo(chatJoinInfo);


        chatJoinInfo = new ChatJoinInfo();
        User toUser = userRepository.findById(chatRoomPostReq.getToUserId()).get();
        chatJoinInfo.setUserId(toUser);

        chatRoom.addJoinInfo(chatJoinInfo);

        Session session = entityManager.unwrap(Session.class);
        session.save(chatRoom);


        return chatRoom.getId();
    }
    @Transactional
    @Override
    public boolean sendDirectMessage(DirectMessagePostReq directMessagePostReq) {
        ChatMessage chatMessage = new ChatMessage();


        Session session = entityManager.unwrap(Session.class);
        Long chatRoomId = directMessagePostReq.getChatRoomId();


        ChatRoom chatRoom = chatRoomRepository.getOne(directMessagePostReq.getChatRoomId());
        User user = userRepository.findById(directMessagePostReq.getUserId()).get();


        chatMessage.setUserId(user);
        chatMessage.setMessage(directMessagePostReq.getMessage());
        chatRoom.setMsgCnt(chatRoom.getMsgCnt() + 1);

        chatRoom.addMessages(chatMessage);

        session.save(chatRoom);

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

    @Transactional
    @Override
    public boolean deleteChatRoom(Long chatRoomId) {

        Session session = entityManager.unwrap(Session.class);
        ChatRoom chatroom = session.get(ChatRoom.class ,chatRoomId );
        session.delete(chatroom);

        return true;
    }
}

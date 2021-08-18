package com.d203.backend.db.repository;

import com.d203.backend.db.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findAllByChatRoomIdOrderByCreateDate(Long chatRoomId);

    @Modifying
    @Transactional
    @Query(value ="delete from chatmessage where chat_room_id = 1?" , nativeQuery = true)
    int chatRoomDelete(Long chatRoomId);
}

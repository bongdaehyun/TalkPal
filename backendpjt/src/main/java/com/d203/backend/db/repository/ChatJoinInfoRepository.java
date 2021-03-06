package com.d203.backend.db.repository;

import com.d203.backend.db.entity.ChatJoinInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatJoinInfoRepository extends JpaRepository<ChatJoinInfo, Long> {

    @Query(value = "select * " +
            "from chatjoininfo j join chatroom r " +
            "on j.chat_room_id = r.id " +
            "where j.chat_room_id in (select chat_room_id from chatjoininfo where user_id = ?1) and user_id != ?1 " +
            "order by r.last_modified_date desc", nativeQuery = true)
    List<ChatJoinInfo> getChatRoomListByUserId(Long userId);

    @Query(value = "select user_id from chatjoininfo where user_id != ?1 and chat_room_id = ?2", nativeQuery = true)
    Long getOpponentId(Long userId, Long chatRoomId);

    @Query(value = "select * " +
            "from chatjoininfo j1 " +
            "join chatjoininfo j2 " +
            "on j1.chat_room_id = j2.chat_room_id " +
            "where j1.user_id = ?1 and j2.user_id = ?2", nativeQuery = true)
    ChatJoinInfo isExist(Long fromUserId, Long toUserId);

    @Modifying
    @Transactional
    @Query(value ="delete from chatjoininfo where chat_room_id = 1?" , nativeQuery = true)
    int chatRoomDelete(Long chatRoomId);
}

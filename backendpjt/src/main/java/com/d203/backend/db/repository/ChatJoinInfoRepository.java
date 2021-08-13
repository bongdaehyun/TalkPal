package com.d203.backend.db.repository;

import com.d203.backend.db.entity.ChatJoinInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatJoinInfoRepository extends JpaRepository<ChatJoinInfo, Long> {

    @Query(value = "select * " +
            "from chatjoininfo j join chatroom r " +
            "on j.chat_room_id = r.id " +
            "where j.chat_room_id in (select chat_room_id from chatjoininfo where user_id = ?1) and user_id != ?1 " +
            "order by r.last_modified_date desc", nativeQuery = true)
    List<ChatJoinInfo> getChatRoomListByUserId(Long userId);
}

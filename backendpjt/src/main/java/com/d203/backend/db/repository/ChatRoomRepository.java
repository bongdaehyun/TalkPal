package com.d203.backend.db.repository;

import com.d203.backend.db.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Modifying
    @Transactional
    @Query(value ="delete from chatroom where id = 1?" , nativeQuery = true)
    int chatRoomDelete(Long chatRoomId);
}

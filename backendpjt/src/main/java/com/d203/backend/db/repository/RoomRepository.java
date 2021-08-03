package com.d203.backend.db.repository;

import java.util.List;
import java.util.Optional;

import com.d203.backend.db.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{

    Page<Room> findAllByNameLike(String name, Pageable pageable);
    Page<Room> findAllByTopic(String topic,Pageable pageable);
    @Query(value = "select * from room r where r.guest_lang = ?1", nativeQuery = true)
    Page<Room> findAllByGuest_lang(Long lang,Pageable pageable);
    @Query(value = "select * from room r where r.guest_lang = ?2 and r.topic LIKE %?1%", nativeQuery = true)
    Page<Room> findAllByTopicAndGuest_lang(String topic,Long lang,Pageable pageable);

    Room findByUuid(String uuid);
}

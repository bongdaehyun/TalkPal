package com.d203.backend.db.repository;

import java.util.List;
import java.util.Optional;

import com.d203.backend.db.entity.Room;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{

    List<Room> findAllByNameLike(String name);
    List<Room> findAllByTopic(String topic);
    @Query(value = "select r.* from room r where r.guest_lang = ?1", nativeQuery = true)
    List<Room> findAllByGuest_lang(Long lang);
}

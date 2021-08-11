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
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query(value = "select * from room r where r.curnum < r.maxnum and " +
            "r.guest_lang LIKE if(?2 = 0,'%',?2) and r.topic LIKE if" +
            "(?1 = '','%',?1) order by r.last_modified_date desc",
            nativeQuery = true)
    Page<Room> findAllBYLangAndTopic(String topic, Long lang, Pageable pageable);

    Room findByUuid(String uuid);
}

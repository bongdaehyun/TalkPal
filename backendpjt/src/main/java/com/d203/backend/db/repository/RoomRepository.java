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


}

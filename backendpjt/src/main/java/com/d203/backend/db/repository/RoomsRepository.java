package com.d203.backend.db.repository;

import com.d203.backend.db.entity.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomsRepository extends JpaRepository<Rooms, Long> {
}

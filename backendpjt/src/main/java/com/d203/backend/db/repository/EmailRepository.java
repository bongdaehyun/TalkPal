package com.d203.backend.db.repository;

import com.d203.backend.db.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailToken,Long> {
    EmailToken findByToken(String token);
}

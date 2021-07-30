package com.d203.backend.db.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.d203.backend.db.entity.Lang;
import com.d203.backend.db.entity.User;

@Repository
public interface LangRepository extends JpaRepository<Lang, Long>{
}

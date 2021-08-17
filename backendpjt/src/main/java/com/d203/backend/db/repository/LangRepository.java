package com.d203.backend.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.d203.backend.db.entity.Lang;

@Repository
public interface LangRepository extends JpaRepository<Lang, Long>{
    Lang findByName(String name);
}

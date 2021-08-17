package com.d203.backend.db.repository;

import com.d203.backend.db.entity.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByTouseridOrderByLastModifiedDateDesc(User touserid, Pageable pageable);

    Page<Review> findAllByFromuseridOrderByLastModifiedDateDesc(User fromuserid, Pageable pageable);

    @Query(value = "select round(avg(r.score),1)  from review as r where r.touserid = ?1", nativeQuery = true)
    double getReviewAvgByTouserid(Long touserid);

}


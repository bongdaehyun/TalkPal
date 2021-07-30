package com.d203.backend.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{


	 Page<Review> findAllByTouserid(User touserid, Pageable pageable);


	 Page<Review> findAllByFromuserid(User fromuserid , Pageable pageable);
	 
	 @Query(value= "select round(avg(score),1)  from review where review.touserid = ?1" ,nativeQuery = true)
	 double getReviewAvgByTouserid(Long touserid);
}


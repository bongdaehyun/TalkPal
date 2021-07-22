package com.d203.backend.db.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long>{

	@Query(value = "select p.* from review p where p.to_user_id = ?1", nativeQuery = true)
	 List<Review> findAllByTouserid(Long userid);

	
	@Query(value = "select p.* from review p where p.from_user_id = ?1", nativeQuery = true)
	List<Review> findAllByFromuserid(Long userid);
	 
	 
	   
}

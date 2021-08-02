package com.d203.backend.db.repository;

import com.d203.backend.db.entity.Follow;
import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {

    @Query(value= "select follow.*  from follow f where follow.fromuserid = ?1 and follow.touserid=?2" ,nativeQuery = true)
    Follow isExist(User fromUserId , User toUserid);

    Page<Follow> findAllByTouserid(User touserid, Pageable pageable);

    Page<Follow> findAllByFromuserid(User fromuserid , Pageable pageable);
}

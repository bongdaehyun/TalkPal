package com.d203.backend.db.repository;

import com.d203.backend.db.entity.Follow;
import com.d203.backend.db.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends JpaRepository<Follow,Long> {

    @Query(value= "select follow.*  from follow where follow.fromUserId = ?1 and follow.toUserId =?2" ,nativeQuery = true)
    boolean isExist(User fromUserId , User toUserid);
}

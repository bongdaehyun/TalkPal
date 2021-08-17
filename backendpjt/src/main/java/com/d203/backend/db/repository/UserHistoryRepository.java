package com.d203.backend.db.repository;

import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;
import com.d203.backend.db.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

    @Query(value = "select p.* from userhistory p where p.from_user_id = ?1 order by p.create_date desc limit 10", nativeQuery = true)
    List<UserHistory> findAllByFrom_user_id(Long userid);

    @Query(value = "select  COUNT(*) from userhistory p where p" +
            ".from_user_id = ?1", nativeQuery = true)
    Long countAll(User userid);

    @Query(value = "select userhistory.*  from userhistory  where userhistory.fromuserid = ?1 and userhistory.touserid=?2", nativeQuery = true)
    Optional<UserHistory> isExist(User fromUserId, User toUserid);
}

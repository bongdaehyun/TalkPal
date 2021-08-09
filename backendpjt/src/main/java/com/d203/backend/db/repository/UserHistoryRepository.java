package com.d203.backend.db.repository;

import com.d203.backend.db.entity.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistory,Long> {

    @Query(value = "select p.* from userhistory p where p.from_user_id = ?1  order by p.create_date desc limit 10", nativeQuery = true)
    List<UserHistory> findAllByFrom_user_id(Long userid);

}

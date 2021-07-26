package com.d203.backend.api.service.history;

import com.d203.backend.db.entity.UserHistory;
import com.d203.backend.db.repository.UserHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("historyService")
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private UserHistoryRepository repo;

    @Override
    public List<UserHistory> getMeetPeople(Long userid) {

        List<UserHistory> result= repo.findAllByFrom_user_id(userid);
        System.out.println("========="+result.size());
        return result;
    }
}

package com.d203.backend.api.service.history;

import com.d203.backend.db.entity.User;
import com.d203.backend.db.entity.UserHistory;
import com.d203.backend.db.repository.UserHistoryRepository;
import com.d203.backend.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("historyService")
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private UserHistoryRepository repo;

    @Autowired
    private UserRepository urepo;

    @Override
    public List<UserHistory> getMeetPeople(Long userid) {

        List<UserHistory> result= repo.findAllByFrom_user_id(userid);
        return result;
    }

    @Override
    public boolean addHistory(Long fromid,Long toid) {
        
        Optional<User> fuser = urepo.findById(fromid);
        Optional<User> tuser = urepo.findById(toid);

        if(fuser.isPresent() && tuser.isPresent()){
            UserHistory userHistory=new UserHistory();
            userHistory.setFrom_user_id(fuser.get());
            userHistory.setTo_user_id(tuser.get());
            repo.save(userHistory);
            return true;
        }
        else{
            return false;
        }
    }
}

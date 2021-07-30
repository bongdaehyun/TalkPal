package com.d203.backend.api.service.follow;


import com.d203.backend.db.entity.Follow;
import com.d203.backend.db.entity.User;
import com.d203.backend.db.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("followService")
public class FollowServiceImpl implements FollowService{

    @Autowired
    FollowRepository followRepository;

    @Override
    public boolean followRegist(User fromUserId, User toUserId) {

        if(followRepository.isExist(fromUserId,toUserId))
        {
            //이미 요청한 것이 존재한다.
            return false;
        }
        Follow follow = new Follow();

        follow.setFromUserId(fromUserId);
        follow.setToUserId(fromUserId);
        follow.setAccept(false);

        followRepository.save(follow);

        return true;
    }
}

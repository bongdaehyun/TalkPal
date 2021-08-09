package com.d203.backend.api.service.follow;


import com.d203.backend.db.entity.Follow;
import com.d203.backend.db.entity.User;
import com.d203.backend.db.repository.FollowRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("followService")
public class FollowServiceImpl implements FollowService {

    @Autowired
    FollowRepository followRepository;

    @Override
    public boolean followRegist(User fromUserId, User toUserId) {

        Follow dupcheck = followRepository.isExist(fromUserId, toUserId);
        if (dupcheck != null) return false;

        Follow follow = new Follow();
        follow.setFromuserid(fromUserId);
        follow.setTouserid(toUserId);
        followRepository.save(follow);

        return true;
    }

    @Override
    public void followDelete(User fromUserId, User toUserId) {
        Follow follow = followRepository.isExist(fromUserId, toUserId);
        System.out.println("Follow Del" + follow.toString());
        followRepository.delete(follow);
    }

    @Override
    public Page<Follow> getFollowList(User userid, int pageno) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageno - 1, 20);
        return followRepository.findAllByFromuserid(userid, firstPageWithTwoElements);
    }

    @Override
    public Page<Follow> getFollowerList(User userid, int pageno) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageno - 1, 10);
        return followRepository.findAllByTouserid(userid, firstPageWithTwoElements);
    }

    @Override
    public boolean checkFollowing(User myid, User toId) {
        Follow follow = followRepository.isExist(myid, toId);
        if (follow == null) return true;
        else return false;
    }

    @Override
    public Long getCountFollower(User myid) {
        Long num= followRepository.countAllByTouserid(myid);
        return num;
    }

    @Override
    public Long getCountFollowing(User myid) {
        Long num=followRepository.countAllByFromuserid(myid);
        return num;
    }


}

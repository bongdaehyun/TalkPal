package com.d203.backend.api.service.follow;

import com.d203.backend.db.entity.User;

public interface FollowService {
    //팔로우 신청 서비스
    boolean followRegist(User fromUserId , User toUserId);
}

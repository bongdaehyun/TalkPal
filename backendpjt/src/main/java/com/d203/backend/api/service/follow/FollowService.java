package com.d203.backend.api.service.follow;

import com.d203.backend.db.entity.Follow;
import com.d203.backend.db.entity.User;
import org.springframework.data.domain.Page;

public interface FollowService {
    //팔로우 신청 서비스
    boolean followRegist(User fromUserId, User toUserId);

    void followDelete(User fromUserId, User toUserId);

    Page<Follow> getFollowList(User userid, int pageno);

    Page<Follow> getFollowerList(User userid, int pageno);

    boolean checkFollowing(User myid, User toId);

    Long getCountFollower(User myid);

    Long getCountFollowing(User myid);
}

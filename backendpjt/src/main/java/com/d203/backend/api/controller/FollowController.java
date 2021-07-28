package com.d203.backend.api.controller;


import com.d203.backend.api.request.Review.ReviewResiterReq;
import com.d203.backend.api.service.User.UserService;
import com.d203.backend.api.service.follow.FollowService;
import com.d203.backend.common.model.response.BaseResponseBody;
import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(value = "팔로우 팔로워 API", tags = {"History"})
@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    //팔로우 요청
    @PostMapping("/{fromUserId}/{toUserId}")
    @ApiOperation(value = "팔로우 신청 ", notes = "팔로우 신청 from -> to")
    public ResponseEntity<? extends BaseResponseBody> followRegist(@PathVariable Long fromUserId,@PathVariable Long toUserId)
    {
        User fromUser = userService.getUserByuserId(fromUserId);
        User toUser = userService.getUserByuserId(toUserId);

        boolean isOk = followService.followRegist(fromUser,toUser);

        if(isOk)
        {
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        }

       return ResponseEntity.status(401).body(BaseResponseBody.of(401, "Dup Request"));
    }

    //팔로우 요청취소


    //팔로우 수락


    //팔로우 거절

    //팔로우 끊기

    //팔로워 대기 목록

    //팔로워 목록

    //팔로우 대기목록


    //팔로우 목록
}

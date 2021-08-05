package com.d203.backend.api.controller;


import com.d203.backend.api.request.Review.ReviewResiterReq;
import com.d203.backend.api.response.Follow.FollowListRes;
import com.d203.backend.api.response.Review.ReviewListRes;
import com.d203.backend.api.service.User.UserService;
import com.d203.backend.api.service.follow.FollowService;
import com.d203.backend.common.model.response.BaseResponseBody;
import com.d203.backend.db.entity.Follow;
import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "팔로우 팔로워 API", tags = {"Follow"})
@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    //팔로우
    @PostMapping("/{fromUserId}/{toUserId}")
    @ApiOperation(value = "팔로우 신청 ", notes = "팔로우 신청 from -> to")
    public ResponseEntity<? extends BaseResponseBody> followRegist(@PathVariable Long fromUserId, @PathVariable Long toUserId) {
        User fromUser = userService.getUserByuserId(fromUserId);
        User toUser = userService.getUserByuserId(toUserId);

        boolean isOk = followService.followRegist(fromUser, toUser);

        if (isOk) {
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        }

        return ResponseEntity.status(401).body(BaseResponseBody.of(401, "Dup Request"));
    }


    //언팔로우
    @DeleteMapping("/{fromUserId}/{toUserId}")
    @ApiOperation(value = "팔로우 해제 ", notes = "팔로우 해제 from -> to")
    public ResponseEntity<? extends BaseResponseBody> followDelete(@PathVariable Long fromUserId, @PathVariable Long toUserId) {

        User fromUser = userService.getUserByuserId(fromUserId);
        User toUser = userService.getUserByuserId(toUserId);
        followService.followDelete(fromUser, toUser);

        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }

    //팔로우 목록 가져오기
    @GetMapping("/ing/{userId}/{pageno}")
    @ApiOperation(value = "팔로우 목록 ", notes = "팔로우 목록 ")
    public ResponseEntity<FollowListRes> getFollowList(@PathVariable Long userId, @PathVariable int pageno) {
        User user = userService.getUserByuserId(userId);
        Page<Follow> firstPage = followService.getFollowList(user, pageno);
        List<Follow> following = firstPage.getContent();

        return ResponseEntity.status(200).body(FollowListRes.getIngList(following));
    }

    //팔로워 목록 가져오기
    @GetMapping("er/{userId}/{pageno}")
    @ApiOperation(value = "팔로워 목록 ", notes = "팔로워 목록 ")
    public ResponseEntity<FollowListRes> getFollowerList(@PathVariable Long userId, @PathVariable int pageno) {

        User user = userService.getUserByuserId(userId);
        System.out.println("ReqUser : try" + user.toString());

        Page<Follow> firstPage = followService.getFollowerList(user, pageno);


        List<Follow> follower = firstPage.getContent();
        System.out.println("ReqUser : try" + follower.toString());
        return ResponseEntity.status(200).body(FollowListRes.getErList(follower));
    }

    @GetMapping("/checkFollowing/{fromuserid}/{touserid}")
    @ApiOperation(value = "팔로잉 채크하기", notes = "팔로워 목록 ")
    public boolean checkFollowing(@PathVariable Long fromuserid, @PathVariable Long touserid) {


        System.out.println("Chek Following : try" + fromuserid + "   " + touserid);

        User myId = userService.getUserByuserId(fromuserid);
        User toId = userService.getUserByuserId(touserid);

        return followService.checkFollowing(myId, toId);
    }

    //팔로우 카운트
    @GetMapping("/countfollowing/{userId}")
    @ApiOperation(value = "팔로우 개수 ", notes = "팔로우 목록 ")
    public ResponseEntity<?> getCountFollow(@PathVariable Long userId ) {
        User user = userService.getUserByuserId(userId);
        //System.out.println("count : try" + user.toString());
        Long num=followService.getCountFollowing(user);
        return ResponseEntity.status(200).body(num);
    }

    //팔로워 카운트
    @GetMapping("/countfollower/{userId}")
    @ApiOperation(value = "팔로워 개수 ", notes = "팔로우 목록 ")
    public ResponseEntity<?> getCountFollowing(@PathVariable Long userId) {
        User user = userService.getUserByuserId(userId);
        //System.out.println("count : try" + user.toString());
        Long num=followService.getCountFollower(user);
        return ResponseEntity.status(200).body(num);

    }
}

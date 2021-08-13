package com.d203.backend.api.controller;

import java.util.List;

import com.d203.backend.api.response.Review.ReviewAvgRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d203.backend.api.request.Review.ReviewResiterReq;
import com.d203.backend.api.request.Review.ReviewUpdateReq;
import com.d203.backend.api.response.Review.ReviewListRes;
import com.d203.backend.api.service.Review.ReviewService;
import com.d203.backend.api.service.User.UserService;
import com.d203.backend.common.auth.SsafyUserDetails;
import com.d203.backend.common.model.response.BaseResponseBody;
import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "유저간 ReView API", tags = {"Review"})
@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {
    //CRUD
    @Autowired
    ReviewService reviewService;

    @Autowired
    UserService userService;

    @PostMapping()
    @ApiOperation(value = "리뷰작성 ", notes = "<strong>유저간 리뷰와 점수<strong>를 작성한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<? extends BaseResponseBody> register(
            @RequestBody @ApiParam(value = "평가 작성 정보", required = true) ReviewResiterReq reviewInfo) {

        Review review = reviewService.createReview(reviewInfo);
        return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
    }

    @GetMapping("/from/{from_user_id}/{pageno}")
    @ApiOperation(value = "작성한 리뷰 조회", notes = "요청하는 유저 pk값에 대응하는 작성한 리뷰를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<ReviewListRes> getReviewFrom(@PathVariable Long from_user_id, @PathVariable int pageno) {
        User user = userService.getUserByuserId(from_user_id);

        Page<Review> firstPage = reviewService.getWriteReviewById(user, pageno);

        List<Review> review = firstPage.getContent();

        return ResponseEntity.status(200).body(ReviewListRes.getlist(review));
    }

    @GetMapping("/to/{to_user_id}/{pageno}")
    @ApiOperation(value = "받은 리뷰 조회", notes = "요청하는 유저 pk값에 대응하는 작성된 리뷰를 조회한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<ReviewListRes> getReviewTo(@PathVariable Long to_user_id, @PathVariable int pageno) {

        User user = userService.getUserByuserId(to_user_id);

        Page<Review> firstPage = reviewService.getReviewById(user, pageno);
        List<Review> review = firstPage.getContent();

        return ResponseEntity.status(200).body(ReviewListRes.getlist(review));
    }

    @PutMapping("{review_id}")
    @ApiOperation(value = "리뷰 작성 수정", notes = "review의 pk 값을 받아와 수정한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<?> updateReview(
            @RequestBody @ApiParam(value = "평가 작성 수정 정보", required = true) ReviewUpdateReq updateReviewInfo,
            @PathVariable Long review_id) {

        if (reviewService.updateReview(review_id, updateReviewInfo)) {
            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
        }

        return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{review_id}")
    public ResponseEntity<?> deleteReview(
            @PathVariable Long review_id,
            @ApiIgnore Authentication authentication
    ) {

        SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
        Long tokenUserId = userDetails.getUser().getId();
        if (reviewService.deleteReview(review_id, tokenUserId)) {
            return ResponseEntity.status(204).body("Review delete success");
        }

        return ResponseEntity.status(401).body("User not macthed");
    }
    
}

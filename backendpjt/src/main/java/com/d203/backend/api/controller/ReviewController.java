package com.d203.backend.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d203.backend.api.request.ReviewResiterReq;
import com.d203.backend.api.response.ReviewItemRes;
import com.d203.backend.api.response.ReviewListRes;
import com.d203.backend.api.response.UserRes;
import com.d203.backend.api.service.ReviewService;
import com.d203.backend.api.service.UserService;
import com.d203.backend.common.model.response.BaseResponseBody;
import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
	@ApiOperation(value = "리뷰작성 ", notes = "<strong>유저간 리뷰와 점수strong>를 작성한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value="평가 작성 정보", required = true) ReviewResiterReq reviewInfo) {
			
		Review review = reviewService.createReview(reviewInfo);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
		
	}
			
	
	@GetMapping("{user_id}")
	@ApiOperation(value = "리뷰 조회", notes = "요청하는 유저 pk값에 대응하는 리뷰를 조회한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<ReviewListRes> getReview (@PathVariable Long user_id) {
		
		System.out.println("ReqUser : try" );
		User user = userService.getUserByuserId(user_id);
		System.out.println("ReqUser : " + user.toString());
		
		System.out.println("ReqUser : " + user.getId().longValue());
		
		List<Review> review = reviewService.getReviewById(user.getId().longValue());
		
		return ResponseEntity.status(200).body(ReviewListRes.getlist(review));
		  
	}
	
	
}

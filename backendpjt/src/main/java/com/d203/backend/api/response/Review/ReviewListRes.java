package com.d203.backend.api.response.Review;

import java.util.*;
import java.util.List;

import com.d203.backend.db.entity.Review;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ReviewListResponse")
public class ReviewListRes {

	
	@ApiModelProperty(name="Review List")
	List<ReviewItemRes> reviewList;

	
	
	
	public static ReviewListRes getlist(List<Review> review) {

		ReviewListRes reviewListRes = new ReviewListRes();

		List<ReviewItemRes> resList = new ArrayList<ReviewItemRes>();
		for (int i = 0; i < review.size(); i++) {
			
			ReviewItemRes res = new ReviewItemRes();
			// 응답 변수에 review_id 추가
			res.setId(review.get(i).getId());
			res.setContent(review.get(i).getContent());
			
			res.setFrom_user_id(review.get(i).getFromuserid().getId());
			res.setFrom_user_nickname(review.get(i).getFromuserid().getNickname());
			
			res.setTo_user_id(review.get(i).getTouserid().getId());
			res.setTo_user_nickname(review.get(i).getTouserid().getNickname());
			
			res.setScore(review.get(i).getScore());

			
			resList.add(res);
			
		}


		reviewListRes.setReviewList(resList);
		

		System.out.println("List : "+ reviewListRes.getReviewList().toString());
		return reviewListRes;
		
	}
}

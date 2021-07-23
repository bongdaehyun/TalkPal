package com.d203.backend.api.response;

import java.util.*;
import java.util.List;
import java.util.Optional;

import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("ReviewListResponse")
public class ReviewListRes {

	
	@ApiModelProperty(name="score")
	double avgScore;
	
	
	@ApiModelProperty(name="Review List")
	List<ReviewItemRes> reviewList;

	
	
	
	public static ReviewListRes getlist(List<Review> review) {

		ReviewListRes reviewListRes = new ReviewListRes();
		double avg = 0;
		List<ReviewItemRes> resList = new ArrayList<ReviewItemRes>();
		for (int i = 0; i < review.size(); i++) {
			
			ReviewItemRes res = new ReviewItemRes();
			// 응답 변수에 review_id 추가
			res.setId(review.get(i).getId());
			res.setContent(review.get(i).getContent());
			res.setFrom_user_id(review.get(i).getFromuserid().getId());
			res.setTo_user_id(review.get(i).getTouserid().getId());
			res.setScore(review.get(i).getScore());
			avg+= review.get(i).getScore();
			
			resList.add(res);
			
		}
		avg /= review.size();
		reviewListRes.setAvgScore(avg);
		reviewListRes.setReviewList(resList);
		
		System.out.println("Avg : " + reviewListRes.getAvgScore());
		System.out.println("List : "+ reviewListRes.getReviewList().toString());
		return reviewListRes;
		
	}
}

package com.d203.backend.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.d203.backend.api.request.ReviewResiterReq;
import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.User;
import com.d203.backend.db.repository.ReviewRepository;
import com.d203.backend.db.repository.UserRepository;

@Service("reviewService")
public class ReviewServiceImpl implements ReviewService{

	@Autowired
	UserRepository userRepository;

	@Autowired
	ReviewRepository reviewRepository;

	
	//리뷰 작성 
	@Override 
	public Review createReview(ReviewResiterReq reviewInfo) {
		// TODO Auto-generated method stub
		Review review = new Review();
		review.setContent(reviewInfo.getContent());
		
		User fromUser =userRepository.findByEmail(reviewInfo.getFrom_user_id()).get();
		System.out.println("fromUser : "+ fromUser.getEmail() + " " + fromUser.getId());
		review.setFromuserid(fromUser );
		
		User toUser =userRepository.findByEmail(reviewInfo.getTo_user_id()).get();
		review.setTouserid(toUser);
		System.out.println("toUser : "+ toUser.getEmail() + " " + toUser.getId());
		
		review.setScore(reviewInfo.getScore());
		
		return reviewRepository.save(review);
	}

	//이메일로 리뷰 조회
	@Override
	public List<Review> getReviewById(Long userid) {
		
		System.out.println("Review Service : try userid: " +" "+ userid);
		
		List<Review> review = reviewRepository.findAllByTouserid(userid);
		
		System.out.println("Review Service : done reviewSize()" +" "+ review.size());
		return review;
	}

}

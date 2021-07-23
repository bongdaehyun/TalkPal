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
		
		Optional<User> fromUser =userRepository.findById(reviewInfo.getFrom_user_id());
		System.out.println("fromUser : "+ fromUser.get().getEmail() + " " + fromUser.get().getId());
		review.setFromuserid(fromUser.get() );
		
		Optional<User> toUser =userRepository.findById(reviewInfo.getTo_user_id());
		review.setTouserid(toUser.get());
		System.out.println("toUser : "+ toUser.get().getEmail() + " " + toUser.get().getId());
		
		review.setScore(reviewInfo.getScore());
		
		return reviewRepository.save(review);
	}

	//유저PK 리뷰 조회
	@Override
	public List<Review> getReviewById(Long userid) {
		
		System.out.println("Review Service : try userid: " +" "+ userid);
		
		List<Review> review = reviewRepository.findAllByTouserid(userid);
		
		System.out.println("Review Service : done reviewSize()" +" "+ review.size());
		return review;
	}

}

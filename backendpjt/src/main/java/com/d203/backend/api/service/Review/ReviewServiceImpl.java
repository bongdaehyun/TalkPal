package com.d203.backend.api.service.Review;

import java.util.List;
import java.util.Optional;

import com.d203.backend.db.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.d203.backend.api.request.Review.ReviewResiterReq;
import com.d203.backend.api.request.Review.ReviewUpdateReq;
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
	public Page<Review> getReviewById(User touserid,int pageno) {
		
		System.out.println("Review Service : try userid: " +" "+ touserid);
		Pageable  firstPageWithTwoElements = PageRequest.of(pageno-1, 5);
		return reviewRepository.findAllByTouserid(touserid,firstPageWithTwoElements);
	}

	//
	@Override
	public boolean updateReview(Long reviewId, ReviewUpdateReq reviwUpdateInfo) {
		
		Optional<Review> review = reviewRepository.findById(reviewId);
		
		Review updateReview = review.get();
		
		updateReview.setContent(reviwUpdateInfo.getContent());
		updateReview.setScore(reviwUpdateInfo.getScore());
		
		if(review.isPresent()) {
			reviewRepository.save(updateReview);
			return true;
		}
		
		return false;
	}

	@Override
	public boolean deleteReview(Long review_id, Long tokenUserId) {
		
		Review delReview = reviewRepository.getOne(review_id);
		Long isAcceptId = delReview.getFromuserid().getId();
		
		if(isAcceptId == tokenUserId)
		{
			reviewRepository.delete(delReview);	
			return true;
		}
		return false;
	}

	@Override
	public double avgReview(Long touserid) {

		double reviewAvg = 0;
		System.out.println("Review Service : try userid: " +" "+ touserid);
		reviewAvg = reviewRepository.getReviewAvgByTouserid(touserid);
		System.out.println("Review Done : : " +" "+ reviewAvg);
		return reviewAvg;
	}

	//요청한 사용자가 작성한 리뷰 보기
	@Override
	public Page<Review> getWriteReviewById(User fromuserid , int pageno) {
		System.out.println("Review Service : try userid: " +" "+ fromuserid);


		Pageable firstPageWithTwoElements = PageRequest.of(pageno-1, 5);

		System.out.println("Review Service" +" "+ firstPageWithTwoElements.getPageSize());

		return reviewRepository.findAllByFromuserid(fromuserid ,firstPageWithTwoElements);
	}
	
}

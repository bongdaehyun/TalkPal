package com.d203.backend.api.service.Review;

import java.util.List;

import com.d203.backend.api.request.Review.ReviewResiterReq;
import com.d203.backend.api.request.Review.ReviewUpdateReq;
import com.d203.backend.db.entity.Review;

public interface ReviewService {

	Review createReview(ReviewResiterReq ReviewInfo);

	List<Review> getReviewById(Long userid);

	List<Review> getWriteReviewById(Long userid);
	
	boolean updateReview(Long reviewId, ReviewUpdateReq updateReviewInfo);

	boolean deleteReview(Long review_id, Long tokenUserId);

	
}

package com.d203.backend.api.service.Review;

import java.util.List;

import com.d203.backend.api.request.Review.ReviewResiterReq;
import com.d203.backend.api.request.Review.ReviewUpdateReq;
import com.d203.backend.db.entity.Review;
import com.d203.backend.db.entity.Room;
import com.d203.backend.db.entity.User;
import org.springframework.data.domain.Page;

public interface ReviewService {

	Review createReview(ReviewResiterReq ReviewInfo);

	Page<Review> getReviewById(User fromuserid , int pageno);

	Page<Review> getWriteReviewById(User touserid , int pageno);
	
	boolean updateReview(Long reviewId, ReviewUpdateReq updateReviewInfo);

	boolean deleteReview(Long review_id, Long tokenUserId);

}

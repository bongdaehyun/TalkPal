package com.d203.backend.api.service;

import java.util.List;
import java.util.Optional;

import com.d203.backend.api.request.ReviewResiterReq;
import com.d203.backend.db.entity.Review;

public interface ReviewService {

	Review createReview(ReviewResiterReq ReviewInfo);

	List<Review> getReviewById(Long userid);

	
}
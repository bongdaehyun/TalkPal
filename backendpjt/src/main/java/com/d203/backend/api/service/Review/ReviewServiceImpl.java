package com.d203.backend.api.service.Review;

import java.util.Optional;

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
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ReviewRepository reviewRepository;


    //리뷰 작성
    @Override
    public Review createReview(ReviewResiterReq reviewInfo) {
        // TODO Auto-generated method stub
        Review review = new Review();

        // 리뷰 작성 유저
        User fromUser = userRepository.findById(reviewInfo.getFrom_user_id()).get();
        review.setFromuserid(fromUser);

        // 리뷰 받는 유저
        User toUser = userRepository.findById(reviewInfo.getTo_user_id()).get();
        review.setTouserid(toUser);

        // 리뷰 내용
        review.setContent(reviewInfo.getContent());

        // 리뷰 점수
        review.setScore(reviewInfo.getScore());

        // 리뷰 받는 유저의 평균 평점 ?
        // toUser.setAvgScore(reviewRepository.getReviewAvgByTouserid(toUser
        // .getId()));

        userRepository.save(toUser);

        return reviewRepository.save(review);
    }

    // 받은 리뷰
    @Override
    public Page<Review> getReviewById(User touserid, int pageno) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageno - 1, 5);
        return reviewRepository.findAllByTouseridOrderByLastModifiedDateDesc(touserid, firstPageWithTwoElements);
    }

    // 작성한 리뷰
    @Override
    public Page<Review> getWriteReviewById(User fromuserid, int pageno) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageno - 1, 5);
        return reviewRepository.findAllByFromuseridOrderByLastModifiedDateDesc(fromuserid, firstPageWithTwoElements);
    }

    //
    @Override
    public boolean updateReview(Long reviewId, ReviewUpdateReq reviwUpdateInfo) {
        Optional<Review> review = reviewRepository.findById(reviewId);

        Review updateReview = review.get();

        updateReview.setContent(reviwUpdateInfo.getContent());
        updateReview.setScore(reviwUpdateInfo.getScore());

        if (review.isPresent()) {
            reviewRepository.save(updateReview);
            return true;
        }

        return false;
    }

    @Override
    public boolean deleteReview(Long review_id, Long tokenUserId) {

        Review delReview = reviewRepository.getOne(review_id);
        Long isAcceptId = delReview.getFromuserid().getId();

        if (isAcceptId == tokenUserId) {
            reviewRepository.delete(delReview);
            return true;
        }
        return false;
    }


}

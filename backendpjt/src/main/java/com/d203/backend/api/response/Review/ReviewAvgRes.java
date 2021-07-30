package com.d203.backend.api.response.Review;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ApiModel("ReviewAvgResponse")
@ToString
public class ReviewAvgRes {
    @ApiModelProperty(name="score")
    double score;

    public static ReviewAvgRes of(double avg)
    {
        ReviewAvgRes reviewAvgRes = new ReviewAvgRes();
        reviewAvgRes.setScore(avg);
        return reviewAvgRes;
    }
}

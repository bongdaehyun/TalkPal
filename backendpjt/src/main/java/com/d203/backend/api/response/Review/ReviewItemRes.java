package com.d203.backend.api.response.Review;

import java.util.List;

import com.d203.backend.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ApiModel("ReviewResponse")
@ToString
public class ReviewItemRes {
    // 응답변수에 review_id 추가
    @ApiModelProperty(name = "review_id")
    Long id;

    @ApiModelProperty(name = "score")
    double score;

    @ApiModelProperty(name = "from_user_id")
    Long from_user_id;

    @ApiModelProperty(name = "from_user_nickname")
    String from_user_nickname;

    @ApiModelProperty(name = "from_user_profile")
    String from_user_profile;

    @ApiModelProperty(name = "to_user_id")
    Long to_user_id;

    @ApiModelProperty(name = "to_user_id")
    String to_user_nickname;

    @ApiModelProperty(name = "to_user_profile")
    String to_user_profile;


    @ApiModelProperty(name = "content")
    String content;

}

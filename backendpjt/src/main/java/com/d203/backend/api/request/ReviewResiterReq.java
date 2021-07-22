package com.d203.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel("ReviewResiterRequest")
public class ReviewResiterReq {
	
	@ApiModelProperty(name="작성하는 유저 email", example="ssafy_web")
	String to_user_id;
	
	@ApiModelProperty(name="작성받는 유저 email", example="ssafy_web")
	String from_user_id;
	
	@ApiModelProperty(name="평가하는 유저의 점수", example="4.5")
	Long score;
	
	@ApiModelProperty(name="유저의 평가내용", example="~~ 좋았습니다.")
	String content;

}

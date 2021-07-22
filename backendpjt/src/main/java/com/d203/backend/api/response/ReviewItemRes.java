package com.d203.backend.api.response;

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

	@ApiModelProperty(name="review_id")
	Long id;
	
	@ApiModelProperty(name="score")
	double score;
	
	@ApiModelProperty(name="from_user_id")
	Long from_user_id;
	
	
	@ApiModelProperty(name="to_user_id")
	Long to_user_id;
	
	
	@ApiModelProperty(name="content")
	String content;
	
}

package com.d203.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
/**
 * 유저 삭제/탈퇴  API ) 요청에 필요한 리퀘스트 바디 정의.
 */
@Getter
@Setter
@ApiModel("UserDeleteRequest")
public class UserDeleteReq {

	@ApiModelProperty(name="유저 email", example="ssafy_web")
	String email;
	@ApiModelProperty(name="유저 Password", example="your_password")
	String password;

}

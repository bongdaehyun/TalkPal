package com.d203.backend.api.response;

import com.d203.backend.common.model.response.BaseResponseBody;
import com.d203.backend.db.entity.User;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 회원 본인 정보 조회 API ([GET] /api/v1/users/me) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("UserResponse")
public class UserRes{
	@ApiModelProperty(name="User ID")
	String email;
	
	@ApiModelProperty(name="User lang")
	String lang;
	
	@ApiModelProperty(name="User nickname")
	String nickname;


	public static UserRes of(User user) {
		UserRes res = new UserRes();
		res.setEmail(user.getEmail());
		//res.setLang(user.getLang());
		res.setNickname(user.getNickname());
		return res;
	}
}

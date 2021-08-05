package com.d203.backend.api.request.User;

import org.springframework.web.multipart.MultipartFile;

import com.d203.backend.db.entity.Lang;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel("UserRequest")
public class UserReq {
	
	@ApiModelProperty(name="유저 pk id", example="1")
	String id;
	@ApiModelProperty(name="유저 email", example="ssafy_web")
	String email;
	@ApiModelProperty(name="유저 Password", example="your_password")
	String password;
	@ApiModelProperty(name="유저 language", example="your_language")
	Long lang;
	@ApiModelProperty(name="유저 nickname", example="your_nickname")
	String nickname;

	@ApiModelProperty(name="유저 sns", example="your_sns")
	String sns;
	@ApiModelProperty(name="유저 자기소개", example="your_introduction")
	String introduction;

}

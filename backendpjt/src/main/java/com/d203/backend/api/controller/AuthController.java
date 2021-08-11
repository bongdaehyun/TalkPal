package com.d203.backend.api.controller;

import com.d203.backend.api.service.Auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.d203.backend.api.request.User.UserLoginReq;
import com.d203.backend.api.response.User.UserLoginPostRes;
import com.d203.backend.api.service.User.UserService;
import com.d203.backend.common.model.response.BaseResponseBody;
import com.d203.backend.db.entity.User;
import com.d203.backend.util.JwtTokenUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

/**
 * 인증 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "인증 API", tags = {"Auth."})
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	@Autowired
	UserService userService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes = "<strong>아이디와 패스워드</strong>를 통해 로그인 한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공", response = UserLoginPostRes.class),
			@ApiResponse(code = 401, message = "잘못된 비밀번호", response = BaseResponseBody.class),
			@ApiResponse(code = 404, message = "사용자 없음", response = BaseResponseBody.class),
			@ApiResponse(code = 500, message = "서버 오류", response = BaseResponseBody.class)
	})
	public ResponseEntity<UserLoginPostRes> login(@RequestBody @ApiParam(value="로그인 정보", required = true) UserLoginReq loginInfo) {
		String email = loginInfo.getEmail();
		String password = loginInfo.getPassword();

		User user = userService.getUserByEamil(email);
		if(user == null){
			return ResponseEntity.status(404).body(UserLoginPostRes.of(404,
					"NO " +
					"USER", null));
		}

		// 로그인 요청한 유저로부터 입력된 패스워드 와 디비에 저장된 유저의 암호화된 패스워드가 같은지 확인.(유효한 패스워드인지 여부 확인)
		if(passwordEncoder.matches(password, user.getPassword())) {
			if (user.getValid()){
				return ResponseEntity.ok(UserLoginPostRes.of(200, "Success", JwtTokenUtil.getToken(user)));
			}else{
				return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "check yout email", null));
			}
		}else{
			// 유효하지 않는 패스워드인 경우, 로그인 실패로 응답.
			return ResponseEntity.status(401).body(UserLoginPostRes.of(401, "Invalid Password", null));
		}
	}


	@PostMapping("/send/{email}")
	public ResponseEntity<?> sendEmail(@PathVariable String email){
		
		try{
			User user=userService.getUserByEamil(email);
			authService.sendVerificationMail(user);
			//System.out.println("sendmail d--- Success");
			return new ResponseEntity<String>("성공",HttpStatus.OK);
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity<String>("오류", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("confirm/{token}")
	public ResponseEntity<?> checkEmail(@PathVariable String token){
		try {
			authService.verifyEmail(token);
			return new ResponseEntity<String>("회원 인증 성공!!!!!!", HttpStatus.OK);
		}catch (Exception e){
			return new ResponseEntity<String>("회원 인증에 실패!?!!!", HttpStatus.BAD_REQUEST);
		}
	}
}


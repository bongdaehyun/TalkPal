package com.d203.backend.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.d203.backend.api.request.UserLoginPostReq;
import com.d203.backend.api.request.UserRegisterPostReq;
import com.d203.backend.api.response.UserLoginPostRes;
import com.d203.backend.api.response.UserRes;
import com.d203.backend.api.service.UserService;
import com.d203.backend.common.auth.SsafyUserDetails;
import com.d203.backend.common.model.response.BaseResponseBody;
import com.d203.backend.db.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 유저 관련 API 요청 처리를 위한 컨트롤러 정의.
 */
@Api(value = "유저 API", tags = {"User"})
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping()
	@ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value="회원가입 정보", required = true) UserRegisterPostReq registerInfo) {

		//임의로 리턴된 User 인스턴스. 현재 코드는 회원 가입 성공 여부만 판단하기 때문에 굳이 Insert 된 유저 정보를 응답하지 않음.

	   User user = userService.createUser(registerInfo);
	   return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
		
		
	}
	
	@GetMapping("/chekemail/{email}")
	@ApiOperation(value = "이메일 중복 확인조회", notes = "이메일 중복을 확인한다")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 409, message = "이메일 중복")
	})
	public ResponseEntity<? extends BaseResponseBody> checkDupEmail(@PathVariable String email) {
		
		
		if(userService.checkEmail(email))
		{
		  System.out.println("User email dup");
		  return ResponseEntity.status(409).body(BaseResponseBody.of(409, "Duplicate Error"));
		 
		}
		else {
			System.out.println("success");
			return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
		}
		  
	}
	
	@GetMapping("/me")
	@ApiOperation(value = "회원 본인 정보 조회", notes = "로그인한 회원 본인의 정보를 응답한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<UserRes> getUserInfo(@ApiIgnore Authentication authentication) {
		/**
		 * 요청 헤더 액세스 토큰이 포함된 경우에만 실행되는 인증 처리이후, 리턴되는 인증 정보 객체(authentication) 통해서 요청한 유저 식별.
		 * 액세스 토큰이 없이 요청하는 경우, 403 에러({"error": "Forbidden", "message": "Access Denied"}) 발생.
		 */
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		String email = userDetails.getUsername();
		User user = userService.getUserByEamil(email);

		return ResponseEntity.status(200).body(UserRes.of(user));
	}

	@PutMapping
	public ResponseEntity<?> updateUser(@RequestBody UserRegisterPostReq userRegisterPostReq){
		
		System.out.println(userRegisterPostReq.toString());
		if(userService.updateUser(userRegisterPostReq)){
			return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		}
		return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
	}

	//회원 삭제
	@DeleteMapping("{email}")
	public ResponseEntity<?> deleteUser(@PathVariable String email){
		if(userService.deleteUser(email)){
			return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		}
		return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
	}
	
	
}
package com.d203.backend.api.controller;

import com.d203.backend.api.request.ReviewUpdateReq;
import com.d203.backend.api.request.RoomReq;
import com.d203.backend.api.request.RoomUpadateReq;
import com.d203.backend.api.request.UserReq;
import com.d203.backend.common.auth.SsafyUserDetails;
import com.d203.backend.db.entity.Room;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.d203.backend.api.service.RoomService;
import com.d203.backend.common.model.response.BaseResponseBody;
import springfox.documentation.annotations.ApiIgnore;

@Api(value = "방(회화 방) API", tags = {"Room"})
@RestController
@RequestMapping("/api/v1/Room")
public class Roomcontroller {
	
	@Autowired
	RoomService roomService;
	
	//CRUD
	@PostMapping()
	@ApiOperation(value = "회원 가입", notes = "<strong>아이디와 패스워드</strong>를 통해 회원가입 한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공"),
			@ApiResponse(code = 401, message = "인증 실패"),
			@ApiResponse(code = 404, message = "사용자 없음"),
			@ApiResponse(code = 500, message = "서버 오류")
	})
	public ResponseEntity<? extends BaseResponseBody> register(
			@RequestBody @ApiParam(value = "방생성 정보", required = true) RoomReq registerInfo) {

		Room room = roomService.createRoom(registerInfo);
		return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
	}

	@PutMapping("{room_id}")
	@ApiOperation(value = "리뷰 작성 수정", notes = "review의 pk 값을 받아와 수정한다.")
	@ApiResponses({
			@ApiResponse(code = 200, message = "성공")
	})
	public ResponseEntity<?> updateRoom(
			@RequestBody @ApiParam(value="평가 작성 수정 정보", required = true) RoomUpadateReq updateRoomInfo,
			@PathVariable Long room_id) {

		if(roomService.updateRoom(room_id,updateRoomInfo)){
			return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
		}

		return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("{room_id}")
	public ResponseEntity<?> deleteRoom(
			@PathVariable Long room_id,
			@ApiIgnore Authentication authentication
	) {
		SsafyUserDetails userDetails = (SsafyUserDetails)authentication.getDetails();
		Long tokenUserId = userDetails.getUser().getId();

		if(roomService.deleteRoom(room_id,tokenUserId)) {
			return  ResponseEntity.status(204).body("Room delete success");
		}

		return  ResponseEntity.status(401).body("User not macthed");
	}

}

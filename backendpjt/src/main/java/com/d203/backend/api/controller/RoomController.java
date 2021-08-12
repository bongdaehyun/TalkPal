package com.d203.backend.api.controller;

import com.d203.backend.api.request.Room.RoomReq;
import com.d203.backend.api.request.Room.RoomUpadateReq;
import com.d203.backend.api.response.Room.RoomListRes;
import com.d203.backend.api.response.Room.RoomRes;
import com.d203.backend.api.service.User.UserService;
import com.d203.backend.common.auth.SsafyUserDetails;
import com.d203.backend.db.entity.Room;
import com.d203.backend.db.entity.User;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.d203.backend.api.service.Room.RoomService;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@Api(value = "방(회화 방) API", tags = {"Room"})
@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    @Autowired
    RoomService roomService;

    //CRUD
    @PostMapping("/create")
    @ApiOperation(value = "방생성", notes = "방생성 작업")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 실패"),
            @ApiResponse(code = 404, message = "사용자 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<RoomRes> register(
            @RequestBody @ApiParam(value = "방생성 정보", required = true) RoomReq registerInfo) {

        Room room = roomService.createRoom(registerInfo);

        return ResponseEntity.status(200).body(RoomRes.of(room));
    }

    @GetMapping("/get/{room_uuid}")
    @ApiOperation(value = "방정보 조회", notes = "방정보의 값을 조최한다.")
    public ResponseEntity<RoomRes> getOneRoom(@PathVariable String room_uuid) {

        Room room = roomService.getRoom(room_uuid);
        return ResponseEntity.status(200).body(RoomRes.of(room));
    }

    @PutMapping("{room_id}")
    @ApiOperation(value = "방 정보 수정", notes = "방정보의 값을 받아와 수정한다.")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공")
    })
    public ResponseEntity<?> updateRoom(
            @RequestBody @ApiParam(value = "평가 작성 수정 정보", required = true) RoomUpadateReq updateRoomInfo,
            @PathVariable Long room_id) {

        if (roomService.updateRoom(room_id, updateRoomInfo)) {
            return new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
        }

        return new ResponseEntity<String>("FAIL", HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("{room_id}")
    public ResponseEntity<?> deleteRoom(
            @PathVariable Long room_id,
            @ApiIgnore Authentication authentication
    ) {
        //System.out.println("[room_id] : " + room_id);
        //System.out.println("[authentication] : " + authentication);
        SsafyUserDetails userDetails = (SsafyUserDetails) authentication.getDetails();
        Long tokenUserId = userDetails.getUser().getId();

        if (roomService.deleteRoom(room_id, tokenUserId)) {
            return ResponseEntity.status(204).body("Room delete success");
        }

        return ResponseEntity.status(401).body("User not macthed");
    }

    //방 조건 검색
    @ApiOperation(value = "방 정보 검색", notes = "방 조건에 따라 검색.")
    @GetMapping("{pageno}")
    public ResponseEntity<?> ConditionSearch(String topic, String lang, @PathVariable int pageno) {
        List<Room> rooms = null;
        Page<Room> pagerooms = null;
        pagerooms = roomService.getRoomList(topic, lang, pageno);

        if (pagerooms != null) {
            rooms = pagerooms.getContent();
        }
        if (rooms == null) {
            return ResponseEntity.status(404).body("No Room");
        } else {
            return ResponseEntity.status(200).body(RoomListRes.getList(rooms));
        }
    }

    //현재인원 체크
    @ApiOperation(value = "방 현재인원 체크", notes = "uuid를 받아와 현재인원 체크")
    @GetMapping("/check/{uuid}")
    public ResponseEntity<?> doCheckJoin(@PathVariable String uuid) {
        if (roomService.getCheckJoin(uuid)) {
            return ResponseEntity.status(200).body("join ok");
        } else {
            return ResponseEntity.status(404).body("Max people");
        }
    }

    //현재인원 산술 계산
    @ApiOperation(value = "방 현재인원 수정", notes = "uuid를 받아와 현재인원 + -.")
    @PutMapping("/cal/{uuid}")
    public ResponseEntity<?> doCalPeople(@PathVariable String uuid, Long num) {
        if (roomService.doControlPeople(uuid, num)) {
            return ResponseEntity.status(200).body("ok");
        }
        return ResponseEntity.status(404).body("No Join");
    }
}

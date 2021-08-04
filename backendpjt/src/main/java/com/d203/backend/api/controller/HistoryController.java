package com.d203.backend.api.controller;

import com.d203.backend.api.request.History.HistoryReq;
import com.d203.backend.api.response.History.HistoryRes;
import com.d203.backend.api.service.history.HistoryService;
import com.d203.backend.common.model.response.BaseResponseBody;
import com.d203.backend.db.entity.UserHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(value = "사용자가 만났던 사람들 API", tags = {"History"})
@RestController
@RequestMapping("/api/v1/history")
public class HistoryController {

    @Autowired
    HistoryService historyService;

    @GetMapping("/{userId}")
    @ApiOperation(value = "내가 만난 사람들 조회", notes = "요청하는 유저의 id를 통하여 to_user_id를 조회")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<HistoryRes> getMeetPeople(@PathVariable Long userId) {

        List<UserHistory> result = historyService.getMeetPeople(userId);

        return ResponseEntity.status(200).body(HistoryRes.getList(result));

    }

    @PostMapping("/add")
    @ApiOperation(value = "내가 만난 사람들 추가", notes = "요청하는 유저의 id를 통하여 to_user_id를 추가")
    @ApiResponses({
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "상대방 아이디가 없음"),
            @ApiResponse(code = 500, message = "서버 오류")
    })
    public ResponseEntity<?> addPeople(@RequestBody HistoryReq req) {

        boolean flag = historyService.addHistory(req.getFromid(), req.getToid());
        if (flag) {
            return ResponseEntity.status(200).body(BaseResponseBody.of(200, "Success"));
        } else {
            return ResponseEntity.status(401).body(BaseResponseBody.of(401, "No find friend ID"));
        }
    }
}

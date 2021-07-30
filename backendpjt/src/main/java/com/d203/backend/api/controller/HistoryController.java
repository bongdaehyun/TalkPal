package com.d203.backend.api.controller;

import com.d203.backend.api.response.History.HistoryRes;
import com.d203.backend.api.service.history.HistoryService;
import com.d203.backend.db.entity.UserHistory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

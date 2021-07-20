package com.d203.backend.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.d203.backend.api.service.RoomService;
import com.d203.backend.common.model.response.BaseResponseBody;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "방(회화 방) API", tags = {"Room"})
@RestController
@RequestMapping("/api/v1/Room")
public class Roomcontroller {
	
	@Autowired
	RoomService roomService;
	
	@GetMapping("/categories")
	@ApiOperation(value="방 카테고리 조회",  notes ="<strong>카테고리</strong> 를 조회한다.")
	public ResponseEntity<? extends BaseResponseBody> searchCaterogies(){
		
		return null;
	}
}

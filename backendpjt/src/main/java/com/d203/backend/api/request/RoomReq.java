package com.d203.backend.api.request;

import java.sql.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("RoomRequest")
public class RoomReq {

	@ApiModelProperty(name="Room 호스트 아이디", example="Room host_id")
	Long hostId;
	
	@ApiModelProperty(name="Room 시작시간", example="start_time")
	Date start_time;
	
	@ApiModelProperty(name="Room 호스트사용언어", example="host_lang")
	Long host_lnag;
	
	@ApiModelProperty(name="Room 게스트사용언어", example="guest_lang")
	Long guset_lang;

	@ApiModelProperty(name="Room 이름", example="room_title")
	String name;
	
	@ApiModelProperty(name="Room 카테고리", example="room_category")
	//카테고리 테이블은 따로 필요하지 않은지?
	String topic;

	@ApiModelProperty(name="방 허용 최대 인원", example="maxnum")
	Long maxnum;

	@ApiModelProperty(name="방 현재 인원", example="curnum")
	Long curnum;
}

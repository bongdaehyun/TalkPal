package com.d203.backend.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@ApiModel("RoomUpdateRequest")
public class RoomUpadateReq {
    //방제목
    //카테고리
    //인원

    @ApiModelProperty(name="방 제목", example="~의 대화방")
    String name;

    @ApiModelProperty(name="회화 카테고리", example="음악.")
    String topic;

    @ApiModelProperty(name="현재 인원", example="방 현재인원")
    Long curnum;

}

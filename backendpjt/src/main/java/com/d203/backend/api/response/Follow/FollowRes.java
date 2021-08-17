package com.d203.backend.api.response.Follow;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("follow Response")
public class FollowRes {
    @ApiModelProperty(name = "id")
    Long id;

    @ApiModelProperty(name = "nickname")
    String nickname;

    @ApiModelProperty(name = "eamil")
    String email;

    @ApiModelProperty(name = "profile_path")
    String imgPath;

    @ApiModelProperty(name = "lang")
    String lang;


}

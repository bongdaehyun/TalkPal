package com.d203.backend.api.response.History;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HistoryRes {
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

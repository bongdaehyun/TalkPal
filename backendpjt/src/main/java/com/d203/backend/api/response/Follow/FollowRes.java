package com.d203.backend.api.response.Follow;

import com.d203.backend.db.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("follow Response")
public class FollowRes {
    @ApiModelProperty(name="user_id")
    Long user_id;

    @ApiModelProperty(name="user_nickname")
    String user_nickname;

    @ApiModelProperty(name="user_eamil")
    String user_email;


}

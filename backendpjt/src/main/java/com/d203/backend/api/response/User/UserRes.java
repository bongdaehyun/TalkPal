package com.d203.backend.api.response.User;

import com.d203.backend.db.entity.User;

import com.d203.backend.db.repository.FollowRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 회원 본인 정보 조회 API ([GET] /api/v1/users/me) 요청에 대한 응답값 정의.
 */
@Getter
@Setter
@ApiModel("UserResponse")
public class UserRes {

    @ApiModelProperty(name = "User ID")
    String email;

    @ApiModelProperty(name = "User lang")
    String lang;

    @ApiModelProperty(name = "User nickname")
    String nickname;


    String sns;
    String introduction;
    String imgPath;

    Long cntFollower;
    Long cntFollowing;
    Long cntHistories;

    public static UserRes of(User user) {

        UserRes res = new UserRes();
        res.setEmail(user.getEmail());
        res.setLang(user.getLang().getName());
        res.setNickname(user.getNickname());
        res.setSns(user.getSns());
        res.setIntroduction(user.getIntroduction());
        res.setImgPath(user.getImgpath());

        res.setCntFollower(user.getCntFollower());
        res.setCntFollowing(user.getCntFollowing());
        res.setCntHistories(user.getCntHistories());

        return res;

    }
}

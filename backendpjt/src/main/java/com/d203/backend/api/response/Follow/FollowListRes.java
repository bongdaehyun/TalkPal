package com.d203.backend.api.response.Follow;


import com.d203.backend.db.entity.Follow;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("FollowListResponse")
@ToString
public class FollowListRes {

    @ApiModelProperty(name = "Follow List")
    List<FollowRes> userList;

    public static FollowListRes getIngList(List<Follow> follow) {
        FollowListRes followListRes = new FollowListRes();

        List<FollowRes> followList = new ArrayList<FollowRes>();
        for (int i = 0; i < follow.size(); i++) {
            FollowRes res = new FollowRes();
            res.setId(follow.get(i).getTouserid().getId());
            res.setNickname(follow.get(i).getTouserid().getNickname());
            res.setEmail(follow.get(i).getTouserid().getEmail());
            res.setImgPath(follow.get(i).getTouserid().getImgpath());
            res.setLang(follow.get(i).getTouserid().getLang().getName());


            followList.add(res);
        }
        followListRes.setUserList(followList);
        return followListRes;
    }


    public static FollowListRes getErList(List<Follow> follow) {
        FollowListRes followListRes = new FollowListRes();

        List<FollowRes> followList = new ArrayList<FollowRes>();
        for (int i = 0; i < follow.size(); i++) {
            FollowRes res = new FollowRes();
            res.setId(follow.get(i).getFromuserid().getId());
            res.setNickname(follow.get(i).getFromuserid().getNickname());
            res.setEmail(follow.get(i).getFromuserid().getEmail());
            res.setImgPath(follow.get(i).getFromuserid().getImgpath());
            res.setLang(follow.get(i).getFromuserid().getLang().getName());

            followList.add(res);
        }
        followListRes.setUserList(followList);
        return followListRes;
    }
}

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

    @ApiModelProperty(name="Follow List")
    List<FollowRes> followList;

    public static FollowListRes getIngList(List<Follow> follow){
        FollowListRes followListRes = new FollowListRes();

        List<FollowRes> followList = new ArrayList<FollowRes>();
        for(int i=0; i< follow.size() ; i++)
        {
            FollowRes res = new FollowRes();
            res.setUser_id(follow.get(i).getTouserid().getId());
            res.setUser_nickname(follow.get(i).getTouserid().getNickname());
            res.setUser_email(follow.get(i).getTouserid().getEmail());

            followList.add(res);
        }
        followListRes.setFollowList(followList);
        return followListRes;
    }


    public static FollowListRes getErList(List<Follow> follow){
        FollowListRes followListRes = new FollowListRes();

        List<FollowRes> followList = new ArrayList<FollowRes>();
        for(int i=0; i< follow.size() ; i++)
        {
            FollowRes res = new FollowRes();
            res.setUser_id(follow.get(i).getFromuserid().getId());
            res.setUser_nickname(follow.get(i).getFromuserid().getNickname());
            res.setUser_email(follow.get(i).getFromuserid().getEmail());

            followList.add(res);
        }
        followListRes.setFollowList(followList);
        return followListRes;
    }
}

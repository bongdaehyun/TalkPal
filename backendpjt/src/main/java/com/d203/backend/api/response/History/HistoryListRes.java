package com.d203.backend.api.response.History;


import com.d203.backend.db.entity.UserHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public class HistoryListRes {

    @ApiModelProperty(name = "History List")
    List<HistoryRes> userList;

    public static HistoryListRes getList(List<UserHistory> list) {
        HistoryListRes res = new HistoryListRes();
        List<HistoryRes> historyList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            HistoryRes item = new HistoryRes();
            //만났던 사람의 유저 아이디
            item.setId(list.get(i).getTo_user_id().getId());
            //만났던 사람의 닉네임
            item.setNickname(list.get(i).getTo_user_id().getNickname());
            //만났던 사람의 이메일
            item.setEmail(list.get(i).getTo_user_id().getEmail());
            //만났던 사람의 프로필 사진
            item.setImgPath(list.get(i).getTo_user_id().getImgpath());
            item.setLang(list.get(i).getTo_user_id().getLang().getName());
            historyList.add(item);
        }
        res.setUserList(historyList);
        return res;
    }
}

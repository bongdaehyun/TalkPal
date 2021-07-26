package com.d203.backend.api.response.History;


import com.d203.backend.db.entity.UserHistory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class HistoryRes {

    @ApiModelProperty(name="History List")
    List<HistoryItemRes> historyList;

    public static HistoryRes getList(List<UserHistory> list){
        HistoryRes res=new HistoryRes();
        List<HistoryItemRes> historyList=new ArrayList<>();

        for(int i=0;i<list.size();i++){
            HistoryItemRes item=new HistoryItemRes();
            //만났던 사람의 유저 아이디
            item.setUserid(list.get(i).getTo_user_id().getId());
            //만났던 사람의 닉네임
            item.setNickname(list.get(i).getTo_user_id().getNickname());
            //만났던 사람의 이메일
            item.setEmail(list.get(i).getTo_user_id().getEmail());

            historyList.add(item);
        }
        res.setHistoryList(historyList);
        return res;
    }
}

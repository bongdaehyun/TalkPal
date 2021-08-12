package com.d203.backend.api.response.Room;


import com.d203.backend.api.service.User.UserService;
import com.d203.backend.db.entity.Room;
import com.d203.backend.db.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ApiModel("RoomListResponse")
public class RoomListRes {

    @ApiModelProperty(name = "Room List")
    List<RoomRes> roomResList;

    public static RoomListRes getList(List<Room> room) {
        RoomListRes roomListRes = new RoomListRes();
        List<RoomRes> roomList = new ArrayList<>();

        for (int i = 0; i < room.size(); i++) {
            RoomRes res = new RoomRes();

            res.setName(room.get(i).getName());
            res.setTopic(room.get(i).getTopic());

            User hostUser = room.get(i).getHostId();

            res.setHostId(room.get(i).getHostId().getId());
            res.setHost_score(hostUser.getAvgScore());
            res.setHost_nickname(hostUser.getNickname());
            res.setHost_imgPath(hostUser.getImgpath());

            res.setGuest_lang(room.get(i).getGuest_lang().getName());
            res.setHost_lang(room.get(i).getHost_lang().getName());

            res.setUuid(room.get(i).getUuid());

            roomList.add(res);
        }
        roomListRes.setRoomResList(roomList);
        return roomListRes;
    }
}

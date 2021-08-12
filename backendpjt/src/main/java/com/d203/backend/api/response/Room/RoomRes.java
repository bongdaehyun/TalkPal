package com.d203.backend.api.response.Room;

import com.d203.backend.api.response.User.UserRes;
import com.d203.backend.db.entity.Lang;
import com.d203.backend.db.entity.Room;
import com.d203.backend.db.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("RoomResponse")
public class RoomRes {
    @ApiModelProperty(name = "roomId")
    Long roomId;

    @ApiModelProperty(name = "hostId")
    Long hostId;

    @ApiModelProperty(name = "host_lang")
    String host_lang;

    @ApiModelProperty(name = "hostimgPath")
    String host_imgPath;

    @ApiModelProperty(name = "host_introduction")
    String host_introduction;

    @ApiModelProperty(name = "host_score")
    double host_score;

    @ApiModelProperty(name = "host_nickname")
    String host_nickname;

    @ApiModelProperty(name = "guest_lang")
    String guest_lang;

    @ApiModelProperty(name = "uuid")
    String uuid;

    @ApiModelProperty(name = "topic")
    String topic;

    @ApiModelProperty(name = "name")
    String name;

    public static RoomRes of(Room room) {
        User hostUser = room.getHostId();

        RoomRes roomRes = new RoomRes();
        roomRes.setRoomId(room.getId());
        roomRes.setTopic(room.getTopic());
        roomRes.setName(room.getName());

        roomRes.setHostId(room.getHostId().getId());
        roomRes.setHost_nickname(hostUser.getNickname());
        roomRes.setHost_introduction((hostUser.getIntroduction()));
        roomRes.setHost_imgPath(hostUser.getImgpath());
        roomRes.setHost_score(hostUser.getAvgScore());

        roomRes.setGuest_lang(room.getGuest_lang().getName());
        roomRes.setHost_lang(room.getHost_lang().getName());
        roomRes.setUuid(room.getUuid());
        return roomRes;
    }
}

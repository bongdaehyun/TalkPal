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


    @ApiModelProperty(name="hostId")
    Long hostId;

    @ApiModelProperty(name="host_lang")
    String host_lang;

    @ApiModelProperty(name="guest_lang")
    String guest_lang;

    @ApiModelProperty(name="uuid")
    String uuid;

    @ApiModelProperty(name="topic")
    String topic;

    public static RoomRes of(Room room) {
        RoomRes roomRes= new RoomRes();

        roomRes.setHostId(room.getHostId().getId());
        roomRes.setGuest_lang(room.getGuest_lang().getName());
        roomRes.setHost_lang(room.getGuest_lang().getName());
        roomRes.setUuid(room.getUuid());
        return roomRes;
    }
}

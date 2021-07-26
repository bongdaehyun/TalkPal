package com.d203.backend.api.service.Room;

import com.d203.backend.api.request.Room.RoomReq;
import com.d203.backend.api.request.Room.RoomUpadateReq;
import com.d203.backend.db.entity.Room;

import java.util.List;

public interface RoomService {
    Room createRoom(RoomReq roomInfo);

    boolean updateRoom(Long roomId, RoomUpadateReq updateRoomInfo);

    boolean deleteRoom(Long room_id, Long tokenUserId);

    List<Room> getRoomList();

    Room getRoom(Long room_id);



}

package com.d203.backend.api.service;

import com.d203.backend.api.request.ReviewUpdateReq;
import com.d203.backend.api.request.RoomReq;
import com.d203.backend.api.request.RoomUpadateReq;
import com.d203.backend.api.response.RoomRes;
import com.d203.backend.db.entity.Room;

import java.util.List;

public interface RoomService {
    Room createRoom(RoomReq roomInfo);

    boolean updateRoom(Long roomId, RoomUpadateReq updateRoomInfo);

    boolean deleteRoom(Long room_id, Long tokenUserId);

    List<Room> getRoomList();

    Room getRoom(Long room_id);



}

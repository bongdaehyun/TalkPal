package com.d203.backend.api.service.Room;

import com.d203.backend.api.request.Room.RoomReq;
import com.d203.backend.api.request.Room.RoomUpadateReq;
import com.d203.backend.db.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {
    Room createRoom(RoomReq roomInfo);

    boolean updateRoom(Long roomId, RoomUpadateReq updateRoomInfo);

    boolean deleteRoom(Long room_id, Long tokenUserId);

    Page<Room> getRoomList(int pageno);

    Room getRoom(Long room_id);

    //방 제목로 검색
    List<Room> getNameList(String name);
    //방 주제로 검색
    List<Room> getTopicList(String topic);
    //방 언어로 검색
    List<Room> getLangList(String lang);
}

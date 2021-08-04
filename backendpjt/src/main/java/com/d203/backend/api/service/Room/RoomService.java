package com.d203.backend.api.service.Room;

import com.d203.backend.api.request.Room.RoomReq;
import com.d203.backend.api.request.Room.RoomUpadateReq;
import com.d203.backend.db.entity.Room;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    Room createRoom(RoomReq roomInfo);

    boolean updateRoom(Long roomId, RoomUpadateReq updateRoomInfo);

    boolean deleteRoom(Long room_id, Long tokenUserId);

    Page<Room> getRoomList(int pageno);

    Room getRoom(String room_uuid);

    //현재인원을 체크
    boolean getCheckJoin(String uuid);

    //현재인원을 + -
    boolean doControlPeople(String uuid,Long people);
    //방 제목로 검색
    Page<Room> getNameList(String name,int pageno);
    //방 주제로 검색
    Page<Room> getTopicList(String topic,int pageno);
    //방 언어로 검색
    Page<Room> getLangList(String lang,int pageno);
    //방 제목과 언어로 검색
    Page<Room> getSearchList(String topic,String lang,int pageno);
}

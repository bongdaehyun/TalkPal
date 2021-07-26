package com.d203.backend.api.service;

import com.d203.backend.api.request.RoomReq;
import com.d203.backend.api.request.RoomUpadateReq;
import com.d203.backend.api.response.RoomRes;
import com.d203.backend.db.entity.Room;
import com.d203.backend.db.repository.LangRepository;
import com.d203.backend.db.repository.RoomRepository;
import com.d203.backend.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("roomService")
public class RoomServiceImpl implements RoomService{

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LangRepository langRepository ;
    @Override
    public Room createRoom(RoomReq roomInfo) {
        Room room = new Room();

        //room.setHostId(userRepository.getOne(roomInfo.getHostId()));
        room.setHostId(userRepository.getOne(1L));
        //room.setCategory(roomInfo.getCategory());
        room.setTopic("음악");
       // room.setMaxnum(roomInfo.getMaxnum());
        room.setMaxnum(2L);
       // room.setCurnum(roomInfo.getCurnum());
        room.setCurnum(1L);

       // room.setTitle(roomInfo.getTitle());
        room.setName(roomInfo.getName());

        //room.setGuset_lang(langRepository.getOne(roomInfo.getGuset_lang()));
        room.setGuset_lang(langRepository.getOne(1L));
        //room.setHost_lnag(langRepository.getOne(roomInfo.getHost_lnag()));
        room.setHost_lnag(langRepository.getOne(1L));

        return roomRepository.save(room);
    }

    @Override
    public boolean updateRoom(Long roomId, RoomUpadateReq updateRoomInfo) {
        Optional<Room> room = roomRepository.findById(roomId);

        Room updateRoom = room.get();

        updateRoom.setName(updateRoomInfo.getName());
        updateRoom.setTopic(updateRoomInfo.getTopic());
        updateRoom.setCurnum(updateRoomInfo.getCurnum());

        if(room.isPresent()){
            roomRepository.save(updateRoom);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRoom(Long room_id, Long tokenUserId) {
        Room delRoom = roomRepository.getOne(room_id);
        Long isAcceptId = delRoom.getHostId().getId();

        if(isAcceptId == tokenUserId) {
            roomRepository.delete(delRoom);
            return true;
        }

        return false;
    }

    @Override
    public List<Room> getRoomList() {

        List<Room> roomList = roomRepository.findAll();
        return roomList;
    }

    @Override
    public Room getRoom(Long room_id) {
       Room room = roomRepository.findById(room_id).get();
        return room;
    }
}

package com.d203.backend.api.service;

import com.d203.backend.api.request.RoomReq;
import com.d203.backend.api.request.RoomUpadateReq;
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

        room.setHostId(userRepository.getOne(roomInfo.getHostId()));
        room.setCategory(roomInfo.getCategory());
        room.setMaxnum(roomInfo.getMaxnum());
        room.setCurnum(roomInfo.getCurnum());
        room.setTitle(roomInfo.getTitle());

        room.setGuset_lang(langRepository.getOne(roomInfo.getGuset_lang()));
        room.setHost_lnag(langRepository.getOne(roomInfo.getHost_lnag()));

        return roomRepository.save(room);
    }

    @Override
    public boolean updateRoom(Long roomId, RoomUpadateReq updateRoomInfo) {
        Optional<Room> room = roomRepository.findById(roomId);

        Room updateRoom = room.get();

        updateRoom.setTitle(updateRoomInfo.getTitle());
        updateRoom.setCategory(updateRoomInfo.getCategory());
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
        return null;
    }

    @Override
    public Room getRoom(Long room_id) {
        return null;
    }
}

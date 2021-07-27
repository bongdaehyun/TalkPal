package com.d203.backend.api.service.Room;

import com.d203.backend.api.request.Room.RoomReq;
import com.d203.backend.api.request.Room.RoomUpadateReq;
import com.d203.backend.db.entity.Room;
import com.d203.backend.db.repository.LangRepository;
import com.d203.backend.db.repository.RoomRepository;
import com.d203.backend.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

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
        room.setGuest_lang(langRepository.getOne(1L));
        //room.setHost_lnag(langRepository.getOne(roomInfo.getHost_lnag()));
        room.setHost_lang(langRepository.getOne(1L));

        room.setUuid(UUID.randomUUID().toString());

        long time = System.currentTimeMillis();
        System.out.println(time);
        SimpleDateFormat simpl = new SimpleDateFormat("yyyy년 MM월 dd일 aa hh시 mm분 ss초");
        String start_time = simpl.format(time); // System.out.println(s);

        room.setStart_time(start_time);

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

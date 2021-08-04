package com.d203.backend.api.service.Room;

import com.d203.backend.api.request.Room.RoomReq;
import com.d203.backend.api.request.Room.RoomUpadateReq;
import com.d203.backend.db.entity.Lang;
import com.d203.backend.db.entity.Room;
import com.d203.backend.db.repository.LangRepository;
import com.d203.backend.db.repository.RoomRepository;
import com.d203.backend.db.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service("roomService")
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LangRepository langRepository;

    @Override
    public Room createRoom(RoomReq roomInfo) {
        Room room = new Room();

        room.setHostId(userRepository.getOne(roomInfo.getHostId()));
        //room.setHostId(userRepository.getOne(1L));
        room.setTopic(roomInfo.getTopic());
        //room.setTopic("음악");
        room.setMaxnum(roomInfo.getMaxnum());
        //room.setMaxnum(2L);
        room.setCurnum(roomInfo.getCurnum());
        //room.setCurnum(1L);

        room.setTopic(roomInfo.getTopic());
        room.setName(roomInfo.getName());
        System.out.println(room.getName());


        room.setGuest_lang(langRepository.getOne(roomInfo.getGuest_lang()));
        //room.setGuest_lang(langRepository.getOne(1L));
        room.setHost_lang(langRepository.getOne(roomInfo.getHost_lang()));
        //room.setHost_lang(langRepository.getOne(1L));

        room.setUuid(UUID.randomUUID().toString());


        return roomRepository.save(room);
    }

    @Override
    public boolean updateRoom(Long roomId, RoomUpadateReq updateRoomInfo) {
        Optional<Room> room = roomRepository.findById(roomId);

        Room updateRoom = room.get();

        updateRoom.setName(updateRoomInfo.getName());
        updateRoom.setTopic(updateRoomInfo.getTopic());
        updateRoom.setCurnum(updateRoomInfo.getCurnum());

        if (room.isPresent()) {
            roomRepository.save(updateRoom);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRoom(Long room_id, Long tokenUserId) {
        Room delRoom = roomRepository.getOne(room_id);
        Long isAcceptId = delRoom.getHostId().getId();

        if (isAcceptId == tokenUserId) {
            roomRepository.delete(delRoom);
            return true;
        }

        return false;
    }

    @Override
    public Page<Room> getRoomList(int pageno) {

        Pageable firstPageWithTwoElements = PageRequest.of(pageno - 1, 20);
        return roomRepository.findAll(firstPageWithTwoElements);
    }

    @Override
    public Room getRoom(String room_uuid) {
        Room room = roomRepository.findByUuid(room_uuid);
        return room;
    }

    @Override
    public boolean getCheckJoin(String uuid) {
        Room room = roomRepository.findByUuid(uuid);
        //찾을 수 없는 방
        if (room==null){
            return false;
        }
        //최대 인원보다 현재 인원 수가 적을 경우
        if(room.getCurnum()<room.getMaxnum()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public boolean doControlPeople(String uuid, Long people) {
        Room room=roomRepository.findByUuid(uuid);
        //찾고자 하는 방이 없을 경우
        if(room==null){
            return false;
        }
        Long curnum=room.getCurnum();
        //현재인원에 더하거나 마이너스 한부분을 체크
        Long newNum=curnum+people;
        if(newNum>room.getMaxnum() ||  newNum<=0){
            return false;
        }
        room.setCurnum(newNum);
        roomRepository.save(room);
        return true;
    }

    @Override
    public Page<Room> getNameList(String name, int pageno) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageno - 1, 20);
        Page<Room> rooms = roomRepository.findAllByNameLike("%" + name + "%", firstPageWithTwoElements);
        return rooms;
    }

    @Override
    public Page<Room> getTopicList(String topic, int pageno) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageno - 1, 20);
        Page<Room> rooms = roomRepository.findAllByTopic(topic, firstPageWithTwoElements);
        return rooms;
    }

    @Override
    public Page<Room> getLangList(String lang, int pageno) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageno - 1, 20);
        Lang findlang = langRepository.findByName(lang);
        Page<Room> rooms = roomRepository.findAllByGuest_lang(findlang.getId(), firstPageWithTwoElements);
        return rooms;
    }

    @Override
    public Page<Room> getSearchList(String topic, String lang, int pageno) {
        Pageable firstPageWithTwoElements = PageRequest.of(pageno - 1, 20);
        Lang findlang = langRepository.findByName(lang);
        Page<Room> rooms = roomRepository.findAllByTopicAndGuest_lang(topic, findlang.getId(), firstPageWithTwoElements);
        return rooms;
    }

}

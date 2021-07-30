package com.d203.backend.webrtc;


import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoomManager {

    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Autowired
    private KurentoClient kurento;

    private final ConcurrentMap<String, RoomSession> rooms = new ConcurrentHashMap<>();

    public RoomSession createRoom(String uuid) {
        log.debug("Room {} not existent. Will create now!", uuid);
        RoomSession roomSession = new RoomSession(uuid, kurento.createMediaPipeline());
        rooms.put(uuid, roomSession);
        return roomSession;
    }

    public RoomSession getRoom(String uuid) {
        log.debug("Searching for room {}", uuid);
        RoomSession roomSession = rooms.get(uuid);

        if (roomSession == null) {
            log.debug("Room not existent");
//            roomSession = new RoomSession(roomName, kurento.createMediaPipeline());
//            rooms.put(roomName, roomSession);
        }
        log.debug("Room {} found!", uuid);
        return roomSession;
    }

    public void removeRoom(RoomSession roomSession) {
        this.rooms.remove(roomSession.getUuid());
        roomSession.close();
        log.info("Room {} removed and closed", roomSession.getUuid());
    }

}

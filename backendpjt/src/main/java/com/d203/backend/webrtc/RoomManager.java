package com.d203.backend.webrtc;


import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Ivan Gracia (izanmail@gmail.com)
 * @since 4.3.1
 */
public class RoomManager {

    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Autowired
    private KurentoClient kurento;

    private final ConcurrentMap<String, RoomSession> rooms = new ConcurrentHashMap<>();

    /**
     * Looks for a room in the active room list.
     *
     * @param roomName the name of the room
     * @return the room if it was already created, or a new one if it is the first time this room is
     * accessed
     */
    public RoomSession getRoom(String roomName) {
        log.debug("Searching for room {}", roomName);
        RoomSession roomSession = rooms.get(roomName);

        if (roomSession == null) {
            log.debug("Room {} not existent. Will create now!", roomName);
            roomSession = new RoomSession(roomName, kurento.createMediaPipeline());
            rooms.put(roomName, roomSession);
        }
        log.debug("Room {} found!", roomName);
        return roomSession;
    }

    /**
     * Removes a room from the list of available rooms.
     *
     * @param roomSession the room to be removed
     */
    public void removeRoom(RoomSession roomSession) {
        this.rooms.remove(roomSession.getName());
        roomSession.close();
        log.info("Room {} removed and closed", roomSession.getName());
    }

}

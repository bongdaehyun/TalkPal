package com.d203.backend.webrtc;


import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

public class ParticipantManager {

    private final ConcurrentHashMap<String, ParticipantSession> usersByUserId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ParticipantSession> usersBySessionId = new ConcurrentHashMap<>();

    public void register(ParticipantSession user) {
        usersByUserId.put(user.getUserId(), user);
        usersBySessionId.put(user.getSession().getId(), user);
    }

    public ParticipantSession getByUserId(String userId) {
        return usersByUserId.get(userId);
    }

    public ParticipantSession getBySession(WebSocketSession session) {
        return usersBySessionId.get(session.getId());
    }

    public boolean exists(String userId) {
        return usersByUserId.keySet().contains(userId);
    }

    public ParticipantSession removeBySession(WebSocketSession session) {
        final ParticipantSession user = getBySession(session);
        usersByUserId.remove(user.getUserId());
        usersBySessionId.remove(session.getId());
        return user;
    }

}

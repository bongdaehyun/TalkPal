package com.d203.backend.webrtc;

import org.springframework.web.socket.WebSocketSession;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private final ConcurrentHashMap<String, UserSession> usersByUserId = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, UserSession> usersBySession = new ConcurrentHashMap<>();

    // 추가
    public void register(UserSession user) {
        usersByUserId.put(user.getUserId(), user);
        usersBySession.put(user.getSession().getId(), user);
    }

    // 삭제
    public void remove(WebSocketSession session) {
        final UserSession user = getBySession(session);
        usersByUserId.remove(user.getUserId());
        usersBySession.remove(user.getSession().getId());
    }

    // UserId로 하나 얻기
    public UserSession getByUserId(String userId) {
        return usersByUserId.get(userId);
    }

    // session으로 하나 얻기
    public UserSession getBySession(WebSocketSession session) {
        return usersBySession.get(session.getId());
    }

    // 모두 얻기
    public Collection<UserSession> getAllUsers() {
        return usersByUserId.values();
    }

    // 존재 확인
    public boolean exists(String userId) {
        return usersByUserId.keySet().contains(userId);
    }
}
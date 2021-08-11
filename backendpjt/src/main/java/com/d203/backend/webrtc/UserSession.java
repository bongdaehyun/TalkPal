package com.d203.backend.webrtc;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

public class UserSession {
    private static final Logger log = LoggerFactory.getLogger(ParticipantSession.class);

    private final String userId;
    private final WebSocketSession session;

    public UserSession(final String userId, final WebSocketSession session) {
        this.userId = userId;
        this.session = session;
    }

    public String getUserId() { return userId; }

    public WebSocketSession getSession() { return session; }

    public void sendMessage(JsonObject message) throws IOException {
        log.debug("USER {}: Sending message {}", userId, message);
        synchronized (session) {
            session.sendMessage(new TextMessage(message.toString()));
        }
    }
}
package com.d203.backend.webrtc;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.kurento.client.IceCandidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class CallHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(CallHandler.class);

    private static final Gson gson = new GsonBuilder().create();

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private UserManager userManager;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        final UserSession user = userManager.getBySession(session);

        if (user != null) {
            log.debug("Incoming message from user '{}': {}", user.getUserId(), jsonMessage);
        } else {
            log.debug("Incoming message from new user: {}", jsonMessage);
        }

        switch (jsonMessage.get("id").getAsString()) {
            case "createRoom":
                createRoom(jsonMessage, session);
                break;
            case "joinRequest":
                joinRequest(jsonMessage, session);
                break;
            case "joinResponse":
                joinResponse(jsonMessage, session);
                break;
            case "joinRoom":
                joinRoom(jsonMessage, session);
                break;
            case "receiveVideoFrom":
                final String senderName = jsonMessage.get("sender").getAsString();
                final UserSession sender = userManager.getByUserId(senderName);
                final String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
                user.receiveVideoFrom(sender, sdpOffer);
                break;
            case "leaveRoom":
                final String hostId = jsonMessage.get("hostId").getAsString();
                leaveRoom(user, hostId);
                break;
            case "onIceCandidate":
                JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

                if (user != null) {
                    IceCandidate cand = new IceCandidate(candidate.get("candidate").getAsString(),
                            candidate.get("sdpMid").getAsString(), candidate.get("sdpMLineIndex").getAsInt());
                    user.addCandidate(cand, jsonMessage.get("name").getAsString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        UserSession user = userManager.removeBySession(session);
        roomManager.getRoom(user.getUuid()).leave(user);
    }

    private void createRoom(JsonObject params, WebSocketSession session) throws IOException {
        final String uuid = params.get("uuid").getAsString();
        final String userId = params.get("userId").getAsString();
        log.info("PARTICIPANT {}: trying to create room {}", userId, uuid);

        RoomSession roomSession = roomManager.createRoom(uuid);
        final UserSession user = roomSession.join(userId, session);
        userManager.register(user);
    }

    private void joinRequest(JsonObject params, WebSocketSession session) throws IOException {
        // 호스트에게 현재 입장 요청한 클라이언트 userId 전송
        final String uuid = params.get("uuid").getAsString();
        final String requestUserId = params.get("requestUserId").getAsString(); // 제이슨객체 수정 (id)
        final String hostId = params.get("hostId").getAsString();

        final JsonObject requestMsg = new JsonObject();
        requestMsg.addProperty("id", "joinQuestion");
        requestMsg.addProperty("uuid", uuid);
        requestMsg.addProperty("requestUserId", requestUserId);
        requestMsg.addProperty("hostId", hostId);

        UserSession hostSession = userManager.getByUserId(hostId);

        synchronized (hostSession) {
            hostSession.sendMessage(requestMsg);
        }

        // 여기서 입장 요청자의 세션정보 등록 (uuid, pipeline은 없어서 임시 보류)
        UserSession requestUser = new UserSession("tmp", null, session, null);
        userManager.register(requestUser);
    }

    private void joinResponse(JsonObject params, WebSocketSession session) throws IOException {
        // 입장 요청한 클라이언트에게 수락/거절여부 + uuid 전송
        final String requestUserId = params.get("requestUserId").getAsString();
        final String uuid = params.get("uuid").getAsString();
        final String answer = params.get("answer").getAsString();

        final JsonObject responseMsg = new JsonObject();
        responseMsg.addProperty("id", "joinAnswer");
        responseMsg.addProperty("uuid", uuid);
        responseMsg.addProperty("answer", answer);

        UserSession requestSession = userManager.getByUserId(requestUserId);

        synchronized (requestSession) {
            requestSession.sendMessage(responseMsg);
        }
        userManager.removeBySession(requestSession.getSession());
    }

    private void joinRoom(JsonObject params, WebSocketSession session) throws IOException {
        final String uuid = params.get("uuid").getAsString();
        final String userId = params.get("userId").getAsString();
        log.info("PARTICIPANT {}: trying to join room {}", userId, uuid);

        RoomSession roomSession = roomManager.getRoom(uuid);
        final UserSession user = roomSession.join(userId, session);
        userManager.register(user);
    }

    private void leaveRoom(UserSession user, String hostId) throws IOException {
        final RoomSession roomSession = roomManager.getRoom(user.getUuid());

        if (user.getUserId().equals(hostId)) { // 타입 체크
            for (UserSession participant : roomSession.getParticipants()) {
                roomSession.hostLeave(participant, hostId);
            }
            roomManager.removeRoom(roomSession);
            return;
        }

        roomSession.leave(user);
    }
}

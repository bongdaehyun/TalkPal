package com.d203.backend.webrtc;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import org.kurento.client.IceCandidate;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CallHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(CallHandler.class);

    private static final Gson gson = new GsonBuilder().create();

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private ParticipantManager participantManager;

    @Autowired
    private KurentoClient kurento;

    @Autowired
    private UserManager userManager;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        final ParticipantSession user = participantManager.getBySession(session);

        if (user != null) {
            log.debug("Incoming message from user '{}': {}", user.getUserId(), jsonMessage);
        } else {
            log.debug("Incoming message from new user: {}", jsonMessage);
        }

        switch (jsonMessage.get("id").getAsString()) {
            case "newUserDM":
                newUserDM(jsonMessage, session);
                break;
            case "sendDM":
                sendDM(jsonMessage);
                break;
            case "sendChat":
                sendChat(jsonMessage);
                break;
            case "createRoom":
                createRoom(jsonMessage, session);
                break;
            case "joinRequest":
                joinRequest(jsonMessage, session);
                break;
            case "joinResponse":
                joinResponse(jsonMessage);
                break;
            case "joinRoom":
                joinRoom(jsonMessage, session);
                break;
            case "receiveVideoFrom":
                final String senderName = jsonMessage.get("sender").getAsString();
                final ParticipantSession sender = participantManager.getByUserId(senderName);
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
        try {
            userManager.remove(session);
        } catch (NullPointerException e) {
            log.error("No User in UserManager");
        }
        try {
            ParticipantSession user = participantManager.removeBySession(session);
            roomManager.getRoom(user.getUuid()).leave(user);
        } catch (NullPointerException e) {
            log.info("No User in ParticipantManager");
        }
    }

    private void newUserDM(JsonObject params, WebSocketSession session) throws IOException {
        final String userId = params.get("userId").getAsString();
        UserSession user = new UserSession(userId, session);
        userManager.register(user);

        log.info("New DM User connected : {}", userId);
    }

    private void sendDM(JsonObject params) throws IOException {
        final String receiver = params.get("receiver").getAsString();
        final String message = params.get("message").getAsString();
        final String senderId = params.get("userId").getAsString();
        final String chatRoomId = params.get("chatRoomId").getAsString();

        if (userManager.exists(receiver)) {
            final JsonObject DMInfo = new JsonObject();
            DMInfo.addProperty("id", "receiveDM");
            DMInfo.addProperty("chatRoomId", chatRoomId);
            DMInfo.addProperty("userId", senderId);
            DMInfo.addProperty("message", message);

            UserSession receiverSession = userManager.getByUserId(receiver);
            synchronized (receiverSession) {
                receiverSession.sendMessage(DMInfo);
            }
        }
    }

    private void sendChat(JsonObject params) throws IOException {
        final String senderId = params.get("senderId").getAsString();
        final String senderNickName = params.get("senderNickName").getAsString();
        final String sendMsg = params.get("sendMsg").getAsString();

        TimeZone timeZone = TimeZone.getTimeZone("Asia/Seoul");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        format.setTimeZone(timeZone);
        Date time = new Date();
        String sendTime = format.format(time);

        log.info("User {} send message in ({}) : {}", senderId, sendTime, sendMsg);

        final JsonObject chatInfo = new JsonObject();
        chatInfo.addProperty("id", "receiveChat");
        chatInfo.addProperty("senderId", senderId);
        chatInfo.addProperty("senderNickName", senderNickName);
        chatInfo.addProperty("sendMsg", sendMsg);
        chatInfo.addProperty("sendTime", sendTime);

        final ParticipantSession sender = participantManager.getByUserId(senderId);
        RoomSession roomSession = roomManager.getRoom(sender.getUuid());
        for (ParticipantSession user : roomSession.getParticipants()){
            synchronized (user) {
                user.sendMessage(chatInfo);
            }
        }
    }

    private void createRoom(JsonObject params, WebSocketSession session) throws IOException {
        final String uuid = params.get("uuid").getAsString();
        final String userId = params.get("userId").getAsString();
        log.info("PARTICIPANT {}: trying to create room {}", userId, uuid);

        RoomSession roomSession = roomManager.createRoom(uuid);
        final ParticipantSession user = roomSession.join(userId, session);
        participantManager.register(user);
    }

    private void joinRoom(JsonObject params, WebSocketSession session) throws IOException {
        final String uuid = params.get("uuid").getAsString();
        final String userId = params.get("userId").getAsString();
        log.info("joinRoom => PARTICIPANT {}: trying to join room {}", userId, uuid);

        RoomSession roomSession = roomManager.getRoom(uuid);
        final ParticipantSession user = roomSession.join(userId, session);
        participantManager.register(user);
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

        ParticipantSession hostSession = participantManager.getByUserId(hostId);

        log.info("joinRequest => uuid : {}, requestUserId : {}, hostId : {}", uuid, requestUserId, hostId);

        ParticipantSession requestUser = new ParticipantSession(requestUserId, "tmp", session, kurento.createMediaPipeline());
        participantManager.register(requestUser);

        synchronized (hostSession) {
            hostSession.sendMessage(requestMsg);
        }
    }

    private void joinResponse(JsonObject params) throws IOException {
        // 입장 요청한 클라이언트에게 수락/거절여부 + uuid 전송
        final String requestUserId = params.get("requestUserId").getAsString();
        final String uuid = params.get("uuid").getAsString();
        final String answer = params.get("answer").getAsString();

        log.info("joinResponse => requestUserId : {}, uuid : {}, answer : {}", requestUserId, uuid, answer);

        final JsonObject responseMsg = new JsonObject();
        responseMsg.addProperty("id", "joinAnswer");
        responseMsg.addProperty("uuid", uuid);
        responseMsg.addProperty("answer", answer);

        ParticipantSession requestUser = participantManager.getByUserId(requestUserId);
        participantManager.removeBySession(requestUser.getSession());

        synchronized (requestUser) {
            requestUser.sendMessage(responseMsg);
        }
    }

    private void leaveRoom(ParticipantSession user, String hostId) throws IOException {
        final RoomSession roomSession = roomManager.getRoom(user.getUuid());

        if (user.getUserId().equals(hostId)) {
            for (ParticipantSession participant : roomSession.getParticipants()) {
                roomSession.deleteRoom(participant, hostId);
            }
            roomManager.removeRoom(roomSession);
            return;
        }
        roomSession.leave(user);
    }
}

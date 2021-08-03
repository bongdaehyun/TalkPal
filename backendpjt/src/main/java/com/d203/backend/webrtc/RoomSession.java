package com.d203.backend.webrtc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.kurento.client.Continuation;
import org.kurento.client.MediaPipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PreDestroy;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoomSession implements Closeable {
    private final Logger log = LoggerFactory.getLogger(RoomSession.class);

    private final ConcurrentMap<String, UserSession> participants = new ConcurrentHashMap<>();
    private final MediaPipeline pipeline;
    private final String uuid;

    public String getUuid() {
        return uuid;
    }

    public RoomSession(String uuid, MediaPipeline pipeline) {
        this.uuid = uuid;
        this.pipeline = pipeline;
        log.info("ROOM {} has been created", uuid);
    }

    @PreDestroy
    private void shutdown() {
        this.close();
    }

    public UserSession join(String userId, WebSocketSession session) throws IOException {
        log.info("ROOM {}: adding participant {}", this.uuid, userId);
        final UserSession participant = new UserSession(userId, this.uuid, session, this.pipeline);
        joinRoom(participant);
        participants.put(participant.getUserId(), participant);
        sendParticipantUserId(participant);
        return participant;
    }

    public void deleteRoom(UserSession user, String hostId) throws IOException {
        final JsonObject deleteRoomMsg = new JsonObject();
        if (user.getUserId().equals(hostId))
            deleteRoomMsg.addProperty("id", "leaveHost"); // 호스트
        else
            deleteRoomMsg.addProperty("id", "leaveGuest"); // 참여자

        try {
            user.sendMessage(deleteRoomMsg);
        } catch (final IOException e) {
            log.debug(e.getMessage());
        }
        // removeParticipant의 cancelVideoFrom..
        participants.remove(user.getUserId());
        user.close();
    }

    public void leave(UserSession user) throws IOException {
        log.debug("PARTICIPANT {}: Leaving room {}", user.getUserId(), this.uuid);
        this.removeParticipant(user.getUserId());
        user.close();
    }

    private Collection<String> joinRoom(UserSession newParticipant) throws IOException {
        final JsonObject newParticipantMsg = new JsonObject();
        newParticipantMsg.addProperty("id", "newParticipantArrived");
        newParticipantMsg.addProperty("userId", newParticipant.getUserId());

        final List<String> participantsList = new ArrayList<>(participants.values().size());
        log.debug("ROOM {}: notifying other participants of new participant {}", uuid,
                newParticipant.getUserId());

        for (final UserSession participant : this.getParticipants()) {
            try {
                participant.sendMessage(newParticipantMsg);
            } catch (final IOException e) {
                log.debug("ROOM {}: participant {} could not be notified", uuid, participant.getUserId(), e);
            }
            participantsList.add(participant.getUserId());
        }

        return participantsList;
    }

    private void removeParticipant(String userId) throws IOException {
        participants.remove(userId);

        log.debug("ROOM {}: notifying all users that {} is leaving the room", this.uuid, userId);

        final List<String> unnotifiedParticipants = new ArrayList<>();
        final JsonObject participantLeftJson = new JsonObject();
        participantLeftJson.addProperty("id", "participantLeft");
        participantLeftJson.addProperty("userId", userId);
        for (final UserSession participant : this.getParticipants()) {
            try {
                participant.cancelVideoFrom(userId);
                participant.sendMessage(participantLeftJson);
            } catch (final IOException e) {
                unnotifiedParticipants.add(participant.getUserId());
            }
        }

        if (!unnotifiedParticipants.isEmpty()) {
            log.debug("ROOM {}: The users {} could not be notified that {} left the room", this.uuid,
                    unnotifiedParticipants, userId);
        }

    }

    public void sendParticipantUserId(UserSession user) throws IOException {

        final JsonArray participantsArray = new JsonArray();
        for (final UserSession participant : this.getParticipants()) {
            if (!participant.equals(user)) {
                final JsonElement participantUserId = new JsonPrimitive(participant.getUserId());
                participantsArray.add(participantUserId);
            }
        }

        final JsonObject existingParticipantsMsg = new JsonObject();
        existingParticipantsMsg.addProperty("id", "existingParticipants");
        existingParticipantsMsg.add("data", participantsArray);
        log.debug("PARTICIPANT {}: sending a list of {} participants", user.getUserId(),
                participantsArray.size());
        user.sendMessage(existingParticipantsMsg);
    }

    public Collection<UserSession> getParticipants() {
        return participants.values();
    }

    public UserSession getParticipant(String userId) {
        return participants.get(userId);
    }

    @Override
    public void close() {
        for (final UserSession user : participants.values()) {
            try {
                user.close();
            } catch (IOException e) {
                log.debug("ROOM {}: Could not invoke close on participant {}", this.uuid, user.getUserId(),
                        e);
            }
        }

        participants.clear();

        pipeline.release(new Continuation<Void>() {

            @Override
            public void onSuccess(Void result) throws Exception {
                log.trace("ROOM {}: Released Pipeline", RoomSession.this.uuid);
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("PARTICIPANT {}: Could not release Pipeline", RoomSession.this.uuid);
            }
        });

        log.debug("Room {} closed", this.uuid);
    }

}

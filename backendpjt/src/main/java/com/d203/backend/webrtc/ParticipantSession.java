package com.d203.backend.webrtc;


import com.google.gson.JsonObject;
import org.kurento.client.*;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ParticipantSession implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(ParticipantSession.class);

    private final String userId;
    private final WebSocketSession session;

    private final MediaPipeline pipeline;

    private final String uuid;
    private final WebRtcEndpoint outgoingMedia;
    private final ConcurrentMap<String, WebRtcEndpoint> incomingMedia = new ConcurrentHashMap<>();

    public ParticipantSession(final String userId, String uuid, final WebSocketSession session,
                              MediaPipeline pipeline) {

        this.pipeline = pipeline;
        this.userId = userId;
        this.session = session;
        this.uuid = uuid;
        this.outgoingMedia = new WebRtcEndpoint.Builder(pipeline).build();

        this.outgoingMedia.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {

            @Override
            public void onEvent(IceCandidateFoundEvent event) {
                JsonObject response = new JsonObject();
                response.addProperty("id", "iceCandidate");
                response.addProperty("userId", userId);
                response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                try {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(response.toString()));
                    }
                } catch (IOException e) {
                    log.debug(e.getMessage());
                }
            }
        });
    }

    public WebRtcEndpoint getOutgoingWebRtcPeer() {
        return outgoingMedia;
    }

    public String getUserId() {
        return userId;
    }

    public WebSocketSession getSession() {
        return session;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void receiveVideoFrom(ParticipantSession sender, String sdpOffer) throws IOException {
        log.info("USER {}: connecting with {} in room {}", this.userId, sender.getUserId(), this.uuid);

        log.trace("USER {}: SdpOffer for {} is {}", this.userId, sender.getUserId(), sdpOffer);

        final String ipSdpAnswer = this.getEndpointForUser(sender).processOffer(sdpOffer);
        final JsonObject scParams = new JsonObject();
        scParams.addProperty("id", "receiveVideoAnswer");
        scParams.addProperty("userId", sender.getUserId());
        scParams.addProperty("sdpAnswer", ipSdpAnswer);

        log.trace("USER {}: SdpAnswer for {} is {}", this.userId, sender.getUserId(), ipSdpAnswer);
        this.sendMessage(scParams);
        log.debug("gather candidates");
        this.getEndpointForUser(sender).gatherCandidates();
    }

    private WebRtcEndpoint getEndpointForUser(final ParticipantSession sender) {
        if (sender.getUserId().equals(userId)) {
            log.debug("PARTICIPANT {}: configuring loopback", this.userId);
            return outgoingMedia;
        }

        log.debug("PARTICIPANT {}: receiving video from {}", this.userId, sender.getUserId());

        WebRtcEndpoint incoming = incomingMedia.get(sender.getUserId());
        if (incoming == null) {
            log.debug("PARTICIPANT {}: creating new endpoint for {}", this.userId, sender.getUserId());
            incoming = new WebRtcEndpoint.Builder(pipeline).build();

            incoming.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {
                @Override
                public void onEvent(IceCandidateFoundEvent event) {
                    JsonObject response = new JsonObject();
                    response.addProperty("id", "iceCandidate");
                    response.addProperty("userId", sender.getUserId());
                    response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
                    try {
                        synchronized (session) {
                            session.sendMessage(new TextMessage(response.toString()));
                        }
                    } catch (IOException e) {
                        log.debug(e.getMessage());
                    }
                }
            });

            incomingMedia.put(sender.getUserId(), incoming);
        }

        log.debug("PARTICIPANT {}: obtained endpoint for {}", this.userId, sender.getUserId());
        sender.getOutgoingWebRtcPeer().connect(incoming);

        return incoming;
    }

    public void cancelVideoFrom(final ParticipantSession sender) {
        this.cancelVideoFrom(sender.getUserId());
    }

    public void cancelVideoFrom(final String senderUserId) {
        log.debug("PARTICIPANT {}: canceling video reception from {}", this.userId, senderUserId);
        final WebRtcEndpoint incoming = incomingMedia.remove(senderUserId);

        log.debug("PARTICIPANT {}: removing endpoint for {}", this.userId, senderUserId);
        incoming.release(new Continuation<Void>() {
            @Override
            public void onSuccess(Void result) throws Exception {
                log.trace("PARTICIPANT {}: Released successfully incoming EP for {}",
                        ParticipantSession.this.userId, senderUserId);
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("PARTICIPANT {}: Could not release incoming EP for {}", ParticipantSession.this.userId,
                        senderUserId);
            }
        });
    }

    @Override
    public void close() throws IOException {
        log.debug("PARTICIPANT {}: Releasing resources", this.userId);
        for (final String remoteParticipantUserId : incomingMedia.keySet()) {

            log.trace("PARTICIPANT {}: Released incoming EP for {}", this.userId, remoteParticipantUserId);

            final WebRtcEndpoint ep = this.incomingMedia.get(remoteParticipantUserId);

            ep.release(new Continuation<Void>() {

                @Override
                public void onSuccess(Void result) throws Exception {
                    log.trace("PARTICIPANT {}: Released successfully incoming EP for {}",
                            ParticipantSession.this.userId, remoteParticipantUserId);
                }

                @Override
                public void onError(Throwable cause) throws Exception {
                    log.warn("PARTICIPANT {}: Could not release incoming EP for {}", ParticipantSession.this.userId,
                            remoteParticipantUserId);
                }
            });
        }

        outgoingMedia.release(new Continuation<Void>() {

            @Override
            public void onSuccess(Void result) throws Exception {
                log.trace("PARTICIPANT {}: Released outgoing EP", ParticipantSession.this.userId);
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("USER {}: Could not release outgoing EP", ParticipantSession.this.userId);
            }
        });
    }

    public void sendMessage(JsonObject message) throws IOException {
        log.debug("USER {}: Sending message {}", userId, message);
        synchronized (session) {
            session.sendMessage(new TextMessage(message.toString()));
        }
    }

    public void addCandidate(IceCandidate candidate, String userId) {
        if (this.userId.compareTo(userId) == 0) {
            outgoingMedia.addIceCandidate(candidate);
        } else {
            WebRtcEndpoint webRtc = incomingMedia.get(userId);
            if (webRtc != null) {
                webRtc.addIceCandidate(candidate);
            }
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof ParticipantSession)) {
            return false;
        }
        ParticipantSession other = (ParticipantSession) obj;
        boolean eq = userId.equals(other.userId);
        eq &= uuid.equals(other.uuid);
        return eq;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + userId.hashCode();
        result = 31 * result + uuid.hashCode();
        return result;
    }
}

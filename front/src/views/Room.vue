<template>
  <div>
    <h1>Room</h1>
    <div class="row" v-if="participants">
      <div
        class="col"
        v-for="participant in participants"
        :key="participant.userId"
      >
        <Participant
          :ref="`participant-${participant.userId}`"
          :userId="participant.userId"
          :ws="ws"
        />
      </div>
    </div>
  </div>
</template>

<script>
import kurentoUtils from "kurento-utils";
import Participant from "@/components/Room/Participant.vue";
import _ from "lodash";

export default {
  name: "Room",
  data() {
    return {
      url: "ws://192.168.0.2:8080/groupcall",
      // url: "ws://localhost:8080/groupcall",
      UUID: this.$route.params.UUID,
      userId: this.$store.getters["userStore/getUserId"],
      participants: [],
      participantComponents: [],
      ws: null,
    };
  },
  methods: {
    sendMessage(message) {
      if (this.ws.readyState !== this.ws.OPEN) {
        console.log(
          "[errMessage] Skip, WebSocket session isn't open" + message
        );
        return;
      }
      const jsonMessage = JSON.stringify(message);
      console.log("[sendMessage] message: " + jsonMessage);
      this.ws.send(jsonMessage);
    },
    sendError(message) {
      console.error("[errMessage] " + message);
      this.sendMessage({
        id: "ERROR",
        message: message,
      });
    },
    startVideo(video) {
      video.play();
    },
    onNewParticipant(request) {
      console.log(request);
      this.receiveVideo(request.name);
    },
    onParticipantLeft(request) {
      // console.log("Participant " + request.name + " left");
      // let participant = this.participants[request.name];
      // participant.dispose();
      // delete this.participants[request.name];
    },
    receiveVideoResponse(result) {
      console.log("receiveVideoResponse");
      console.log(this.participantComponents[result.name]);
      // this.participantComponents[result.name].rtcPeer.processAnswer(
      //   result.sdpAnswer,
      //   (error) => {
      //     if (error) return console.error(error);
      //   }
      // );
    },
    callResponse(message) {
      if (message.response != "accepted") {
        console.info("Call not accepted by peer. Closing call");
        stop();
      } else {
        this.webRtcPeer.processAnswer(message.sdpAnswer, (error) => {
          if (error) return console.error(error);
        });
      }
    },
    async receiveVideo(senderId) {
      console.log(senderId);
      let res = await this.$store.dispatch(
        "userStore/requestUserInfo",
        senderId
      );

      const userInfo = { userId: senderId, ...res.data };
      this.participants.push(userInfo);

      this.$nextTick(() => {
        // NOTE: 접속사 participant 컴포넌트 생성
        let participant = this.$refs[`participant-${senderId}`][0];

        //  NOTE: 접속자 비디오 컴포넌트 추가
        this.participantComponents[senderId] = participant;

        let video = participant.getVideoElement();

        let options = {
          remoteVideo: video,
          onicecandidate: participant.onIceCandidate.bind(participant),
        };

        participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerRecvonly(
          options,
          function (error) {
            if (error) {
              return console.error(error);
            }
            this.generateOffer(
              participant.offerToReceiveVideo.bind(participant)
            );
          }
        );
      });
    },
    async onExistingParticipants(msg) {
      const constraints = {
        audio: true,
        video: {
          mandatory: {
            maxWidth: 320,
            maxFrameRate: 15,
            minFrameRate: 15,
          },
        },
      };
      console.log(this.userId + " registered in room " + this.UUID);
      // NOTE: 유저 정보 요청
      let res = await this.$store.dispatch(
        "userStore/requestUserInfo",
        this.userId
      );
      const userInfo = { userId: this.userId, ...res.data };
      // NOTE: 참가자 추가
      this.participants.push(userInfo);

      // NOTE: this.$nextTick - UI 작업이 끝나고 다음 작업을 실행함.
      this.$nextTick(() => {
        // NOTE: participant 컴포넌트
        let participant = this.$refs[`participant-${this.userId}`][0];

        //  NOTE: 컴포넌트 추가
        this.participantComponents[this.userId] = participant;

        // NOTE: 비디오 엘리먼트 가져오기
        let video = participant.getVideoElement();

        let options = {
          localVideo: video,
          mediaConstraints: constraints,
          onicecandidate: participant.onIceCandidate.bind(participant),
        };
        participant.rtcPeer = new kurentoUtils.WebRtcPeer.WebRtcPeerSendonly(
          options,
          function (error) {
            if (error) {
              return console.error(error);
            }
            this.generateOffer(
              participant.offerToReceiveVideo.bind(participant)
            );
          }
        );
      });
    },
    connect() {
      this.ws = new WebSocket(this.url);
      this.ws.onmessage = (message) => {
        let parsedMessage = JSON.parse(message.data);
        console.log(parsedMessage);
        switch (parsedMessage.id) {
          case "existingParticipants":
            this.onExistingParticipants(parsedMessage);
            break;
          case "newParticipantArrived":
            this.onNewParticipant(parsedMessage);
            break;
          case "participantLeft":
            this.onParticipantLeft(parsedMessage);
            break;
          case "receiveVideoAnswer":
            this.receiveVideoResponse(parsedMessage);
            break;
          case "iceCandidate":
            this.participantComponents[
              parsedMessage.name
            ].rtcPeer.addIceCandidate(parsedMessage.candidate, (error) => {
              if (error) {
                console.error("Error adding candidate: " + error);
                return;
              }
            });
            break;
          default:
            console.error("Unrecognized message", parsedMessage);
        }
      };
      this.ws.onopen = () => {
        console.log(this.ws);
        this.join();
      };
    },
    join() {
      let message = {
        id: "joinRoom",
        name: this.userId,
        room: this.UUID,
      };
      this.sendMessage(message);
    },
  },
  created() {
    this.connect();
  },
  components: {
    Participant,
  },
};
</script>

<style>
</style>
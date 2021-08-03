<template>
  <div>
    <v-container fluid class="pa-0">
      <v-row style="height: 95vh">
        <!-- NOTE: 화상 구역 -->
        <div
          class="pa-0"
          :class="[showGuide ? 'col-9' : 'col-12']"
          style="width: 100%; height: 100%"
        >
          <ResizeDetector observe-width observe-height @resize="onResize" />
          <v-card
            class="d-flex justify-center align-center"
            :class="[isRow ? `flex-row` : `flex-column`]"
            outlined
            style="background-color: white; height: 100%"
          >
            <div v-for="participant in participants" :key="participant.userId">
              <Participant
                :ref="`participant-${participant.userId}`"
                :userId="participant.userId"
                :ws="ws"
                :videoWidth="videoWidth"
                :videoHeight="videoHeight"
              />
            </div>
          </v-card>
        </div>
        <!-- NOTE:가이드 & 기타 버튼 구역 -->
        <div
          :class="{ 'col-3': showGuide }"
          style="background-color: red"
        ></div>
      </v-row>
    </v-container>
    <v-bottom-navigation app>
      <v-btn @click="leaveRoom">
        <v-icon>나가기</v-icon>
      </v-btn>
      <v-btn @click="toggleGuide">
        <v-icon>가이드</v-icon>
      </v-btn>
    </v-bottom-navigation>
  </div>
</template>

<script>
import kurentoUtils from "kurento-utils";
import Participant from "@/components/Room/Participant.vue";
import ResizeDetector from "vue-resize-detector";
import _ from "lodash";

export default {
  name: "Room",
  data() {
    return {
      hostId: null,
      roomId: null,
      socketUrl: process.env.VUE_APP_SOCKET_URL,
      UUID: this.$route.params.UUID,
      userId: this.$store.getters["userStore/getUserId"],
      participants: [],
      participantComponents: [],
      ws: null,
      showGuide: false,
      innerHeight: window.innerHeight,
      innerWidth: window.innerWidth,
      isRow: true,
      videoWidth: null,
      videoHeight: null,
    };
  },
  computed: {
    windowHeight() {
      return `${window.innerHeight}px`;
    },
    windowWidth() {
      return `${window.innerWidth}px`;
    },
  },
  methods: {
    requestRoomInfo() {
      this.$store
        .dispatch("roomStore/reqeustRoomInfo", { uuid: this.UUID })
        .then((res) => {
          console.log(res);
          this.hostId = res.data.hostId;
          this.roomId = res.data.roomId;
        });
    },
    toggleGuide() {
      this.showGuide = !this.showGuide;
    },
    handleResize() {
      this.innerHeight = window.innerHeight;
    },
    onResize(width, height) {
      if (width * 3 >= height * 4) {
        this.isRow = true;
        this.videoWidth = width / 2;
        this.videoHeight = height;
      } else {
        this.isRow = false;
        this.videoWidth = width;
        this.videoHeight = height / 2;
      }
    },
    sendMessage(message) {
      if (this.ws.readyState !== this.ws.OPEN) {
        this.$log("[errMessage] Skip, WebSocket session isn't open" + message);
        return;
      }
      const jsonMessage = JSON.stringify(message);
      this.$log("[sendMessage] message: " + jsonMessage);
      this.ws.send(jsonMessage);
    },
    sendError(message) {
      this.$log("[errMessage] " + message);
      this.sendMessage({
        id: "ERROR",
        message: message,
      });
    },
    startVideo(video) {
      video.play();
    },
    onJoinQuestion(request) {
      // NOTE: 다른 유저의 입장 요청 (uuid, requestUserId, hostId)
      // TODO: 요청 수락/거절 띄워서 입력 받기 (임시로 check 사용)
      let check = true;

      this.$log("getJoinQuestion");

      let message = {
        id: "joinResponse",
        requestUserId: request.requestUserId,
        uuid: request.uuid,
        answer: check,
      };

      this.sendMessage(message);
    },
    onNewParticipant(request) {
      this.$log(request);
      this.receiveVideo(request.userId);
    },
    onParticipantLeft(request) {
      this.$log("Participant " + request.userId + " left");

      const index = _.findIndex(this.participants, (participant) => {
        return participant.userId == request.userId;
      });

      this.participants.splice(index, 1);

      let participantComponent = this.participantComponents[request.userId];
      participantComponent.dispose();

      delete this.participantComponents[request.userId];
    },
    receiveVideoResponse(result) {
      this.$log("receiveVideoResponse");
      this.$log(this.participantComponents[result.userId]);

      this.participantComponents[result.userId].rtcPeer.processAnswer(
        result.sdpAnswer,
        (error) => {
          if (error) return this.$error(error);
        }
      );
    },
    callResponse(message) {
      if (message.response != "accepted") {
        this.$log("Call not accepted by peer. Closing call");
        stop();
      } else {
        this.webRtcPeer.processAnswer(message.sdpAnswer, (error) => {
          if (error) return this.$error(error);
        });
      }
    },
    async receiveVideo(senderId) {
      this.$log(senderId);
      let res = await this.$store.dispatch(
        "userStore/requestUserInfo",
        senderId
      );

      const userInfo = { userId: Number(senderId), ...res.data };
      this.participants.push(userInfo);

      this.$nextTick(() => {
        let participant = this.$refs[`participant-${senderId}`][0];
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
              return this.$error(error);
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
      this.$log(this.userId + " registered in room " + this.UUID);
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
              return this.$error(error);
            }
            this.generateOffer(
              participant.offerToReceiveVideo.bind(participant)
            );
          }
        );
        // NOTE: 신규 참가자에게 기존 참가자들 비디오 표시
        msg.data.forEach(this.receiveVideo);
      });
    },
    join() {
      let message = {
        id: "joinRoom",
        userId: this.userId,
        uuid: this.UUID,
      };
      this.sendMessage(message);
    },
    leaveRoom() {
      this.$log("leaveRoom");
      // 1. Host가 나갈 떄 Host => leaveHost 메세지 수신, Guest => leaveGeust 메세지 수신
      // 2. Guest가 혼자 나갈 때 => 메세지 수신 X
      let message = {
        id: "leaveRoom",
        hostId: this.hostId,
      };
      this.sendMessage(message);
      // this.exitRoom();
    },
    leaveHost() {
      this.$log("leaveHost");
      // 방 삭제 API 호출
      this.$store
        .dispatch("roomStore/requestDelete", {
          roomId: this.roomId,
          header: this.$store.getters["userStore/getHeader"],
        })
        .then((res) => {
          console.log(res);
          this.exitRoom();
        });
    },
    leaveGuest() {
      this.$log("leaveGuest");
      // 방 나가기
      this.exitRoom();
    },
    exitRoom() {
      this.$store.dispatch("roomStore/exitRoom");
      this.ws.close();
      this.$router.push({ name: "Rooms" });
    },
    connect() {
      this.ws = new WebSocket(this.socketUrl);
      this.ws.onmessage = (message) => {
        let parsedMessage = JSON.parse(message.data);
        console.log("[parsedMessage]");
        console.log(parsedMessage);
        // this.$info(`[parsedMessage] : ${parsedMessage}`);
        switch (parsedMessage.id) {
          case "joinQuestion":
            this.onJoinQuestion(parsedMessage);
            break;
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
          //  Host 가 나갈 때, 방 삭제 API 호출 필요
          case "leaveHost":
            this.leaveHost();
            break;
          // Host 가 나갈 때, Guest 강제 퇴실 혹은 Guest 가 혼자 나갈 때,
          case "leaveGuest":
            this.leaveGuest();
            break;
          case "iceCandidate":
            this.participantComponents[
              parsedMessage.userId
            ].rtcPeer.addIceCandidate(parsedMessage.candidate, (error) => {
              if (error) {
                this.$error("Error adding candidate: " + error);
                return;
              }
            });
            break;
          default:
            this.$error("Unrecognized message", parsedMessage);
        }
      };
      this.ws.onopen = () => {
        this.$log(this.ws);
        this.join();
      };
    },
  },
  mounted() {
    window.addEventListener("resize", this.handleResize);
  },
  created() {
    this.requestRoomInfo();
    this.connect();
    this.$store.dispatch("roomStore/enterRoom");
  },
  beforeDestroy() {
    this.$log("Room Destory");
    this.leaveRoom();
  },
  components: {
    Participant,
    ResizeDetector,
  },
};
</script>


<style scoped>
</style>
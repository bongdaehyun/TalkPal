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
            style="background-color: black; height: 100%"
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
  // TODO: leaveroom 메세지 보낼 때 hostUserId
  // TODO: Host가 방 나갈 때 방 삭제 API도 호출
  methods: {
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
      console.log(this.videoWidth, this.videoHeight);
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
    onNewParticipant(request) {
      this.$log(request);
      this.receiveVideo(request.userId);
    },
    onParticipantLeft(request) {
      this.$log("Participant " + request.userId + " left");
      let participant = this.participants[request.userId];
      participant.dispose();
      delete this.participants[request.userId];
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

      const userInfo = { userId: senderId, ...res.data };
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
      this.participants.push({ userId: this.userId + 1, ...res.data });

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
      });
    },
    connect() {
      this.ws = new WebSocket(this.socketUrl);
      this.ws.onmessage = (message) => {
        let parsedMessage = JSON.parse(message.data);
        this.$info(`[parsedMessage] : ${parsedMessage}`);
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
      this.$destroy();
    },
  },
  mounted() {
    window.addEventListener("resize", this.handleResize);
  },
  created() {
    this.connect();
    this.$store.dispatch("roomStore/enterRoom");
  },
  beforeDestroy() {
    this.$log("Room Destory");
    this.$store.dispatch("roomStore/exitRoom");
    this.sendMessage({
      id: "leaveRoom",
    });
    this.ws.close();
    this.$router.push({ name: "Rooms" });
  },
  components: {
    Participant,
    ResizeDetector,
  },
};
</script>


<style scoped>
</style>
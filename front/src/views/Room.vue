<template>
  <div>
    <v-container fluid class="pa-0" style="height: 100vh">
      <v-row style="background-color: black; height: 100%">
        <!-- NOTE: 화상 구역 -->
        <div
          class="pa-0"
          :class="[showGuideChat ? 'col-9' : 'col-12']"
          style="height: 100%"
        >
          <ResizeDetector observe-width observe-height @resize="onResize" />
          <v-card
            class="d-flex justify-center align-center"
            :class="[isRow ? `flex-row` : `flex-column`]"
            style="height: 100%; background-color: black"
            outlined
          >
            <div v-for="participant in participants" :key="participant.userId">
              <Participant
                class="pa-6"
                v-show="checkMobile(participant.userId)"
                :ref="`participant-${participant.userId}`"
                :userId="participant.userId"
                :ws="ws"
                :videoWidth="videoWidth"
                :videoHeight="videoHeight"
              />
            </div>
          </v-card>
        </div>
        <!-- NOTE:가이드 & 채팅 -->
        <div
          v-if="showGuideChat"
          :class="{ ' col-3': showGuideChat }"
          class="d-flex flex-column"
          style="height: 100%"
        >
          <Guide v-if="showGuide" :height="guideHeight" />
          <Chat
            v-if="showChat"
            :items="msgList"
            :height="chatHeight"
            @onSubmitMessage="submitMessage"
          />
        </div>
      </v-row>
    </v-container>
    <Navigation
      @onLeaveRoom="leaveRoom"
      @onToggleChat="showChat = !showChat"
      @onToggleGuide="showGuide = !showGuide"
    />
    <QuestionDialog
      :timer="timer"
      :joinQuestionDialog="joinQuestionDialog"
      @onQuestionResponse="questionResponse"
    />
  </div>
</template>

<script>
// NOTE: 컴포넌트
import Participant from "@/components/Room/Participant.vue";
import Chat from "@/components/Room/Chat.vue";
import Guide from "@/components/Room/Guide.vue";
import QuestionDialog from "@/components/Room/QuestionDialog.vue";
import Navigation from "@/components/Room/Navigation.vue";

// NOTE: MIXIN
import WebRTCMixin from "@/mixin/WebRTCMixin.js";
import isMobile from "@/mixin/isMobile.js";

// NOTE: 외부 모듈
import adapter from "webrtc-adapter";
import ResizeDetector from "vue-resize-detector";
import _ from "lodash";

export default {
  name: "Room",
  mixins: [WebRTCMixin, isMobile],
  data() {
    return {
      // NOTE: 채팅 관련 변수
      msgList: [],

      // NOTE: 레이아웃 관련 변수
      showGuide: false,
      showChat: false,
      isRow: true,
      innerHeight: window.innerHeight,
      innerWidth: window.innerWidth,
      videoWidth: null,
      videoHeight: null,
    };
  },
  computed: {
    guideHeight() {
      if (this.showChat) {
        return "50%";
      } else {
        return "100%";
      }
    },
    chatHeight() {
      if (this.showGuide) {
        return "50%";
      } else {
        return "100%";
      }
    },
    showGuideChat() {
      return this.showGuide || this.showChat;
    },
    videoContaierHeight() {
      return `${window.innerHeight - 56} px`;
    },
    windowHeight() {
      return `${window.innerHeight}px`;
    },
    windowWidth() {
      return `${window.innerWidth}px`;
    },
  },
  methods: {
    checkMobile(userId) {
      console.log("checkMobile");
      console.log(this.isMobile);
      if (this.isMobile === false) {
        return true;
      }
      const myUserId = this.$store.getters["userStore/getUserId"];
      console.log(userId, myUserId);
      console.log(userId == myUserId);

      if (userId == myUserId) {
        return false;
      } else {
        return true;
      }
    },
    requestRoomInfo() {
      this.$store
        .dispatch("roomStore/reqeustRoomInfo", { uuid: this.UUID })
        .then((res) => {
          console.log(res);
          this.hostId = res.data.hostId;
          this.roomId = res.data.roomId;
        });
    },
    handleResize() {
      this.innerHeight = window.innerHeight;
    },
    onResize(width, height) {
      if (width * 3 >= height * 4) {
        this.isRow = true;
        this.videoWidth = width / 2.5;
        this.videoHeight = height * 0.9;
      } else {
        this.isRow = false;
        this.videoWidth = width * 0.9;
        this.videoHeight = height / 2.5;
      }
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
    Chat,
    Guide,
    QuestionDialog,
    Navigation,
  },
};
</script> 
<template>
  <div :style="{ height: containerHeight }" style="background-color: #202124">
    <v-container
      fluid
      class="ma-0 maxHeight"
      :class="[isMobile ? 'd-flex flex-column pa-0' : 'row pa-3']"
    >
      <!-- NOTE: 화상 구역 -->
      <div
        class="maxHeight"
        :class="[
          { 'col-10': showGuideChat && !isMobile },
          { 'col-12': !showGuideChat && !isMobile },
          { 'd-flex justify-center align-center maxHeight': !isMobile },
        ]"
        style="background-color: #202124"
      >
        <ResizeDetector observe-width observe-height @resize="onResize" />
        <div v-for="participant in participants" :key="participant.userId">
          <Participant
            v-show="checkMobile(participant.userId)"
            :ref="`participant-${participant.userId}`"
            :userId="participant.userId"
            :ws="ws"
            :videoWidth="videoWidth"
            :videoHeight="videoHeight"
          />
        </div>
      </div>
      <!-- NOTE: 가이드 & 채팅 -->
      <!-- NOTE: 모바일 버전 -->
      <div
        v-if="showGuideChat && isMobile"
        class="d-flex flex-column-reverse maxWidth maxHeight"
        style="position: fixed; bottom: 56px"
      >
        <Guide
          v-if="showGuide"
          :height="guideHeight"
          :hostLang="hostLang"
          :guestLang="guestLang"
        />
        <Chat
          v-if="showChat"
          :items="msgList"
          :height="chatHeight"
          @onSubmitMessage="submitMessage"
        />
      </div>
      <!-- NOTE: 데스크탑 버전 -->
      <v-sheet
        v-if="showGuideChat && !isMobile"
        :class="{ 'col-2': showGuideChat }"
        class="d-flex flex-column maxHeight"
        rounded="xl"
      >
        <Guide
          v-if="showGuide"
          :height="guideHeight"
          :hostLang="hostLang"
          :guestLang="guestLang"
        />
        <Chat
          v-if="showChat"
          :items="msgList"
          :height="chatHeight"
          @onSubmitMessage="submitMessage"
        />
      </v-sheet>
    </v-container>
    <Navigation
      @onLeaveRoom="leaveRoom"
      @onToggleChat="toggleChat"
      @onToggleGuide="toggleGuide"
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
      videoWidth: null,
      videoHeight: null,
      innerHeight: window.innerHeight,

      hostLang: null,
      guestLang: null,
    };
  },
  computed: {
    containerHeight() {
      return `${this.innerHeight - 56}px`;
    },
    guideHeight() {
      if (this.isMobile) {
        return "50%";
      }
      if (this.showChat) {
        return "50%";
      } else {
        return "100%";
      }
    },
    chatHeight() {
      if (this.isMobile) {
        return "50%";
      }
      if (this.showGuide) {
        return "50%";
      } else {
        return "100%";
      }
    },
    showGuideChat() {
      return this.showGuide || this.showChat;
    },
  },
  methods: {
    toggleChat() {
      this.showGuide = false;
      this.showChat = !this.showChat;
    },
    toggleGuide() {
      this.showGuide = !this.showGuide;
      this.showChat = false;
    },
    checkMobile(userId) {
      if (this.isMobile === false) {
        return true;
      }
      const myUserId = this.$store.getters["userStore/getUserId"];

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
          this.hostId = res.data.hostId;
          this.roomId = res.data.roomId;
          this.hostLang = res.data.host_lang;
          this.guestLang = res.data.guest_lang;
        });
    },
    onResize(width, height) {
      if (this.isMobile) {
        this.videoHeight = height * 0.95;
        this.videoWidth = width;
        return;
      }
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
    handleResize() {
      this.innerHeight = window.innerHeight;
    },
  },
  mounted() {
    window.addEventListener("resize", this.handleResize);
  },
  unmounted() {
    window.removeEventListener("resize", this.handleResize);
  },
  created() {
    this.requestRoomInfo();
    this.connect();
    this.$store.dispatch("roomStore/enterRoom");
  },
  beforeDestroy() {
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
<style scoped>
.maxHeight {
  height: 100%;
}
.maxWidth {
  width: 100%;
}
</style>
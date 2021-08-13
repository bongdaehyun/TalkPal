<template>
  <div
    style="
      position: fixed;
      bottom: 0px;
      background-color: #202124;
      height: 56px;
      width: 100vw;
    "
    class="d-flex justify-space-between"
  >
    <div class="d-flex" style="flex: 1"></div>
    <div class="d-flex align-center justify-center" style="flex: 3">
      <v-tooltip top>
        <template v-slot:activator="{ on }">
          <v-btn
            @click="toggleMic"
            class="me-3"
            :color="micIcon.color"
            small
            fab
            v-on="on"
          >
            <v-icon color="white">{{ micIcon.icon }}</v-icon>
          </v-btn>
        </template>
        <span>{{ micIcon.tip }}</span>
      </v-tooltip>
      <!-- NOTE: 나가기 버튼  -->
      <v-btn
        small
        @click="leaveRoom"
        fab
        color="#EA5044"
        :disabled="activeLeave"
      >
        <v-icon color="white">mdi-location-exit</v-icon>
      </v-btn>
      <v-tooltip top>
        <template v-slot:activator="{ on }">
          <v-btn
            @click="toggleCamera"
            class="ms-3"
            small
            fab
            :color="cameraIcon.color"
            v-on="on"
          >
            <v-icon color="white">{{ cameraIcon.icon }}</v-icon>
          </v-btn>
        </template>
        <span>{{ cameraIcon.tip }}</span>
      </v-tooltip>
    </div>
    <div class="d-flex align-center justify-end" style="flex: 1">
      <v-btn v-if="activeChat" @click="toggleChat()" fab icon color="primary">
        <v-icon>mdi-message-text</v-icon>
      </v-btn>
      <v-btn v-else @click="toggleChat()" fab icon color="white">
        <v-icon>mdi-message-text</v-icon>
      </v-btn>
      <v-btn v-if="activeGuide" fab icon @click="toggleGuide()" color="primary">
        <v-icon>mdi-notebook</v-icon>
      </v-btn>
      <v-btn v-else fab icon @click="toggleGuide()" color="white">
        <v-icon>mdi-notebook</v-icon>
      </v-btn>
    </div>
  </div>
</template>

<script>
import isMobile from "@/mixin/isMobile.js";

export default {
  name: "Navigation",
  mixins: [isMobile],
  data() {
    return {
      micIcon: {
        icon: "mdi-microphone",
        status: true,
        color: "#434649",
        tip: this.$t("mic_off"),
      },
      cameraIcon: {
        icon: "mdi-camera",
        status: true,
        color: "#434649",
        tip: this.$t("camera_off"),
      },
      activeChat: false,
      activeGuide: false,
      activeLeave: true,
    };
  },
  methods: {
    toggleCamera() {
      this.$emit("toggleCamera");
      this.cameraIcon.status = !this.cameraIcon.status;
      if (this.cameraIcon.status) {
        this.cameraIcon.icon = "mdi-camera";
        this.cameraIcon.color = "#434649";
        this.cameraIcon.tip = this.$t("camera_off");
      } else {
        this.cameraIcon.icon = "mdi-camera-off";
        this.cameraIcon.color = "#EA5044";
        this.cameraIcon.tip = this.$t("camera_on");
      }
    },
    toggleMic() {
      this.$emit("toggleMic");
      this.micIcon.status = !this.micIcon.status;

      if (this.micIcon.status) {
        this.micIcon.icon = "mdi-microphone";
        this.micIcon.color = "#434649";
        this.micIcon.tip = this.$t("mic_off");
      } else {
        this.micIcon.icon = "mdi-microphone-off";
        this.micIcon.color = "#EA5044";
        this.micIcon.tip = this.$t("mic_on");
      }
    },
    leaveRoom() {
      this.$emit("onLeaveRoom");
    },
    toggleChat() {
      this.activeChat = !this.activeChat;
      this.activeGuide = false;
      this.$emit("onToggleChat");
    },
    toggleGuide() {
      this.activeGuide = !this.activeGuide;
      this.activeChat = false;
      this.$emit("onToggleGuide");
    },
  },
  mounted() {
    setTimeout(() => {
      this.activeLeave = false;
    }, 4000);
  },
};
</script>

<style>
</style>
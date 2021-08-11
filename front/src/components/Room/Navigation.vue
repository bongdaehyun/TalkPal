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
      <!-- TODO: 마이크 ON/OFF 함수 구현 -->
      <v-btn small fab color="#434649" class="me-3">
        <v-icon color="white">mdi-microphone-off </v-icon>
      </v-btn>
      <!-- NOTE: 나가기 버튼  -->
      <v-btn small @click="leaveRoom" fab color="#EA5044" :disabled="activeLeave">
        <v-icon color="white">mdi-location-exit</v-icon>
      </v-btn>
      <!-- TODO: 카메라 ON/OFF 함수 구현 -->
      <v-btn small fab color="#434649" class="ms-3">
        <v-icon color="white">mdi-camera-off</v-icon>
      </v-btn>
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
  <!-- <v-bottom-navigation elevation="0" app background-color="#202124">
  </v-bottom-navigation> -->
</template>

<script>
import isMobile from "@/mixin/isMobile.js";

export default {
  name: "Navigation",
  mixins: [isMobile],
  data() {
    return {
      activeChat: false,
      activeGuide: false,
      activeLeave: true,
    };
  },
  methods: {
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
    }, 4000)
  },
  
};
</script>

<style>
</style>
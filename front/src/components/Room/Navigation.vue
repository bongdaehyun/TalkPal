<template>
  <v-bottom-navigation app dark>
    <!-- TODO: 마이크 ON/OFF 함수 구현 -->
    <v-btn>
      <v-icon>mdi-microphone-off </v-icon>
    </v-btn>
    <!-- TODO: 카메라 ON/OFF 함수 구현 -->
    <v-btn>
      <v-icon>mdi-camera-off</v-icon>
    </v-btn>
    <!-- NOTE: 나가기 버튼  -->
    <v-btn @click="leaveRoom">
      <v-icon>mdi-location-exit</v-icon>
    </v-btn>
    <!-- TODO: 채팅창 구현 -->
    <v-btn @click="toggleChat()">
      <v-icon v-if="activeChat" color="primary">mdi-message-text</v-icon>
      <v-icon v-else>mdi-message-text</v-icon>
    </v-btn>
    <v-btn @click="toggleGuide()">
      <v-icon v-if="activeGuide" color="primary">mdi-notebook</v-icon>
      <v-icon v-else>mdi-notebook</v-icon>
    </v-btn>
  </v-bottom-navigation>
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
    };
  },
  methods: {
    leaveRoom() {
      this.$emit("onLeaveRoom");
    },
    toggleChat() {
      this.activeChat = !this.activeChat;
      this.activeGuide = this.isMobile ? false : this.activeGuide;
      this.$emit("onToggleChat");
    },
    toggleGuide() {
      this.activeGuide = !this.activeGuide;
      this.activeChat = this.isMobile ? false : this.activeChat;
      this.$emit("onToggleGuide");
    },
  },
};
</script>

<style>
</style>
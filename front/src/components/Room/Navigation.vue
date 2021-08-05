<template>
  <v-bottom-navigation class="" absolute app>
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
      <v-icon>mdi-message-text</v-icon>
    </v-btn>
    <v-btn @click="toggleGuide()">
      <v-icon>mdi-notebook</v-icon>
    </v-btn>
  </v-bottom-navigation>
</template>

<script>
export default {
  name: "Navigation",
  methods: {
    leaveRoom() {
      this.$emit("onLeaveRoom");
    },
    toggleChat() {
      this.$emit("onToggleChat");
    },
    toggleGuide() {
      this.$emit("onToggleGuide");
    },
  },
};
</script>

<style>
</style>
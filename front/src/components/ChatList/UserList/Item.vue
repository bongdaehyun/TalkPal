<template>
  <v-list dense>
    <v-list-item-group color="primary">
      <v-list-item @click="onSelectChatRoom">
        <v-list-item-avatar size="50">
          <v-img :src="profilePath"></v-img>
        </v-list-item-avatar>
        <v-list-item-content>
          <v-list-item-title v-text="chatRoom.nickName"></v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list-item-group>
  </v-list>
</template>

<script>
import getProfilePath from "@/mixin/getProfilePath.js";
export default {
  name: "item",
  mixins: [getProfilePath],

  data() {
    return {
      profilePath: null,
    }
  },
  props: {
    chatRoom: {
      type: Object,
    },
  },
  methods: {
    onSelectChatRoom() {
      this.$emit("onSelectChatRoom", this.chatRoom.chatRoomId);
    },
  },
  created() {
    this.profilePath = this.getProfilePath(this.chatRoom.profilePath);
  },
};
</script>

<style>
</style>
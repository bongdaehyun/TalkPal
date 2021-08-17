<template>
  <div class="d-flex align-center pa-3" @click="onSelectChatRoom">
    <v-avatar size="48">
      <v-img :src="profilePath"></v-img>
    </v-avatar>
    <span class="ms-3 font-weight-black">{{ chatRoom.nickName }} </span>
  </div>
</template>

<script>
import getProfilePath from "@/mixin/getProfilePath.js";
export default {
  name: "item",
  mixins: [getProfilePath],

  data() {
    return {
      userId: this.$store.getters["userStore/getUserId"],
    };
  },
  computed: {
    profilePath() {
      return this.getProfilePath(this.chatRoom.imgPath);
    },
  },
  props: {
    chatRoom: {
      type: Object,
    },
  },
  methods: {
    async onSelectChatRoom() {
      // NOTE : 채팅 방 선택
      await this.$store.dispatch("chatStore/setSelectChatRoomId", {
        selectChatRoomId: this.chatRoom.chatRoomId,
      });

      // NOTE : 채팅 목록 클리어
      await this.$store.dispatch("chatStore/clearMsgList");

      // NOTE : 채팅 목록 불러오기
      let res = await this.$store.dispatch("chatStore/requestChatMessageList", {
        chatRoomId: this.chatRoom.chatRoomId,
      });
      // NOTE : 채팅 목록 저장
      await res.data.chatMessageList.forEach((chatMessage) => {
        this.$store.dispatch("chatStore/pushMsgList", { chatMessage });
      });

      // NOTE: 상대방 ID 요청
      res = await this.$store.dispatch("chatStore/requestOpponentId", {
        userId: this.userId,
        chatRoomId: this.chatRoom.chatRoomId,
      });

      await this.$store.dispatch("chatStore/setOpponentId", {
        opponentId: res.data,
      });
    },
  },
};
</script>

<style>
</style>
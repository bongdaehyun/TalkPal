<template>
  <div class="d-flex justify-space-between align-center pa-3">
    <div @click="onSelectChatRoom" style="cursor: pointer">
      <v-avatar size="48">
        <v-img :src="profilePath"></v-img>
      </v-avatar>
      <span class="ms-3 font-weight-black">{{ chatRoom.nickName }} </span>
    </div>
    <div>
      <v-btn @click="deleteChatRoom" icon> ❌ </v-btn>
    </div>
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
    // NOTE: 채팅방 삭제
    async deleteChatRoom() {
      // 방 삭제
      try {
        const chatRoomId = this.chatRoom.chatRoomId;
        const url = "chatStore/deleteChatRoom";

        await this.$store.dispatch(url, { chatRoomId });
      } catch (error) {
        console.log(error);
      }

      // NOTE: 방 목록 다시 설정
      try {
        // NOTE: 채팅 관련 state 삭제
        this.$store.dispatch("chatStore/clearState");

        // NOTE : 채팅 방 목록 불러오기
        const userId = this.userId;
        const url = "chatStore/requestChatRoomList";
        const res = await this.$store.dispatch(url, { userId });

        // NOTE: 채팅 방 목록 저장
        const chatRooms = res.data.chatRoomList;
        await this.$store.dispatch("chatStore/setChatRooms", { chatRooms });
      } catch (error) {
        console.log(error);
      }
    },
    // NOTE : 채팅 방 선택
    async onSelectChatRoom() {
      let url = "chatStore/setSelectChatRoomId";
      await this.$store.dispatch(url, {
        selectChatRoomId: this.chatRoom.chatRoomId,
      });

      // NOTE : 채팅 목록 클리어
      url = "chatStore/clearMsgList";

      await this.$store.dispatch(url);

      // NOTE : 채팅 목록 불러오기
      const chatRoomId = this.chatRoom.chatRoomId;
      url = "chatStore/requestChatMessageList";
      let res = await this.$store.dispatch(url, { chatRoomId });

      // NOTE : 채팅 목록 저장
      url = "chatStore/pushMsgList";
      await res.data.chatMessageList.forEach((chatMessage) => {
        this.$store.dispatch(url, { chatMessage });
      });

      // NOTE: 상대방 ID 요청
      url = "chatStore/requestOpponentId";
      const payload = {
        userId: this.userId,
        chatRoomId: this.chatRoom.chatRoomId,
      };
      res = await this.$store.dispatch(url, payload);

      // NOTE: 채팅 상대 ID 저장
      url = "chatStore/setOpponentId";
      const opponentId = res.data;
      await this.$store.dispatch(url, { opponentId });
    },
  },
};
</script>

<style>
</style>
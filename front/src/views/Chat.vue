<template>
  <v-container class="pa-0">
    <div class="d-flex justify-center">
      <div
        class="row no-gutters"
        :class="[isMobile ? 'col-10' : 'col-8']"
        :style="{ height: innerHeight + `px` }"
      >
        <UserList v-if="chatRooms" :chatRooms="chatRooms" />
        <ChatRoom
          v-if="opponentId"
          ref="chatRoom"
          :ws="ws"
          @onSubmitMessage="submitMessage"
          @onSendMessage="sendMessage"
        />
        <v-sheet v-else color="white" class="col-9" style="height: 100%">
          <v-img src="@/assets/image/talking.png"></v-img>
        </v-sheet>
      </div>
    </div>
  </v-container>
</template>

<script>
import UserList from "@/components/ChatList/UserList";
import ChatRoom from "@/components/ChatList/ChatRoom";
import isMobile from "@/mixin/isMobile.js";

export default {
  mixins: [isMobile],
  data() {
    return {
      ws: null,
      socketUrl: process.env.VUE_APP_SOCKET_URL,
      userId: this.$store.getters["userStore/getUserId"],
      chatRooms: this.$store.getters["chatStore/getChatRooms"],
      innerHeight: window.innerHeight - (60 + 64),
    };
  },
  computed: {
    opponentId() {
      return this.$store.getters["chatStore/getOpponentId"];
    },
  },
  methods: {
    connect() {
      this.ws = new WebSocket(this.socketUrl);

      this.ws.onopen = () => {
        let msg = {
          id: "newUserDM",
          userId: this.userId,
        };
        this.sendMessage(msg);
      };
    },

    // NOTE: 채팅방 목록 요청
    requestChatRoomList() {
      this.$store
        .dispatch("chatStore/requestChatRoomList", {
          userId: this.userId,
        })
        .then((res) => {
          this.$store.dispatch("chatStore/setChatRooms", {
            chatRooms: res.data.chatRoomList,
          });
        });
    },
    submitMessage(data) {
      this.$store.dispatch("chatStore/sendDirectMessage", data);
    },
    sendMessage(message) {
      if (this.ws.readyState !== this.ws.OPEN) {
        return;
      }
      const jsonMessage = JSON.stringify(message);
      this.ws.send(jsonMessage);
    },

    handleResize() {
      this.innerHeight = window.innerHeight - (60 + 64);
    },
  },

  created() {
    this.$store.dispatch("chatStore/clearState");
    this.connect();
    this.requestChatRoomList();
    window.addEventListener("resize", this.handleResize);
  },
  components: {
    UserList,
    ChatRoom,
  },
};
</script>

<style scoped>
</style>
<template>
  <v-container class="pa-0 mt-3">
    <!-- NOTE : 데스크탑 버전 -->
    <div class="d-flex justify-center" v-if="!isMobile">
      <div
        class="row no-gutters pa-0 col-8"
        :style="{ height: innerHeight + `px` }"
      >
        <UserList />
        <ChatRoom
          v-if="opponentId"
          ref="chatRoom"
          :ws="ws"
          @onSubmitMessage="submitMessage"
          @onSendMessage="sendMessage"
        />
        <!-- 기본 이미지 -->
        <v-sheet v-else class="col-9 d-flex align-center" color="white">
          <v-img
            contain
            :max-height="innerHeight + `px`"
            src="@/assets/image/talking.png"
          ></v-img>
        </v-sheet>
      </div>
    </div>
    <!-- NOTE : 모바일버전 -->
    <div class="d-flex justify-center" v-else>
      <div class="pa-0 col-10" :style="{ height: innerHeight + `px` }">
        <UserList v-if="isActive === 'userList'" />
        <ChatRoom
          v-if="isActive === 'chatRoom'"
          ref="chatRoom"
          :ws="ws"
          @onSubmitMessage="submitMessage"
          @onSendMessage="sendMessage"
        />
      </div>
    </div>
  </v-container>
</template>

<script>
import UserList from "@/components/Chat/UserList";
import ChatRoom from "@/components/Chat/ChatRoom";
import isMobile from "@/mixin/isMobile.js";

export default {
  mixins: [isMobile],
  data() {
    return {
      ws: null,
      socketUrl: process.env.VUE_APP_SOCKET_URL,
      userId: this.$store.getters["userStore/getUserId"],
      chatRooms: this.$store.getters["chatStore/getChatRooms"],
      innerHeight: window.innerHeight - (60 + 64 + 24),
    };
  },
  computed: {
    opponentId() {
      return this.$store.getters["chatStore/getOpponentId"];
    },
    isActive() {
      return this.$store.getters["chatStore/getIsActive"];
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
      this.innerHeight = window.innerHeight - (60 + 64 + 24);
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
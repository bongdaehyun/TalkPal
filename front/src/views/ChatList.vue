<template>
  <v-container class="pa-0 mt-12">
    <v-row justify="center" no-gutters>
      <v-sheet
        color="white"
        elevation="2"
        class="row no-gutters"
        :class="[isMobile ? 'col-10' : 'col-8']"
      >
        <v-sheet class="col-3" elevation="3">
          <UserList @onSelectChatRoom="selectChatRoom" />
        </v-sheet>
        <v-sheet class="col-9">
          <ChatRoom
            ref="chatRoom"
            :ws="ws"
            @onSubmitMessage="submitMessage"
            @onSendMessage="sendMessage"
          />
        </v-sheet>
      </v-sheet>
    </v-row>
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
    };
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

      this.ws.onmessage = (message) => {
        let parsedMessage = JSON.parse(message.data);
        console.log("[parsedMessage]");
        console.log(parsedMessage);
        switch (parsedMessage.id) {
          default:
        }
      };
    },
    requestChatRoomList() {
      this.$store
        .dispatch("dmStore/requestChatRoomList", {
          userId: this.userId,
        })
        .then((res) => {
          this.$store.commit("dmStore/SET_CHAT_ROOMS", res.data.chatRoomList);
          console.log(res.data.chatRoomList)
        });
    },
    selectChatRoom(selectedChatRoomId) {
      console.log(selectedChatRoomId);
      this.$refs.chatRoom.requestChatMessageList(selectedChatRoomId);
    },
    submitMessage(data) {
      this.$store.dispatch("dmStore/sendDirectMessage", data);
    },
    sendMessage(message) {
      if (this.ws.readyState !== this.ws.OPEN) {
        return;
      }
      const jsonMessage = JSON.stringify(message);
      this.ws.send(jsonMessage);
    },
    // NOTE: 새로운 채팅 생성 (상대 아이디만 파라미터로)
    createChatRoom(toUserId) {
      this.$store
        .dispatch("dmStore/createChatRoom", {
          fromUserId: this.userId,
          toUserId: toUserId,
        })
        .then(() => {
          // TODO: 방 목록에 새로운 채팅방 추가
        })
        .catch(() => {
          // NOTE: 채팅방 생성 실패 경우(보통 이미 존재하는 채팅방) 안내메시지 출력
          this.$store.dispatch("onSnackbar", {
            text: "이미 존재하는 채팅방입니다.",
            color: "error",
          });
        });
    },
  },

  created() {
    this.connect();
    this.requestChatRoomList();
  },
  components: {
    UserList,
    ChatRoom,
  },
};
</script>

<style scoped>
</style>
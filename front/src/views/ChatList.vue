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
          <UserList />
        </v-sheet>
        <v-sheet class="col-9">
          <ChatRoom />
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
  data() {
    return {
      ws: null,
      socketUrl: process.env.VUE_APP_SOCKET_URL,
      userId: this.$store.getters["userStore/getUserId"],
      chatRooms: [],
    };
  },
  mixins: [isMobile],
  methods: {
    connect() {
      this.ws = new WebSocket(this.socketUrl);

      this.ws.onopen = () => {};

      this.ws.onmessage = (message) => {
        let parsedMessage = JSON.parse(message.data);
        console.log("[parsedMessage]");
        console.log(parsedMessage);
        switch (parsedMessage.id) {
          default:
        }
      };
    },
  },

  created() {
    this.connect();
    // TODO: 채팅방 목록 불러오기
    // TODO: 최근 메세지 순으로 정렬.. ChatRoom에 마지막 메세지 시간 속성 추가 必
    // TODO: 읽지 않은 채팅방 표시.. ............ 이것도 ChatRoom 속성..?
  },
  components: {
    UserList,
    ChatRoom,
  },
};
</script>

<style scoped>
</style>
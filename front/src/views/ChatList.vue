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
          <UserList 
            :chatRooms="chatRooms"
            @onSelectChatRoom="selectChatRoom"
          />
        </v-sheet>
        <v-sheet class="col-9">
          <ChatRoom 
            ref="chatRoom"
            :ws="ws"
            @onSubmitMessage="submitMessage"
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
      chatRooms: [],
    };
  },
  methods: {
    connect() {
      this.ws = new WebSocket(this.socketUrl);

      this.ws.onopen = () => {
        let msg = {
          
        }
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
          userId: this.userId
          })
        .then((res) => {
          console.log(res.data.chatRoomList);
          this.chatRooms = res.data.chatRoomList;
        })
    },
    selectChatRoom(selectedChatRoomId) {
      console.log(selectedChatRoomId);
      this.$refs.chatRoom.requestChatMessageList(selectedChatRoomId);
    },
    submitMessage(data) {
      // TODO: 공백일 때, 채팅방 선택되지 않았을 때 보낼 수 없도록 변경
      this.$store
        .dispatch("dmStore/sendDirectMessage", data)
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
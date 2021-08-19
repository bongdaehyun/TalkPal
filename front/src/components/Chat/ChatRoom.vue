<template>
  <v-sheet
    color="white"
    :class="['d-flex flex-column', { 'col-9': !isMobile }]"
    style="height: 100%"
  >
    <!-- NOTE: 대화 상대정보 -->
    <UserInfo @loadComplete="load = true" />
    <!-- NOTE: 채팅 목록 -->
    <v-list ref="chatList" style="overflow: auto" v-if="load">
      <template v-for="(message, index) in msgList">
        <!-- NOTE: 본인이 보낸 채팅 (우측, 파란색) -->
        <div
          v-if="message.userId == userId"
          :key="index"
          class="d-flex justify-end pa-3 ms-12"
        >
          <v-sheet
            rounded="xl"
            color="blue"
            class="pa-2 white--text"
            elevation="3"
          >
            {{ message.message }}
          </v-sheet>
        </div>
        <!-- NOTE: 상대가 보낸 채팅 (좌측, 기본회색) -->
        <div v-else :key="index" class="d-flex justify-start pa-3 me-12">
          <v-sheet
            rounded="xl"
            color="background darken-2"
            class="pa-2"
            elevation="3"
          >
            {{ message.message }}
          </v-sheet>
        </div>
      </template>
    </v-list>
    <!-- NOTE: 메세지 입력 -->
    <v-card-actions class="pa-3 mt-auto" v-if="load">
      <v-text-field
        style="position: sticky; bottom: 0px; width: 100%"
        v-model="inputMessage"
        placeholder="메세지를 입력하세요."
        @keyup.13="submitDirectMsg"
        hide-details
        filled
        dense
        rounded
        :disabled="chatRoomId == null ? true : false"
      ></v-text-field>
    </v-card-actions>
  </v-sheet>
</template>

<script>
import UserInfo from "@/components/Chat/ChatRoom/UserInfo";
import isMobile from "@/mixin/isMobile.js";

export default {
  data() {
    return {
      userId: this.$store.getters["userStore/getUserId"],
      inputMessage: "",
      load: false,
    };
  },
  mixins: [isMobile],

  props: {
    ws: {
      type: WebSocket,
    },
  },
  computed: {
    msgList() {
      return this.$store.getters["chatStore/getMsgList"];
    },
    chatRoomId() {
      return this.$store.getters["chatStore/getSelectChatRoomId"];
    },
    opponentId() {
      return this.$store.getters["chatStore/getOpponentId"];
    },
  },
  methods: {
    // NOTE: 메세지 전송
    submitDirectMsg() {
      // NOTE: 메세지 길이 최소 1
      if (this.inputMessage.length < 1) return;
      // NOTE: DB에 저장하는 상위 컴포넌트 메서드 호출
      this.$emit("onSubmitMessage", {
        userId: this.userId,
        chatRoomId: this.chatRoomId,
        message: this.inputMessage,
      });

      // NOTE: 본인 채팅창에 보낸 메세지 표시
      const chatMessage = {
        userId: this.userId,
        message: this.inputMessage,
      };
      this.pushMsg(chatMessage);

      // NOTE: 소켓 메세지 전송
      this.$emit("onSendMessage", {
        id: "sendDM",
        userId: this.userId,
        chatRoomId: this.chatRoomId,
        receiver: this.opponentId,
        message: this.inputMessage,
      });

      this.inputMessage = "";
    },
    onReceiveDM(newMsg) {
      // NOTE: 현재 보고있는 채팅방과 같다면 붙여줌
      if (newMsg.chatRoomId == this.chatRoomId) {
        const chatMessage = {
          userId: newMsg.userId,
          message: newMsg.message,
          chatRoomId: this.chatRoomId,
        };
        this.pushMsg(chatMessage);
      }
    },
    pushMsg(chatMessage) {
      this.$store.dispatch("chatStore/pushMsgList", { chatMessage });
    },
    connect() {
      this.ws.onmessage = (message) => {
        let parsedMessage = JSON.parse(message.data);
        switch (parsedMessage.id) {
          case "receiveDM":
            // NOTE: 새로운 메세지 수신 시 붙여주기
            this.onReceiveDM(parsedMessage);
            break;
          default:
            this.$error("Unrecognized message", parsedMessage);
        }
      };
    },
  },
  created() {
    this.connect();
  },

  // 스크롤 맨 아래로
  updated() {
    this.$nextTick(() => {
      this.$refs.chatList.$el.scrollTop = this.$refs.chatList.$el.scrollHeight;
    });
  },
  mounted() {
    this.$nextTick(() => {
      if (this.load) {
        this.$refs.chatList.$el.scrollTop =
          this.$refs.chatList.$el.scrollHeight;
      }
    });
  },
  components: {
    UserInfo,
  },
};
</script>

<style scoped>
</style>
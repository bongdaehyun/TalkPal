<template>
  <div class="d-flex flex-column" style="height: 100%">
    <!-- NOTE: 대화 상대정보 -->
    <div class="d-flex justify-center align-center text-button">
      <span class="ms-3 font-weight-black">대화 상대 정보 출력</span>
    </div>
    <v-divider></v-divider>
    <!-- NOTE: 채팅 목록 -->
    <v-list ref="chatList" style="overflow: auto">
      <template v-for="(message, index) in msgList">
        <v-list-item :key="index" v-if="message.userId == userId" class="justify-end">
          <!-- NOTE: 본인이 보낸 채팅 오른쪽에 표시 -->
          <!-- TODO: 오른쪽 정렬 -->
          <v-chip
            color="primary"
            >
            {{ message.message }}
          </v-chip>
        </v-list-item>
        <v-list-item :key="index" v-else>
          <!-- NOTE: 본인이 보낸 채팅 오른쪽에 표시 -->
          <!-- TODO: 오른쪽 정렬 -->
          <v-chip>
            {{ message.message }}
          </v-chip>
        </v-list-item>
      </template>
    </v-list>
    <!-- NOTE: 메세지 입력 -->
    <v-card-actions class="pa-0 mt-auto">
      <!-- NOTE: v-textarea => v-text-field로 변경 -->
      <v-text-field
        style="position: sticky; bottom: 0px; width: 100%"
        v-model="inputMessage"
        placeholder="메세지를 입력하세요."
        @keyup.13="submitDirectMsg"
        hide-details
        filled
        dense
        rounded
      ></v-text-field>
    </v-card-actions>
  </div>
</template>

<script>
export default {
  data() {
    return {
      userId: this.$store.getters["userStore/getUserId"],
      chatRoomId: null,
      opponentId: null,
      msgList: [],
      inputMessage: "",
    };
  },
  props: {
    ws: {
      type: WebSocket,
    },
  },
  methods: {
    submitDirectMsg() {
      // NOTE: 메세지 길이 최소 1
      if (this.inputMessage.length < 1) return;

      // NOTE: DB에 저장하는 상위 컴포넌트 메서드 호출
      this.$emit("onSubmitMessage", {
        chatRoomId: this.chatRoomId,
        message: this.inputMessage,
        userId: this.userId,
      });

      // NOTE: 본인 채팅창에 보낸 메세지 표시
      this.msgList.push({
        userId: this.userId,
        message: this.inputMessage,
      })

      // NOTE: 소켓 메세지 전송
      this.$emit("onSendMessage", {
        id: "sendDM",
        receiver: this.opponentId,
        chatRoomId: this.chatRoomId,
        userId: this.userId,
        message: this.inputMessage,
      });
      
      this.inputMessage = "";
    },
    onReceiveDM(newMsg) {
      // NOTE: 현재 보고있는 채팅방과 같다면 붙여줌
      if (newMsg.chatRoomId == this.chatRoomId) {
        this.msgList.push({
          message: newMsg.message,
          userId: newMsg.userId,
          chatRoomId: this.chatRoomId,
          // TODO: 시간 정보는 굳이 필요 없긴 함........ 임시로 "tmpTime 저장됨"
          time: newMsg.time,
        });
      }
    },
    requestChatMessageList(chatRoomId) { 
      this.chatRoomId = chatRoomId;
      this.msgList = [];
      this.$store
        .dispatch("dmStore/requestChatMessageList", {
          chatRoomId: this.chatRoomId,
        })
        .then((res) => {
          res.data.chatMessageList.forEach(chatMessage => {
            this.msgList.push(chatMessage);
          });
          this.requestOpponentId();
        });
      
    },
    requestOpponentId() {
      this.$store
        .dispatch("dmStore/requestOpponentId", {
          userId: this.userId,
          chatRoomId: this.chatRoomId,
        })
        .then((res) => {
          this.opponentId = res.data;
        });
    },
    connect() {
      this.ws.onmessage = (message) => {
        let parsedMessage = JSON.parse(message.data);
        this.$info(`[parsedMessage] : ${parsedMessage}`);
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
};
</script>

<style scoped>
</style>
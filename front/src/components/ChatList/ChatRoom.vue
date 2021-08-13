<template>
  <div class="d-flex flex-column" style="height: 100%">
    <!-- NOTE: 대화 상대정보 -->
    <div class="d-flex justify-center align-center text-button">
      <span class="ms-3 font-weight-black">대화 상대 정보 출력</span>
    </div>
    <v-divider></v-divider>
    <!-- NOTE: 채팅 목록 -->
    <v-list ref="chatList" style="overflow: auto">
      <template v-for="(message, index) in items">
        <v-list-item :key="index">
          <v-list-item-content class="mb-2 mt-2 pa-0"> </v-list-item-content>
        </v-list-item>
      </template>
    </v-list>
    <!-- NOTE: 메세지 입력 -->
    <v-card-actions class="pa-0 mt-auto">
      <v-textarea
        style="position: sticky; bottom: 0px; width: 100%"
        v-model="inputMessage"
        placeholder="메세지를 입력하세요."
        hide-details
        rows="1"
        filled
        dense
        rounded
        auto-grow
      ></v-textarea>
    </v-card-actions>
  </div>
</template>

<script>
export default {
  data() {
    return {
      chatRoomId: null,
      msgList: [],
    };
  },
  props: {
    ws: {
      type: WebSocket,
    },
  },
  methods: {
    submitDirectMsg(msg) {
      // this.$$store
      //     .dispatch("", data)
      //     .then((res) => {
      //         this.$emit("", data2);
      //     });
    },
    onReceiveDM(newMsg) {},
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
    // TODO: 채팅 내역 불러오기
  },
};
</script>

<style scoped>
</style>
<template>
  <!-- NOTE: 채팅 -->
  <div
    class="d-flex flex-column"
    style="position: relative; background-color: #ffffff"
    :style="{ height: height }"
  >
    <div class="d-flex justify-center align-center text-button">
      <v-icon>mdi-keyboard-outline</v-icon>
      <span class="ms-3 font-weight-black">chat</span>
    </div>
    <v-divider></v-divider>
    <!-- NOTE: 채팅 내역 -->
    <v-list ref="chatList" style="overflow: auto">
      <template v-for="(message, index) in items">
        <v-list-item :key="index">
          <v-list-item-content class="mb-2 mt-2 pa-0">
            <div class="d-flex align-center">
              <span class="font-weight-bold">{{ message.nick }}</span>
              <span class="grey--text caption ms-1">
                {{ message.time }}
              </span>
            </div>
            <v-textarea
              :value="message.content"
              rows="1"
              readonly
              auto-grow
              hide-details
              dense
            >
            </v-textarea>
          </v-list-item-content>
        </v-list-item>
      </template>
    </v-list>
    <!-- NOTE: 채팅 입력 -->
    <v-card-actions class="pa-0 mt-auto">
      <v-textarea
        style="position: sticky; bottom: 0px; width: 100%"
        v-model="inputMessage"
        placeholder="메세지를 입력하세요."
        @keyup.13="submitMessage"
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
import isMobile from "@/mixin/isMobile.js";

export default {
  name: "chat",
  mixins: [isMobile],
  data() {
    return {
      inputMessage: "",
    };
  },
  methods: {
    submitMessage() {
      this.$emit("onSubmitMessage", this.inputMessage);
      this.inputMessage = "";
    },
  },
  created() {},
  props: {
    items: {
      type: Array,
    },
    height: {
      type: String,
    },
  },
  updated() {
    this.$nextTick(() => {
      this.$refs.chatList.$el.scrollTop = this.$refs.chatList.$el.scrollHeight;
    });
  },
  mounted() {
    this.$nextTick(() => {
      this.$refs.chatList.$el.scrollTop = this.$refs.chatList.$el.scrollHeight;
    });
  },
};
</script>

<style scoped>
.msg {
  display: inline-block;
  border-radius: 15px;
  padding: 7px 15px;
  /* margin-bottom: 10px;
    margin-top: 5px; */
  background-color: #0288d1;
}
.myMsg {
  text-align: right;
  color: #fff;
}
</style>
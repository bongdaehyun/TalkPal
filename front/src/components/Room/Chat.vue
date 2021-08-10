<template>
  <!-- NOTE: 채팅 -->
  <div
    class="d-flex flex-column"
    style="position: relative; background-color: #f8f9fa"
    :style="{ height: height }"
    color="#0288d1"
  >
    <div class="d-flex justify-center align-center py-2 text-button">
      <v-icon>mdi-keyboard-outline</v-icon>
      <span class="ms-3"> chat </span>
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
            {{ message.content }}
          </v-list-item-content>
        </v-list-item>
        <v-divider
          :key="index + 'r'"
          v-if="index != Object.keys(items).length - 1"
        ></v-divider>
      </template>
    </v-list>
    <!-- NOTE: 채팅 입력 -->
    <v-card-actions class="pa-0 mt-auto">
      <v-textarea
        style="position: sticky; bottom: 0px; width: 100%"
        v-model="inputMessage"
        placeholder="메세지를 입력하세요."
        @keyup.13="submitMessage"
        hide-details="false"
        rows="2"
        filled
        dense
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
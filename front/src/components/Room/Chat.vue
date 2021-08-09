<template>
  <!-- NOTE: 채팅 -->
  <v-card
    class="d-flex flex-column justify-space-between"
    style="position: relative; background-color: #f8f9fa"
    :style="{ height: height }"
  >
    <!-- NOTE: 채팅 내역 -->
    <v-list ref="chatList" style="overflow: auto">
      <template v-for="(message, index) in items">
        <v-list-item :key="index">
          <v-list-item-content class="pb-0">
            {{ message.sender }} {{ message.time }}<br />
            {{ message.content }}
          </v-list-item-content>
        </v-list-item>
        <v-divider
          :key="index"
          v-if="index != Object.keys(items).length - 1"
        ></v-divider>
      </template>
    </v-list>

    <!-- NOTE: 채팅 입력 -->
    <v-card-actions class="pa-0">
      <v-text-field
        style="position: sticky; bottom: 0px; width: 100%"
        v-model="inputMessage"
        placeholder="메세지를 입력하세요."
        @keyup.13="submitMessage"
        single-line
        filled
        dense
        hide-details="false"
      ></v-text-field>
    </v-card-actions>
  </v-card>
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

<style>
</style>
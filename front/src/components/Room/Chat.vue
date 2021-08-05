<template>
  <!-- NOTE: 채팅 -->
  <v-card
    class="d-flex flex-column justify-space-between"
    style="position: relative; background-color: green"
    :style="{ height: height }"
  >
    <!-- NOTE: 채팅 내역 -->
    <v-list ref="chatList" class="d-flex flex-column" style="overflow: auto">
      <v-list-item v-for="(message, index) in items" :key="index">
        {{ message.sender }} {{ message.time }}<br />
        {{ message.content }}
      </v-list-item>
    </v-list>

    <!-- NOTE: 채팅 입력 -->
    <v-card-actions>
      <v-text-field
        style="position: sticky; bottom: 0px; width: 100%"
        v-model="inputMessage"
        placeholder="메세지를 입력하세요."
        solo
        @keyup.13="submitMessage"
      ></v-text-field>
    </v-card-actions>
  </v-card>
</template>

<script>
export default {
  name: "chatComponent",
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
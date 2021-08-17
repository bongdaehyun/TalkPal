<template>
  <div>
    <!-- NOTE: 데스크탑 버전 -->

    <!-- NOTE: 새로운 채팅 추가 -->
    유저 목록
    <div
      v-if="!isMobile"
      class="mt-2"
      style="text-align: center; overflow: auto; height: 70vh"
    >
      <Item 
        :chatRoom="chatRoom" 
        v-for="(chatRoom, index) in chatRooms" 
        :key="index" 
        @onSelectChatRoom="onSelectChatRoom"
      />
    </div>
    <!-- NOTE: 모바일 버전 -->
    <div v-else></div>
  </div>
</template>
<script>
import Item from "@/components/ChatList/UserList/Item";

import isMobile from "@/mixin/isMobile.js";

export default {
  data() {
    return {
      selectedItem: 0,
    };
  },
  mixins: [isMobile],

  props: {
    chatRooms: {},
  },

  methods: {
    onSelectChatRoom(data) {
      this.$emit("onSelectChatRoom", data);
    },
  },

  components: {
    Item,
  },
};
</script>
<style lang="">
</style>
<template>
  <div>
    <!-- NOTE: 데스크탑 버전 -->
    유저 목록
    <div
      v-if="!isMobile"
      class="mt-2"
      style="text-align: center; overflow: auto; height: 70vh"
    >
      <Item :item="item" v-for="(item, index) in items" :key="index" />
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
      items: [
        { text: "유저1", icon: "mdi-folder" },
        { text: "유저1 ", icon: "mdi-account-multiple" },
        { text: "유저1", icon: "mdi-star" },
        { text: "유저1", icon: "mdi-history" },
        { text: "유저1", icon: "mdi-check-circle" },
        { text: "유저1", icon: "mdi-upload" },
        { text: "유저1", icon: "mdi-cloud-upload" },
        { text: "유저1", icon: "mdi-history" },
        { text: "유저1", icon: "mdi-check-circle" },
        { text: "유저1", icon: "mdi-upload" },
        { text: "유저1", icon: "mdi-cloud-upload" },
        { text: "유저1", icon: "mdi-folder" },
        { text: "유저1 ", icon: "mdi-account-multiple" },
        { text: "유저1", icon: "mdi-star" },
        { text: "유저1", icon: "mdi-history" },
        { text: "유저1", icon: "mdi-check-circle" },
        { text: "유저1", icon: "mdi-upload" },
        { text: "유저1", icon: "mdi-cloud-upload" },
        { text: "유저1", icon: "mdi-history" },
        { text: "유저1", icon: "mdi-check-circle" },
        { text: "유저1", icon: "mdi-upload" },
        { text: "유저1", icon: "mdi-cloud-upload" },
      ],
    };
  },
  mixins: [isMobile],

  components: {
    Item,
  },
};
</script>
<style lang="">
</style>
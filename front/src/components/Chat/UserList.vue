<template>
  <v-sheet
    color="white"
    :class="['d-flex flex-column', { 'col-3': !isMobile }]"
    style="height: 100%"
  >
    <!-- NOTE : 채팅방 추가 버튼 -->
    <v-btn @click="openDialog" tile depressed color="primary">
      <v-icon> fa-edit </v-icon>
    </v-btn>
    <!-- NOTE : 데스크탑 버전 -->
    <v-list v-if="!isMobile" style="overflow: auto">
      <template v-for="(chatRoom, index) in chatRooms">
        <Item :chatRoom="chatRoom" :key="index" />
      </template>
    </v-list>
    <!-- NOTE : 모바일 버전 -->
    <div v-else>
      <template v-for="(chatRoom, index) in chatRooms">
        <Item :chatRoom="chatRoom" :key="index" />
      </template>
    </div>
    <!-- NOTE : 채팅 추가 유저 다이얼로그 -->
    <v-dialog v-model="dialog" :max-width="dialogMaxWidth" scrollable>
      <UserAddDialog :profileId="loginId" :Item="following" />
    </v-dialog>
  </v-sheet>
</template>
<script>
import Item from "@/components/Chat/UserList/Item";
import UserAddDialog from "@/components/Chat/UserList/UserAddDialog.vue";
import isMobile from "@/mixin/isMobile.js";
export default {
  data() {
    return {
      loginId: this.$store.getters[`userStore/getUserId`],
      following: {
        list: [],
        url: "requestFollowings",
        page: 0,
        count: 0,
      },
      selectedItem: 0,
    };
  },
  mixins: [isMobile],
  props: {},
  computed: {
    chatRooms() {
      return this.$store.getters[`chatStore/getChatRooms`];
    },
    dialog() {
      return this.$store.getters[`chatStore/getDialog`];
    },
    dialogMaxWidth() {
      if (this.isMobile) {
        return "90%";
      }
      return "15%";
    },
  },
  methods: {
    openDialog() {
      this.$store.dispatch("chatStore/openDialog");
    },
  },

  components: {
    UserAddDialog,
    Item,
  },
};
</script>
<style lang="">
</style>
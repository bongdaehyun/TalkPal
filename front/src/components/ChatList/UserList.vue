<template>
  <div>
    <!-- NOTE: 데스크탑 버전 -->

    <!-- NOTE: 새로운 채팅 추가 -->
    유저 목록
    <v-btn @click="following.dialog = true">유저추가</v-btn>
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
    <!-- NOTE: 팔로워 다이얼로그 -->
    <v-dialog v-model="following.dialog" :max-width="dialogMaxWidth" scrollable>
      <UserAddDialog :profileId="loginId" :Item="following" />
    </v-dialog>
  </div>
</template>
<script>
import Item from "@/components/ChatList/UserList/Item";
import UserAddDialog from "@/components/ChatList/UserList/UserAddDialog.vue";
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
        dialog: false,
      },
      selectedItem: 0,
    };
  },
  mixins: [isMobile],
  computed: {
    dialogMaxWidth() {
      if (this.isMobile) {
        return "90%";
      }
      return "15%";
    },
  },
  methods: {
    onSelectChatRoom(data) {
      this.$emit("onSelectChatRoom", data);
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
<template>
  <div class="d-flex justify-space-between align-center" v-if="userInfo">
    <div style="flex: 1">
      <v-btn v-if="isMobile" plain color="primary" @click="returnUserList">
        ⬅
      </v-btn>
    </div>
    <div class="d-flex justify-center align-center" style="flex: 2">
      <v-avatar size="48">
        <v-img :src="profilePath"></v-img>
      </v-avatar>
      <span class="ms-3 font-weight-bold" style="font-size: 1.5rem">
        {{ userInfo.nickname }}
      </span>
    </div>
    <div style="flex: 1"></div>
  </div>
</template>

<script>
import getProfilePath from "@/mixin/getProfilePath.js";
import isMobile from "@/mixin/isMobile.js";

export default {
  name: "UserInfo",
  data() {
    return {
      userInfo: null,
    };
  },
  mixins: [getProfilePath, isMobile],

  computed: {
    opponentId() {
      return this.$store.getters["chatStore/getOpponentId"];
    },
    profilePath() {
      return this.getProfilePath(this.userInfo.imgPath);
    },
  },
  watch: {
    opponentId(newValue, oldValue) {
      this.requestUserInfo();
    },
  },
  methods: {
    async returnUserList() {
      // NOTE: 모바일, 채팅방 목록으로 전환
      const url = "chatStore/setIsActive";
      const isActive = "userList";
      await this.$store.dispatch(url, { isActive });
    },
    requestUserInfo() {
      this.$store
        .dispatch("userStore/requestUserInfo", this.opponentId)
        .then((res) => {
          this.userInfo = res.data;
          this.$emit("loadComplete");
        });
    },
  },
  created() {
    this.requestUserInfo();
  },
};
</script>

<style>
</style>
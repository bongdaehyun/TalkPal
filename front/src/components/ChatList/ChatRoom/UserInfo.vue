<template>
  <div class="d-flex justify-center" v-if="userInfo">
    <v-avatar size="32">
      <v-img :src="profilePath"></v-img>
    </v-avatar>
    <span class="ms-3">
      {{ userInfo.nickname }}
    </span>
  </div>
  <div v-else>유저정보</div>
</template>

<script>
import getProfilePath from "@/mixin/getProfilePath.js";

export default {
  name: "UserInfo",
  data() {
    return {
      userInfo: null,
    };
  },
  mixins: [getProfilePath],

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
    requestUserInfo() {
      this.$store
        .dispatch("userStore/requestUserInfo", this.opponentId)
        .then((res) => {
          this.userInfo = res.data;
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
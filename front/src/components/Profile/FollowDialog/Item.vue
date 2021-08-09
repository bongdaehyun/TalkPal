
<template>
  <div class="d-flex align-center mt-3" v-if="item != null && isFollow != null">
    <v-avatar size="32" @click="goProfile" style="cursor: pointer">
      <img :src="profileImg" />
    </v-avatar>
    <span class="ms-3" @click="goProfile" style="cursor: pointer">
      {{ this.item.nickname }}
    </span>
    <span class="ml-auto">
      {{ this.item.lang }}
    </span>
  </div>
</template>

<script>
export default {
  props: {
    item: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      loginId: this.$store.getters[`userStore/getUserId`],
      profileId: this.item.id,
      isFollow: null,
    };
  },
  computed: {
    profileImg: {
      get() {
        try {
          return require(`@/assets/image/profile/${this.item.imgPath}`);
        } catch (error) {
          return require(`@/assets/image/profile/default_profileImg.png`);
        }
      },
      set() {},
    },
  },
  created() {
    this.checkFollow();
  },
  methods: {
    checkFollow() {
      let url = "userStore/checkFollow";
      let followInfo = {
        fromuserid: this.loginId,
        touserid: this.profileId,
      };
      this.$store.dispatch(url, followInfo).then((res) => {
        console.log(res);
        this.isFollow = res.data;
      });
    },
    // NOTE: 팔로우 제거
    delFollow() {
      let url = "userStore/deleteFollow";
      let followInfo = {
        fromuserid: this.loginId,
        touserid: this.profileId,
      };
      this.$store.dispatch(url, followInfo).then(() => {
        this.$store.dispatch("onSnackbar", {
          text: "팔로우 해제",
          color: "success",
        });
        // this.$router.go();
      });
    },
    // NOTE: 팔로우 추가
    addFollow() {
      let url = "userStore/addFollow";
      let followInfo = {
        fromuserid: this.loginId,
        touserid: this.profileId,
      };
      this.$store
        .dispatch(url, followInfo)
        .then(() => {
          this.$store.dispatch("onSnackbar", {
            text: "팔로우 추가",
            color: "success",
          });
          // this.$router.go();
        })
        .catch((err) => {
          console.error(err);
          this.$log("이미 요청한 팔로우");
        });
    },
    goProfile() {
      this.$router.push({
        name: "Profile",
        params: { userId: this.item.id },
      });
      this.$router.go();
    },
  },
};
</script>
<style scoped>
</style>
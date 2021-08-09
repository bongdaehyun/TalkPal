
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
        this.isFollow = res.data;
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
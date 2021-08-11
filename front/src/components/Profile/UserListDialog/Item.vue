
<template>
  <div class="d-flex align-center mt-3" v-if="item != null && isFollow != null">
    <v-avatar size="32" @click="goProfile" style="cursor: pointer">
      <img :src="profilePath" />
    </v-avatar>
    <span class="ms-3" @click="goProfile" style="cursor: pointer">
      {{ this.item.nickname }}
    </span>
    <!-- <span class="ml-auto">
      {{ this.item.lang }}
    </span> -->
    <v-avatar size="32" @click="goProfile" class="ml-auto" style="cursor: pointer">
      <img :src="langImage" />
    </v-avatar>
  </div>
</template>

<script>
import getProfilePath from "@/mixin/getProfilePath.js";

export default {
  data() {
    return {
      loginId: this.$store.getters[`userStore/getUserId`],
      profileId: this.item.id,
      isFollow: null,
    };
  },

  props: {
    item: {
      type: Object,
      default: null,
    },
  },
  mixins: [getProfilePath],

  computed: {
    profilePath: {
      get() {
        return this.getProfilePath(this.item.imgPath);
        
      },
      set() {},
    },
    langImage:{
      get(){
        return require(`@/assets/image/flag/${this.item.lang}.png`)
      }
    }
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

<template>
  <div class="d-flex align-center mt-3" v-if="item" @click="addChatUser">
    <v-avatar size="32" style="cursor: pointer">
      <img :src="profilePath" />
    </v-avatar>
    <span class="ms-3" style="cursor: pointer">
      {{ this.item.nickname }}
    </span>
    <v-avatar size="32" class="ml-auto" style="cursor: pointer">
      <img :src="langImage" />
    </v-avatar>
  </div>
</template>

<script>
import getProfilePath from "@/mixin/getProfilePath.js";

export default {
  data() {
    return {};
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
    langImage: {
      get() {
        return require(`@/assets/image/flag/${this.item.lang}.png`);
      },
    },
  },
  methods: {
    // 유저 추가 메서드
    addChatUser() {
      console.log("채팅 유저 추가");
      console.log(this.item);
    },
  },
};
</script>
<style scoped>
</style>
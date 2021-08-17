
<template>
  <div
    class="d-flex align-center mt-3"
    style="cursor: pointer"
    v-if="item"
    @click="addChatUser"
  >
    <v-avatar size="32">
      <img :src="profilePath" />
    </v-avatar>
    <span class="ms-3">
      {{ this.item.nickname }}
    </span>
    <v-avatar size="32" class="ml-auto">
      <img :src="langImage" />
    </v-avatar>
  </div>
</template>

<script>
import getProfilePath from "@/mixin/getProfilePath.js";

export default {
  data() {
    return {
      userId: this.$store.getters["userStore/getUserId"],
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
    langImage: {
      get() {
        return require(`@/assets/image/flag/${this.item.lang}.png`);
      },
    },
  },
  methods: {
    // NOTE: 유저 추가 메서드
    async addChatUser() {
      await this.$store.dispatch("chatStore/closeDialog");

      const payload = {
        fromUserId: this.userId,
        toUserId: this.item.id,
      };
      // 방 생성
      await this.$store.dispatch("chatStore/createChatRoom", payload);

      // 방 새로 불러오기
      const res = await this.$store.dispatch("chatStore/requestChatRoomList", {
        userId: this.userId,
      });
      try {
        await this.$store.dispatch("chatStore/setChatRooms", {
          chatRooms: res.data.chatRoomList,
        });
      } catch {
        this.$store.dispatch("onSnackbar", {
          text: "이미 대화중인 유저입니다.",
          color: "error",
        });
      }
    },
  },
};
</script>
<style scoped>
</style>
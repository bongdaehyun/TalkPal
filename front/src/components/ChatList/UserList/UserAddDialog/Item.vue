
<template>
  <div
    class="d-flex align-center mt-3"
    @click="addChatUser"
    style="cursor: pointer"
    v-if="item"
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
    // NOTE: 채팅방  추가 메서드
    async addChatUser() {
      await this.$store.dispatch("chatStore/closeDialog");

      const payload = {
        fromUserId: this.userId,
        toUserId: this.item.id,
      };
      // 방 생성
      try {
        const res = await this.$store.dispatch(
          "chatStore/createChatRoom",
          payload
        );

        console.log(res);
      } catch (error) {
        // 방 중복 에러
        this.$store.dispatch("onSnackbar", {
          text: "이미 존재하는 방입니다.",
          color: "error",
        });
      }

      // 방 목록 다시 불러오기
      try {
        const res = await this.$store.dispatch(
          "chatStore/requestChatRoomList",
          {
            userId: this.userId,
          }
        );
        await this.$store.dispatch("chatStore/setChatRooms", {
          chatRooms: res.data.chatRoomList,
        });
      } catch (error) {
        console.log(error);
      }
    },
  },
};
</script>
<style scoped>
</style>
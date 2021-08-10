<template>
  <v-card elevation="1" outlined>
    <v-img
      class="white--text align-end"
      height="200px"
      src="@/assets/image/food.png"
    >
      <v-card-title>
        <span class="white black--text bold">{{ item.name }}</span>
      </v-card-title>
    </v-img>
    <v-card-subtitle class="pb-0"> {{ $t(item.topic) }} </v-card-subtitle>
    <v-card-text class="text--primary">
      <div>{{ item.host_lang }}</div>
      <div>{{ item.guest_lang }}</div>
    </v-card-text>
    <v-card-actions>
      <v-spacer></v-spacer>
      <v-btn color="primary" @click="goRoom" text>
        {{ $t("room_enter") }}
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import getProfilePath from "@/mixin/getProfilePath.js";

export default {
  name: "item",
  mixins: [getProfilePath],

  data() {
    return {
      name: "",
      userInfo: null,
      profilePath: null,
    };
  },
  props: {
    item: {
      type: Object,
    },
  },
  methods: {
    goRoom() {
      this.$emit("onEnterRoom", this.item);
      // this.$router.push({ name: "Room", params: { UUID: this.item.uuid } });
    },
    goProfile() {
      this.$router.push({
        name: "Profile",
        params: { userId: this.item.hostId },
      });
    },
    onClick() {
      this.flipped = !this.flipped;
    },
    requestUserInfo() {
      this.$store
        .dispatch("userStore/requestUserInfo", this.item.hostId)
        .then((res) => {
          this.userInfo = res.data;
          const imgPath = this.userInfo.imgPath;
          this.profilePath = this.getProfilePath(imgPath);
        })
        .catch((err) => {
          this.$log(err);
        });
    },
  },
  mounted() {
    this.requestUserInfo();
  },
  components: {},
};
</script>

<style src="vue-flipper/dist/vue-flipper.css" />

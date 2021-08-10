<template>
  <flipper :flipped="flipped" @click="onClick">
    <!-- NOTE : 카드 뒤집기 앞면 방 정보 -->
    <div slot="front">
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
    </div>
    <!-- NOTE : 카드 뒤집기 뒷면 호스트 정보 -->
    <div slot="back">
      <v-card elevation="1" outlined>
        <v-img
          class="white--text align-end"
          height="200px"
          :src="profileImg"
        >
        </v-img>
        <v-card-subtitle class="pb-0 " ><h2 style="color : black; text-align:center;">{{userInfo.nickname}} </h2> </v-card-subtitle>
        <v-card-text class="text--primary">
          <v-textarea
            class="mt-5"
            prepend-inner-ico="mdi-account-circle"
              rows="2"
              outlined
              hide-details
              v-model="userInfo.introduction"
              readonly
          ></v-textarea>
        </v-card-text>
        <v-card-actions>
          <v-btn color="primary" @click="goProfile" text>
            {{ $t("profile_go") }}
          </v-btn>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="goRoom" text>
            {{ $t("room_enter") }}
          </v-btn>
        </v-card-actions>
      </v-card>
    </div>
  </flipper>
</template>

<script>
// NOTE : 카드 뒤집기 https://github.com/VitorLuizC/vue-flipper
import Flipper from "vue-flipper";
export default {
  name: "item",
  data() {
    return {
      name: "",
      flipped: false,
      userInfo : null,
      profileImg: null,
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
          // NOTE: 프로필 유저 정보 배포된 서버 설정 필요
          if (imgPath) {
            this.profileImg = require(`@/assets/image/profile/${imgPath}`);
          } else {
            this.profileImg = require(`@/assets/image/profile/default_profileImg.png`);
          }
        })
        .catch((err) => {
          this.$log(err);
        });
    },
  },
  mounted() {
    this.requestUserInfo()
  },
  components: {
    Flipper,
  },
};
</script>

<style src="vue-flipper/dist/vue-flipper.css" />

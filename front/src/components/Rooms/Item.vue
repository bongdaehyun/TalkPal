<template>
  <v-hover v-slot="{ hover }">
    <v-card elevation="1" outlined>
      <v-img
        :aspect-ratio="16 / 9"
        class="white--text align-end"
        height="200px"
        src="@/assets/image/food.png"
      >
        <!-- <v-card-title v-show="!hover">
        <span class="white black--text bold">{{ item.name }}</span>
      </v-card-title> -->
        <!-- NOTE : 이미지 클릭시 생성 -->
        <v-expand-transition v-if="userInfo">
          <v-card
            v-if="hover"
            elevation="1"
            outlined
            class="transition-fast-in-fast-out"
          >
            <v-img
              class="white--text align-end"
              height="110px"
              :src="profilePath"
            >
            </v-img>
            <v-card-subtitle class="pb-0 mt-5"
              ><h2 style="text-align: center; color: black">
                {{ userInfo.nickname }}
              </h2>
            </v-card-subtitle>
            <v-card-text class="mt-3">
              <div style="color: black" class="d-flex justify-center">
                <span v-for="item in avgScore" :key="item.id">
                  <i style="color: #0288d1" class="fas fa-star"></i
                ></span>
                {{ avgScore }}
              </div>
            </v-card-text>
          </v-card>
        </v-expand-transition>
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
  </v-hover>
</template>

<script>
import getProfilePath from "@/mixin/getProfilePath";
export default {
  name: "item",
  mixins: [getProfilePath],
  data() {
    return {
      name: "",
      userInfo: null,
      avgScore: 0,
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
    requestUserInfo() {
      this.$store
        .dispatch("userStore/requestUserInfo", this.item.hostId)
        .then((res) => {
          this.userInfo = res.data;
          //console.log(this.userInfo)
          const imgPath = this.userInfo.imgPath;
          this.profilePath = this.getProfilePath(imgPath);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    requestAvgScore() {
      this.$store
        .dispatch("userStore/requestAvgScore", this.item.hostId)
        .then((res) => {
          //console.log(res.data)
          this.avgScore = res.data.score;
        });
    },
  },
  mounted() {
    this.requestUserInfo();
    this.requestAvgScore();
  },
  components: {},
};
</script>


<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <div class="d-flex align-center">
        <!-- 네비게이션바 왼쪽 -->
      </div>
      <!-- 중간 여백 -->
      <v-spacer></v-spacer>
      <v-btn text>
        <span class="mr-2" @click="goRoom">방 목록 페이지</span>
      </v-btn>
      <v-btn text>
        <span class="mr-2" @click="goProfile">내 정보</span>
      </v-btn>
      <v-btn text>
        <span class="mr-2" @click="goMain">메인 페이지</span>
      </v-btn>
    </v-app-bar>
    <v-main>
      <v-container>
        <router-view />
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: "App",
  methods: {
    goRoom() {
      this.$router.push({ name: "Room" });
    },
    goProfile() {
      this.$router.push({ name: "Profile" });
    },
    goMain() {
      this.$router.push({ name: "Main" });
    },
  },
};
</script>

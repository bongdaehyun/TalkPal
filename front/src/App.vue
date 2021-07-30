<template>
  <v-app>
    <v-app-bar app color="white" v-if="loginStatus">
      <div class="d-flex align-center">
        <!-- NOTE: 네비게이션바 왼쪽 -->
        <v-img src="@/assets/image/logo.png" max-width="224px" contain></v-img>
      </div>
      <v-spacer></v-spacer>
      <v-btn text>
        <span class="mr-2" color="secondary" @click="goRooms">
          {{ $t("nav_rooms") }}
        </span>
      </v-btn>
      <v-btn text>
        <span class="mr-2" color="secondary" @click="goProfile">
          {{ $t("nav_profile") }}
        </span>
      </v-btn>
      <v-btn text>
        <span class="mr-2" color="secondary" @click="onLogout">
          {{ $t("nav_logout") }}
        </span>
      </v-btn>
    </v-app-bar>
    <v-main>
      <!-- NOTE: 대화방 페이지일 때 -->
      <router-view />
    </v-main>
  </v-app>
</template>

<script>
export default {
  name: "App",
  data() {
    return {};
  },
  computed: {
    loginStatus() {
      return this.$store.getters["userStore/getLoginStatus"];
    },
  },
  methods: {
    goRooms() {
      this.$router.push({ name: "Rooms" });
    },
    goProfile() {
      this.$router.push({
        name: "Profile",
        params: { userId: this.$store.getters["userStore/getUserId"] },
      });
    },
    goMain() {
      this.$router.push({ name: "Main" });
    },
    onLogout() {
      this.$store.dispatch("userStore/logout");
      this.$router.push({ name: "Main" });
    },
  },
};
</script>
<style scoped>
</style>
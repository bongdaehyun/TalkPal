<template>
  <v-app>
    <v-app-bar app color="white" v-if="loginStatus">
      <div class="d-flex align-center">
        <!-- NOTE: 네비게이션바 왼쪽 -->
        <v-img src="@/assets/image/logo.png" max-width="224px" contain></v-img>
      </div>
      <!-- 중간 여백 -->
      <v-spacer></v-spacer>
      <v-btn text>
        <v-icon class="mr-2" color="secondary" @click="goRooms">
          mdi-phone-in-talk
        </v-icon>
      </v-btn>
      <v-btn text>
        <v-icon class="mr-2" color="secondary" @click="goProfile">
          mdi-account-box
        </v-icon>
      </v-btn>
      <v-btn text>
        <v-icon class="mr-2" color="secondary" @click="onLogout">
          mdi-logout
        </v-icon>
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
  data() {
    return {
      userId: this.$store.getters["userStore/getUserId"],
    };
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
      this.$router.push({ name: "Profile", params: { userId: this.userId } });
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
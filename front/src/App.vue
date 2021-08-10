<template>
  <v-app style="background-color: #f8f9fa">
    <v-app-bar app elevation="1" color="#FFFFFF" v-if="loginStatus && !isRoom">
      <div class="d-flex align-center">
        <v-img src="@/assets/image/logo.png" max-width="224px" contain></v-img>
      </div>
      <v-spacer></v-spacer>
      <!-- NOTE: 데스크탑 버전 -->
      <div v-if="!isMobile">
        <v-btn text>
          <span class="mr-2" @click="goRooms">
            {{ $t("nav_rooms") }}
          </span>
        </v-btn>
        <v-btn text>
          <span class="mr-2" @click="goProfile">
            {{ $t("nav_profile") }}
          </span>
        </v-btn>
        <v-btn text>
          <span class="mr-2" @click="onLogout">
            {{ $t("nav_logout") }}
          </span>
        </v-btn>
      </div>
      <!-- NOTE: 모바일 버전  -->
      <!-- 모바일 특석상 간단한 아이콘이 좋아보인다는 생각 -->
      <div v-else>
        <v-menu bottom left>
          <template v-slot:activator="{ on, attrs }">
            <v-btn dark icon v-bind="attrs" v-on="on" color="primary">
              <i class="fas fa-bars"></i>
            </v-btn>
          </template>
          <v-list>
            <v-list-item>
              <v-btn text>
                <span class="mr-2" @click="goRooms">
                  <i class="fas fa-door-open"></i>
                  <!-- {{ $t("nav_rooms") }} -->
                </span>
              </v-btn>
            </v-list-item>
            <v-list-item>
              <v-btn text>
                <span class="mr-2" @click="goProfile">
                  <v-icon>mdi-account-circle</v-icon>
                  <!-- {{ $t("nav_profile") }} -->
                </span>
              </v-btn>
            </v-list-item>
            <v-list-item>
              <v-btn text>
                <span class="mr-2" @click="onLogout">
                  <v-icon>mdi-logout</v-icon>
                  <!-- {{ $t("nav_logout") }} -->
                </span>
              </v-btn>
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </v-app-bar>
    <v-main class="header" :class="{ 'pa-0': isRoom }">
      <router-view />
      <v-snackbar
        v-model="snackbarStatus"
        :timeout="timeout"
        :color="snackbarColor"
        outlined
        top
      >
        {{ snackbarText }}
      </v-snackbar>
    </v-main>
  </v-app>
</template>

<script>
import isMobile from "@/mixin/isMobile.js";
import i18n from "@/i18n.js";

export default {
  name: "App",
  mixins: [isMobile],
  data() {
    return {
      timeout: 2000,
    };
  },
  computed: {
    snackbarText() {
      return this.$store.getters["getSnackbarText"];
    },
    snackbarStatus: {
      get() {
        return this.$store.getters["getSnackbarStatus"];
      },
      set() {},
    },
    snackbarColor() {
      return this.$store.getters["getSnackbarColor"];
    },
    loginStatus() {
      return this.$store.getters["userStore/getLoginStatus"];
    },
    isRoom() {
      return this.$store.getters["roomStore/getIsRoom"];
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
      this.$store.dispatch("onSnackbar", {
        text: i18n.t("App_logout"),
        color: "primary",
      });
    },
  },
};
</script>
<style>
* {
  font-family: "Font", serif;
}
@font-face {
  font-family: "Font";
  src: url("./assets/font/AppleSDGothicNeoM.ttf");
}
</style>
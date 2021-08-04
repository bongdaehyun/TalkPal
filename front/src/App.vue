<template>
  <v-app>
    <v-app-bar app color="white" v-if="loginStatus">
      <div class="d-flex align-center">
        <!-- NOTE: 네비게이션바 왼쪽 -->
        <v-img src="@/assets/image/logo.png" max-width="224px" contain></v-img>
      </div>
      <v-spacer></v-spacer>
      <!-- NOTE: 네비게이션바 모바일도 가능하게-->
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
      <!-- NOTE: 모바일 버전일때  -->
      <!-- 모바일 특석상 간단한 아이콘이 좋아보인다는 생각 -->
      <div v-else>
        <v-menu bottom left>
          <template v-slot:activator="{ on, attrs }">
            <v-btn dark icon v-bind="attrs" v-on="on" style="color: #a200e0">
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
    <v-main class="header">
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
export default {
  name: "App",
  data() {
    return {
      timeout: 2000,
    };
  },
  computed: {
    snackbarText() {
      return this.$store.getters["getSnackbarText"];
    },
    snackbarStatus() {
      return this.$store.getters["getSnackbarStatus"];
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
    isMobile() {
      switch (this.$vuetify.breakpoint.name) {
        // NOTE: 모바일에서 여백 조절
        case "xs":
          return true;
        case "sm":
          return true;
        default:
          return false;
      }
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
        text: "로그아웃 성공",
        color: "success",
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
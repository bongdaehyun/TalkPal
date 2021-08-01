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
      </div>
      <!-- 모바일 버전일때  -->
      <div v-else>
        <v-menu
            bottom
            left
          >
            <template v-slot:activator="{ on, attrs }">
              <v-btn
                dark
                icon
                v-bind="attrs"
                v-on="on"
                color="secondary"
              >
              <v-icon>mdi-widgets</v-icon>
              </v-btn>
            </template>

            <v-list>
              <v-list-item>
                <v-btn text>
                  <span class="mr-2" color="secondary" @click="goRooms">
                    {{ $t("nav_rooms") }}
                  </span>
                </v-btn>
              </v-list-item>
              <v-list-item>
                  <v-btn text>
                  <span class="mr-2" color="secondary" @click="goProfile">
                    {{ $t("nav_profile") }}
                  </span>
                </v-btn>
              </v-list-item>
              <v-list-item>
                <v-btn text>
                  <span class="mr-2" color="secondary" @click="onLogout">
                    {{ $t("nav_logout") }}
                  </span>
                </v-btn>
              </v-list-item>
            </v-list>
          </v-menu>
      </div>
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
    return {
      isMobile: false,
    };
  },
  beforeDestroy(){
    if (typeof window === 'undefined') return

    window.removeEventListener('resize', this.onResize, { passive: true })
  },
  mounted(){
    this.onResize()

     window.addEventListener('resize', this.onResize, { passive: true })
  },
  computed: {
    loginStatus() {
      return this.$store.getters["userStore/getLoginStatus"];
    },
  },
  methods: {
    onResize () {
        this.isMobile = window.innerWidth < 600
    },
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
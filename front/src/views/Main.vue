<template>
  <v-container>
    <v-row class="tab-wrap" justify="center" align="center">
      <!-- NOTE: col 클래스로 width 조절 -->
      <!-- NOTE: 로그인 & 회원가입 TAB -->
      <v-sheet
        color="white"
        class="col-10 col-md-4 pa-6"
        elevation="10"
        rounded="xl"
      >
        <v-row>
          <v-col>
            <v-img src="@/assets/image/logo.png"></v-img>
          </v-col>
          <v-col>
            <v-tabs centered fixed-tabs color="primary" v-model="tab">
              <v-tab>
                {{ $t("main_login") }}
              </v-tab>
              <v-tab>
                {{ $t("main_register") }}
              </v-tab>
            </v-tabs>
            <v-tabs-items v-model="tab">
              <v-tab-item>
                <Login />
              </v-tab-item>
              <v-tab-item>
                <Register @onRegister="onRegister" />
              </v-tab-item>
            </v-tabs-items>
          </v-col>
        </v-row>
      </v-sheet>
      <!-- NOTE: 사진 -->
      <v-col class="col-12 col-md-6">
        <v-img class="" src="@/assets/image/talking.png"></v-img>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
import Login from "@/components/Main/Login";
import Register from "@/components/Main/Register";

export default {
  data() {
    return {
      tab: null,
    };
  },
  computed: {
    padding() {
      switch (this.$vuetify.breakpoint.name) {
        // NOTE: 모바일에서 여백 조절
        case "xs":
          return "0px 1rem 0px 1rem";
        case "sm":
          return "0px 1rem 0px 1rem";
        default:
          return "0px 0px 0px 0px";
      }
    },
  },
  components: {
    Login,
    Register,
  },
  methods: {
    onRegister() {
      this.tab = null;
    },
  },
};
</script>
<style scoped>
.tab-wrap {
  height: 100vh;
}
</style>
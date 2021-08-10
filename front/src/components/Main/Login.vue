<template>
  <v-container>
    <v-text-field
      label="Email"
      prepend-inner-icon="mdi-email"
      v-model="email"
    ></v-text-field>
    <v-text-field
      label="Password"
      prepend-inner-icon="mdi-lock"
      v-model="password"
      type="password"
    ></v-text-field>
    <div class="d-flex justify-space-between">
      <v-btn class="ma-2" color="primary" outlined @click="$emit(`onBackStep`)">
        <v-icon left> mdi-arrow-left </v-icon>
        {{ $t("main_back") }}
      </v-btn>
      <v-btn class="ma-2" color="primary" outlined @click="requestLogin">
        {{ $t("main_login") }}
        <v-icon right> mdi-login </v-icon>
      </v-btn>
    </div>
  </v-container>
</template>

<script>
import i18n from "@/i18n.js";
export default {
  name: "Login",
  data() {
    return {
      // email: "",
      // password: "",
      email: "test@gmail.com",
      password: "1q2w3e4r!",
    };
  },
  methods: {
    // 로그인 요청
    requestLogin() {
      this.$store
        .dispatch("userStore/requestLogin", {
          email: this.email,
          password: this.password,
        })
        .then((res) => {
          // NOTE: Toast Message 출력
          this.$store.dispatch("onSnackbar", {
            text: i18n.t("main_login_success"),
            color: "primary",
          });

          // 인증 Token
          let accessToken = res.data.accessToken;

          // 로그인 상태 설정
          this.$store.dispatch("userStore/login", accessToken);

          // 페이지 이동
          this.$router.push({ name: "Rooms" });
        })
        .catch((err) => {
          this.$store.dispatch("onSnackbar", {
            text: i18n.t("main_login_error"),
            color: "error",
          });
        });
    },
  },
};
</script>

<style>
</style>
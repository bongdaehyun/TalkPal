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
    <v-btn class="ma-2" color="primary" dark @click="requestLogin">
      {{ $t("main_login") }}
      <v-icon dark right> mdi-checkbox-marked-circle </v-icon>
    </v-btn>
  </v-container>
</template>

<script>
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
          this.$store.dispatch("onSnackbar", {
            text: "로그인 성공",
            color: "success",
          });

          // 인증 Token
          let accessToken = res.data.accessToken;

          // 로그인 상태 설정
          this.$store.dispatch("userStore/login", accessToken);

          // 페이지 이동
          this.$router.push({ name: "Rooms" });
        })
        .catch((err) => {
          alert("로그인 실패");
          // console.log(err);
        });
    },
  },
};
</script>

<style>
</style>
<template>
  <v-tab-item>
    <v-container>
      <v-row>
        <v-text-field
          label="Email"
          prepend-inner-icon="mdi-email"
          v-model="credentials.email"
          type="email"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="Password"
          prepend-inner-icon="mdi-lock"
          v-model="credentials.password"
          type="password"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="Password Confirm"
          prepend-inner-icon="mdi-lock"
          v-model="credentials.passwordConfirm"
          type="password"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="NickName"
          prepend-inner-icon="mdi-text"
          v-model="credentials.nickname"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-select
          v-model="credentials.lang"
          :items="items"
          item-text="name"
          item-value="value"
          label="Language"
        ></v-select>
      </v-row>
      <v-row class="text-right">
        <v-btn class="ma-2" color="primary" dark @click="requestRegister">
          {{ $t("main_register") }}
          <v-icon dark right> mdi-checkbox-marked-circle </v-icon>
        </v-btn>
      </v-row>
    </v-container>
  </v-tab-item>
</template>

<script>
export default {
  data() {
    return {
      credentials: {
        email: "",
        password: "",
        passwordConfirm: "",
        nickname: "",
        lang: "",
      },
      items: [
        { name: "English", value: "en" },
        { name: "한국어", value: "ko" },
      ],
    };
  },
  methods: {
    // 회원가입 요청
    requestRegister() {
      this.$store
        .dispatch("userStore/requestRegister", this.credentials)
        .then(() => {
          this.credentials = {
            email: "",
            password: "",
            passwordConfirm: "",
            nickname: "",
            lang: "",
          };
          // 탭 이동 요청 이벤트
          this.$emit("onRegister");
        })
        .catch((err) => {
          alert("회원가입 실패");
        });
    },
  },
};
</script>

<style>
</style>
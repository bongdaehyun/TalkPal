<template>
  <v-container>
    <v-text-field
      label="Email"
      prepend-inner-icon="mdi-email"
      v-model="email"
      :error-messages="emailErrors"
      @input="$v.email.$touch()"
      @blur="$v.email.$touch()"
      type="email"
    ></v-text-field>
    <v-text-field
      label="Password"
      prepend-inner-icon="mdi-lock"
      v-model="password"
      :error-messages="passwordErrors"
      @input="$v.password.$touch()"
      @blur="$v.password.$touch()"
      type="password"
    ></v-text-field>
    <v-text-field
      label="Password Confirm"
      prepend-inner-icon="mdi-lock"
      v-model="repeatPassword"
      :error-messages="repeatPasswordErrors"
      @input="$v.repeatPassword.$touch()"
      @blur="$v.repeatPassword.$touch()"
      type="password"
    ></v-text-field>
    <v-text-field
      label="NickName"
      prepend-inner-icon="mdi-text"
      v-model="nickname"
      :error-messages="nicknameErrors"
      @input="$v.nickname.$touch()"
      @blur="$v.nickname.$touch()"
      type="text"
    ></v-text-field>
    <v-select
      v-model="lang"
      :items="items"
      item-text="name"
      item-value="value"
      label="Language"
      :error-messages="langErrors"
      @input="$v.lang.$touch()"
      @blur="$v.lang.$touch()"
    ></v-select>
    <div class="d-flex justify-space-between">
      <v-btn
        class="ma-2"
        outlined
        color="primary"
        dark
        @click="$emit(`onBackStep`)"
      >
        <v-icon left> mdi-arrow-left </v-icon>
        {{ $t("main_back") }}
      </v-btn>
      <v-btn class="ma-2" outlined color="primary" @click="requestRegister">
        {{ $t("main_register") }}
        <v-icon dark right> mdi-checkbox-marked-circle </v-icon>
      </v-btn>
    </div>
  </v-container>
</template>

<script>
import langItems from "@/assets/data/lang.json";
import { validationMixin } from "vuelidate";
import {
  required,
  minLength,
  maxLength,
  email,
  sameAs,
  alphaNum,
} from "vuelidate/lib/validators";
import i18n from "@/i18n.js";
import http from "@/util/http-common";

export default {
  data() {
    return {
      // email: "",
      // password: "",
      // repeatPassword: "",
      // nickname: "",
      // lang: "",
      email: "test@gmail.com",
      password: "1q2w3e4r!",
      repeatPassword: "1q2w3e4r!",
      nickname: "test",
      lang: "",

      items: langItems,
    };
  },
  methods: {
    // Form 초기화 함수
    resetCredential() {
      this.email = "";
      this.password = "";
      this.repeatPassword = "";
      this.nickname = "";
      this.lang = "";
    },

    // 회원가입 요청
    requestRegister() {
      this.$v.$touch();
      // 유효성 검증 통과 확인
      if (!this.$v.$invalid) {
        let credentials = {
          email: this.email,
          password: this.password,
          repeatPassword: this.repeatPassword,
          nickname: this.nickname,
          lang: this.lang,
        };
        this.$store
          .dispatch("userStore/requestRegister", credentials)
          .then(() => {
            // NOTE: Toast Message 출력
            this.$store.dispatch("onSnackbar", {
              text: i18n.t("register_mail"),
              color: "success",
            });

            let email = credentials.email;
            http
              .post(`/auth/send/${email}`)
              .then((res) => {
                console.log("result", res);
              })
              .catch((err) => {
                console.log("error", err);
              });
            // Form 초기화
            this.resetCredential();
            // 탭 이동 요청 이벤트 발생
            this.$emit("onRegister");
          })
          .catch((err) => {
            //alert("회원가입 실패");
          });
      }
    },
  },

  /* 
    유효성 검사 
    참고 자료
    1. https://vuetifyjs.com/en/components/forms/#vuelidate
    2. https://www.notion.so/720e938f8223446996aba3500b12f953#85f87afad3274019bbaadadd98b14088
  */
  mixins: [validationMixin],

  validations: {
    email: {
      required,
      email,
      maxLength: maxLength(30),
      async isUnique(email) {
        if (email === "") return true;
        try {
          // 중복 검사 통과
          const res = await http.get(`/users/checkemail/${email}`);
          return true;
        } catch (error) {
          // 중복 검사 실패
          return false;
        }
      },
    },
    password: {
      required,
      minLength: minLength(9),
      maxLength: maxLength(16),
      valid: (password) => {
        const containsLowercase = /[a-z]/.test(password);
        const containsNumber = /[0-9]/.test(password);
        const containsSpecial = /[#?!@$%^&*-]/.test(password);
        return containsLowercase && containsNumber && containsSpecial;
      },
    },
    repeatPassword: { required, sameAsPassword: sameAs("password") },
    nickname: {
      required,
      minLength: minLength(2),
      maxLength: maxLength(16),
      alphaNum,
      async isUnique(nickname) {
        if (nickname === "") return true;
        try {
          // 중복 검사 통과
          const res = await http.get(`/users/checknick/${nickname}`);
          return true;
        } catch (error) {
          // 중복 검사 실패
          return false;
        }
      },
    },
    lang: {
      required,
    },
  },

  computed: {
    emailErrors() {
      const errors = [];
      if (!this.$v.email.$dirty) return errors;
      !this.$v.email.required && errors.push(i18n.t("register_required"));
      !this.$v.email.email && errors.push(i18n.t("register_error_email"));
      !this.$v.email.maxLength &&
        errors.push(i18n.t("register_error_email_len"));
      !this.$v.email.isUnique &&
        errors.push(i18n.t("register_error_email_valid"));
      return errors;
    },
    passwordErrors() {
      const errors = [];
      if (!this.$v.password.$dirty) return errors;
      !this.$v.password.required && errors.push(i18n.t("register_required"));
      (!this.$v.password.minLength || !this.$v.password.maxLength) &&
        errors.push(i18n.t("register_error_pwd_len"));
      !this.$v.password.valid &&
        errors.push(i18n.t("register_error_pwd_valid"));
      return errors;
    },
    repeatPasswordErrors() {
      const errors = [];
      if (!this.$v.repeatPassword.$dirty) return errors;
      !this.$v.repeatPassword.required &&
        errors.push(i18n.t("register_required"));
      !this.$v.repeatPassword.sameAsPassword &&
        errors.push(i18n.t("register_error_pwd_same"));
      return errors;
    },
    nicknameErrors() {
      const errors = [];
      if (!this.$v.nickname.$dirty) return errors;
      !this.$v.nickname.required && errors.push(i18n.t("register_required"));
      (!this.$v.nickname.minLength || !this.$v.nickname.maxLength) &&
        errors.push(i18n.t("register_error_nick_len"));
      !this.$v.nickname.alphaNum &&
        errors.push(i18n.t("register_error_nick_alpha"));
      !this.$v.nickname.isUnique &&
        errors.push(i18n.t("register_error_nick_same"));
      return errors;
    },
    langErrors() {
      const errors = [];
      if (!this.$v.lang.$dirty) return errors;
      !this.$v.lang.required && errors.push(i18n.t("register_required"));
      return errors;
    },
  },
};
</script>

<style>
</style>
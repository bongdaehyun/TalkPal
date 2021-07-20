<template>
  <v-tab-item>
    <v-container>
      <v-row>
        <v-text-field
          label="Email"
          prepend-inner-icon="mdi-email"
          v-model="email"
          :error-messages="emailErrors"
          @input="$v.email.$touch()"
          @blur="$v.email.$touch()"
          type="email"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="Password"
          prepend-inner-icon="mdi-lock"
          v-model="password"
          :error-messages="passwordErrors"
          @input="$v.password.$touch()"
          @blur="$v.password.$touch()"
          type="password"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="Password Confirm"
          prepend-inner-icon="mdi-lock"
          v-model="repeatPassword"
          :error-messages="repeatPasswordErrors"
          @input="$v.repeatPassword.$touch()"
          @blur="$v.repeatPassword.$touch()"
          type="password"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="NickName"
          prepend-inner-icon="mdi-text"
          v-model="nickname"
          :error-messages="nicknameErrors"
          @input="$v.nickname.$touch()"
          @blur="$v.nickname.$touch()"
          type="text"
        ></v-text-field>
      </v-row>
      <v-row>
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
      </v-row>
      <v-row class="text-right">
        <v-btn class="ma-2" color="primary" @click="requestRegister">
          {{ $t("main_register") }}
          <v-icon dark right> mdi-checkbox-marked-circle </v-icon>
        </v-btn>
      </v-row>
    </v-container>
  </v-tab-item>
</template>

<script>
import { validationMixin } from "vuelidate";
import {
  required,
  minLength,
  maxLength,
  email,
  sameAs,
  alphaNum,
} from "vuelidate/lib/validators";

export default {
  data() {
    return {
      email: "",
      password: "",
      repeatPassword: "",
      nickname: "",
      lang: "",
      items: [
        { name: "English", value: "en" },
        { name: "한국어", value: "ko" },
      ],
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
            alert("회원가입 성공");
            // Form 초기화
            this.resetCredential();
            // 탭 이동 요청 이벤트 발생
            this.$emit("onRegister");
          })
          .catch((err) => {
            alert("회원가입 실패");
          });
      }
    },
  },

  mixins: [validationMixin],

  validations: {
    email: { required, email, maxLength: maxLength(30) },
    password: {
      required,
      minLength: minLength(9),
      maxLength: maxLength(16),
      valid: function (value) {
        const containsLowercase = /[a-z]/.test(value);
        const containsNumber = /[0-9]/.test(value);
        const containsSpecial = /[#?!@$%^&*-]/.test(value);
        return containsLowercase && containsNumber && containsSpecial;
      },
    },
    repeatPassword: { required, sameAsPassword: sameAs("password") },
    nickname: {
      required,
      minLength: minLength(2),
      maxLength: maxLength(16),
      alphaNum,
    },
    lang: {
      required,
    },
  },

  computed: {
    emailErrors() {
      const errors = [];
      if (!this.$v.email.$dirty) return errors;
      !this.$v.email.required && errors.push("필수 항목입니다.");
      !this.$v.email.email && errors.push("이메일을 입력하세요.");
      !this.$v.email.maxLength &&
        errors.push("최대 30 글자까지 입력 가능합니다.");
      return errors;
    },
    passwordErrors() {
      const errors = [];
      if (!this.$v.password.$dirty) return errors;
      !this.$v.password.required && errors.push("필수 항목입니다.");
      (!this.$v.password.minLength || !this.$v.password.maxLength) &&
        errors.push("최소 9 글자 최대 16 글자를 입력해야 합니다.");
      !this.$v.password.valid &&
        errors.push("영문 소문자, 숫자, 특수문자를 조합해서 사용해야 합니다.");
      return errors;
    },
    repeatPasswordErrors() {
      const errors = [];
      if (!this.$v.repeatPassword.$dirty) return errors;
      !this.$v.repeatPassword.required && errors.push("필수 항목입니다.");
      !this.$v.repeatPassword.sameAsPassword &&
        errors.push("비밀번호가 다릅니다.");
      return errors;
    },
    nicknameErrors() {
      const errors = [];
      if (!this.$v.nickname.$dirty) return errors;
      !this.$v.nickname.required && errors.push("필수 항목입니다.");
      (!this.$v.nickname.minLength || !this.$v.password.maxLength) &&
        errors.push("최소 2 글자 최대 16 글자를 입력해야 합니다.");
      !this.$v.nickname.alphaNum &&
        errors.push("영문 소문자 및 숫자만 입력해야 합니다.");
      return errors;
    },
    langErrors() {
      const errors = [];
      if (!this.$v.lang.$dirty) return errors;
      !this.$v.lang.required && errors.push("필수 항목입니다.");
      return errors;
    },
  },
};
</script>

<style>
</style>
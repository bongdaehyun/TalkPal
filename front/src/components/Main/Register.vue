<template>
  <v-tab-item>
    <v-container>
      <v-row>
        <v-text-field
          label="Email"
          prepend-inner-icon="mdi-email"
          v-model="credentials.email"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="Password"
          prepend-inner-icon="mdi-lock"
          v-model="credentials.pwd"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="Password Confirm"
          prepend-inner-icon="mdi-lock"
          v-model="credentials.pwdconfirm"
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
        <v-select :items="items" label="Language"></v-select>
      </v-row>
      <v-row class="text-right">
        <v-btn class="ma-2" color="primary" dark>
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
        pwd: "",
        pwdconfirm: "",
        nickname: "",
      },
      items: ["English", "한국어"],
    };
  },
};
</script>

<style>
</style>
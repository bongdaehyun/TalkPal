<template>
  <v-tab-item>
    <v-container>
      <v-row>
        <v-text-field
          label="Email"
          prepend-inner-icon="mdi-email"
          v-model="email"
        ></v-text-field>
      </v-row>
      <v-row>
        <v-text-field
          label="Password"
          prepend-inner-icon="mdi-lock"
          v-model="pwd"
        ></v-text-field>
      </v-row>
      <v-row class="text-right">
        <v-btn class="ma-2" color="primary" dark @click="onLogin">
          {{ $t("main_login") }}
          <v-icon dark right> mdi-checkbox-marked-circle </v-icon>
        </v-btn>
      </v-row>
    </v-container>
  </v-tab-item>
</template>

<script>
export default {
  name: "Login",
  data() {
    return {
      email: "",
      pwd: "",
    };
  },
  methods: {
    onLogin() {
      this.$router.push({ name: "room" });
    },
  },
};
</script>

<style>
</style>
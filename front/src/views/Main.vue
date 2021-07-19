<template>
  <v-row>
    <v-col>
      <v-tabs fixed-tabs background-color="indigo" dark v-model="tab">
        <v-tab>
          {{ $t("main_login") }}
        </v-tab>
        <v-tab>
          {{ $t("main_register") }}
        </v-tab>
      </v-tabs>
      <v-tabs-items v-model="tab">
        <Login />
        <Register />
      </v-tabs-items>
    </v-col>
    <v-col>
      <v-img
        lazy-src="https://picsum.photos/id/11/10/6"
        max-height=""
        max-width="500"
        src="https://picsum.photos/id/11/500/300"
      ></v-img>
    </v-col>
  </v-row>
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
  components: {
    Login,
    Register,
  },
};
</script>
<style>
</style>
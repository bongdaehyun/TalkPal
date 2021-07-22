<template>
  <v-row class="tab-wrap" justify="center" align="center">
    <v-col class="col-sm-12 col-md-4" :style="{ padding: padding }">
      <v-card>
        <v-tabs fixed-tabs background-color="indigo" dark v-model="tab">
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
      </v-card>
    </v-col>
    <v-col class="col-sm-12 col-md-4 d-none d-lg-block d-print-block">
      <v-img class="" src="@/assets/image/talking.jpg"> </v-img>
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
  computed: {
    padding() {
      switch (this.$vuetify.breakpoint.name) {
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
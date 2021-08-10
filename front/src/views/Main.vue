<template>
  <v-stepper v-model="e1">
    <v-stepper-items>
      <v-stepper-content step="1" class="pa-0">
        <v-container>
          <v-row class="tab-wrap" justify="center" align="center">
            <v-sheet
              color="white"
              class="col-10 col-md-3 pa-6"
              elevation="10"
              rounded="xl"
            >
              <Country @onSettingLang="settingLang" />
              <!-- <v-btn class="ma-2" outlined color="primary" @click="e1 = 2">
                Countinue
              </v-btn> -->
            </v-sheet>
          </v-row>
        </v-container>
      </v-stepper-content>
      <v-stepper-content step="2" class="pa-0">
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
                      <Login @onBackStep="e1 = 1" />
                    </v-tab-item>
                    <v-tab-item>
                      <Register @onRegister="onRegister" @onBackStep="e1 = 1" />
                    </v-tab-item>
                  </v-tabs-items>
                </v-col>
              </v-row>
            </v-sheet>
            <!-- NOTE: 사진 -->
            <v-col class="col-md-6 d-none d-lg-block d-print-block">
              <v-img class="" src="@/assets/image/talking.png"> </v-img>
            </v-col>
          </v-row>
        </v-container>
      </v-stepper-content>
    </v-stepper-items>
  </v-stepper>
</template>
<script>
import Login from "@/components/Main/Login";
import Register from "@/components/Main/Register";
import Country from "@/components/Main/Country";

export default {
  data() {
    return {
      tab: null,
      e1: 1,
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
    Country,
  },
  methods: {
    onRegister() {
      this.tab = null;
    },
    settingLang(val) {
      this.$i18n.locale = val;
      console.log("Main", val);
      this.e1 = 2;
    },
  },
};
</script>
<style scoped>
.tab-wrap {
  height: 100vh;
}
</style>
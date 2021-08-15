<template>
  <div class="text-cjoin">
    <v-dialog v-model="dialog" width="500">
      <v-sheet
        elevation="2"
        background-color="white"
        class="d-flex flex-column"
      >
        <div class="text-subtitle-1 primary pa-3">
          <span class="white--text">
            {{ $t("questiondialog") }}
          </span>
          <span class="white--text font-weight-black">
            {{ timer }}
          </span>
        </div>
        <div class="d-flex px-3">
          <v-avatar size="128">
            <v-img :src="profilePath"> </v-img>
          </v-avatar>
          <div class="d-flex flex-column justify-cjoin ml-5">
            <div class="text-h5 mb-1">
              <v-avatar size="24" class="mb-1">
                <v-img :src="getPlagPath"></v-img>
              </v-avatar>
              {{ joinRequestUser.nickname }}
            </div>
            <div class="text-body-2 mb-1">
              {{ joinRequestUser.introduction }}
            </div>
          </div>
        </div>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" tile @click="questionResponse(true)">
            {{ $t("questiondialog_yes") }}
          </v-btn>
          <v-btn tile @click="closeQuestionDialog">
            {{ $t("questiondialog_no") }}
          </v-btn>
        </v-card-actions>
      </v-sheet>
    </v-dialog>
  </div>
</template>

<script>
import LanguageMixin from "@/mixin/LanguageMixin.js";
import getProfilePath from "@/mixin/getProfilePath.js";

export default {
  name: "QuestionDialog",
  mixins: [LanguageMixin, getProfilePath],
  computed: {
    dialog() {
      return this.$store.getters["questionStore/getDialog"];
    },
    joinRequestUser() {
      return this.$store.getters["questionStore/getJoinRequestUser"];
    },
    timer() {
      return this.$store.getters["questionStore/getTimer"];
    },
    profilePath() {
      return this.getProfilePath(this.joinRequestUser.imgPath);
    },
    getPlagPath() {
      return require(`@/assets/image/flag/${this.joinRequestUser.lang}.png`);
    },
  },
  methods: {
    // NOTE: 입장 요청 다이얼로그 응답 이벤트
    questionResponse(answer) {
      this.$store.dispatch("questionStore/setAnswer", { answer });
    },
    // NOTE: 다이얼로그 close
    closeQuestionDialog() {
      this.$store.dispatch("questionStore/closeDialog");
    },
  },
  watch: {
    dialog(newValue, oldValue) {
      // NOTE: 다이얼로그 열렸을 때 유저 언어로 전환
      if (newValue) {
        this.setUserLocale();
      }
    },
  },
  created() {
    this.setUserLocale();
  },
};
</script>

<style>
</style>
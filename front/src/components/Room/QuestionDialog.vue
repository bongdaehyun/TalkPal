<template>
  <div class="text-center">
    <v-dialog v-model="joinQuestionDialog" width="500">
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
          <div class="d-flex flex-column justify-center">
            <div>
              <v-avatar size="24" class="mb-1">
                <v-img :src="getPlagPath"></v-img>
              </v-avatar>
            </div>
            <div class="text-h5 mb-1">
              {{ requestUserInfo.nickname }}
            </div>
            <div class="text-body-2 mb-1">
              {{ requestUserInfo.introduction }}
            </div>
          </div>
        </div>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn large color="primary" text @click="questionResponse(true)">
            {{ $t("questiondialog_yes") }}
          </v-btn>
          <v-btn large color="grey" text @click="questionResponse(false)">
            {{ $t("questiondialog_no") }}
          </v-btn>
        </v-card-actions>
      </v-sheet>
    </v-dialog>
  </div>
</template>

<script>
export default {
  name: "QuestionDialog",
  data() {
    return {};
  },
  props: {
    joinQuestionDialog: {
      type: Boolean,
    },
    timer: {
      type: Number,
    },
    requestUserInfo: {
      type: Object,
    },
    profilePath: {
      type: String,
    },
  },
  methods: {
    questionResponse(response) {
      this.$emit("onQuestionResponse", response);
    },
  },
  computed: {
    getPlagPath() {
      return require(`@/assets/image/flag/${this.requestUserInfo.lang}.png`);
    },
  },
};
</script>

<style>
</style>
<template>
  <div class="text-center">
    <v-dialog v-model="joinQuestionDialog" width="500">
      <v-card>
        <v-card-title class="text-h6 success lighten-2">
          {{ timer }} {{ $t("questiondialog") }}
        </v-card-title>
        <v-card>
          <v-list-item three-line>
            <v-avatar size="128">
              <v-img :src="profilePath"> </v-img>
            </v-avatar>
            <v-list-item-content>
              <div class="text-overline">
                {{ requestUserInfo.lang }}
              </div>

              <v-list-item-title class="text-h5 mb-1">
                {{ requestUserInfo.nickname }}
              </v-list-item-title>

              <v-list-item-subtitle>
                {{ requestUserInfo.introduction }}</v-list-item-subtitle
              >
            </v-list-item-content>
          </v-list-item>

          <v-card-title> </v-card-title>
        </v-card>
        <v-divider></v-divider>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn large color="primary" text @click="questionResponse(true)">
            {{ $t("questiondialog_yes") }}
          </v-btn>
          <v-btn large color="primary" text @click="questionResponse(false)">
            {{ $t("questiondialog_no") }}
          </v-btn>
        </v-card-actions>
      </v-card>
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
};
</script>

<style>
</style>